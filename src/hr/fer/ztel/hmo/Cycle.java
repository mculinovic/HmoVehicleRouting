package hr.fer.ztel.hmo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Cycle {
	
	private int warehouse;
	private ProblemInstance instance;
	private Set<Integer> users;
	private List<Integer> route;
	private int cost;
	
	public Cycle(ProblemInstance instance) {
		this.instance = instance;
		users = new HashSet<>();
		route = new ArrayList<>();
		cost = -1;
	}
	
	public Cycle(int warehouse) {
		this.warehouse = warehouse;
	}

	public int getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(int warehouse) {
		this.warehouse = warehouse;
	}

	public Set<Integer> getUsers() {
		return users;
	}

	public void setUsers(Set<Integer> users) {
		this.users = users;
	}
	
	public boolean equals(Object that) {
		if (that == null) return false;
		if (!(that instanceof Cycle)) return false;
		Cycle other = (Cycle) that;
		return this.warehouse == other.warehouse &&
				this.users.equals(other.users);
	}

	public void addUser(int id) {
		users.add(id);
	}
	
	public void generateInitialRoute() {
		TreeSet<User> allUsers = instance.getDistFromWh(warehouse);
		for (User user: allUsers) {
			if (users.contains(user.getId())) {
				route.add(user.getId());
				break;
			}
		}
		int next = route.get(0);
		for (int i = 0; i < users.size() - 1; ++i) {
			TreeSet<User> userDistances = instance.getDistFromUser(next);
			for (User user: userDistances) {
				if (users.contains(user.getId()) && !route.contains(user.getId())) {
					route.add(user.getId());
					next = user.getId();
					break;
				}
			}
		}
	}
	
	private void calculateCost() {
		cost = 0;
		cost += instance.whUsersDist[warehouse][route.get(0)];
		cost += instance.whUsersDist[warehouse][route.get(route.size() - 1)];
		for (int i = 0; i < route.size() - 1; ++i) {
			cost += instance.usersDist[route.get(i)][route.get(i + 1)];
		}
	}

	public int getRouteCost() {
		if (cost == -1) calculateCost();
		return cost;
	}

	public List<Integer> getRoute() {
		return route;
	}

}
