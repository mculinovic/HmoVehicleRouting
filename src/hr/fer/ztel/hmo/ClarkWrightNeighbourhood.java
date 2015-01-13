package hr.fer.ztel.hmo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

public class ClarkWrightNeighbourhood implements INeighbourhood {
	
	private int firstWh;
	private int secondWh;
	
	private User u1;
	private User u2;
	
	private Solution sol;
	private List<Cycle> cycles;
	private List<Cycle> newCycles;
	boolean moved;
	
	private int oldCost;
	private int fstOldCapacity;
	private int sndOldCapacity;
	
	private Set<Integer> usersWh1;
	private Set<Integer> usersWh2;
	
	public ClarkWrightNeighbourhood(Solution sol) {
		this.sol = sol;
		this.moved = false;
		this.oldCost = sol.getCost();
		this.usersWh1 = new HashSet<>();
		this.usersWh2 = new HashSet<>();
		this.cycles = new ArrayList<Cycle>();
		this.newCycles = new ArrayList<Cycle>();
	}

	@Override
	public void makeMove() {
		Random rand = new Random();
		// warehouses
		while (firstWh == -1) {
			int id = rand.nextInt(sol.getInstance().getWarehousesNum());
			if (!sol.isClosed(id)) firstWh = id;
		}
				
		for (int i = 0; i < sol.getInstance().getWarehousesNum(); ++i) {
			if (i != firstWh && !sol.isClosed(i)) {
				secondWh = i;
				break;
			}
		}

		for (Cycle c: sol.getCycles()) {
			cycles.add(c);
			if (c.getWarehouse() == firstWh) {
				usersWh1.addAll(c.getUsers());
			} else if (c.getWarehouse() == secondWh) {
				usersWh2.addAll(c.getUsers());
			} else {
				newCycles.add(c);
			}
		}
		
		int fstId = rand.nextInt(usersWh1.size());
		int fstUID = new ArrayList<Integer>(usersWh1).get(fstId);
		u1 = sol.getInstance().getUsers().get(fstUID);
		int sndId = rand.nextInt(usersWh2.size());
		int sndUID = new ArrayList<Integer>(usersWh2).get(sndId);
		u2 = sol.getInstance().getUsers().get(sndUID);
		
		int wh1Capacity = sol.getRemainingCapacity(firstWh) + u1.getDemand() - u2.getDemand();
		int wh2Capacity = sol.getRemainingCapacity(secondWh) + u2.getDemand() - u1.getDemand();
		
		if (wh1Capacity >= 0 && wh2Capacity >= 0) {
			moved = true;
			
			usersWh1.remove(u1.getId());
			usersWh2.remove(u2.getId());
			usersWh1.add(u2.getId());
			usersWh2.add(u1.getId());
			
			fstOldCapacity = sol.getRemainingCapacity(firstWh);
			sol.setRemainingCapacity(firstWh, wh1Capacity);
			sndOldCapacity = sol.getRemainingCapacity(secondWh);
			sol.setRemainingCapacity(secondWh, wh2Capacity);
			
			newCycles.addAll(ClarkWrightAlgorithm.execute(sol, firstWh, new ArrayList<Integer>(usersWh1)));
			newCycles.addAll(ClarkWrightAlgorithm.execute(sol, secondWh, new ArrayList<Integer>(usersWh2)));
//			savingsAlgorithm(firstWh, new ArrayList<Integer>(usersWh1));
//			savingsAlgorithm(secondWh, new ArrayList<Integer>(usersWh2));
			
			sol.setCycles(newCycles);
			for (Cycle c: newCycles) {
				c.generateOptimalRoute();
			}
			sol.resetCost();
		}

	}

	@SuppressWarnings("unused")
	private void savingsAlgorithm(int wh, List<Integer> users) {
		int size = users.size();
		int s[][] = new int[size][size];
		
		TreeMap<Integer, List<Integer>> savings = new TreeMap<Integer, List<Integer>>(new Comparator<Integer>() {		
			@Override
			public int compare(Integer a, Integer b) {
				if (Integer.valueOf(a) > Integer.valueOf(b)) {
					return -1;
				}
				else return 1;
			}
		});
		
		// step 1 - calculate savings
		for (int i = 0; i < size; ++i) {
			int uid1 = users.get(i);
			for (int j = i + 1; j < size; ++j) {
				int uid2 = users.get(j);
				s[i][j] = sol.getInstance().whUsersDist[wh][uid1] + sol.getInstance().whUsersDist[wh][uid2]
						  - sol.getInstance().usersDist[uid1][uid2];
				s[j][i] = s[i][j];

				// step 2 - create savings list
				List<Integer> key = new ArrayList<>();
				key.add(i);
				key.add(j);
				savings.put(s[i][j], key);
			}
		}
		
		// step 3 create routes
		Set<Cycle> routes = new HashSet<>();
		Set<Integer> closed = new HashSet<Integer>();
		Map<Integer, Cycle> mapping = new HashMap<>();
		
		for (Entry<Integer, List<Integer>> entry: savings.entrySet()) {
			int i = entry.getValue().get(0);
			int j = entry.getValue().get(1);
			int uid1 = users.get(i);
			int uid2 = users.get(j);
			
			// case a
			if (!closed.contains(uid1) && !closed.contains(uid2)) {
				Cycle c = new Cycle(sol.getInstance());
				c.setWarehouse(wh);
				c.addUserCW(uid1);
				c.addUserCW(uid2);
				mapping.put(uid1, c);
				mapping.put(uid2, c);
				routes.add(c);
				closed.add(uid1);
				closed.add(uid2);
				routes.add(c);
			}
			
			// case b
			if (closed.contains(uid1) && !closed.contains(uid2)) {
				Cycle c = mapping.get(uid1);
				User u = sol.getInstance().getUsers().get(uid2);
				if (c.getRemainingCapacity() - u.getDemand() < 0) continue;
				
				List<Integer> route = c.getRoute();
				int index = -1;
				if (route.get(0) == uid1) index = 0;
				else if (route.get(route.size() - 1) == uid1) index = route.size();
				if (index != -1) {
					c.addRouteCW(index, uid2);
					mapping.put(uid2, c);
					closed.add(uid2);
				}
			}
			
			if (!closed.contains(uid1) && closed.contains(uid2)) {
				Cycle c = mapping.get(uid2);
				User u = sol.getInstance().getUsers().get(uid1);
				if (c.getRemainingCapacity() - u.getDemand() < 0) continue;
				
				List<Integer> route = c.getRoute();
				int index = -1;
				if (route.get(0) == uid2) index = 0;
				else if (route.get(route.size() - 1) == uid2) index = route.size();
				if (index != -1) {
					c.addRouteCW(index, uid1);
					mapping.put(uid1, c);
					closed.add(uid1);
				}
			}
			
			// case c
			if (closed.contains(uid1) && closed.contains(uid2)) {
				Cycle c1 = mapping.get(uid1);
				Cycle c2 = mapping.get(uid2);
				if (c1 == c2) continue;
				if (c1.getRoute().get(0) == uid1 &&
					c2.getRoute().get(c2.getRoute().size() - 1) == uid2) {
					int demand = 0;
					for (Integer id: c1.getUsers()) {
						demand += sol.getInstance().getUsers().get(id).getDemand();
					}
					if (c2.getRemainingCapacity() - demand >= 0) {
						c2.merge(c1);
						for (Integer id: c1.getUsers()) {
							mapping.put(id, c2);
						}
						routes.remove(c1);
					}
				}
				if (c2.getRoute().get(0) == uid2 &&
					c1.getRoute().get(c1.getRoute().size() - 1) == uid1) {
					int demand = 0;
					for (Integer id: c2.getUsers()) {
						demand += sol.getInstance().getUsers().get(id).getDemand();
					}
					if (c1.getRemainingCapacity() - demand >= 0) {
						c1.merge(c2);
						for (Integer id: c2.getUsers()) {
							mapping.put(id, c1);
						}
						routes.remove(c2);
					}
				}
			}
		}
		if (closed.size() != users.size()) {
			for (Integer id: users) {
				if (!closed.contains(id)) {
					Cycle c = new Cycle(sol.getInstance());
					c.setWarehouse(wh);
					c.addUserCW(id);
					routes.add(c);
				}
			}
		}
		newCycles.addAll(routes);
	}

	@Override
	public void reverse() {
		if (!moved) return;
		
		sol.setCycles(cycles);
		sol.setRemainingCapacity(firstWh, fstOldCapacity);
		sol.setRemainingCapacity(secondWh, sndOldCapacity);
		sol.resetCost();

	}

	@Override
	public int getDelta() {
		return sol.getCost() - oldCost;
	}

	@Override
	public void makeMove(User u1, User u2) {}

}
