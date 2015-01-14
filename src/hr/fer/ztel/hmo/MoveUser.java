package hr.fer.ztel.hmo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MoveUser implements INeighbourhood {

	private Solution sol;
	
	private int firstWh;
	private int secondWh;
	
	private Cycle c1;
	private Cycle c2;
	
	private int fstId;
	
	private int fstOldCapacity;
	private int sndOldCapacity;
	private int oldCost;
	
	private boolean moved;

	public MoveUser(Solution sol) {
		super();
		this.sol = sol;
		this.oldCost = sol.getCost();
	}

	@Override
	public void makeMove() {
		Random rand = new Random();
		
		List<Cycle> cycles = sol.getCycles();
		
		int fstCycleId = rand.nextInt(cycles.size());
		c1 = cycles.get(fstCycleId);
		List<Integer> fstUsers = new ArrayList<>(c1.getUsers());
		int fstUserId = rand.nextInt(fstUsers.size());
		fstId = fstUsers.get(fstUserId);
		User u1 = sol.getInstance().getUsers().get(fstId);
		
		for (Cycle c: cycles) {
			if (c == c1) continue;
			int wh = c.getWarehouse();
			if (c.getRemainingCapacity() - u1.getDemand() >= 0
				&& sol.getRemainingCapacity(wh) - u1.getDemand() >= 0) {
				c2 = c;
				moved = true;
				break;
			}
		}
		
		if (!moved) return;
		
		firstWh = c1.getWarehouse();
		secondWh = c2.getWarehouse();
		int c1Capacity = c1.getRemainingCapacity() + u1.getDemand();
		int c2Capacity = c2.getRemainingCapacity() - u1.getDemand();
		int wh1Capacity = sol.getRemainingCapacity(firstWh) + u1.getDemand();
		int wh2Capacity = sol.getRemainingCapacity(secondWh) - u1.getDemand();

		if (c1Capacity >= 0 && c2Capacity >= 0 && wh1Capacity >= 0 && wh2Capacity >= 0) {
			
			c1.removeUser(fstId);
			fstOldCapacity = sol.getRemainingCapacity(firstWh);
			sol.setRemainingCapacity(firstWh, wh1Capacity);
			c1.generateOptimalRoute();
			
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
		
		c1.addUser(fstId);
		sol.setRemainingCapacity(firstWh, fstOldCapacity);
		c1.generateOptimalRoute();
		
		c2.removeUser(fstId);
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
		// TODO Auto-generated method stub
		
	}

}
