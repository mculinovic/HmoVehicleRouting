package hr.fer.ztel.hmo;

import java.util.List;

public class Main {
	
	
	public static void main(String[] args) {
		
		ProblemInstance instance = Utility.readInstanceFromFile("HMO-projekt_instanca_problema.txt");
		instance.precalculateDistances();
		
		List<Solution> initialSolutions = Solver.generateInitialSolutions(instance);
		
		for (Solution s: initialSolutions) {
			System.out.println(s.getCost());
//			Utility.writeToFile("res-projekt.txt", s);
		}
	}

}
