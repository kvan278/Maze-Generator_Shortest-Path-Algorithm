/*
 * Khanh Van | kxv230013 | Project 3 | CS 3345
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		// Prompt user to enter the number of rows for the maze.
		System.out.print("Please enter the row: ");
		int row = sc.nextInt();

		// Prompt user to enter the number of columns for the maze.
		System.out.print("Please enter the col: ");
		int col = sc.nextInt();

		// Create a new Maze object with the specified dimensions.
		Maze m = new Maze(row, col);

		// Print the initial maze structure.
		m.printMaze();

		// Prompt the user to choose whether to display the solution path or terminate.
		System.out.print("\nEnter (Y | y) to draw path OR (N | n) to terminate: ");
		String input = sc.next();
		input = input.toUpperCase(); // Convert input to upper-case to simplify comparison.
		char c = input.charAt(0);
		// Determine the user's choice and respond accordingly.
		if (c == 'Y') {
			m.printPath(); // If user chooses 'Y', print the maze with the solution path.
		} else if (c == 'N') {
			System.out.println("Successfully Terminated!"); // If user chooses 'N', terminate the program.
		} else {
			System.out.println("Invalid Input - Terminated!"); // If input is invalid, terminate with a message.
		}
		sc.close();
	}
}

class Maze {
	public HashMap<Integer, ArrayList<Cell>> wallPairs; // Stores pairs of cells that share a wall.
	int row;
	int col;
	Cell[][] matrix; // 2D array representing the maze grid.

	public Maze(int row, int col) {
		this.row = row;
		this.col = col;
		matrix = new Cell[row][col]; // Initialize the maze grid with the given dimensions.

		generate(); // Generate the initial grid of cells.
		generatePairs(); // Create pairs of adjacent cells to represent potential walls.
		knockWalls(); // Randomly knock down walls to create a solvable maze.
	}

	private void generatePairs() {
		wallPairs = new HashMap<>(); // Initialize the map to store cell pairs.
		int key = 1;

		// Iterate through the grid to pair vertically adjacent cells.
		for (int i = 0; i < row - 1; i++) {
			for (int j = 0; j < col; j++) {
				ArrayList<Cell> list = new ArrayList<>();
				list.add(matrix[i][j]); // Add the current cell.
				list.add(matrix[i + 1][j]); // Add the cell directly below.
				wallPairs.put(key, list); // Store the pair in the map.
				key++;
			}
		}

		// Iterate through the grid to pair horizontally adjacent cells.
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col - 1; j++) {
				ArrayList<Cell> list = new ArrayList<>();
				list.add(matrix[i][j]); // Add the current cell.
				list.add(matrix[i][j + 1]); // Add the cell directly to the right.
				wallPairs.put(key, list); // Store the pair in the map.
				key++;
			}
		}
	}

	private void generate() {
		int data = 0;

		// Iterate through each cell in the grid to initialize it and connect to
		// adjacent cells.
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				Cell c = new Cell(data); // Create a new cell with a unique identifier.

				if (i == 0 && j == 0) {
					// Mark the top-left cell as the start of the maze.
					matrix[i][j] = c;
					matrix[i][j].startCell = true;
				} else if (i == 0) {
					// Connect horizontally to the previous cell if on the top row.
					matrix[i][j] = c;
					matrix[i][j].setWest(matrix[i][j - 1]);
					matrix[i][j - 1].setEast(c);
				} else if (j == 0) {
					// Connect vertically to the previous cell if on the leftmost column.
					matrix[i][j] = c;
					matrix[i][j].setNorth(matrix[i - 1][j]);
					matrix[i - 1][j].setSouth(c);
				} else {
					// Connect both vertically and horizontally if not on an edge.
					matrix[i][j] = c;
					matrix[i][j].setNorth(matrix[i - 1][j]);
					matrix[i - 1][j].setSouth(c);
					matrix[i][j].setWest(matrix[i][j - 1]);
					matrix[i][j - 1].setEast(c);
				}

				data++; // Increment the cell identifier.
			}
		}

		// Mark the bottom-right cell as the end of the maze.
		matrix[row - 1][col - 1].endCell = true;
	}

	public void printMaze() {
		// Iterate over each cell in the grid to print the maze's visual representation.
		for (int i = 0; i < row; i++) {
			// Print the horizontal walls.
			for (int j = 0; j < col; j++) {
				System.out.printf("%-3s", "-");
				if (!matrix[i][j].northWall) {
					System.out.printf("%-3s", " ");
				} else {
					System.out.printf("%-3s", "-");
				}
			}
			System.out.printf("%-3s", "-");
			System.out.println();

			// Print the vertical walls.
			for (int j = 0; j < col; j++) {
				if (!matrix[i][j].westWall) {
					System.out.printf("%-3s", "");
					System.out.printf("%-3s", "");
				} else {
					System.out.printf("%-3s", "|");
					System.out.printf("%-3s", "");
				}
			}
			System.out.print("|");
			System.out.println();
		}

		// Print the bottom border of the maze.
		for (int k = 0; k <= (col * 2); k++) {
			System.out.printf("%-3s", "-");
		}
	}

	public void printPath() {
		// Find the solution path through the maze.
		ArrayList<Integer> pathList = findPath();

		// Print the maze again, highlighting the solution path.
		printPathHelper(pathList);
	}

	private void printPathHelper(ArrayList<Integer> pathList) {
		// Iterate over each cell in the grid to print the maze with the solution path.
		for (int i = 0; i < row; i++) {
			// Print the horizontal walls.
			for (int j = 0; j < col; j++) {
				System.out.printf("%-3s", "-");
				if (!matrix[i][j].northWall) {
					System.out.printf("%-3s", " ");
				} else {
					System.out.printf("%-3s", "-");
				}
			}
			System.out.printf("%-3s", "-");
			System.out.println();

			// Print the vertical walls and the solution path ('o') if applicable.
			for (int j = 0; j < col; j++) {
				if (!matrix[i][j].westWall) {
					System.out.printf("%-3s", "");
					if (pathList.contains(matrix[i][j].data)) {
						System.out.printf("%-3s", "o");
					} else {
						System.out.printf("%-3s", "");
					}
				} else {
					System.out.printf("%-3s", "|");
					if (pathList.contains(matrix[i][j].data)) {
						System.out.printf("%-3s", "o");
					} else {
						System.out.printf("%-3s", "");
					}
				}
			}
			System.out.print("|");
			System.out.println();
		}

		// Print the bottom border of the maze.
		for (int k = 0; k <= (col * 2); k++) {
			System.out.printf("%-3s", "-");
		}
	}

	public ArrayList<Integer> findPath() {
		// Initialize an ArrayList to store the sequence of cell identifiers in the
		// solution path.
		ArrayList<Integer> pathList = new ArrayList<>();
		ArrayList<Character> directions = new ArrayList<>();
		// Recursively find the path from the start cell to the end cell.
		findPathHelper(matrix[0][0], pathList, directions);
		System.out.println(directions.toString());

		// Return the list of cell identifiers in the solution path.
		return pathList;
	}

	private boolean findPathHelper(Cell currCell, ArrayList<Integer> cellNums, ArrayList<Character> directions) {
		boolean isPath;
		// Add the current cell's identifier to the solution path.
		cellNums.add(currCell.data);
		currCell.visited = true;

		// If the current cell is the end cell, the path is complete.
		if (currCell.isEndCell()) {
			return true;
		}

		// Recursively explore the adjacent cells if they have not been visited and the
		// wall is knocked down.
		if (currCell.northCell != null && !currCell.northCell.visited && !currCell.northWall) {
			directions.add('N');
			isPath = findPathHelper(currCell.northCell, cellNums, directions);
			if (isPath) {
				return true;
			}
		}
		if (currCell.eastCell != null && !currCell.eastCell.visited && !currCell.eastWall) {
			directions.add('E');
			isPath = findPathHelper(currCell.eastCell, cellNums, directions);
			if (isPath) {
				return true;
			}
		}
		if (currCell.southCell != null && !currCell.southCell.visited && !currCell.southWall) {
			directions.add('S');
			isPath = findPathHelper(currCell.southCell, cellNums, directions);
			if (isPath) {
				return true;
			}
		}
		if (currCell.westCell != null && !currCell.westCell.visited && !currCell.westWall) {
			directions.add('W');
			isPath = findPathHelper(currCell.westCell, cellNums, directions);
			if (isPath) {
				return true;
			}
		}

		// If no path is found, backtrack by removing the current cell from the solution
		// path.
		directions.remove(directions.size() - 1);
		cellNums.remove(cellNums.size() - 1);
		currCell.visited = false;

		return false;
	}

	private void knockWalls() {
		List<Integer> n = new ArrayList<>();
		for (int i = 1; i <= wallPairs.size(); i++) {
			n.add(i); // Populate the list with the keys of all wall pairs.
		}

		List<ArrayList<Integer>> pairList = new ArrayList<>();
		for (int i = 0; i < (row * col); i++) {
			ArrayList<Integer> numList = new ArrayList<>();
			numList.add(i);
			pairList.add(numList); // Initialize a list for each cell to track connected components.
		}

		Random random = new Random();
		int firstIndex = 0;
		int secondIndex = 0;

		// Calculate the number of vertical and horizontal wall pairs.
		int verticalPairs = col * (row - 1);
		int horizontalPairs = row * (col - 1);

		// Iterate through the wall pairs and randomly knock down walls to create a
		// maze.
		for (int i = 1; i < wallPairs.size(); i++) {
			boolean isConnected = false;
			int randomIndex = random.nextInt(n.size());
			int keyNum = n.get(randomIndex); // Select a random wall pair.
			List<Cell> pair = wallPairs.get(keyNum);

			// Check if the two cells in the pair are already connected.
			for (ArrayList<Integer> integers : pairList) {
				if (integers.contains(pair.get(0).data)) {
					if (integers.contains(pair.get(1).data)) {
						isConnected = true;
					}
				}
			}

			// Knock down vertical walls.
			if (keyNum <= verticalPairs) {
				if (!isConnected) {
					pair.get(0).southWall = false;
					pair.get(1).northWall = false;

					// Merge the connected components.
					for (int k = 0; k < pairList.size(); k++) {
						if (pairList.get(k).contains(pair.get(0).data)) {
							firstIndex = k;
						}
						if (pairList.get(k).contains(pair.get(1).data)) {
							secondIndex = k;
						}
					}
					pairList.get(firstIndex).addAll(pairList.get(secondIndex));
					pairList.remove(secondIndex);
				}
				n.remove(randomIndex);

				// Knock down horizontal walls.
			} else {
				if (!isConnected) {
					pair.get(0).eastWall = false;
					pair.get(1).westWall = false;

					// Merge the connected components.
					for (int k = 0; k < pairList.size(); k++) {
						if (pairList.get(k).contains(pair.get(0).data)) {
							firstIndex = k;
						}
						if (pairList.get(k).contains(pair.get(1).data)) {
							secondIndex = k;
						}
					}
					pairList.get(firstIndex).addAll(pairList.get(secondIndex));
					pairList.remove(secondIndex);
				}
				n.remove(randomIndex);
			}
		}
	}
}

class Cell {
	boolean visited; // Flag to mark if the cell has been visited.
	int data; // Unique identifier for the cell.
	boolean northWall, eastWall, southWall, westWall; // Boolean flags to indicate the presence of walls.
	Cell northCell, eastCell, southCell, westCell; // References to adjacent cells.
	boolean startCell, endCell; // Flags to mark start and end cells of the maze.

	public Cell(int data) {
		visited = false;
		this.data = data;

		// Initialize all walls as present.
		northWall = true;
		eastWall = true;
		southWall = true;
		westWall = true;
	}

	// Methods to set references to adjacent cells.
	public void setNorth(Cell cell) {
		this.northCell = cell;
	}

	public void setEast(Cell cell) {
		this.eastCell = cell;
	}

	public void setSouth(Cell cell) {
		this.southCell = cell;
	}

	public void setWest(Cell cell) {
		this.westCell = cell;
	}

	// Method to remove a specific wall from the cell.
	public void removeWall(char c) {
		switch (c) {
		case 'N' -> northWall = false;
		case 'E' -> eastWall = false;
		case 'S' -> southWall = false;
		case 'W' -> westWall = false;
		}
	}

	// Method to check if the cell is the start of the maze.
	public boolean isStartCell() {
		return startCell;
	}

	// Method to check if the cell is the end of the maze.
	public boolean isEndCell() {
		return endCell;
	}
}
