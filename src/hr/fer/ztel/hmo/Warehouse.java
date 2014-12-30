package hr.fer.ztel.hmo;

public class Warehouse {
	
	private int x;
	private int y;
	private int capacity;
	private int openingCost;
	
	public Warehouse(int x, int y) {
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

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public int getOpeningCost() {
		return openingCost;
	}

	public void setOpeningCost(int openingCost) {
		this.openingCost = openingCost;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Warehouse)) return false;
		Warehouse other = (Warehouse) obj;
		return Integer.valueOf(x).equals(Integer.valueOf(other.x))
			   && Integer.valueOf(y).equals(Integer.valueOf(other.y));
	}
	
	@Override
	public int hashCode() {
		return Integer.valueOf(x) ^ Integer.valueOf(y)
				^ Integer.valueOf(capacity) ^ Integer.valueOf(openingCost);
	}
}
