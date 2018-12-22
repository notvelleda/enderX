/*
 * EnderXComponent.java
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

package xyz.pugduddly.enderX;

import javax.swing.JComponent;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import xyz.pugduddly.enderX.ui.Desktop;
import xyz.pugduddly.enderX.ui.platinum.PlatinumDesktop;

import com.jcraft.weirdx.WeirdX;

public class EnderXComponent extends JComponent implements KeyListener, MouseListener, MouseMotionListener, Runnable {
    public boolean shouldInitialize = true;
    public static EnderXComponent inst;
    
    public Desktop desktop;
    
    public EnderXComponent() {
        super();
        inst = this;
        this.setFocusable(true);
		this.requestFocus();
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Cursor c = toolkit.createCustomCursor(EnderX.loadImage(getClass().getResourceAsStream("/pointer.png")), new Point(0, 0), "default");
        setCursor(c);
        
        Thread t = new Thread(this);
        t.setName("Component Repainter");
        t.start();
    }
    
    public void update(Graphics g){
        paint(g);
    }
    
    public void run() {
        while (true) {
            long t = System.currentTimeMillis();
            this.repaint();
            this.fpsDelay(t);
        }
    }
    
    public void paint(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        
        Point p = java.awt.MouseInfo.getPointerInfo().getLocation();
        Point p2 = this.getLocationOnScreen();
        Point mp = new Point((int) (p.getX() - p2.getX()), (int) (p.getY() - p2.getY()));
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (this.shouldInitialize) {
            g.drawString("Loading...", 10, 20);
            EnderX.loadBuiltinFonts();
            EnderX.getHomeDirectory(); // Make sure directory exists
            this.desktop = new PlatinumDesktop();
            new Thread("WeirdX") {
                @Override
                public void run() {
                    WeirdX.init_enderX(800, 600, EnderX.getContainer());
                }
            }.start();
            this.shouldInitialize = false;
        } else {
            this.desktop.paint(g, mp);
        }
    }
    
    private void fpsDelay(long lastTime) {
        try {
            while (System.currentTimeMillis() - lastTime < 1000 / 60) {
                Thread.sleep(1);
            }
        } catch (InterruptedException e) {
            fpsDelay(lastTime);
        }
    }
    
    public void keyTyped(KeyEvent e) {
        this.desktop.keyTyped(e);
    }
    
	public void keyPressed(KeyEvent e) {
        this.desktop.keyPressed(e);
    }
    
	public void keyReleased(KeyEvent e) {
        this.desktop.keyReleased(e);
    }
    
	public void mousePressed(MouseEvent e) {
        this.desktop.mousePressed(e);
    }
    
	public void mouseReleased(MouseEvent e) {
        this.desktop.mouseReleased(e);
    }
    
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
    
	public void mouseClicked(MouseEvent e) {
        this.desktop.mouseClicked(e);
    }
    
    public void mouseMoved(MouseEvent e) {
        this.desktop.mouseMoved(e);
    }
    
    public void mouseDragged(MouseEvent e) {
        this.desktop.mouseDragged(e);
    }
    
    public void setScreenSize(Dimension size) {
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);
    }
}

