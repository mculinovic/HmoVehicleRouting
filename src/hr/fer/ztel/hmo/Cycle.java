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
	private int remainingCapacity;
	
	public Cycle(ProblemInstance instance) {
		this.instance = instance;
		users = new HashSet<>();
		route = new ArrayList<>();
		remainingCapacity = instance.getVehicleCapacity();
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
	
	@Override
	public boolean equals(Object that) {
		if (that == null) return false;
		if (!(that instanceof Cycle)) return false;
		Cycle other = (Cycle) that;
		return this.warehouse == other.warehouse &&
				this.users.equals(other.users);
	}

	public void addUser(int id) {
		users.add(id);
		cost = -1;
		remainingCapacity -= instance.getUsers().get(id).getDemand();
	}
	
	public void removeUser(int id) {
		users.remove(id);
		cost = -1;
		remainingCapacity += instance.getUsers().get(id).getDemand();
	}
	
	public int getRemainingCapacity() {
		return remainingCapacity;
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
	
	public void generateOptimalRoute() {
		int[] route = new int[users.size()];
		int i = 0;
		for (Integer id: users) {
			route[i++] = id;
		}
		permute(route, 0);
	}
	
	private void permute(int[] route, int start) {
		int size = route.length;

        if (size == start + 1) {
            List<Integer> newRoute = new ArrayList<>();
            for (int  i = 0; i < size; ++i) {
            	newRoute.add(route[i]);
            }
            int newCost = calculateCost(newRoute);
            if (this.cost == -1 || newCost < this.cost) {
            	this.route = newRoute;
            	this.cost = newCost;
            }
            	
        } else {
            for (int i = start; i < size; i++) {
            	int[] route_new = route.clone();
                int temp = route_new[i];
                route_new[i] = route_new[start];
                route_new[start] = temp;
                permute(route_new, start + 1);
            }
        }
	}
	
	private int calculateCost(List<Integer> route) {
		int cost = 0;
		cost += instance.whUsersDist[warehouse][route.get(0)];
		cost += instance.whUsersDist[warehouse][route.get(route.size() - 1)];
		for (int i = 0; i < route.size() - 1; ++i) {
			cost += instance.usersDist[route.get(i)][route.get(i + 1)];
		}
		return cost;
	}

	public int getRouteCost() {
		if (cost == -1) cost = calculateCost(this.route);
		// if (cost == -1) generateOptimalRoute();
		return cost;
	}

	public List<Integer> getRoute() {
		return route;
	}

	public void addUserCW(int id) {
		users.add(id);
		cost = -1;
		remainingCapacity -= instance.getUsers().get(id).getDemand();
		route.add(id);
	}

	public void addRouteCW(int index, int id) {
		users.add(id);
		cost = -1;
		remainingCapacity -= instance.getUsers().get(id).getDemand();
		route.add(index, id);
	}

	public void merge(Cycle c2) {
		List<Integer> routeC2 = c2.getRoute();
		for (Integer id: routeC2) {
			this.addUserCW(id);
		}
	}
	
}
