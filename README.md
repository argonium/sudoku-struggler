# Sudoku Struggler
The Sudoku Struggler is a desktop application, written in Java, that can be used to solve Sudoku puzzles. It won't generate a new puzzle, but it can solve a puzzle pretty well. It can solve a medium difficulty puzzle in under 50 milliseconds and a hard one in 100-120 milliseconds. It also lets a user save a Sudoku puzzle to a file as well as load that puzzle file.

To run the program, Java 5 or later is required. From an Explorer-type of window, you can usually just double-click the jar file to run it. From a command line, use this command to run it:

```
  java -jar sudokustruggler.jar
```

There are no command-line parameters. When running the application and entering values in the puzzle, you can use the arrow keys to navigate across cells (the cursor will wrap around rows and columns).

The buttons on the main offer the following functionality:

* Clear - Clear the puzzle contents
* Solve - Solve the puzzle (shift+click will undo the last solve)
* Load - Load an .sud file into memory
* Save - Save the puzzle to an .sud file
* Valid? - Check whether the puzzle is valid, without solving it
* Copy - Copy the application window to the clipboard
* About - Show the About box
* Quit - Quit the application

Several sample Sudoku puzzles are included.

The source code is released under the MIT license.
