package hr.fer.ztel.hmo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Solver {
	
	public static Solution generateInitialSolution(ProblemInstance instance) {
		Solution initial = new Solution();
		Set<Integer> assigned = new HashSet<>();  // set of users assigned to warehouses
		
		while (assigned.size() != instance.getUsersNum()) {
			for (Warehouse wh: instance.getWarehouses()) {
				int capacity = wh.getCapacity();
				Cycle cycle = new Cycle();
				TreeSet<User> distances = instance.getDistFromWh(wh.getId());
				cycle.setWarehouse(wh.getId());
				for (User user: distances) {
					int uid = user.getId();
					if (!assigned.contains(uid) && capacity - user.getDemand() > 0) {
						cycle.addUser(uid);
						assigned.add(uid);
					}
					if (capacity == 0) break;
				}
				initial.addCycle(cycle);
			}
		}
		return initial;
	}

}
