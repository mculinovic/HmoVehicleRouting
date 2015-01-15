package hr.fer.ztel.hmo.neighbourhood;

import hr.fer.ztel.hmo.problem.Cycle;
import hr.fer.ztel.hmo.problem.User;
import hr.fer.ztel.hmo.solution.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Switch2UsersWh implements INeighbourhood {
	
	private Solution sol;
	
	private int firstWh;
	private int secondWh;
	
	private Cycle c1;
	private Cycle c11;
	private Cycle c2;
	private Cycle c22;
	
	private int fstId;
	private int fstId2;
	private int sndId;
	private int sndId2;
	
	private int fstOldCapacity;
	private int sndOldCapacity;
	private int oldCost;
	
	private boolean moved;

	public Switch2UsersWh(Solution sol) {
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
//		
//		// System.out.println(firstWh + " : " + secondWh);
//		

//		while (secondWh == -1) {
//			int id = rand.nextInt(max);
//		    if (id != firstWh && !sol.isClosed(id)) secondWh = id;
//		}
		
		// cycles
		List<Cycle> cycles = sol.getCycles();
		List<Cycle> firstCycles = new ArrayList<>();
		List<Cycle> secondCycles = new ArrayList<>();
		for (Cycle c: cycles) {
			if (c.getWarehouse() == firstWh) firstCycles.add(c);
			if (c.getWarehouse() == secondWh) secondCycles.add(c);
		}
		
		int fstCycleId = rand.nextInt(firstCycles.size());
		int fstCycleId2 = fstCycleId;
		for (int i = 0; i < firstCycles.size(); ++i)  {
			if (fstCycleId != i) {
				fstCycleId2 = i;
				break;
			}
		}
		
		int sndCycleId = rand.nextInt(secondCycles.size());
		int sndCycleId2 = sndCycleId;
		for (int i = 0; i < secondCycles.size(); ++i)  {
			if (sndCycleId != i) {
				sndCycleId2 = i;
				break;
			}
		}
		
		c1 = firstCycles.get(fstCycleId);
		c11 = firstCycles.get(fstCycleId2);
		c2 = secondCycles.get(sndCycleId);
		c22 = secondCycles.get(sndCycleId2);
	
		List<Integer> fstUsers = new ArrayList<>(c1.getUsers());
		List<Integer> fstUsers2 = new ArrayList<>(c11.getUsers());
		List<Integer> sndUsers = new ArrayList<>(c2.getUsers());
		List<Integer> sndUsers2 = new ArrayList<>(c22.getUsers());
		
		int fstUserId = rand.nextInt(fstUsers.size());
		int fstUserId2 = rand.nextInt(fstUsers2.size());
		int sndUserId = rand.nextInt(sndUsers.size());
		int sndUserId2 = rand.nextInt(sndUsers2.size());
		
		fstId = fstUsers.get(fstUserId);
		fstId2 = fstUsers2.get(fstUserId2);
		sndId = sndUsers.get(sndUserId);
		sndId2 = sndUsers2.get(sndUserId2);
		
		// System.out.println(fstId + " : " + sndId);
		
		User u1 = sol.getInstance().getUsers().get(fstId);
		User u11 = sol.getInstance().getUsers().get(fstId2);
		User u2 = sol.getInstance().getUsers().get(sndId);
		User u22 = sol.getInstance().getUsers().get(sndId2);
		
//		printCycle(c1);
//		printCycle(c2);
		
		int c1Capacity = c1.getRemainingCapacity() + u1.getDemand() - u2.getDemand();
		int c11Capacity = c11.getRemainingCapacity() + u11.getDemand() - u22.getDemand();
		int c2Capacity = c2.getRemainingCapacity() + u2.getDemand() - u1.getDemand();
		int c22Capacity = c22.getRemainingCapacity() + u22.getDemand() - u11.getDemand();
		int wh1Capacity = sol.getRemainingCapacity(firstWh) + u1.getDemand() - u2.getDemand() + u11.getDemand() - u22.getDemand();
		int wh2Capacity = sol.getRemainingCapacity(secondWh) + u2.getDemand() - u1.getDemand() + u22.getDemand() - u11.getDemand();

		if (c1Capacity >= 0 && c2Capacity >= 0 && wh1Capacity >= 0 && wh2Capacity >= 0
				&& c11Capacity >= 0 && c22Capacity >= 0) {
			
			c1.removeUser(fstId);
			c1.addUser(sndId);
			c11.removeUser(fstId2);
			c11.addUser(sndId2);
			fstOldCapacity = sol.getRemainingCapacity(firstWh);
			sol.setRemainingCapacity(firstWh, wh1Capacity);
			c1.generateOptimalRoute();
			c11.generateOptimalRoute();
			
			c2.removeUser(sndId);
			c2.addUser(fstId);
			c22.removeUser(sndId2);
			c22.addUser(fstId2);
			sndOldCapacity = sol.getRemainingCapacity(secondWh);
			sol.setRemainingCapacity(secondWh, wh2Capacity);
			c2.generateOptimalRoute();
			c22.generateOptimalRoute();
			// System.out.println("Move made");
			moved = true;
			sol.resetCost();
			
//			printCycle(c1);
//			printCycle(c2);
		}
	}

	@Override
	public void reverse() {
		if (!moved) return;
		
		// System.out.println("REVERSE");
		c1.removeUser(sndId);
		c1.addUser(fstId);
		c11.removeUser(sndId2);
		c11.addUser(fstId2);
		sol.setRemainingCapacity(firstWh, fstOldCapacity);
		c1.generateOptimalRoute();
		c11.generateOptimalRoute();
		
		c2.removeUser(fstId);
		c2.addUser(sndId);
		c22.removeUser(fstId2);
		c22.addUser(sndId2);
		sol.setRemainingCapacity(secondWh, sndOldCapacity);
		c2.generateOptimalRoute();
		c22.generateOptimalRoute();
		
		sol.resetCost();
		
		// printCycle(c1);
		// printCycle(c2);
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
		// TODO Auto-generated method stub
		
	}

}
