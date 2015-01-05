package hr.fer.ztel.hmo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SwitchUsersCycles implements INeighbourhood {
	
	private Solution sol;
	
	private int firstWh;
	
	private Cycle c1;
	private Cycle c2;
	
	private int fstId;
	private int sndId;
	
	private int oldCost;
	
	private boolean moved;

	public SwitchUsersCycles(Solution sol) {
		super();
		this.sol = sol;
		this.oldCost = sol.getCost();
	}

	@Override
	public void makeMove() {
		int max = sol.getInstance().getWarehousesNum();
		Random rand = new Random();
		
		firstWh = -1;
		
		// warehouses
		while (firstWh == -1) {
		    int id = rand.nextInt(max);
		    if (!sol.isClosed(id)) firstWh = id;
		}
		
		
		// cities
		List<Cycle> cycles = sol.getCycles();
		List<Cycle> firstCycles = new ArrayList<>();
		for (Cycle c: cycles) {
			if (c.getWarehouse() == firstWh) firstCycles.add(c);
		}
		
		int fstCycleId = rand.nextInt(firstCycles.size());
		int sndCycleId = fstCycleId;
		while (sndCycleId == fstCycleId) {
			sndCycleId = rand.nextInt(firstCycles.size());
		}
		
		c1 = firstCycles.get(fstCycleId);
		c2 = firstCycles.get(sndCycleId);
	
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

		if (c1Capacity > 0 && c2Capacity > 0) {
			
			c1.removeUser(fstId);
			c1.addUser(sndId);
			c1.generateOptimalRoute();
			
			c2.removeUser(sndId);
			c2.addUser(fstId);
			c2.generateOptimalRoute();
			
			moved = true;
			sol.resetCost();
		}
	}

	@Override
	public void reverse() {
		if (!moved) return;
		
		// System.out.println("REVERSE");
		c1.removeUser(sndId);
		c1.addUser(fstId);
		c1.generateOptimalRoute();
		
		c2.removeUser(fstId);
		c2.addUser(sndId);
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

}
