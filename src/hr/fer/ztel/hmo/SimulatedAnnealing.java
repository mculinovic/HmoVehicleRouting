package hr.fer.ztel.hmo;

public class SimulatedAnnealing {
	
	private static final double START_TEMPERATURE = 1000;
	private static final double TFACTOR = 0.98;
	private static final int STEPS = 150;
	
	public static void anneal(Solution sol, int neighbour) {
		
		System.out.println("Annealing started");
		System.out.println(sol.getCost());
		System.out.println("------------------");
		
		double temperature = START_TEMPERATURE;
		int tempAttemptsThreshold = sol.getInstance().getUsersNum() * 1000;
		int successfulAttemptsThreshold = tempAttemptsThreshold / 10;
		
		
		for (int i = 0; i < STEPS; ++i) {
			
			int successfulAttempts = 0;
			
			for (int j = 0; j < tempAttemptsThreshold; ++j) {
				// generate neighborhood
				INeighbourhood neighbourhood = null;
				if (Math.random() < 0.5) {
					neighbourhood = new SwitchUsersWh(sol);
				} else {
					neighbourhood = new SwitchUsersCycles(sol);
				}

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
