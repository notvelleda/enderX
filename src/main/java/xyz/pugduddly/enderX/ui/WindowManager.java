/*
 * WindowManager.java
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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

import xyz.pugduddly.enderX.EnderX;

/**
 * Simple Window manager.
 */
public class WindowManager {
	private ArrayList<Window> windows = new ArrayList<Window>();
    
    /**
     * Adds window.
     * @param window The window to be added.
     * @return the WindowManager object.
     */
    public WindowManager addWindow(Window window) {
        Window oldTopWindow = this.getTopWindow();
        this.windows.add(window);
        EnderX.getDesktop().newWindowAdded(window);
        Window newTopWindow = this.getTopWindow();
        if (oldTopWindow != newTopWindow) {
            if (oldTopWindow != null) {
                oldTopWindow.removeFocus();
                for (WindowListener l : oldTopWindow.getWindowListeners())
                    l.windowDeactivated(new WindowEvent((java.awt.Window) EnderX.getContainer(), WindowEvent.WINDOW_DEACTIVATED));
            }
            if (newTopWindow != null) {
                newTopWindow.addFocus();
                for (WindowListener l : newTopWindow.getWindowListeners())
                    l.windowActivated(new WindowEvent((java.awt.Window) EnderX.getContainer(), WindowEvent.WINDOW_ACTIVATED));
            }
        }
        return this;
    }
    
    /**
     * Removes window.
     * @param window The window to be removed.
     * @return the WindowManager object.
     */
    public WindowManager removeWindow(Window window) {
        Window oldTopWindow = this.getTopWindow();
        int index = this.getWindowIndex(window);
        if (index >= 0) {
            this.windows.remove(index);
        }
        Window newTopWindow = this.getTopWindow();
        if (oldTopWindow != newTopWindow) {
            if (oldTopWindow != null) {
                oldTopWindow.removeFocus();
                for (WindowListener l : oldTopWindow.getWindowListeners())
                    l.windowDeactivated(new WindowEvent((java.awt.Window) EnderX.getContainer(), WindowEvent.WINDOW_DEACTIVATED));
            }
            if (newTopWindow != null) {
                newTopWindow.addFocus();
                for (WindowListener l : newTopWindow.getWindowListeners())
                    l.windowActivated(new WindowEvent((java.awt.Window) EnderX.getContainer(), WindowEvent.WINDOW_ACTIVATED));
            }
        }
        return this;
    }
    
    /**
     * Moves window to the top.
     * @param window The window to be moved to the top.
     * @return the WindowManager object.
     */
    public WindowManager moveToTop(Window window) {
        Window oldTopWindow = this.getTopWindow();
        int index = this.getWindowIndex(window);
        if (index >= 0) {
            this.windows.remove(index);
            this.windows.add(window);
        }
        Window newTopWindow = this.getTopWindow();
        if (oldTopWindow != newTopWindow) {
            if (oldTopWindow != null) {
                oldTopWindow.removeFocus();
                for (WindowListener l : oldTopWindow.getWindowListeners())
                    l.windowDeactivated(new WindowEvent((java.awt.Window) EnderX.getContainer(), WindowEvent.WINDOW_DEACTIVATED));
            }
            if (newTopWindow != null) {
                newTopWindow.addFocus();
                for (WindowListener l : newTopWindow.getWindowListeners())
                    l.windowActivated(new WindowEvent((java.awt.Window) EnderX.getContainer(), WindowEvent.WINDOW_ACTIVATED));
            }
        }
        return this;
    }
    
    /**
     * Gets the array of Windows added to the WindowManager.
     * @return the array of Windows.
     */
    public Window[] getWindows() {
        return this.windows.toArray(new Window[this.windows.size()]);
    }
    
    /**
     * Gets the Window that is set to be on top.
     * @return the Window that is on top.
     */
    public Window getTopWindow() {
        if (this.windows.size() > 0)
            for (int i = this.windows.size() - 1; i >= 0; i --) {
                if (!this.windows.get(i).isVisible()) continue;
                return this.windows.get(i);
            }
        return null;
    }
    
    /**
     * Get the index of a Window in the array.
     * @return the index.
     */
    public int getWindowIndex(Window window) {
        return this.windows.indexOf(window);
    }
    
    /**
     * Get whether a Window is on top.
     * @param window The window.
     * @return whether the Window is on top.
     */
    public boolean isWindowOnTop(Window window) {
        if (!window.isVisible()) return false;
        int index = getWindowIndex(window);
        for (int i = this.windows.size() - 1; i >= 0; i --) {
            if (!this.windows.get(i).isVisible()) continue;
            return i == index;
        }
        return false;
    }
}

