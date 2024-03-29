package hr.fer.ztel.hmo.solution;

import hr.fer.ztel.hmo.problem.Cycle;
import hr.fer.ztel.hmo.problem.ProblemInstance;
import hr.fer.ztel.hmo.problem.Warehouse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solution implements Comparable<Solution> {
	
	private List<Cycle> cycles;
	private int cost;
	private ProblemInstance instance;
	private List<Integer> remainingCapacities;
	private Set<Integer> closed;
	
	// parameters for GA
	public int fitness;
	public double probability;
	public int[] usersToWh;
	public int GAcost;
	
	
	public Solution(ProblemInstance instance) {
		this.instance = instance;
		cycles = new ArrayList<>();
		cost = -1;
		remainingCapacities = new ArrayList<>();
		closed = new HashSet<>();
		for (Warehouse wh: instance.getWarehouses()) {
			remainingCapacities.add(wh.getCapacity());
		}
		usersToWh = new int[instance.getUsersNum()];
	}
	
	// Copy constructor
	public Solution(Solution sol) {
		this.cycles = new ArrayList<>();
		for (Cycle c: sol.getCycles()) {
			Cycle cycle = new Cycle(sol.getInstance());
			for (Integer uid: c.getUsers()) {
				cycle.addUser(uid);
			}
			this.cycles.add(cycle);
		}
		
		this.cost = sol.cost;
		this.remainingCapacities = new ArrayList<>();
		for (Integer c: sol.remainingCapacities) {
			this.remainingCapacities.add(c);
		}
		this.closed = new HashSet<>(sol.closed);
		this.fitness = sol.fitness;
		this.probability = sol.probability;
		this.instance = sol.instance;
		this.usersToWh = new int[instance.getUsersNum()];
		System.arraycopy(sol.usersToWh, 0, this.usersToWh, 0, sol.usersToWh.length);
	}

	public List<Cycle> getCycles() {
		return cycles;
	}
	
	public void setCycles(List<Cycle> cycles) {
		this.cycles = cycles;
	}

	public void addCycle(Cycle cycle) {
		cycles.add(cycle);
	}
	
	public int getCost() {
		if (cost == -1)
			calculateCost();
		return cost;
	}
	
	public void resetCost() {
		cost = -1;
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

	@Override
	public int compareTo(Solution o) {
		return Double.compare(this.probability, o.probability);
	}
	
}
