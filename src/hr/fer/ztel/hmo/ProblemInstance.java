package hr.fer.ztel.hmo;

import java.util.ArrayList;
import java.util.List;

public class ProblemInstance {
	
	private int usersNum;
	private int warehousesNum;
	
	private List<User> users;
	private List<Warehouse> warehouses;
	
	private int vehicleCapacity;
	private int vehicleCost;
	
	
	public ProblemInstance(int usersNum, int warehousesNum) {
		super();
		this.usersNum = usersNum;
		this.warehousesNum = warehousesNum;
		this.users = new ArrayList<>();
		this.warehouses = new ArrayList<>();
	}


	public int getUsersNum() {
		return usersNum;
	}


	public void setUsersNum(int usersNum) {
		this.usersNum = usersNum;
	}


	public int getWarehousesNum() {
		return warehousesNum;
	}


	public void setWarehousesNum(int warehousesNum) {
		this.warehousesNum = warehousesNum;
	}

	public void addUser(User user) {
		users.add(user);
	}

	public List<User> getUsers() {
		return users;
	}


	public void setUsers(List<User> users) {
		this.users = users;
	}

	
	public void addWarehouse(Warehouse wh) {
		warehouses.add(wh);
	}

	public List<Warehouse> getWarehouses() {
		return warehouses;
	}


	public void setWarehouses(List<Warehouse> warehouses) {
		this.warehouses = warehouses;
	}


	public int getVehicleCapacity() {
		return vehicleCapacity;
	}


	public void setVehicleCapacity(int vehicleCapacity) {
		this.vehicleCapacity = vehicleCapacity;
	}


	public int getVehicleCost() {
		return vehicleCost;
	}


	public void setVehicleCost(int vehicleCost) {
		this.vehicleCost = vehicleCost;
	}
	
}
