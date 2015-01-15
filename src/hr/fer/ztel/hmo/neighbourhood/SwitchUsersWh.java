package hr.fer.ztel.hmo.neighbourhood;

import hr.fer.ztel.hmo.problem.Cycle;
import hr.fer.ztel.hmo.problem.User;
import hr.fer.ztel.hmo.solution.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SwitchUsersWh implements INeighbourhood {
	
	private Solution sol;
	
	private int firstWh;
	private int secondWh;
	
	private Cycle c1;
	private Cycle c2;
	
	private int fstId;
	private int sndId;
	
	private int fstOldCapacity;
	private int sndOldCapacity;
	private int oldCost;
	
	private boolean moved;

	public SwitchUsersWh(Solution sol) {
		super();
		this.sol = sol;
		this.oldCost = sol.getCost();
	}

	@Override
	public void makeMove() {
		int max = sol.getInstance().getWarehousesNum();
		Random rand = new Random();
		
		firstWh = -1;
		secondWh = -1;
		
		// warehouses
		while (firstWh == -1) {
		    int id = rand.nextInt(max);
		    if (!sol.isClosed(id)) firstWh = id;
		}
		
		for (int i = 0; i < sol.getInstance().getWarehousesNum(); ++i) {
			if (i != firstWh && !sol.isClosed(i)) {
				secondWh = i;
				break;
			}
		}
		
		// cycles
		List<Cycle> cycles = sol.getCycles();
		List<Cycle> firstCycles = new ArrayList<>();
		List<Cycle> secondCycles = new ArrayList<>();
		for (Cycle c: cycles) {
			if (c.getWarehouse() == firstWh) firstCycles.add(c);
			if (c.getWarehouse() == secondWh) secondCycles.add(c);
		}
		
		int fstCycleId = rand.nextInt(firstCycles.size());
		int sndCycleId = rand.nextInt(secondCycles.size());
		
		c1 = firstCycles.get(fstCycleId);
		c2 = secondCycles.get(sndCycleId);
	
		List<Integer> fstUsers = new ArrayList<>(c1.getUsers());
		List<Integer> sndUsers = new ArrayList<>(c2.getUsers());
		
		int fstUserId = rand.nextInt(fstUsers.size());
		int sndUserId = rand.nextInt(sndUsers.size());
		
		fstId = fstUsers.get(fstUserId);
		sndId = sndUsers.get(sndUserId);
		
		User u1 = sol.getInstance().getUsers().get(fstId);
		User u2 = sol.getInstance().getUsers().get(sndId);

		
		int c1Capacity = c1.getRemainingCapacity() + u1.getDemand() - u2.getDemand();
		int c2Capacity = c2.getRemainingCapacity() + u2.getDemand() - u1.getDemand();
		int wh1Capacity = sol.getRemainingCapacity(firstWh) + u1.getDemand() - u2.getDemand();
		int wh2Capacity = sol.getRemainingCapacity(secondWh) + u2.getDemand() - u1.getDemand();

		if (c1Capacity >=0 && c2Capacity >= 0 && wh1Capacity >= 0 && wh2Capacity >= 0) {
			
			c1.removeUser(fstId);
			c1.addUser(sndId);
			fstOldCapacity = sol.getRemainingCapacity(firstWh);
			sol.setRemainingCapacity(firstWh, wh1Capacity);
			c1.generateOptimalRoute();
			
			c2.removeUser(sndId);
			c2.addUser(fstId);
			sndOldCapacity = sol.getRemainingCapacity(secondWh);
			sol.setRemainingCapacity(secondWh, wh2Capacity);
			c2.generateOptimalRoute();

			moved = true;
			sol.resetCost();
			
		}
	}

	@Override
	public void reverse() {
		if (!moved) return;
		
		c1.removeUser(sndId);
		c1.addUser(fstId);
		sol.setRemainingCapacity(firstWh, fstOldCapacity);
		c1.generateOptimalRoute();
		
		c2.removeUser(fstId);
		c2.addUser(sndId);
		sol.setRemainingCapacity(secondWh, sndOldCapacity);
		c2.generateOptimalRoute();
		
		sol.resetCost();
	}

	@SuppressWarnings("unused")
	private void printCycle(Cycle c) {
		String ret = new String();
		ret += (c.getWarehouse()) + ": ";
		for (Integer uid: c.getRoute()) {
			ret += " " + uid;
		}
		System.out.println(ret);
	}

	@Override
	public int getDelta() {
		return sol.getCost() - oldCost;
	}

	@Override
	public void makeMove(User u1, User u2) {
		
		fstId = u1.getId();
		sndId = u2.getId();
		
		// warehouses
		for (Cycle c: sol.getCycles()) {
			if (c.getUsers().contains(fstId)) {
				c1 = c;
				firstWh = c1.getWarehouse();
			}
			if (c.getUsers().contains(sndId)) {
				c2 = c;
				secondWh = c2.getWarehouse();
			}
		}
	
		if (c1.equals(c2)) return;
		int c1Capacity = c1.getRemainingCapacity() + u1.getDemand() - u2.getDemand();
		int c2Capacity = c2.getRemainingCapacity() + u2.getDemand() - u1.getDemand();
		
		int wh1Capacity = sol.getRemainingCapacity(firstWh);
		int wh2Capacity = sol.getRemainingCapacity(secondWh);
		
		if (firstWh != secondWh) {
			wh1Capacity = sol.getRemainingCapacity(firstWh) + u1.getDemand() - u2.getDemand();
			wh2Capacity = sol.getRemainingCapacity(secondWh) + u2.getDemand() - u1.getDemand();
		}

		if (c1Capacity >=0 && c2Capacity >= 0 && wh1Capacity >= 0 && wh2Capacity >= 0) {
			
			c1.removeUser(fstId);
			c1.addUser(sndId);
			fstOldCapacity = sol.getRemainingCapacity(firstWh);
			sol.setRemainingCapacity(firstWh, wh1Capacity);
			c1.generateOptimalRoute();
			
			c2.removeUser(sndId);
			c2.addUser(fstId);
			sndOldCapacity = sol.getRemainingCapacity(secondWh);
			sol.setRemainingCapacity(secondWh, wh2Capacity);
			c2.generateOptimalRoute();
			moved = true;
			sol.resetCost();
		}
		
	}

}
