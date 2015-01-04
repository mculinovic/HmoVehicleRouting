package hr.fer.ztel.hmo;

public class SimulatedAnnealing {
	
	private static final double START_TEMPERATURE = 1000;
	private static final double TFACTOR = 0.9;
	private static final int STEPS = 50;
	
	public static void anneal(Solution sol) {
		
		System.out.println("Annealing started");
		System.out.println("------------------");
		
		double temperature = START_TEMPERATURE;
		int tempAttemptsThreshold = sol.getInstance().getUsersNum() * 1000;
		int successfulAttemptsThreshold = tempAttemptsThreshold / 10;
		
		
		for (int i = 0; i < STEPS; ++i) {
			
			int successfulAttempts = 0;
			
			for (int j = 0; j < tempAttemptsThreshold; ++j) {
				// generate neighborhood
				Neighbourhood neighbourhood = new Neighbourhood(sol);
				neighbourhood.makeMove();
				
				int delta = 0;
				delta = neighbourhood.getDelta();
				
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
			System.out.println("------------------------------");
			
			temperature *= TFACTOR;
			if (successfulAttempts == 0) {
				break;
			}
		}
		
		System.out.println("Annealing ended");
		System.out.println("-----------------------");
	}
	
	
	private static boolean Metropolis(int delta, double temperatura) {
		return delta < 0.0 || Math.random() < Math.exp((-1) * delta / temperatura);
	} 

}
