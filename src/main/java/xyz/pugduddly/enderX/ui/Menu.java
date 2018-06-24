/*
 * Menu.java
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

import java.awt.Image;
import java.util.ArrayList;

/**
 * A menu.
 * Menus are displayed in the system menu bar.
 */
public class Menu {
	private String name;
    private Image img;
    private boolean selected = false;
    private ArrayList<MenuItem> items = new ArrayList<MenuItem>();
    
    /**
     * Creates a new Menu instance.
     * @param name The name of the menu.
     */
    public Menu(String name) {
        this.name = name;
    }
    
    /**
     * Creates a new Menu instance.
     * @param img The image to be displayed in place of text.
     */
    public Menu(Image img) {
        this.img = img;
    }
    
    /**
     * Sets this menu to be selected.
     * @return the Menu object, for convenience.
     */
    public Menu select() {
        this.selected = true;
        return this;
    }
    
    /**
     * Sets this menu to not be selected.
     * @return the Menu object, for convenience.
     */
    public Menu deselect() {
        this.selected = false;
        return this;
    }
    
    /**
     * Gets whether this menu is selected.
     * @return `true` if this menu is selected, `false` otherwise.
     */
    public boolean isSelected() {
        return this.selected;
    }
    
    /**
     * Adds a MenuItem to this menu.
     * @param item The menu item to be added.
     * @return the Menu object, for convenience.
     * @see MenuItem
     */
    public Menu addItem(MenuItem item) {
        this.items.add(item);
        return this;
    }
    
    /**
     * Adds a MenuItem to this menu at index `index`.
     * @param item The menu item to be added.
     * @return the Menu object, for convenience.
     * @see MenuItem
     */
    public Menu addItem(MenuItem item, int index) {
        this.items.add(index, item);
        return this;
    }
    
    /**
     * Gets the MenuItem at index `index`.
     * @param index The index of the MenuItem to get.
     * @return the MenuItem at index `index`.
     * @see MenuItem
     */
    public MenuItem get(int index) {
        return this.items.get(index);
    }
    
    /**
     * Gets an array of all MenuItems added to this Menu.
     * @return the array of MenuItems.
     * @see MenuItem
     */
    public MenuItem[] getItems() {
        return this.items.toArray(new MenuItem[this.items.size()]);
    }
    
    /**
     * Gets the name of this Menu.
     * @return the name of this Menu, or `null` if it has none.
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * Gets the Image to be shown in place of text.
     * @return the Image to be shown, or `null` if no image has been registered.
     */
    public Image getImage() {
        return this.img;
    }
}

