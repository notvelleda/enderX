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
import java.util.ArrayList;
import java.util.Random;

import xyz.pugduddly.enderX.EnderX;

public class WindowManager {
	private ArrayList<Window> windows = new ArrayList<Window>();
    
    public WindowManager addWindow(Window window) {
        this.windows.add(window);
        EnderX.getDesktop().newWindowAdded(window);
        return this;
    }
    
    public WindowManager removeWindow(Window window) {
        int index = this.getWindowIndex(window);
        if (index >= 0) {
            this.windows.remove(index);
        }
        return this;
    }
    
    public WindowManager moveToTop(Window window) {
        int index = this.getWindowIndex(window);
        if (index >= 0) {
            this.windows.remove(index);
            this.windows.add(window);
        }
        return this;
    }
    
    public ArrayList<Window> getWindows() {
        return this.windows;
    }
    
    public Window getTopWindow() {
        if (this.windows.size() > 0)
            for (int i = this.windows.size() - 1; i >= 0; i --) {
                if (!this.windows.get(i).isVisible()) continue;
                
                return this.windows.get(i);
            }
        return null;
    }
    
    public int getWindowIndex(Window window) {
        return this.windows.indexOf(window);
    }
    
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

