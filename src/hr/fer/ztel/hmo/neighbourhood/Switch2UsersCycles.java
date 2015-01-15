package hr.fer.ztel.hmo.neighbourhood;

import hr.fer.ztel.hmo.problem.Cycle;
import hr.fer.ztel.hmo.problem.User;
import hr.fer.ztel.hmo.solution.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Switch2UsersCycles implements INeighbourhood {
	
	private Solution sol;
	
	private int firstWh;
	
	private Cycle c1;
	private Cycle c2;
	
	private int fstId;
	private int fstId2;
	private int sndId;
	private int sndId2;
	
	private int oldCost;
	
	private boolean moved;
	
	public Switch2UsersCycles(Solution sol) {
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
		
		
		// cycles
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
		
		if (fstUsers.size() <= 1 || sndUsers.size() <= 1)
			return;
		
		int fstUserId = rand.nextInt(fstUsers.size());
		int fstUserId2 = fstUserId;
		while (fstUserId2 == fstUserId) {
			fstUserId2 = rand.nextInt(fstUsers.size());
		}
		int sndUserId = rand.nextInt(sndUsers.size());
		int sndUserId2 = sndUserId;
		while (sndUserId2 == sndUserId) {
			sndUserId2 = rand.nextInt(sndUsers.size());
		}
		
		fstId = fstUsers.get(fstUserId);
		fstId2 = fstUsers.get(fstUserId2);
		sndId = sndUsers.get(sndUserId);
		sndId2 = sndUsers.get(sndUserId2);
		
		User u1 = sol.getInstance().getUsers().get(fstId);
		User u11 = sol.getInstance().getUsers().get(fstId2);
		User u2 = sol.getInstance().getUsers().get(sndId);
		User u22 = sol.getInstance().getUsers().get(sndId2);
		
		int c1Capacity = c1.getRemainingCapacity() + u1.getDemand() - u2.getDemand() + u11.getDemand() - u22.getDemand();
		int c2Capacity = c2.getRemainingCapacity() + u2.getDemand() - u1.getDemand() + u22.getDemand() - u11.getDemand();

		if (c1Capacity >= 0 && c2Capacity >= 0) {
			
			c1.removeUser(fstId);
			c1.removeUser(fstId2);
			c1.addUser(sndId);
			c1.addUser(sndId2);
			c1.generateOptimalRoute();
			
			c2.removeUser(sndId);
			c2.removeUser(sndId2);
			c2.addUser(fstId);
			c2.addUser(fstId2);
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
		c1.removeUser(sndId2);
		c1.addUser(fstId);
		c1.addUser(fstId2);
		c1.generateOptimalRoute();
		
		c2.removeUser(fstId);
		c2.removeUser(fstId2);
		c2.addUser(sndId);
		c2.addUser(sndId2);
		c2.generateOptimalRoute();
			
		sol.resetCost();
	}

	@Override
	public int getDelta() {
		return sol.getCost() - oldCost;
	}

	@Override
	public void makeMove(User u1, User u2) {
		// TODO Auto-generated method stub
		
	}

}
