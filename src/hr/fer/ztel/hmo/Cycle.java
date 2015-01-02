package hr.fer.ztel.hmo;

import java.util.HashSet;
import java.util.Set;

public class Cycle {
	
	private int warehouse;
	private Set<Integer> users;
	
	public Cycle() {
		users = new HashSet<>();
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

}
