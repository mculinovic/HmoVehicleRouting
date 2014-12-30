package hr.fer.ztel.hmo;

public class User {
	
	private int x;
	private int y;
	private int demand;
	
	public User(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getDemand() {
		return demand;
	}

	public void setDemand(int demand) {
		this.demand = demand;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof User)) return false;
		User other = (User) obj;
		return Integer.valueOf(x).equals(Integer.valueOf(other.x))
			   && Integer.valueOf(y).equals(Integer.valueOf(other.y));
	}
	
	@Override
	public int hashCode() {
		return Integer.valueOf(x) ^ Integer.valueOf(y) ^ Integer.valueOf(demand);
	}
}
