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

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class is the GUI for the Sudoku Struggler.
 * 
 * @author Mike Wallace (mwallace at pobox.com)
 * @version 1.0
 */
public final class App implements KeyListener
{
  /**
   * The application frame.
   */
  private JFrame m_appFrame = null;
  
  /**
   * The Solve button.
   */
  private JButton btnSolve = null;
  
  /**
   * The Load button.
   */
  private JButton btnLoad = null;
  
  /**
   * The Clear button.
   */
  private JButton btnClear = null;
  
  /**
   * The "Valid?" button.
   */
  private JButton btnValid = null;
  
  /**
   * The Quit button.
   */
  private JButton btnQuit = null;
  
  /**
   * The Save button.
   */
  private JButton btnSave = null;
  
  /**
   * The About button.
   */
  private JButton btnAbout = null;
  
  /**
   * The Copy button.
   */
  private JButton btnCopy = null;
  
  /**
   * The working directory.  Default to the current directory.
   */
  private String currentDirectory = ".";
  
  /**
   * The array of text fields (one text field per cell).
   */
  private JTextField[] atfPuzzle = null;
  
  /**
   * The backup puzzle.  Used to undo a solve.
   */
  private Sudoku sudokuBackup = null;
  
  
  /**
   * Default constructor.
   */
  public App()
  {
    // Instantiate the backup puzzle
    sudokuBackup = new Sudoku();
  }
  
  
  /**
   * Create the application's GUI.
   */
  private void createApp()
  {
    // Create and set up the window
    m_appFrame = new JFrame("Sudoku Struggler");
    m_appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    // Set the window size and center it
    m_appFrame.setMinimumSize(new Dimension(100, 100));
    m_appFrame.setPreferredSize(new Dimension(350, 350));
    m_appFrame.setSize(new Dimension(350, 350));
    centerOnScreen();
    
    // Generate the GUI and add it to the frame
    buildUI();
    
    // Display the window
    m_appFrame.pack();
    m_appFrame.setVisible(true);
  }
  
  
  /**
   * Construct the user interface.
   */
  private void buildUI()
  {
    // Create the widgets for the GUI
    createWidgets();
    
    // Create the panel for the puzzle
    JPanel mainPanel = new JPanel(new GridLayout(9, 9));
    for (int i = 0; i < 81; ++i)
    {
      mainPanel.add(atfPuzzle[i]);
    }
    
    // Add the main panel to the content pane
    m_appFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
    
    // Create a panel for the buttons
    JPanel buttonPanel = new JPanel(new GridLayout(2, 4, 5, 5));
    buttonPanel.add(btnClear);
    buttonPanel.add(btnSolve);
    buttonPanel.add(btnLoad);
    buttonPanel.add(btnSave);
    buttonPanel.add(btnValid);
    buttonPanel.add(btnCopy);
    buttonPanel.add(btnAbout);
    buttonPanel.add(btnQuit);
    
    // Set operation to do when user presses enter.
    m_appFrame.getRootPane().setDefaultButton(btnSolve);
    
    // Add the button panel to the frame
    m_appFrame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
  }
  
  
  /**
   * Computes the background color for a puzzle cell.
   * 
   * @param location the location of the current cell
   * @return whether to use a white background
   */
  private static boolean useWhite(final int location)
  {
    boolean useWhite = false;
    int normalizeByRow = location / 3;
    switch (normalizeByRow)
    {
      case 1:
      case 4:
      case 7:
      case 10:
      case 13:
      case 16:
      case 19:
      case 22:
      case 25:
        useWhite = false;
        break;
      
      default:
        useWhite = true;
    }
    
    // Reverse for the middle set of 3 blocks
    if ((location >= 27) && (location < 54))
    {
      useWhite = !useWhite;
    }
    
    return useWhite;
  }
  
  
  /**
   * Instantiate the widgets for the GUI.
   */
  private void createWidgets()
  {
    // Initialize the Solve button
    btnSolve = new JButton("Solve");
    btnSolve.setMnemonic(KeyEvent.VK_S);
    btnSolve.setToolTipText("Solve the puzzle (shift-click to undo)");
    btnSolve.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(final java.awt.event.ActionEvent evt)
      {
        // Check if the shift button was pressed during the button click.
        // We use the shift button to undo the last solve.
        boolean bShift = ((evt.getModifiers() &
                          java.awt.event.ActionEvent.SHIFT_MASK) != 0);
        
        // Solve the puzzle or undo the last solve
        solvePuzzle(bShift);
      }
    });
    
    // Initialize the Load button
    btnLoad = new JButton("Load");
    btnLoad.setMnemonic(KeyEvent.VK_L);
    btnLoad.setToolTipText("Load a puzzle file");
    btnLoad.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(final java.awt.event.ActionEvent evt)
      {
        // Load a puzzle file
        loadPuzzle();
      }
    });
    
    // Initialize the Save button
    btnSave = new JButton("Save");
    btnSave.setMnemonic(KeyEvent.VK_E);
    btnSave.setToolTipText("Save to a file");
    btnSave.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(final java.awt.event.ActionEvent evt)
      {
        // Save the puzzle to a file
        savePuzzle();
      }
    });
    
    // Initialize the About button
    btnAbout = new JButton("About");
    btnAbout.setMnemonic(KeyEvent.VK_A);
    btnAbout.setToolTipText("About this application");
    btnAbout.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(final java.awt.event.ActionEvent evt)
      {
        // Show the About box
        showAboutMsg();
      }
    });
    
    // Initialize the Clear button
    btnClear = new JButton("Clear");
    btnClear.setMnemonic(KeyEvent.VK_C);
    btnClear.setToolTipText("Clear the puzzle");
    btnClear.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(final java.awt.event.ActionEvent evt)
      {
        // Clear the puzzle
        clearPuzzle();
      }
    });
    
    // Initialize the Valid? button
    btnValid = new JButton("Valid?");
    btnValid.setMnemonic(KeyEvent.VK_V);
    btnValid.setToolTipText("Check the validity");
    btnValid.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(final java.awt.event.ActionEvent evt)
      {
        // Check the puzzle's validity
        checkValidity();
      }
    });
    
    // Initialize the Copy button
    btnCopy = new JButton("Copy");
    btnCopy.setMnemonic(KeyEvent.VK_O);
    btnCopy.setToolTipText("Copy to the clipboard");
    btnCopy.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(final java.awt.event.ActionEvent evt)
      {
        // Copy the puzzle to the clipboard
        copyToClipboard();
      }
    });
    
    // Initialize the Quit button
    btnQuit = new JButton("Quit");
    btnQuit.setMnemonic(KeyEvent.VK_Q);
    btnQuit.setToolTipText("Quit the application");
    btnQuit.addActionListener(new java.awt.event.ActionListener()
    {
      public void actionPerformed(final java.awt.event.ActionEvent evt)
      {
        // Quit
        exitApp();
      }
    });
    
    // Instantiate the text fields
    atfPuzzle = new JTextField[81];
    final Color whiteColor = new Color(0xFFFFFF);
    final Color grayColor = new Color(0xEEEEEE);
    for (int i = 0; i < 81; ++i)
    {
      atfPuzzle[i] = new JTextField("", 1);
      atfPuzzle[i].addKeyListener(this);
      if (useWhite(i))
      {
        atfPuzzle[i].setBackground(whiteColor);
      }
      else
      {
        atfPuzzle[i].setBackground(grayColor);
      }
    }
  }
  
  
  /**
   * Check if the puzzle is valid.
   */
  private void checkValidity()
  {
    // Save the puzzle data as a string
    final String data = getPuzzleData();
    
    // Construct a puzzle
    Sudoku sudoku = new Sudoku();
    sudoku.setData(data);
    
    // Check if it's valid
    if (sudoku.isValid())
    {
      JOptionPane.showMessageDialog(m_appFrame, "The puzzle is valid", "Valid Puzzle",
          JOptionPane.INFORMATION_MESSAGE);
    }
    else
    {
      JOptionPane.showMessageDialog(m_appFrame, "The puzzle is NOT VALID!",
          "Invalid Puzzle", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  
  /**
   * Attempt to solve the puzzle.
   * 
   * @param undoSolve whether to undo the previous solve
   */
  private void solvePuzzle(final boolean undoSolve)
  {
    // Check whether to solve the puzzle or undo the last solve
    if (undoSolve)
    {
      updateCells(sudokuBackup.getData());
    }
    else
    {
      // Get the data from the puzzle
      final String data = getPuzzleData();
      
      // Construct a puzzle
      Sudoku sudoku = new Sudoku();
      sudoku.setData(data);
      
      // Check if it's valid before we start
      if (!sudoku.isValid())
      {
        JOptionPane.showMessageDialog(m_appFrame, "The puzzle is NOT VALID!",
            "Invalid Puzzle", JOptionPane.ERROR_MESSAGE);
        return;
      }
      
      // Save the current puzzle state
      sudokuBackup.setData(data);
      
      // Get the current time, so we can time the solving
      // long lTime = System.currentTimeMillis();
      
      // Try to solve it, and save whether it was solved
      final boolean bSolved = sudoku.solveWithGuess();
      
      // Print out the time it took to solve the puzzle
      // lTime = System.currentTimeMillis() - lTime;
      // System.out.println("Time to solve: " + Long.toString(lTime) + " ms");
      
      // Check again if it's valid
      if (!sudoku.isValid())
      {
        JOptionPane.showMessageDialog(m_appFrame, "The puzzle is NOT VALID!",
            "Invalid Puzzle", JOptionPane.ERROR_MESSAGE);
        return;
      }
      
      // Now update the puzzle
      updateCells(sudoku.getData());
    }
  }
  
  
  /**
   * Returns the puzzle data as a string.
   * 
   * @return the puzzle data as a string
   */
  private String getPuzzleData()
  {
    // Declare our string builder
    StringBuilder sb = new StringBuilder(81);
    
    // Iterate over the text fields
    for (int i = 0; i < 81; ++i)
    {
      // Get the text for this cell
      String value = atfPuzzle[i].getText();
      
      // Check if it's been filled in
      if ((value == null) || (value.length() < 1))
      {
        // It has not, so default to zero
        sb.append("0");
      }
      else
      {
        // It's filled in, so save the first digit
        sb.append(value.charAt(0));
      }
    }
    
    return sb.toString();
  }
  
  
  /**
   * Update the puzzle cells with new data.
   * 
   * @param line the input string
   */
  private void updateCells(final String line)
  {
    StringBuilder sb = new StringBuilder(2);
    
    // Iterate over the characters
    for (int j = 0; j < 81; ++j)
    {
      // Check if the input string is null or too short
      if ((line == null) || (line.length() <= j))
      {
        atfPuzzle[j].setText("");
      }
      else
      {
        // Check if it was solved
        char ch = line.charAt(j);
        if (ch != '0')
        {
          sb.setLength(0);
          sb.append(ch);
          atfPuzzle[j].setText(sb.toString());
        }
        else
        {
          atfPuzzle[j].setText("");
        }
      }
    }
  }
  
  
  /**
   * Saves the puzzle to a file.
   */
  private void savePuzzle()
  {
    // This is the output file
    File outputFile = null;
    
    // Create a file chooser
    JFileChooser chooser = new JFileChooser();
    
    // Add the file filters
    chooser.addChoosableFileFilter(new SudokuFilter());
    chooser.addChoosableFileFilter(chooser.getAcceptAllFileFilter());
    
    // Default to the current directory
    chooser.setCurrentDirectory(new File(currentDirectory));
    
    // Hold the file's directory temporarily
    String tempDirectory = null;
    
    // Let the user open a file and get the return value
    int returnVal = chooser.showSaveDialog(m_appFrame);
    
    // See if the user hit the "Open" button
    if (returnVal == JFileChooser.APPROVE_OPTION)
    {
       try
      {
         outputFile = chooser.getSelectedFile().getCanonicalFile();
         tempDirectory = chooser.getSelectedFile().getParent();
      }
      catch (IOException ioe)
      {
        JOptionPane.showMessageDialog(m_appFrame,
            "Error while saving file: " + ioe.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        
        outputFile = null;
      }
    }
    
    // Check if the user didn't want to open the input file, or
    // an error occurred
    if ((outputFile == null) || (outputFile.isDirectory()))
    {
      return;
    }
    
    // Save the working directory
    currentDirectory = tempDirectory;
    
    // Write out the contents of the puzzle
    BufferedWriter out = null;
    
    try
    {
      // Open the writer for the selected file
      out = new BufferedWriter(new FileWriter(outputFile));
      
      // Write the header
      out.write("# Sudoku puzzle saved on ");
      out.write(getDateAsString());
      out.write("\n");
      
      // Get the puzzle data
      String puzzleData = getPuzzleData();
      
      // Write out the puzzle data
      out.write(puzzleData + "\n");
      
      // Close the writer
      out.close();
      out = null;
    }
    catch (IOException ioe)
    {
      JOptionPane.showMessageDialog(m_appFrame,
          "Error while saving: " + ioe.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
    }
    finally
    {
      if (out != null)
      {
        try
        {
          out.close();
          out = null;
        }
        catch (IOException e)
        {
          JOptionPane.showMessageDialog(m_appFrame,
              "Error while closing: " + e.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
          
          out = null;
        }
      }
    }
  }
  
  
  /**
   * Copy the frame to the clipboard.
   */
  private void copyToClipboard()
  {
    // Save the frame's rectangle
    final Rectangle rect = m_appFrame.getBounds();
    
    try
    {
      // Instantiate a robot so we can capture the frame
      Robot robot = new Robot();
      
      // Copy the frame's image
      BufferedImage image = robot.createScreenCapture(rect);
      
      // Create an image selection, based on the image
      ImageSelection sel = new ImageSelection(image);
      
      // Copy the image selection to the clipboard
      java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
      
      // Show the success message
      JOptionPane.showMessageDialog(m_appFrame,
          "The application screen was copied to the clipboard",
          "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    catch (AWTException e)
    {
      JOptionPane.showMessageDialog(m_appFrame, "Error while copying: " + e.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  
  /**
   * Get the current date as a String.
   * 
   * @return the generated string
   */
  private static String getDateAsString()
  {
    // Construct a calendar
    Calendar cal = new GregorianCalendar();
    
    // Create our formatter
    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    
    // Apply the format to the date and return it
    return formatter.format(cal.getTime());
  }
  
  
  /**
   * Shows the About dialog box.
   */
  private void showAboutMsg()
  {
    // Show the About box
    JOptionPane.showMessageDialog(m_appFrame, getAboutText(), "About Sudoku Struggler",
        JOptionPane.INFORMATION_MESSAGE);
  }
  
  
  /**
   * Returns the text for the About box.
   * 
   * @return the text for the About box
   */
  private String getAboutText()
  {
    // This will hold the About message text
    StringBuilder sb = new StringBuilder(200);
    
    // Build the string
    sb.append("Sudoku Struggler: A Sudoku puzzle solver.  ")
      .append("Written by Mike Wallace, 2006.\n")
      .append("Available at http://mfwallace.googlepages.com/\n")
      .append("Released under the MIT license.  Free for any use.\n");
    
    // Return the About message text
    return sb.toString();
  }
  
  
  /**
   * Load a puzzle from a file.
   */
  private void loadPuzzle()
  {
    // This will hold the input file pointer
    File inputFile = null;
    
    // Create a file chooser
    JFileChooser chooser = new JFileChooser();
    
    // Add the file filters
    chooser.addChoosableFileFilter(new SudokuFilter());
    chooser.addChoosableFileFilter(chooser.getAcceptAllFileFilter());
    
    // Default to the current directory
    chooser.setCurrentDirectory(new File(currentDirectory));
    
    // Let the user open a file and get the return value
    int returnVal = chooser.showOpenDialog(m_appFrame);
    
    // Hold the file's directory temporarily
    String tempDirectory = null;
    
    // See if the user hit the "Open" button
    if (returnVal == JFileChooser.APPROVE_OPTION)
    {
       try
      {
         inputFile = chooser.getSelectedFile().getCanonicalFile();
         tempDirectory = chooser.getSelectedFile().getParent();
      }
      catch (IOException ioe)
      {
        JOptionPane.showMessageDialog(m_appFrame,
            "Error while opening: " + ioe.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
        
        inputFile = null;
      }
    }
    
    // Check if the user didn't want to open the input file, or
    // an error occurred
    if ((inputFile == null) || (inputFile.isDirectory()))
    {
      return;
    }
    
    // Save the working directory
    currentDirectory = tempDirectory;
    
    // Read the input file
    BufferedReader reader = null;
    try
    {
      // Open the input file
      reader = new BufferedReader(new FileReader(inputFile));
      String line = reader.readLine();
      while ((line != null) && (line.trim().startsWith("#")))
      {
        line = reader.readLine();
      }
      
      if (line != null)
      {
        updateCells(line);
      }
      
      reader.close();
      reader = null;
      
      inputFile = null;
    }
    catch (FileNotFoundException fnfe)
    {
      JOptionPane.showMessageDialog(m_appFrame,
          "Error finding file: " + fnfe.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
    }
    catch (IOException ioe)
    {
      JOptionPane.showMessageDialog(m_appFrame,
          "Error while loading: " + ioe.getMessage(),
          "Error", JOptionPane.ERROR_MESSAGE);
    }
    finally
    {
      if (reader != null)
      {
        try
        {
          reader.close();
        }
        catch (IOException e)
        {
          JOptionPane.showMessageDialog(m_appFrame,
              "Error while closing: " + e.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
          
          reader = null;
        }
        
        reader = null;
      }
    }
  }
  
  
  /**
   * Clear the puzzle.
   */
  private void clearPuzzle()
  {
    // Clear the cells
    updateCells(null);
  }
  
  
  /**
   * Quit the application.
   */
  private void exitApp()
  {
    System.exit(0);
  }
  
  
  /**
   * A key was typed.
   * 
   * @param key the key
   */
  public void keyTyped(final KeyEvent key)
  {
  }
  
  
  /**
   * A key was pressed.
   * 
   * @param key the key
   */
  public void keyPressed(final KeyEvent key)
  {
    // Determine which field has the focus
    int focus = -1;
    for (int i = 0; (i < 81) && (focus < 0); ++i)
    {
      // Does this text field have the focus?
      if (atfPuzzle[i].hasFocus())
      {
        // It does, so save its position
        focus = i;
      }
    }
    
    // If nothing has the focus, return
    if (focus < 0)
    {
      return;
    }
    
    // This will hold the destination focus
    int dest = -1;
    
    // Check for the arrow keys
    if (key.getKeyCode() == 37)
    {
      if ((focus % 9) == 0)
      {
        dest = focus + 8;
      }
      else
      {
        dest = focus - 1;
      }
    }
    else if (key.getKeyCode() == 38)
    {
      // Up arrow
      dest = focus - 9;
      if (dest < 0)
      {
        dest += 81;
      }
    }
    else if (key.getKeyCode() == 39)
    {
      // Right arrow
      dest = focus + 1;
      if ((dest % 9) == 0)
      {
        dest -= 9;
      }
    }
    else if (key.getKeyCode() == 40)
    {
      // Down arrow
      dest = (focus + 9) % 81;
    }
    
    // Request the focus if we modified the destination
    if (dest >= 0)
    {
      atfPuzzle[dest].requestFocus();
    }
  }
  
  
  /**
   * A key was released.
   * 
   * @param key the key
   */
  public void keyReleased(final KeyEvent key)
  {
  }
  
  
  /**
   * Center the application on the screen.
   */
  private void centerOnScreen()
  {
    // Get the size of the screen
    java.awt.Dimension screenDim = java.awt.Toolkit.getDefaultToolkit()
            .getScreenSize();

    // Determine the new location of the window
    int x = (screenDim.width - m_appFrame.getSize().width) / 2;
    int y = (screenDim.height - m_appFrame.getSize().height) / 2;

    // Move the window
    m_appFrame.setLocation(x, y);
  }
  
  
  /**
   * Use the default look and feel.
   */
  private static void initLookAndFeel()
  {
    // Use this system's look and feel
    try
    {
      javax.swing.UIManager.setLookAndFeel(
        javax.swing.UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e)
    {
      System.out.println("Exception setting the look and feel: " + e.getMessage());
    }
  }
  
  
  /**
   * Initialize the look and feel, instantiate the app, and run it.
   */
  private static void createAndRun()
  {
    initLookAndFeel();
    App app = new App();
    app.createApp();
  }
  
  
  /**
   * Make the application compatible with Apple Macs.
   * 
   * @param appName the name of the application
   */
  public static void makeMacCompatible(final String appName)
  {
    // Set the system properties that a Mac uses
    System.setProperty("apple.awt.brushMetalLook", "true");
    System.setProperty("apple.laf.useScreenMenuBar", "true");
    System.setProperty("apple.awt.showGrowBox", "true");
    System.setProperty("com.apple.mrj.application.apple.menu.about.name", appName);
  }
  
  
  /**
   * Entry point for the application.
   * 
   * @param args the arguments to the application
   */
  public static void main(final String[] args)
  {
    // Set up the Mac-related properties
    makeMacCompatible("SudokuStruggler");
    
    // Schedule a job for the event-dispatching thread
    javax.swing.SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        createAndRun();
      }
    });
  }
}
