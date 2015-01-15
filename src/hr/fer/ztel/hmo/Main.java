package hr.fer.ztel.hmo;

import hr.fer.ztel.hmo.problem.ProblemInstance;
import hr.fer.ztel.hmo.solution.Solution;
import hr.fer.ztel.hmo.solution.Solver;
import hr.fer.ztel.hmo.utility.Utility;

import java.util.List;

public class Main {
	
	
	public static void main(String[] args) {
		
		ProblemInstance instance = Utility.readInstanceFromFile("HMO-projekt_instanca_problema.txt");
		instance.precalculateDistances();
		
		List<Solution> initialSolutions = Solver.generateInitialSolutions(instance);
		
		for (Solution sol: initialSolutions) {
			System.out.println("Initial: " + sol.getCost());
		}
		// vrtimo samo na ovom jer se pokazalo najboljim
		Utility.writeToFile("res-pocetno.txt", initialSolutions.get(8));
		Solver.optimizeSolution(initialSolutions.get(8));
	}

}
