package hr.fer.ztel.hmo.algorithm;

import hr.fer.ztel.hmo.neighbourhood.ClarkWrightNeighbourhood;
import hr.fer.ztel.hmo.neighbourhood.INeighbourhood;
import hr.fer.ztel.hmo.solution.Solution;

public class SimulatedAnnealingCW {
	
	public static void anneal(Solution sol, double temperature,
							  double tfactor, int steps) {
		
		System.out.println("Annealing started");
		System.out.println(sol.getCost());
		System.out.println("------------------");
		
		int tempAttemptsThreshold = sol.getInstance().getUsersNum() * 50;
		int successfulAttemptsThreshold = tempAttemptsThreshold / 5;
		
		for (int i = 0; i < steps; ++i) {
			
			int successfulAttempts = 0;
			
			for (int j = 0; j < tempAttemptsThreshold; ++j) {
				// generate neighborhood
				INeighbourhood neighbourhood = null;
				
				neighbourhood = new ClarkWrightNeighbourhood(sol);
				neighbourhood.makeMove();
				
				int delta = neighbourhood.getDelta();
				
				boolean isAcceptable = Metropolis(delta, temperature);
				if (isAcceptable) {
					successfulAttempts++;
				} else {
					neighbourhood.reverse();
				}
				
				if (successfulAttempts >= successfulAttemptsThreshold) {
					break;
				}
			}
			
			
			System.out.println("Temperature: " + temperature);
			System.out.println("Successful moves: " + successfulAttempts);
			System.out.println("Cost: " + sol.getCost());
			System.out.println("------------------------------");
			
			temperature *= tfactor;
			if (successfulAttempts == 0) {
				break;
			}
		}
		
		System.out.println("Annealing ended");
		System.out.println(sol.getCost());
		System.out.println("-----------------------");
	}
	
	
	private static boolean Metropolis(int delta, double temperatura) {
		return delta < 0.0 || Math.random() < Math.exp((-1) * delta / temperatura);
	} 

}
