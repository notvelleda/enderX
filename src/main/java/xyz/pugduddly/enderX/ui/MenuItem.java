/*
 * MenuItem.java
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

/**
 * A menu item.
 * MenuItems are added to Menu objects.
 */
public class MenuItem {
	private String name;
    private boolean disabled = false;
    
    /**
     * Creates a new MenuItem object.
     * @param name The name of the menu item.
     */
    public MenuItem(String name) {
        this.name = name;
    }
    
    /**
     * Called when the menu item is clicked.
     * @see Menu
     */
    public void actionPerformed() {}
    
    /**
     * Gets the name of this menu item.
     * @return the name of this menu item.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Sets whether this menu item is disabled.
     * @param disabled `true` if this menu item should be disabled, `false` otherwise.
     * @return the MenuItem object, for convenience.
     */
    public MenuItem setDisabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }
    
    /**
     * Gets whether this menu item is disabled.
     * @return `true` if this menu item should be disabled, `false` otherwise.
     */
    public boolean isDisabled() {
        return this.disabled;
    }
}

