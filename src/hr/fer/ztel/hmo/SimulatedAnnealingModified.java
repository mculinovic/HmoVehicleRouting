package hr.fer.ztel.hmo;

public class SimulatedAnnealingModified {
	
	public static void anneal(Solution sol, boolean isVRP, double temperature,
							  double tfactor, int steps) {
		
		System.out.println("Annealing started");
		System.out.println(sol.getCost());
		System.out.println("------------------");
		
//		int tempAttemptsThreshold = sol.getInstance().getUsersNum() * 1000;
//		int successfulAttemptsThreshold = tempAttemptsThreshold / 5;
//		if (isVRP) successfulAttemptsThreshold /= 2;
		
		int tempAttemptsThreshold = sol.getInstance().getUsersNum() * 50;
		int successfulAttemptsThreshold = tempAttemptsThreshold / 5;
		
		for (int i = 0; i < steps; ++i) {
			
			int successfulAttempts = 0;
			
			for (int j = 0; j < tempAttemptsThreshold; ++j) {
				// generate neighborhood
				INeighbourhood neighbourhood = null;
				
//				if (isVRP) {
//					if (rand < 0.4){
//						neighbourhood = new Switch2UsersCycles(sol);
//					} else {
//						neighbourhood = new SwitchUsersCycles(sol);
//					}
//				} else {
//					if (rand < 0.3) {
//						neighbourhood = new Switch2UsersWh(sol);
//					} else {
//						neighbourhood = new SwitchUsersWh(sol);
//					}
//				}
				
				neighbourhood = new ClarkWright(sol);
				neighbourhood.makeMove();
				
//				System.out.println(sol.getCost());
//				boolean ret = true;
//				if (ret) return;
				
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
			
//			if (!isVRP) {
//				SimulatedAnnealingModified.anneal(sol, true, 50, 0.98, 10);
//			}
			
			
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
