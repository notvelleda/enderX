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

public class Menu {
	private String name;
    private Image img;
    private boolean selected = false;
    private ArrayList<MenuItem> items = new ArrayList<MenuItem>();
    
    public Menu(String name) {
        this.name = name;
    }
    
    public Menu(Image img) {
        this.img = img;
    }
    
    public Menu select() {
        this.selected = true;
        return this;
    }
    
    public Menu deselect() {
        this.selected = false;
        return this;
    }
    
    public boolean isSelected() {
        return this.selected;
    }
    
    public Menu addItem(MenuItem item) {
        this.items.add(item);
        return this;
    }
    
    public MenuItem get(int index) {
        return this.items.get(index);
    }
    
    public ArrayList<MenuItem> getItems() {
        return this.items;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Image getImage() {
        return this.img;
    }
}

