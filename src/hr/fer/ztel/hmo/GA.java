package hr.fer.ztel.hmo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GA {
	
	private int numCromosome;
	@SuppressWarnings("unused")
	private double totalCost;
	private int numIterations;
	private Solution sol;
	private Solution best;
	
	private Random rand;
	
	public GA(int numCromosome, double totalCost, int numIterations,
			  Solution sol) {
		super();
		this.numCromosome = numCromosome;
		this.totalCost = totalCost;
		this.numIterations = numIterations;
		this.sol = sol;
		rand = new Random(System.currentTimeMillis());
	}
	
	public void optimize() {
		
		System.out.println("---------------------------");
		System.out.println("GA started");
		System.out.println("---------------------------");
		
		
		Solution[] population = createPopulation(sol);
		evaluate(population);
		int generation = 1;
		System.out.println(generation + " : " + best.getCost());
		
		while (generation < numIterations - 1) {
			Solution[] next = new Solution[numCromosome];
			calculateProbabilites(population);
			
			int cnt = 0;
			next[cnt++] = best; // elitism
			while (cnt < numCromosome) {
				int idx = select(population);
				Solution s1 = population[idx];
				idx = select(population);
				Solution s2 = population[idx];
				Solution s = crossover(s1, s2);
				mutate(s);
				next[cnt++] = s;
			}
			
			population = next;
			evaluate(population);
			++generation;
			if (generation % 1000 == 0) System.out.println(generation + " : " + best.getCost());		
		}
	
	}
	
	private Solution crossover(Solution s1, Solution s2) {
		Solution s = new Solution(s1);
		int idx = rand.nextInt(s1.usersToWh.length);
		for (int i = idx; i < s1.usersToWh.length; ++i) {
			s.usersToWh[i] = s2.usersToWh[i];
		}
		createRoutes(s);
		return s;
	}

	private Solution[] createPopulation(Solution sol) {
		Solution[] population = new Solution[numCromosome];
		population[0] = sol;
		
		for (Cycle c: sol.getCycles()) {
			int wh = c.getWarehouse();
			for (Integer uid: c.getUsers()) {
				sol.usersToWh[uid] = wh;
			}
		}
		
		createRoutes(sol);
		
		for (int i = 1; i < numCromosome; ++i) {
			Solution init = new Solution(sol);
			for (int j = 0; j < 30; ++j) {
				int id = rand.nextInt(init.usersToWh.length);
				int wh = -1;
				while (wh == -1) {
					int val = rand.nextInt(init.getInstance().getWarehousesNum());
					if (!init.isClosed(val)) wh = val;
				}
				init.usersToWh[id] = wh;
			}
			population[i] = init;
			createRoutes(init);
		}

		return population;
	}

	private void createRoutes(Solution sol) {
		List<Cycle> cycles = new ArrayList<>();
		for (int i = 0; i < sol.getInstance().getWarehousesNum(); ++i) {
			if (!sol.isClosed(i)) {
				List<Integer> users = new ArrayList<>();
				for (int j = 0; j < sol.getInstance().getUsersNum(); ++j) {
					if (sol.usersToWh[j] == i) users.add(j);
				}
				Set<Cycle> created = ClarkWrightAlgorithm.execute(sol, i, users);
				if (!created.isEmpty()) {
					cycles.addAll(created);
				}
			}
		}
		for (Cycle c: cycles) {
			c.generateOptimalRoute();
		}
		sol.setCycles(cycles);
		sol.resetCost();
	}

	private void calculateProbabilites(Solution[] population) {

		Arrays.sort(population);
		double p = 0.0;
	
		for (Solution s : population) {
			s.probability += p;
			p = s.probability;
		}
	}


	private double evaluate(Solution[] population) {

		int maxCost = 0;
		for (Solution s : population) {
			int cost = s.getCost();
			if ((best == null || best.getCost() > cost) && isFeasible(s)) {
				best = new Solution(s);
			}

			if (cost > maxCost) {
				maxCost = cost;
			}
		}
		
		int totalFitness = 0;
		for (Solution s: population) {
			s.fitness = maxCost - s.getCost();
			totalFitness += s.fitness;
		}
		
		for (Solution s: population) {
			s.probability = ((double) s.fitness) / totalFitness;
		}
		
		return maxCost;
	}
	

	private boolean isFeasible(Solution s) {
		int[] capacities = new int[s.getInstance().getWarehousesNum()];
		for (Cycle c: s.getCycles()) {
			for (Integer uid: c.getUsers()) {
				User u = s.getInstance().getUsers().get(uid);
				capacities[c.getWarehouse()] += u.getDemand();
				if (capacities[c.getWarehouse()] > s.getInstance().getWarehouses().get(c.getWarehouse()).getCapacity()) {
					return false;
				}
			}
		}
		return true;
	}

	public void mutate(Solution s) {
		int n = (int) (s.usersToWh.length * 0.1);
		for (int i = 0; i < n; ++i) {
			int id = rand.nextInt(s.usersToWh.length);
			int wh = -1;
			while (wh == -1) {
				int val = rand.nextInt(s.getInstance().getWarehousesNum());
				if (!s.isClosed(val)) wh = val;
			}
			s.usersToWh[id] = wh;
		}
		createRoutes(s);
	}

	
	public int select(Solution[] populacija) {
		
		//Arrays.sort(populacija);
		double probability = Math.random();
		for (int i = 0; i < populacija.length; i++) {
			Solution sol = populacija[i];
			if (probability <= sol.probability) {
				return i;
			}
		}
		return -1;
	}

}
