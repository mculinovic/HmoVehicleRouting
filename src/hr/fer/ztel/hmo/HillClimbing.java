package hr.fer.ztel.hmo;

import java.util.List;

public class HillClimbing {

	public static void optimize(Solution sol) {
		
		while (true) {
			boolean moved = false;
			INeighbourhood neighbourhood;
				
			List<User> users = sol.getInstance().getUsers();
				
			for (int i = 0; i < users.size(); ++i) {
				User u1 = sol.getInstance().getUsers().get(i);
				for (int j = 0; j < users.size(); ++j) {
					if (i == j) continue;
					User u2 = sol.getInstance().getUsers().get(j);
					neighbourhood = new SwitchUsersWh(sol);
					neighbourhood.makeMove(u1, u2);
					if (neighbourhood.getDelta() < 0) {
						moved = true;
					} else {
						neighbourhood.reverse();
					} 
				}
			}
			if (!moved) break;
		}
		
		System.out.println("Hill climbing: " + sol.getCost());
	}
}
