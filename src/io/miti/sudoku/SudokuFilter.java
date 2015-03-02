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

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * File filter for .sud files.
 * 
 * @author Mike Wallace (mwallace at pobox.com)
 * @version 1.0
 */
public final class SudokuFilter extends FileFilter
{
  /**
   * Default constructor.
   */
  public SudokuFilter()
  {
    super();
  }
  
  
  /**
   * Return whether to accept the file.
   * 
   * @param file the input file
   * @return whether to accept the file
   */
  public boolean accept(final File file)
  {
    String filename = file.getName();
    return filename.endsWith(".sud");
  }
  
  
  /**
   * Return a description of the filter.
   * 
   * @return a description of the filter
   */
  public String getDescription()
  {
    return "*.sud (Sudoku files)";
  }
}