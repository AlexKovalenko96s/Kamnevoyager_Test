package ua.kas.main;

public class TSPEnvironment { // Tabu Search Environment

	public int[][] distances;

	public int getObjectiveFunctionValue(int solution[]) {
		int cost = 0;

		for (int i = 0; i < solution.length - 1; i++) {
			cost += distances[solution[i]][solution[i + 1]];
		}
		return cost;
	}
}