package hr.fer.ztel.hmo;

public class Main {
	
	public static void main(String[] args) {
		
		ProblemInstance instance = Utility.readInstanceFromFile("HMO-projekt_instanca_problema.txt");
		instance.precalculateDistances();
		Solution sol = Solver.generateInitialSolution(instance);
		
		System.out.println(sol.getCycles().size());
	}

}
