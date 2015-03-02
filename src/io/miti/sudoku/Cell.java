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

/**
 * This class encapsulates the functionality for a single Sudoku cell.
 * 
 * @author Mike Wallace (mwallace at pobox.com)
 * @version 1.0
 */
public final class Cell
{
  /**
   * The location of this cell in the puzzle.
   */
  private int location = -1;
  
  /**
   * The value in this cell.
   */
  private int value = 0;
  
  
  /**
   * Default constructor.
   */
  @SuppressWarnings("unused")
  private Cell()
  {
    super();
  }
  
  
  /**
   * Constructor.
   * 
   * @param srcLocation the location of this cell
   * @param srcValue the value for this cell
   */
  public Cell(final int srcLocation, final int srcValue)
  {
    if ((srcLocation < 0) || (srcLocation > 80))
    {
      throw new RuntimeException("Illegal location for the cell: " +
                                 Integer.toString(srcLocation));
    }
    else if ((srcValue < 0) || (srcValue > 9))
    {
      throw new RuntimeException("Illegal value for this cell");
    }
    
    location = srcLocation;
    value = srcValue;
  }
  
  
  /**
   * @return Returns the location.
   */
  public int getLocation()
  {
    return location;
  }
  
  
  /**
   * @return Returns the value.
   */
  public int getValue()
  {
    return value;
  }
  
  
  /**
   * Return whether this cell is solved.
   * 
   * @return whether this cell is solved
   */
  public boolean isSolved()
  {
    return (value != 0);
  }
  
  
  /**
   * Return this cell as a string.
   * 
   * @return this cell as a string
   */
  @Override
  public String toString()
  {
    return new StringBuilder().append(value).toString();
  }
}
