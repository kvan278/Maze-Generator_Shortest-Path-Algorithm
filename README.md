# Maze Generator and Shortest Path Algorithm

This project implements a Maze Generator and a Shortest Path Algorithm in Java. The program generates a random maze using the randomized Prim's algorithm and provides the functionality to find and display the shortest path from the start to the end cell of the maze. 

## Features

- **Maze Generation**: Generates a random maze based on user-defined dimensions (rows and columns).
- **Shortest Path Finding**: Implements a depth-first search algorithm to find the path from the starting cell to the ending cell.
- **User Interaction**: Allows users to specify the maze dimensions and choose whether to display the solution path.
- **Visual Representation**: Outputs the maze in the console with walls represented visually and the solution path marked with 'o'.

## Code Structure

- **Main Class**: Contains the main method and handles user interaction
- **Maze Class**: Responsible for generating the maze structure, knocking down walls, and printing the maze.
- **Cell Class**: Represents each cell in the maze and manages wall presence and adjacent cells.

## Algorithm
- **Maze Generation**: The maze is created by connecting cells and randomly knocking down walls to ensure a single path between the start and end points.
- **Pathfinding**: The program utilizes a recursive depth-first search to explore the maze and track the path from the start to the end cell.

## Authors
- [Khanh Van](https://www.github.com/kvan278)
