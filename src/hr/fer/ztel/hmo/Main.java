package hr.fer.ztel.hmo;

import java.util.List;

public class Main {
	
	
	public static void main(String[] args) {
		
		ProblemInstance instance = Utility.readInstanceFromFile("HMO-projekt_instanca_problema.txt");
		instance.precalculateDistances();
		
		List<Solution> initialSolutions = Solver.generateInitialSolutions(instance);
		
		for (Solution s: initialSolutions) {
			System.out.println("Initial: " + s.getCost());
			// HillClimbing.optimize(s);
		}
		
		// 3 je dobar, 11 isto
		SimulatedAnnealing.anneal(initialSolutions.get(8));
		HillClimbing.optimize(initialSolutions.get(8));

		Utility.writeToFile("res-projekt.txt", initialSolutions.get(8));
	}

}
