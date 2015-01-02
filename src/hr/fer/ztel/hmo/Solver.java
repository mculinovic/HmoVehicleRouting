package hr.fer.ztel.hmo;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Solver {
	
	public static Solution generateInitialSolution(ProblemInstance instance) {
		Solution initial = new Solution(instance);
		Set<Integer> assigned = new HashSet<>();  // set of users assigned to warehouses
		
		int i = 0;
		while (assigned.size() != instance.getUsersNum()) {
			for (Warehouse wh: instance.getWarehouses()) {
				int capacity = instance.getVehicleCapacity();
				Cycle cycle = new Cycle(instance);
				TreeSet<User> distances = instance.getDistFromWh(wh.getId());
				cycle.setWarehouse(wh.getId());
				for (User user: distances) {
					int uid = user.getId();
					if (!assigned.contains(uid) && capacity - user.getDemand() > 0
							&& wh.getRemainingCapacity() - user.getDemand() > 0) {
						cycle.addUser(uid);
						assigned.add(uid);
						capacity -= user.getDemand();
						wh.setRemainingCapacity(wh.getRemainingCapacity() - user.getDemand());
					}
					if (capacity == 0) break;
				}
				if (cycle.getUsers().size() > 0) {
					cycle.generateInitialRoute();
					initial.addCycle(cycle);
				}
			}
			++i;
//			if (i == 2) break;
		}
		return initial;
	}

}
