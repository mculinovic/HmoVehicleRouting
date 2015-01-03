package hr.fer.ztel.hmo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Solver {
	
	private static Solution generateInitialSolution(ProblemInstance instance, Set<Integer> closed) {
		Solution initial = new Solution(instance);
		for (Integer wh: closed) {
			initial.setClosed(wh, true);
		}
		Set<Integer> assigned = new HashSet<>();  // set of users assigned to warehouses
		
		while (assigned.size() != instance.getUsersNum()) {
			int created = 0;
			for (Warehouse wh: instance.getWarehouses()) {
				if (initial.isClosed(wh.getId())) continue;
				int capacity = instance.getVehicleCapacity();
				Cycle cycle = new Cycle(instance);
				TreeSet<User> distances = instance.getDistFromWh(wh.getId());
				cycle.setWarehouse(wh.getId());
				for (User user: distances) {
					int uid = user.getId();
					if (!assigned.contains(uid) && capacity - user.getDemand() > 0
							&& initial.getRemainingCapacity(wh.getId()) - user.getDemand() > 0) {
						cycle.addUser(uid);
						assigned.add(uid);
						capacity -= user.getDemand();
						initial.setRemainingCapacity(wh.getId(), initial.getRemainingCapacity(wh.getId()) - user.getDemand());
					}
					if (capacity == 0) break;
				}
				if (cycle.getUsers().size() > 0) {
					cycle.generateInitialRoute();
					initial.addCycle(cycle);
					created++;
				}
			}
			
			if (created == 0) {
				System.out.println("Solution not feasible");
				break;
			}
		}
		return initial;
	}
	
	public static List<Solution> generateInitialSolutions(ProblemInstance instance) {
		
		List<Solution> initialSolutions = new ArrayList<>();
		
		Set<Integer> closed = new HashSet<>();
		Solution sol = Solver.generateInitialSolution(instance, closed);
		initialSolutions.add(sol);
		
		for (int i = 0; i < instance.getWarehousesNum(); ++i) {
			closed.add(i);
			sol = generateInitialSolution(instance, closed);
			initialSolutions.add(sol);
			for (int j = i + 1; j < instance.getWarehousesNum(); ++j) {
				closed.add(j);
				sol = generateInitialSolution(instance, closed);
				initialSolutions.add(sol);
				closed.remove(j);
			}
			closed.remove(i);
		}
		return initialSolutions;
	}

}
