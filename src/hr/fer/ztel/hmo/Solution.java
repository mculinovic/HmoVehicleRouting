package hr.fer.ztel.hmo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solution {
	
	private List<Cycle> cycles;
	private int cost;
	private ProblemInstance instance;
	private List<Integer> remainingCapacities;
	private Set<Integer> closed;
	
	public Solution(ProblemInstance instance) {
		this.instance = instance;
		cycles = new ArrayList<>();
		cost = -1;
		remainingCapacities = new ArrayList<>();
		closed = new HashSet<>();
		for (Warehouse wh: instance.getWarehouses()) {
			remainingCapacities.add(wh.getCapacity());
		}
	}

	public List<Cycle> getCycles() {
		return cycles;
	}


	public void addCycle(Cycle cycle) {
		cycles.add(cycle);
	}
	
	public int getCost() {
		if (cost == -1)
			calculateCost();
		return cost;
	}
	
	private void calculateCost() {
		Set<Integer> opened = new HashSet<Integer>();
		cost = 0;
		for (Cycle c: cycles) {
			if (!opened.contains(c.getWarehouse())) {
				cost += instance.getWarehouses().get(c.getWarehouse()).getOpeningCost();
				opened.add(c.getWarehouse());
			}
			cost += instance.getVehicleCost();
			cost += c.getRouteCost();
		}
	}
	
	public int getRemainingCapacity(int index) {
		return remainingCapacities.get(index);
	}
	
	public void setRemainingCapacity(int index, int value) {
		remainingCapacities.set(index, value);
	}
	
	public void setClosed(int id, boolean value) {
		if (!value) closed.remove(id);
		else closed.add(id);
	}
	
	public boolean isClosed(int id) {
		return closed.contains(id);
	}
	
	public ProblemInstance getInstance() {
		return instance;
	}

	@Override
	public String toString() {
		String sol = new String();
		sol += cycles.size() + "\n\n";
		for (Cycle c: cycles) {
			sol += (c.getWarehouse()) + ": ";
			for (Integer uid: c.getRoute()) {
				sol += " " + uid;
			}
			sol += "\n\n";
		}
		sol += getCost() + "\n";
		return sol;
	}
	
}
