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

public class MenuItem {
	private String name;
    private boolean disabled = false;
    
    public MenuItem(String name) {
        this.name = name;
    }
    
    public void actionPerformed() {}
    
    public String getName() {
        return this.name;
    }
    
    public MenuItem setDisabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }
    
    public boolean isDisabled() {
        return this.disabled;
    }
}

