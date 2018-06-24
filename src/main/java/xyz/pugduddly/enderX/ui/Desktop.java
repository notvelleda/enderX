/*
 * Desktop.java
 * 
 * Copyright 2018 Pugduddly <pugduddly@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 * 
 */

package xyz.pugduddly.enderX.ui;

import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;

/**
 * Describes an interface that a Desktop implements.
 */
public interface Desktop {
    /**
     * Paints this Desktop.
     * @param graphics The Graphics object to draw on.
     * @param mouse The current position of the mouse.
     */
    public void paint(Graphics2D graphics, Point mouse);
    
    /**
     * Called when a key is typed.
     * @param event The event object.
     */
    public void keyTyped(KeyEvent event);
    
    /**
     * Called when a key is pressed.
     * @param event The event object.
     */
    public void keyPressed(KeyEvent event);
    
    /**
     * Called when a key is released.
     * @param event The event object.
     */
    public void keyReleased(KeyEvent event);
    
    /**
     * Called when the mouse is pressed.
     * @param event The event object.
     */
    public void mousePressed(MouseEvent event);
    
    /**
     * Called when the mouse is released.
     * @param event The event object.
     */
    public void mouseReleased(MouseEvent event);
    
    /**
     * Called when the mouse is clicked.
     * @param event The event object.
     */
    public void mouseClicked(MouseEvent event);
    
    /**
     * Called when the mouse is moved.
     * @param event The event object.
     */
    public void mouseMoved(MouseEvent event);
    
    /**
     * Called when the mouse is dragged.
     * @param event The event object.
     */
    public void mouseDragged(MouseEvent event);
    
    /**
     * Sets the wallpaper to the supplied file.
     * @param file The file to set as the wallpaper.
     */
    public void setWallpaper(File file);
    
    /**
     * Get the currently set wallpaper.
     * @return the location of the current wallpaper file.
     */
    public File getWallpaper();
    
    /**
     * Called when a new window is added to a WindowManager.
     * @param window The window that was added.
     */
    public void newWindowAdded(Window window);
    
    /**
     * Called when you set the screen size with {@link xyz.pugduddly.enderX.EnderX#setScreenSize(Dimension)}.
     * @param size The new size of the screen.
     */
    public void setScreenSize(Dimension size);
    
    /**
     * Gets the current WindowManager.
     * @return the current WindowManager.
     */
    public WindowManager getWindowManager();
}

