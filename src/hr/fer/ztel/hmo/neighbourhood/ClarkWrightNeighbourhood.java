package hr.fer.ztel.hmo.neighbourhood;

import hr.fer.ztel.hmo.algorithm.ClarkWrightAlgorithm;
import hr.fer.ztel.hmo.problem.Cycle;
import hr.fer.ztel.hmo.problem.User;
import hr.fer.ztel.hmo.solution.Solution;

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
			
			sol.setCycles(newCycles);
			for (Cycle c: newCycles) {
				c.generateOptimalRoute();
			}
			sol.resetCost();
		}

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
