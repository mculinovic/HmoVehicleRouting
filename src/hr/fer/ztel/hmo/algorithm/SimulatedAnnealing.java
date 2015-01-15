package hr.fer.ztel.hmo.algorithm;

import hr.fer.ztel.hmo.neighbourhood.INeighbourhood;
import hr.fer.ztel.hmo.neighbourhood.Switch2UsersCycles;
import hr.fer.ztel.hmo.neighbourhood.Switch2UsersWh;
import hr.fer.ztel.hmo.neighbourhood.SwitchUsersCycles;
import hr.fer.ztel.hmo.neighbourhood.SwitchUsersWh;
import hr.fer.ztel.hmo.solution.Solution;

public class SimulatedAnnealing {
	
	private static double START_TEMPERATURE = 400;
	private static double TFACTOR = 0.98; // 0.98
	private static int STEPS = 150; // 150

	
	public static void anneal(Solution sol) {
		
		System.out.println("Annealing started");
		System.out.println(sol.getCost());
		System.out.println("------------------");
		
		double temperature = START_TEMPERATURE;
		int tempAttemptsThreshold = sol.getInstance().getUsersNum() * 1000;
		int successfulAttemptsThreshold = tempAttemptsThreshold / 5; // /5
		
		for (int i = 0; i < STEPS; ++i) {
			
			int successfulAttempts = 0;
			
			for (int j = 0; j < tempAttemptsThreshold; ++j) {
				// generate neighborhood
				INeighbourhood neighbourhood = null;
				double rand = Math.random();
				if (rand < 0.2) { // 0.1
					neighbourhood = new Switch2UsersWh(sol);
				} else if (rand < 0.4) { // 0.3
					neighbourhood = new SwitchUsersWh(sol);
				} else if (rand< 0.6){
					neighbourhood = new Switch2UsersCycles(sol);
				} else {
					neighbourhood = new SwitchUsersCycles(sol);
				}

				neighbourhood.makeMove();
				
				int delta = neighbourhood.getDelta();
				if (delta == 0) --j;
				
				boolean isAcceptable = Metropolis(delta, temperature);
				if (isAcceptable) {
					if (delta != 0) successfulAttempts++;
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
			
			temperature *= TFACTOR;
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
