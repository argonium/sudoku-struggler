/*
 * Written by Mike Wallace (mfwallace at gmail.com).  Available
 * on the web site http://mfwallace.googlepages.com/.
 * 
 * Copyright (c) 2006 Mike Wallace.
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom
 * the Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package io.miti.sudoku;

import java.util.List;
import java.util.ArrayList;
import java.awt.Point;

/**
 * This class encapsulates a Sudoku puzzle.
 * 
 * Rows are numbered 0-8, starting with the top row.
 * Columns are numbered 0-8, starting with the left column.
 * Each cell in a block is numbered 0-8, like this:
 * 
 *   012
 *   345
 *   678
 * 
 * The 9 blocks in a puzzle are similarly numbered.
 * 
 * @author Mike Wallace (mwallace at pobox.com)
 * @version 1.0
 */
public final class Sudoku
{
  /**
   * The number of cells in the puzzle.
   */
  private static final int NUM_CELLS = 81;
  
  /**
   * The puzzle data.
   */
  private int[] aiMatrix = null;
  
  
  /**
   * Default constructor.
   */
  public Sudoku()
  {
    aiMatrix = new int[NUM_CELLS];
    java.util.Arrays.fill(aiMatrix, 0);
  }
  
  
  /**
   * Fill in the puzzle.
   * 
   * @param data the string of digits
   */
  public void setData(final String data)
  {
    // Check the data
    if (data == null)
    {
      return;
    }
    
    // Save the length
    final int nLen = Math.min(data.length(), 81);
    
    // Iterate over the list
    for (int i = 0; i < nLen; ++i)
    {
      char ch = data.charAt(i);
      if (Character.isDigit(ch))
      {
        aiMatrix[i] = Character.digit(ch, 10);
      }
      else
      {
        aiMatrix[i] = 0;
      }
    }
  }
  
  
  /**
   * Return the puzzle.
   * 
   * @return the puzzle data
   */
  public String getData()
  {
    StringBuilder sb = new StringBuilder(NUM_CELLS);
    
    for (int i = 0; i < NUM_CELLS; ++i)
    {
      sb.append(Integer.toString(aiMatrix[i]));
    }
    
    return sb.toString();
  }
  
  
  /**
   * Return the cell at the specified location.
   * 
   * @param row the row of interest
   * @param col the column of interest
   * @return the cell at (row, col)
   */
  private Cell getCellAt(final int row, final int col)
  {
    final int location = getLocation(row, col);
    return new Cell(location, aiMatrix[location]);
  }
  
  
  /**
   * Return the list of cells in the specified row.
   * 
   * @param row the row of interest
   * @return the cells in the specified row
   */
  private List<Cell> getRow(final int row)
  {
    // Check the input
    if ((row < 0) || (row > 8))
    {
      throw new RuntimeException("Illegal row value in getRow");
    }
    
    List<Cell> cells = new ArrayList<Cell>(9);
    for (int i = 0; i < 9; ++i)
    {
      final int location = (row * 9) + i;
      cells.add(new Cell(location, aiMatrix[location]));
    }
    
    return cells;
  }
  
  
  /**
   * Return the list of unsolved cells in the specified row.
   * 
   * @param row the row of interest
   * @return the unsolved cells in the specified row
   */
  private List<Cell> getRowsUnsolvedCells(final int row)
  {
    // Check the input
    if ((row < 0) || (row > 8))
    {
      throw new RuntimeException("Illegal row value");
    }
    
    // Declare the list of cells that we'll return
    List<Cell> cells = new ArrayList<Cell>(9);
    
    // Compute the 9 locations for the cells we're interested in
    for (int i = 0; i < 9; ++i)
    {
      // Compute the location for the current cell
      final int location = (row * 9) + i;
      
      // Check if it's solved
      if (aiMatrix[location] == 0)
      {
        // It's not solved, so add it to the list
        cells.add(new Cell(location, 0));
      }
    }
    
    // Return the list of unsolved cells
    return cells;
  }
  
  
  /**
   * Return the column of cells in the specified column.
   * 
   * @param column the column of interest
   * @return the cells in the specified column
   */
  private List<Cell> getColumn(final int column)
  {
    // Check the input
    if ((column < 0) || (column > 8))
    {
      throw new RuntimeException("Illegal column value in getColumn");
    }
    
    List<Cell> cells = new ArrayList<Cell>(9);
    for (int i = 0; i < 9; ++i)
    {
      final int location = column + (i * 9);
      cells.add(new Cell(location, aiMatrix[location]));
    }
    
    return cells;
  }
  
  
  /**
   * Return the list of unsolved cells in the specified column.
   * 
   * @param column the column of interest
   * @return the unsolved cells in the specified column
   */
  private List<Cell> getColumnsUnsolvedCells(final int column)
  {
    // Check the input
    if ((column < 0) || (column > 8))
    {
      throw new RuntimeException("Illegal column value");
    }
    
    // Declare the list of cells that we'll return
    List<Cell> cells = new ArrayList<Cell>(9);
    
    // Compute the 9 locations for the cells we're interested in
    for (int i = 0; i < 9; ++i)
    {
      // Compute the location for the current cell
      final int location = column + (i * 9);
      
      // Check if it's solved
      if (aiMatrix[location] == 0)
      {
        // It's not solved, so add it to the list
        cells.add(new Cell(location, 0));
      }
    }
    
    // Return the list of unsolved cells
    return cells;
  }
  
  
  /**
   * Return the integer location for the specified row and column.
   * 
   * @param row the row of interest
   * @param col the column of interest
   * @return the location corresponding to the specified row and column
   */
  private static int getLocation(final int row, final int col)
  {
    // Check the inputs
    if ((row < 0) || (row > 8) || (col < 0) || (col > 8))
    {
      throw new RuntimeException("Illegal value in getLocation()");
    }
    
    return ((row * 9) + col);
  }
  
  
  /**
   * Return the row and column for the specified location.
   * 
   * @param location the location of interest
   * @return the row and column wrapped in a Point
   */
  private static java.awt.Point getRowColumn(final int location)
  {
    // Check the inputs
    if ((location < 0) || (location > 80))
    {
      throw new RuntimeException("Illegal value in getRowColumn()");
    }
    
    // Compute the row and column
    final int row = location / 9;
    final int col = location % 9;
    
    // Return a point with the row and column values
    return new Point(row, col);
  }
  
  
  /**
   * Return the list of cells in the specified block.
   * 
   * @param block the block number
   * @return the cells in the specified block
   */
  private List<Cell> getBlock(final int block)
  {
    // Check the input
    if ((block < 0) || (block > 8))
    {
      throw new RuntimeException("Illegal block value in getBlock");
    }
    
    // Declare the list of cells for this block
    List<Cell> cells = new ArrayList<Cell>(9);
    
    // Get the cells in the specified block
    final int rowStart = (block / 3) * 3;
    final int colStart = (block % 3) * 3;
    for (int rowIndex = 0; rowIndex < 3; ++rowIndex)
    {
      int row = rowStart + rowIndex;
      for (int colIndex = 0; colIndex < 3; ++colIndex)
      {
        int col = colStart + colIndex;
        cells.add(getCellAt(row, col));
      }
    }
    
    // Return the cells
    return cells;
  }
  
  
  /**
   * Return the list of unsolved cells in the specified block.
   * 
   * @param block the block number
   * @return the unsolved cells in the specified block
   */
  private List<Cell> getBlocksUnsolvedCells(final int block)
  {
    // Check the input
    if ((block < 0) || (block > 8))
    {
      throw new RuntimeException("Illegal block value");
    }
    
    // Declare the list of cells for this block
    List<Cell> cells = new ArrayList<Cell>(9);
    
    // Get the cells in the specified block
    final int rowStart = (block / 3) * 3;
    final int colStart = (block % 3) * 3;
    for (int rowIndex = 0; rowIndex < 3; ++rowIndex)
    {
      int row = rowStart + rowIndex;
      for (int colIndex = 0; colIndex < 3; ++colIndex)
      {
        int col = colStart + colIndex;
        
        // Get the cell
        Cell cell = getCellAt(row, col);
        
        // Check if it's solved
        if (cell.getValue() == 0)
        {
          // It's not solved, so save it
          cells.add(cell);
        }
      }
    }
    
    // Return the cells
    return cells;
  }
  
  
  /**
   * Return whether any of the cells in the list contain the
   * value of interest.
   * 
   * @param cells the list of cells
   * @param value the value to look for
   * @return whether any of the cells in the list contain value
   */
  private boolean cellListHasValue(final List<Cell> cells, final int value)
  {
    // Check the input
    if ((cells == null) || (cells.size() < 1))
    {
      return false;
    }
    
    // This is the value we'll return
    boolean bFound = false;
    
    // Iterate over the list of cells
    for (Cell cell : cells)
    {
      // See if the values match
      if (cell.getValue() == value)
      {
        // They do, so update the boolean and break out of the loop
        bFound = true;
        break;
      }
    }
    
    // Return whether the value was found
    return bFound;
  }
  
  
  /**
   * Whether the row for the specified location has
   * the specified value.
   * 
   * @param location the location of the row to check
   * @param value the value to check
   * @return whether the row has the value
   */
  private boolean rowHasValueByLocation(final int location, final int value)
  {
    // Check the input
    if ((location < 0) || (location >= NUM_CELLS))
    {
      return false;
    }
    else if ((value < 1) || (value > 9))
    {
      return false;
    }
    
    List<Cell> cells = getRowByLocation(location);
    return (cellListHasValue(cells, value));
  }
  
  
  /**
   * Whether the column for the specified location has
   * the specified value.
   * 
   * @param location the location of the column to check
   * @param value the value to check
   * @return whether the column has the value
   */
  private boolean columnHasValueByLocation(final int location, final int value)
  {
    // Check the input
    if ((location < 0) || (location >= NUM_CELLS))
    {
      return false;
    }
    else if ((value < 1) || (value > 9))
    {
      return false;
    }
    
    List<Cell> cells = getColumnByLocation(location);
    return (cellListHasValue(cells, value));
  }
  
  
  /**
   * Whether the block for the specified location has
   * the specified value.
   * 
   * @param location the location of the block to check
   * @param value the value to check
   * @return whether the row has the value
   */
  private boolean blockHasValueByLocation(final int location, final int value)
  {
    // Check the input
    if ((location < 0) || (location >= NUM_CELLS))
    {
      return false;
    }
    else if ((value < 1) || (value > 9))
    {
      return true;
    }
    
    List<Cell> cells = getBlockByLocation(location);
    return (cellListHasValue(cells, value));
  }
  
  
  /**
   * Return the row number for the specified location.
   * 
   * @param location the location of interest
   * @return the row number for the location
   */
  private int getRowNumber(final int location)
  {
    // Return the row number for this location
    return getRowColumn(location).x;
  }
  
  
  /**
   * Return the column number for the specified location.
   * 
   * @param location the location of interest
   * @return the column number for the location
   */
  private int getColumnNumber(final int location)
  {
    // Return the column number for this location
    return getRowColumn(location).y;
  }
  
  
  /**
   * Return the block number for the specified location.
   * 
   * @param location the location of interest
   * @return the block number for the location
   */
  private int getBlockNumber(final int location)
  {
    // Check the inputs
    if ((location < 0) || (location > 80))
    {
      throw new RuntimeException("Illegal value in getBlockNumber()");
    }
    
    // Get the row and column for this location
    int row = getRowNumber(location) / 3;
    int col = getColumnNumber(location) / 3;
    
    // Compute the block number and return it
    return ((row * 3) + col);
  }
  
  
  /**
   * Return the row of cells for the specified location.
   * 
   * @param location the location of interest
   * @return the cells crossing the location
   */
  private List<Cell> getRowByLocation(final int location)
  {
    return (getRow(getRowNumber(location)));
  }
  
  
  /**
   * Return the column of cells for the specified location.
   * 
   * @param location the location of interest
   * @return the cells crossing the location
   */
  private List<Cell> getColumnByLocation(final int location)
  {
    return (getColumn(getColumnNumber(location)));
  }
  
  
  /**
   * Return the column of cells for the specified location.
   * 
   * @param location the location of interest
   * @return the cells crossing the location
   */
  private List<Cell> getBlockByLocation(final int location)
  {
    return (getBlock(getBlockNumber(location)));
  }
  
  
  /**
   * Return the list of possible values for a given location.
   * 
   * @param location the cell location
   * @return the list of values possible for the location
   */
  private List<Integer> getValuesNotAt(final int location)
  {
    // This is the array that we'll return
    List<Integer> values = new ArrayList<Integer>(9);
    
    // Iterate over the list of possible values
    for (int value = 1; value <= 9; ++value)
    {
      if ((!rowHasValueByLocation(location, value)) &&
          (!columnHasValueByLocation(location, value)) &&
          (!blockHasValueByLocation(location, value)))
      {
        values.add(new Integer(value));
      }
    }
    
    return values;
  }
  
  
  /**
   * Return the number of occurrences of the specified value
   * in the list of cells.
   * 
   * @param cells the list of cells to check
   * @param value the value to check for
   * @return the number of matching values in the list
   */
  private int getValueCountInList(final List<Cell> cells,
                                  final int value)
  {
    // This is the variable we'll return
    int nNum = 0;
    
    // Iterate over the list, looking for the specified value
    for (Cell cell : cells)
    {
      if (cell.getValue() == value)
      {
        ++nNum;
      }
    }
    
    // Return the number of matching values in the list
    return nNum;
  }
  
  
  /**
   * Return the number of missing values (zeros) in the list of cells.
   * 
   * @param cells the list of cells to check
   * @return the number of missing values in the list
   */
  private int getNumberOfMissingValues(final List<Cell> cells)
  {
    return getValueCountInList(cells, 0);
  }
  
  
  /**
   * Return the list of integers not in the specified row.
   * 
   * @param row the row of interest
   * @return the list of integers not in the row
   */
  private List<Integer> getValuesNotInRow(final int row)
  {
    // Get the cells in this row
    List<Cell> cells = getRow(row);
    
    // Get the number of digits missing from the list
    final int nNumMissingDigits = getNumberOfMissingValues(cells);
    
    // This is the array that we'll return
    List<Integer> values = new ArrayList<Integer>(nNumMissingDigits);
    
    // Check if any are missing
    if (nNumMissingDigits < 1)
    {
      // None are missing, so there's nothing to do
      return values;
    }
    
    // Default to the current value was not found
    boolean bFound = false;
    
    // Iterate over the list of possible values for a cell
    for (int value = 1; value <= 9; ++value)
    {
      // Default to the current value was not found
      bFound = false;
      
      // Iterate over the cells until we hit the end or hit a match
      for (int i = 0; (i < 9) && (!bFound); ++i)
      {
        // Update bFound with whether a match was found
        bFound = (cells.get(i).getValue() == value);
      }
      
      // Check if a match was found
      if (!bFound)
      {
        // A match was not found, so add the value to the list to return
        values.add(value);
        
        // Check if we hit the number of missing values
        if (values.size() == nNumMissingDigits)
        {
          break;
        }
      }
    }
    
    // Return the list of values not found
    return values;
  }
  
  
  /**
   * Return the list of integers not in the specified block.
   * 
   * @param block the block of interest
   * @return the list of integers not in the block
   */
  private List<Integer> getValuesNotInBlock(final int block)
  {
    // Get the cells in this block
    List<Cell> cells = getBlock(block);
    
    // Get the number of digits missing from the list
    final int nNumMissingDigits = getNumberOfMissingValues(cells);
    
    // This is the array that we'll return
    List<Integer> values = new ArrayList<Integer>(nNumMissingDigits);
    
    // Check if any are missing
    if (nNumMissingDigits < 1)
    {
      // None are missing, so there's nothing to do
      return values;
    }
    
    // Default to the current value was not found
    boolean bFound = false;
    
    // Iterate over the list of possible values for a cell
    for (int value = 1; value <= 9; ++value)
    {
      // Default to the current value was not found
      bFound = false;
      
      // Iterate over the cells until we hit the end or hit a match
      for (int i = 0; (i < 9) && (!bFound); ++i)
      {
        // Update bFound with whether a match was found
        bFound = (cells.get(i).getValue() == value);
      }
      
      // Check if a match was found
      if (!bFound)
      {
        // A match was not found, so add the value to the list to return
        values.add(value);
        
        // Check if we hit the number of missing values
        if (values.size() == nNumMissingDigits)
        {
          break;
        }
      }
    }
    
    // Return the list of values not found
    return values;
  }
  
  
  /**
   * Return the list of integers not in the specified column.
   * 
   * @param column the column of interest
   * @return the list of integers not in the column
   */
  private List<Integer> getValuesNotInColumn(final int column)
  {
    // Get the cells in this column
    List<Cell> cells = getColumn(column);
    
    // Get the number of digits missing from the list
    final int nNumMissingDigits = getNumberOfMissingValues(cells);
    
    // This is the array that we'll return
    List<Integer> values = new ArrayList<Integer>(nNumMissingDigits);
    
    // Check if any are missing
    if (nNumMissingDigits < 1)
    {
      // None are missing, so there's nothing to do
      return values;
    }
    
    // Default to the current value was not found
    boolean bFound = false;
    
    // Iterate over the list of possible values for a cell
    for (int value = 1; value <= 9; ++value)
    {
      // Default to the current value was not found
      bFound = false;
      
      // Iterate over the cells until we hit the end or hit a match
      for (int i = 0; (i < 9) && (!bFound); ++i)
      {
        // Update bFound with whether a match was found
        bFound = (cells.get(i).getValue() == value);
      }
      
      // Check if a match was found
      if (!bFound)
      {
        // A match was not found, so add the value to the list to return
        values.add(value);
        
        // Check if we hit the number of missing values
        if (values.size() == nNumMissingDigits)
        {
          break;
        }
      }
    }
    
    // Return the list of values not found
    return values;
  }
  
  
  /**
   * Return whether the puzzle is solved.
   * 
   * @return whether the puzzle is solved
   */
  public boolean isSolved()
  {
    boolean isSolved = true;
    
    // Iterate over the list
    for (int i = 0; (i < NUM_CELLS) && (isSolved); ++i)
    {
      if (aiMatrix[i] == 0)
      {
        isSolved = false;
      }
    }
    
    return isSolved;
  }
  
  
  /**
   * Return the number of unsolved cells.
   * 
   * @return the number of unsolved cells
   */
  @SuppressWarnings("unused")
  private int getNumberOfUnsolvedCells()
  {
    int nNum = 0;
    
    // Iterate over the list
    for (int i = 0; i < NUM_CELLS; ++i)
    {
      if (aiMatrix[i] == 0)
      {
        ++nNum;
      }
    }
    
    return nNum;
  }
  
  
  /**
   * Check an unsolved cell for having only one possibility.
   * 
   * @return whether the puzzle was changed
   */
  private boolean checkForSinglePossibleValue()
  {
    boolean bChanged = false;
    
    // Iterate over the list
    for (int i = 0; i < NUM_CELLS; ++i)
    {
      if (aiMatrix[i] == 0)
      {
        List<Integer> possibleValues = getValuesNotAt(i);
        if (possibleValues.size() == 1)
        {
          aiMatrix[i] = possibleValues.get(0).intValue();
          bChanged = true;
        }
      }
    }
    
    return bChanged;
  }
  
  
  /**
   * Check each row to see if each missing value can only
   * be in one empty slot.
   * 
   * @return whether the puzzle was changed
   */
  private boolean checkRowForElimination()
  {
    // This is the variable we'll return at the end
    boolean bChanged = false;
    
    // Iterate over each row
    for (int row = 0; row < 9; ++row)
    {
      // Get the unsolved cells in the current row
      List<Cell> values = getRowsUnsolvedCells(row);
      
      // Get the values not in the row
      List<Integer> missingValues = getValuesNotInRow(row);
      
      // Iterate over the missing values
      for (Integer missingValue : missingValues)
      {
        // The number of possible spots for this value
        int numOpenings = 0;
        
        // The last recorded viable cell for the current value
        int openLocation = 0;
        
        // Iterate over the unsolved cells in the row
        for (Cell cell : values)
        {
          // See if the block or column for this cell has the value
          if (!(blockHasValueByLocation(cell.getLocation(), missingValue.intValue())))
          {
            if (!(columnHasValueByLocation(cell.getLocation(), missingValue.intValue())))
            {
              // The current missing value is not in this block or column,
              // so save the location and increase the number of possible
              // spots for this value
              ++numOpenings;
              openLocation = cell.getLocation();
            }
          }
        }
        
        // See if there is only one possibility for the current missing value
        if (numOpenings == 1)
        {
          // There is only one possibility, so update the array and
          // record that the array changed
          aiMatrix[openLocation] = missingValue.intValue();
          bChanged = true;
        }
      }
    }
    
    // Return whether any changes were made
    return bChanged;
  }
  
  
  /**
   * Check each column to see if each missing value can only
   * be in one empty slot.
   * 
   * @return whether the puzzle was changed
   */
  private boolean checkColumnForElimination()
  {
    // This is the variable we'll return at the end
    boolean bChanged = false;
    
    // Iterate over each column
    for (int col = 0; col < 9; ++col)
    {
      // Get the unsolved cells in the current column
      List<Cell> values = getColumnsUnsolvedCells(col);
      
      // Get the values not in the column
      List<Integer> missingValues = getValuesNotInColumn(col);
      
      // Iterate over the missing values
      for (Integer missingValue : missingValues)
      {
        // The number of possible spots for this value
        int numOpenings = 0;
        
        // The last recorded viable cell for the current value
        int openLocation = 0;
        
        // Iterate over the unsolved cells in the column
        for (Cell cell : values)
        {
          // See if the block or row for this cell has the value
          if (!(blockHasValueByLocation(cell.getLocation(), missingValue.intValue())))
          {
            if (!(rowHasValueByLocation(cell.getLocation(), missingValue.intValue())))
            {
              // The current missing value is not in this block or row,
              // so save the location and increase the number of possible
              // spots for this value
              ++numOpenings;
              openLocation = cell.getLocation();
            }
          }
        }
        
        // See if there is only one possibility for the current missing value
        if (numOpenings == 1)
        {
          // There is only one possibility, so update the array and
          // record that the array changed
          aiMatrix[openLocation] = missingValue.intValue();
          bChanged = true;
        }
      }
    }
    
    // Return whether any changes were made
    return bChanged;
  }
  
  
  /**
   * Check each block to see if each missing value can only
   * be in one empty slot.
   * 
   * @return whether the puzzle was changed
   */
  private boolean checkBlockForElimination()
  {
    // This is the variable we'll return at the end
    boolean bChanged = false;
    
    // Iterate over each block
    for (int block = 0; block < 9; ++block)
    {
      // Get the unsolved cells in the current block
      List<Cell> values = getBlocksUnsolvedCells(block);
      
      // Get the values not in the block
      List<Integer> missingValues = getValuesNotInBlock(block);
      
      // Iterate over the missing values
      for (Integer missingValue : missingValues)
      {
        // The number of possible spots for this value
        int numOpenings = 0;
        
        // The last recorded viable cell for the current value
        int openLocation = 0;
        
        // Iterate over the unsolved cells in the block
        for (Cell cell : values)
        {
          // See if the row or column for this cell has the value
          if (!(rowHasValueByLocation(cell.getLocation(), missingValue.intValue())))
          {
            if (!(columnHasValueByLocation(cell.getLocation(), missingValue.intValue())))
            {
              // The current missing value is not in this row or column,
              // so save the location and increase the number of possible
              // spots for this value
              ++numOpenings;
              openLocation = cell.getLocation();
            }
          }
        }
        
        // See if there is only one possibility for the current missing value
        if (numOpenings == 1)
        {
          // There is only one possibility, so update the array and
          // record that the array changed
          aiMatrix[openLocation] = missingValue.intValue();
          bChanged = true;
        }
      }
    }
    
    // Return whether any changes were made
    return bChanged;
  }
  
  
  /**
   * Check each type of group (row, column, block) to see if
   * each missing value can only be in one empty slot in
   * that group.
   * 
   * @return whether the puzzle was changed
   */
  private boolean checkGroupForElimination()
  {
    // Check each type of grouping for changes to the puzzle
    boolean bChanged1 = checkRowForElimination();
    boolean bChanged2 = checkColumnForElimination();
    boolean bChanged3 = checkBlockForElimination();
    
    // Return whether any of the methods above made a change to the puzzle
    return ((bChanged1) || (bChanged2) || (bChanged3));
  }
  
  
  /**
   * Return whether the rows are all valid.
   * 
   * @return whether the rows are all valid
   */
  private boolean rowsAreValid()
  {
    // This is the value we'll return
    boolean bValid = true;
    
    // Iterate over each group
    for (int group = 0; (group < 9) && (bValid); ++group)
    {
      // Get the current group
      List<Cell> cells = getRow(group);
      
      // Iterate over the possible (solved) values
      for (int val = 1; (val <= 9) && (bValid); ++val)
      {
        // See if the current group has more than one occurrence
        // of the current possible value
        if (getValueCountInList(cells, val) > 1)
        {
          // It does, so this group is not valid
          bValid = false;
        }
      }
    }
    
    // Return the validity
    return bValid;
  }
  
  
  /**
   * Return whether the columns are all valid.
   * 
   * @return whether the columns are all valid
   */
  private boolean columnsAreValid()
  {
    // This is the value we'll return
    boolean bValid = true;
    
    // Iterate over each group
    for (int group = 0; (group < 9) && (bValid); ++group)
    {
      // Get the current group
      List<Cell> cells = getColumn(group);
      
      // Iterate over the possible (solved) values
      for (int val = 1; (val <= 9) && (bValid); ++val)
      {
        // See if the current group has more than one occurrence
        // of the current possible value
        if (getValueCountInList(cells, val) > 1)
        {
          // It does, so this group is not valid
          bValid = false;
        }
      }
    }
    
    // Return the validity
    return bValid;
  }
  
  
  /**
   * Return whether the blocks are all valid.
   * 
   * @return whether the blocks are all valid
   */
  private boolean blocksAreValid()
  {
    // This is the value we'll return
    boolean bValid = true;
    
    // Iterate over each group
    for (int group = 0; (group < 9) && (bValid); ++group)
    {
      // Get the current group
      List<Cell> cells = getBlock(group);
      
      // Iterate over the possible (solved) values
      for (int val = 1; (val <= 9) && (bValid); ++val)
      {
        // See if the current group has more than one occurrence
        // of the current possible value
        if (getValueCountInList(cells, val) > 1)
        {
          // It does, so this group is not valid
          bValid = false;
        }
      }
    }
    
    // Return the validity
    return bValid;
  }
  
  
  /**
   * Return whether the empty cells are all valid.
   * 
   * @return whether the empty cells are all valid
   */
  private boolean emptiesAreValid()
  {
    // This is the value we'll return
    boolean bValid = true;
    
    // Iterate over all cells in the puzzle
    for (int index = 0; (index < NUM_CELLS) && (bValid); ++index)
    {
      // Get the value in this cell
      int value = aiMatrix[index];
      
      // Check if it's solved
      if (value == 0)
      {
        // It's not solved.  See if there are any possible values for it
        List<Integer> values = getValuesNotAt(index);
        if ((values == null) || (values.size() < 1))
        {
          bValid = false;
        }
      }
    }
    
    // Return the validity
    return bValid;
  }
  
  
  /**
   * Return whether the puzzle is in a consistent state.
   * 
   * @return whether the puzzle is in a consistent state
   */
  public boolean isValid()
  {
    // Check each row, column, block and empty cells for duplicate values
    return (rowsAreValid() && columnsAreValid() &&
            blocksAreValid() && emptiesAreValid());
  }
  
  
  /**
   * Solve the puzzle.
   * 
   * @return whether the puzzle was solved
   */
  public boolean solve()
  {
    // This is the variable we return, to denote whether the
    // puzzle changed at all
    boolean puzzleChanged = false;
    
    do
    {
      // Check each empty cell in the puzzle, and see if
      // there is only one possible value for that cell
      puzzleChanged = checkForSinglePossibleValue();
      
      // For each R/C/B, check if a missing value can
      // only be in one location in that R/C/B
      boolean bChanged1 = checkGroupForElimination();
      
      // See if there were any changes made to the puzzle
      puzzleChanged = (puzzleChanged || bChanged1);
    } while (puzzleChanged);
   
    // Return whether the puzzle was solved
    return isSolved();
  }
  
  
  /**
   * Try to solve a puzzle, making a guess if it can't solve it.
   * 
   * @return whether the puzzle was solved
   */
  public boolean solveWithGuess()
  {
    // Check if it's valid
    if (!isValid())
    {
      return false;
    }
    
    // Try to solve
    boolean solved = solve();
    
    // This is the puzzle copy that will be used below
    Sudoku puzzle = new Sudoku();
    
    // Is it solved?
    if (!solved)
    {
      // It's not, so iterate over all the cells
      for (int i = 0; (i < NUM_CELLS) && (!solved); ++i)
      {
        // Is this cell solved?
        if (aiMatrix[i] == 0)
        {
          // It's not, so get the possible values for the cell
          List<Integer> possibles = getValuesNotAt(i);
          
          // Iterate over the possible values
          for (int poss = 0; (poss < possibles.size()) && (!solved); ++poss)
          {
            // Copy the puzzle
            puzzle.copyMatrix(this);
            
            // Set the current cell to the current possible value
            puzzle.aiMatrix[i] = possibles.get(poss).intValue();
            
            // Try to solve the copy
            boolean rc = puzzle.solve();
            
            // Did it solve the puzzle and is it valid?
            if ((rc) && (puzzle.isValid()))
            {
              // It is, so save the data and update the solved boolean
              copyMatrix(puzzle);
              solved = true;
            }
          }
        }
      }
    }
    
    // Return whether the puzzle was solved
    return solved;
  }
  
  
  /**
   * Convert the matrix into a printable format.
   * 
   * @return a string representation of this
   */
  @Override
  public String toString()
  {
    // Declare the variable to return
    StringBuilder sb = new StringBuilder(120);
    
    // Iterate over the data
    sb.append("-----------\n");
    for (int i = 0; i < NUM_CELLS; ++i)
    {
      if (aiMatrix[i] != 0)
      {
        sb.append(Integer.toString(aiMatrix[i]));
      }
      else
      {
        sb.append('_');
      }
      
      if (((i + 1) % 27) == 0)
      {
        sb.append("\n-----------\n");
      }
      else if (((i + 1) % 9) == 0)
      {
        sb.append("\n");
      }
      else if (((i + 1) % 3) == 0)
      {
        sb.append("|");
      }
    }
    
    // Return the string
    return sb.toString();
  }
  
  
  /**
   * Copy the matrix data from a puzzle to this.
   * 
   * @param puzzle the object to copy
   */
  private void copyMatrix(final Sudoku puzzle)
  {
    // Copy all cells in aiMatrix
    for (int i = 0; i < NUM_CELLS; ++i)
    {
      aiMatrix[i] = puzzle.aiMatrix[i];
    }
  }
  
  
  /**
   * Clone this object.
   * 
   * @return a clone of this
   * @throws CloneNotSupportedException clone operation is not supported
   */
  @Override
  protected Object clone() throws CloneNotSupportedException
  {
    // Create a new Sudoku object
    Sudoku sudoku = new Sudoku();
    
    // Copy the data from this to the new object
    sudoku.copyMatrix(this);
    
    // Return the new object
    return sudoku;
  }
  
  
  /**
   * Returns whether the argument is equal to this.
   * 
   * @param obj the object to test for equality with this
   * @return whether the argument is equal to this
   */
  @Override
  public boolean equals(final Object obj)
  {
    // Check the argument type
    if (obj == null)
    {
      // The argument is null, so return false
      return false;
    }
    else if (obj instanceof Sudoku)
    {
      // The argument is a Sudoku object, so cast away
      Sudoku sudoku = (Sudoku) (obj);
      
      // Save the 2 data strings
      String data1 = getData();
      String data2 = sudoku.getData();
      
      // Check if they're equal
      if ((data1 == null) && (data2 == null))
      {
        // They're both null
        return true;
      }
      else if (data1 == null)
      {
        // Only one is null
        return false;
      }
      else if (data2 == null)
      {
        // Only one is null
        return false;
      }
      else
      {
        // Neither is null
        return (data1.equals(data2));
      }
    }
    
    // The default is they're not equal
    return false;
  }
  
  
  /**
   * Return the hash code for this instance.
   * 
   * @return the hash code for this instance
   */
  public int hashCode()
  {
    return (getData().hashCode());
  }
  
  
  /**
   * Main entry point for the application.
   * 
   * @param args the arguments passed to the program
   */
  public static void main(final String[] args)
  {
    // Create the puzzle
    Sudoku puzzle = new Sudoku();
    
    // Set the data (81 digits, zero for unknown values)
    puzzle.setData("0500608000300070409400801000000003080607" +
                   "05010409000000005090076090800030001070080");
    
    // Solve the puzzle
    boolean solved = puzzle.solveWithGuess();
    
    // Print out whether it was solved, and the puzzle
    System.out.println("Solved? " + ((solved ? "Yes" : "No")));
    System.out.println("Valid? " + ((puzzle.isValid() ? "Yes" : "No")));
    System.out.println(puzzle.toString());
  }
}
