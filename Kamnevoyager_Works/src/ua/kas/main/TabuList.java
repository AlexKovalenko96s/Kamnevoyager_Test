package ua.kas.main;

public class TabuList {

	int[][] tabuList;

	public TabuList(int numCities) {
		tabuList = new int[numCities][numCities];
	}

	public void tabuMove(int city1, int city2) {
		tabuList[city1][city2] += 5;
		tabuList[city2][city1] += 5;
	}

	public void decrementTabu() {
		for (int i = 0; i < tabuList.length; i++) {
			for (int j = 0; j < tabuList.length; j++) {
				tabuList[i][j] -= tabuList[i][j] <= 0 ? 0 : 1;
			}
		}
	}

}
