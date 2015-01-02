package hr.fer.ztel.hmo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solution {
	
	private List<Cycle> cycles;
	
	public Solution() {
		cycles = new ArrayList<>();
	}

	public List<Cycle> getCycles() {
		return cycles;
	}


	public void addCycle(Cycle cycle) {
		cycles.add(cycle);
	}
	
	
}
