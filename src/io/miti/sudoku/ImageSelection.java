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

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 * This class is used to copy an image to the clipboard.
 * 
 * @author Mike Wallace (mwallace at pobox.com)
 * @version 1.0
 */
public final class ImageSelection implements Transferable
{
  /**
   * The image we're storing.
   */
  private Image image = null;
  
  
  /**
   * Default constructor.
   */
  public ImageSelection()
  {
    super();
  }
  
  
  /**
   * Constructor taking an image.
   * 
   * @param srcImage the image to store
   */
  public ImageSelection(final Image srcImage)
  {
    image = srcImage;
  }
  
  
  /**
   * Return that we're supporting image transfers.
   * 
   * @return support for image flavors
   */
  public DataFlavor[] getTransferDataFlavors()
  {
    return new DataFlavor[] {DataFlavor.imageFlavor};
  }
  
  
  /**
   * Return whether we support the specified flavor.
   * 
   * @param flavor the flavor to test for support
   * @return whether the specified flavor is supported
   */
  public boolean isDataFlavorSupported(final DataFlavor flavor)
  {
    return DataFlavor.imageFlavor.equals(flavor);
  }
  
  
  /**
   * Return the image.
   * 
   * @param flavor the flavor to test for support
   * @return the image
   */
  public Object getTransferData(final DataFlavor flavor)
  {
    // Check if the flavor is supported
    if (isDataFlavorSupported(flavor))
    {
      // It is, so return the image
      return image;
    }
    
    // The requested flavor is not supported, so return null
    return null;
  }
}
