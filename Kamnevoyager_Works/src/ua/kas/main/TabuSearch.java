package ua.kas.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

public class TabuSearch {

	public static int[] getBestNeighbour(TabuList tabuList, TSPEnvironment tspEnviromnet, int[] initSolution) {

		int[] bestSol = new int[initSolution.length];

		System.arraycopy(initSolution, 0, bestSol, 0, bestSol.length);
		int bestCost = tspEnviromnet.getObjectiveFunctionValue(initSolution);
		int city1 = 0;
		int city2 = 0;
		boolean firstNeighbor = true;

		for (int i = 1; i < bestSol.length - 1; i++) {
			for (int j = 2; j < bestSol.length - 1; j++) {
				if (i == j) {
					continue;
				}

				int[] newBestSol = new int[bestSol.length];

				System.arraycopy(bestSol, 0, newBestSol, 0, newBestSol.length);

				newBestSol = swapOperator(i, j, initSolution);

				int newBestCost = tspEnviromnet.getObjectiveFunctionValue(newBestSol);

				if ((newBestCost > bestCost || firstNeighbor) && tabuList.tabuList[i][j] == 0) {
					firstNeighbor = false;
					city1 = i;
					city2 = j;
					System.arraycopy(newBestSol, 0, bestSol, 0, newBestSol.length);
					bestCost = newBestCost;
				}

			}
		}

		if (city1 != 0) {
			tabuList.decrementTabu();
			tabuList.tabuMove(city1, city2);
		}
		return bestSol;
	}

	// swaps two cities
	public static int[] swapOperator(int city1, int city2, int[] solution) {
		int temp = solution[city1];
		solution[city1] = solution[city2];
		solution[city2] = temp;
		return solution;
	}

	public static void main(String[] args) throws IOException {

		System.out.println("Enter the number of nodes in the graph");
		Scanner scanner = new Scanner(System.in);
		int number_of_nodes = scanner.nextInt();

		TSPEnvironment tspEnvironment = new TSPEnvironment();

		tspEnvironment.distances = new int[number_of_nodes][number_of_nodes];

		int[] currSolution = new int[number_of_nodes + 1];

		for (int i = 0; i < currSolution.length - 1; i++) {
			currSolution[i] = i;
		}
		currSolution[number_of_nodes] = 0;

		// for excel enter
		InputStream in = new FileInputStream("test.xls");
		HSSFWorkbook wb = new HSSFWorkbook(in);

		Sheet sheet = wb.getSheetAt(0);
		Iterator<Row> it = sheet.iterator();
		int x, y, weight;
		while (it.hasNext()) {
			Row row = it.next();

			x = (int) row.getCell(0).getNumericCellValue();
			y = (int) row.getCell(1).getNumericCellValue();
			weight = (int) row.getCell(2).getNumericCellValue();

			tspEnvironment.distances[x][y] = weight;
			tspEnvironment.distances[y][x] = weight;
		}
		wb.close();

		int numberOfIterations = 1000;
		int tabuLength = number_of_nodes;
		TabuList tabuList = new TabuList(tabuLength);

		int[] bestSol = new int[currSolution.length];

		System.arraycopy(currSolution, 0, bestSol, 0, bestSol.length);
		int bestCost = tspEnvironment.getObjectiveFunctionValue(bestSol);

		for (int i = 0; i < numberOfIterations; i++) {
			currSolution = TabuSearch.getBestNeighbour(tabuList, tspEnvironment, currSolution);

			int currCost = tspEnvironment.getObjectiveFunctionValue(currSolution);

			if (currCost < bestCost) {
				System.arraycopy(currSolution, 0, bestSol, 0, bestSol.length);
				bestCost = currCost;
			}
		}

		System.out.println("Search done! \nBest Solution cost found = " + bestCost + "\nBest Solution :");

		printSolution(bestSol);
		scanner.close();
	}

	public static void printSolution(int[] solution) {
		for (int i = 0; i < solution.length; i++) {
			System.out.print(solution[i] + " ");
		}
		System.out.println();
	}
}
