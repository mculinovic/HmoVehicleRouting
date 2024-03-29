package hr.fer.ztel.hmo.problem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

public class ProblemInstance {
	
	private int usersNum;
	private int warehousesNum;
	
	private List<User> users;
	private List<Warehouse> warehouses;
	
	public int[][] usersDist;
	public int[][] whUsersDist;
	
	private int vehicleCapacity;
	private int vehicleCost;
	
	
	public ProblemInstance(int usersNum, int warehousesNum) {
		super();
		this.usersNum = usersNum;
		this.warehousesNum = warehousesNum;
		this.users = new ArrayList<>();
		this.warehouses = new ArrayList<>();
		this.usersDist = new int[usersNum][usersNum];
		this.whUsersDist = new int[warehousesNum][usersNum];
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
	
	public void precalculateDistances() {
		// user to user
		for (int i = 0; i < usersNum; ++i) {
			for (int j = i + 1; j < usersNum; ++j) {
				int x_diff = users.get(i).getX() - users.get(j).getX();
				int y_diff = users.get(i).getY() - users.get(j).getY();
				double value = Math.sqrt(x_diff * x_diff + y_diff * y_diff);
				usersDist[i][j] = (int) Math.floor(100.0 * value);
				usersDist[j][i] = usersDist[i][j];
			}
		}
		
		// warehouse to user
		for (int i = 0; i < warehousesNum; ++i) {
			for (int j = 0; j < usersNum; ++j) {
				int x_diff = warehouses.get(i).getX() - users.get(j).getX();
				int y_diff = warehouses.get(i).getY() - users.get(j).getY();
				double value = Math.sqrt(x_diff * x_diff + y_diff * y_diff);
				whUsersDist[i][j] = (int) Math.floor(100.0 * value);
			}
		}
	}


	public TreeSet<User> getDistFromWh(final int id) {
		TreeSet<User> distances = new TreeSet<User>(new Comparator<User>() {		
			@Override
			public int compare(User o1, User o2) {
				if (whUsersDist[id][o1.getId()] < whUsersDist[id][o2.getId()])
					return -1;
				if (whUsersDist[id][o1.getId()] > whUsersDist[id][o2.getId()])
					return 1;
				if (whUsersDist[id][o1.getId()] == whUsersDist[id][o2.getId()]) {
					if (o1.getId() < o2.getId()) return -1;
					else return 1;
				}
				return 0;
			}
		});
		
		for (int i = 0; i < usersNum; ++i) {
			distances.add(users.get(i));
		}
		if (distances.size() != usersNum) System.out.println("Error: comparator not working");
		return distances;
	}


	public TreeSet<User> getDistFromUser(final int id) {
		TreeSet<User> distances = new TreeSet<User>(new Comparator<User>() {		
			@Override
			public int compare(User o1, User o2) {
				if (usersDist[id][o1.getId()] < usersDist[id][o2.getId()])
					return -1;
				if (usersDist[id][o1.getId()] > usersDist[id][o2.getId()])
					return 1;
				if (usersDist[id][o1.getId()] == usersDist[id][o2.getId()]) {
					if (o1.getId() < o2.getId()) return -1;
					else return 1;
				}
				return 0;
			}
		});
		
		for (int i = 0; i < usersNum; ++i) {
			distances.add(users.get(i));
		}
		if (distances.size() != usersNum) System.out.println("Error: comparator not working");
		return distances;
	}
	
}
