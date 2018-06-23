/*
 * Window.java
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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowListener;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import xyz.pugduddly.enderX.EnderX;
import xyz.pugduddly.enderX.ui.Component;

public class Window {
	private String title;
    private Dimension size;
    private Dimension originalSize;
    private Point position;
    private Point originalPosition;
    private BufferedImage canvas;
    private BufferedImage cptCanvas;
    private boolean close = true;
    private boolean resize = true;
    private boolean roll = true;
    private boolean canClose = false;
    private boolean dragging = false;
    private boolean resizing = false;
    private boolean rolled = false;
    private boolean maximized = false;
    private boolean visible = false;
    private Point dragOrigin;
    private Point resizeOrigin;
    private ArrayList<Menu> menus = new ArrayList<Menu>();
    private ArrayList<java.awt.Component> components = new ArrayList<java.awt.Component>();
    private ArrayList<KeyListener> keyListeners = new ArrayList<KeyListener>();
    private ArrayList<MouseListener> mouseListeners = new ArrayList<MouseListener>();
    private ArrayList<MouseMotionListener> mouseMotionListeners = new ArrayList<MouseMotionListener>();
    private ArrayList<WindowListener> windowListeners = new ArrayList<WindowListener>();
    private ArrayList<ComponentListener> componentListeners = new ArrayList<ComponentListener>();
    
    public Window(String title) {
        this.title = title;
        this.setSize(new Dimension(320, 240));
        this.setPosition(null);
        EnderX.getDesktop().getWindowManager().addWindow(this);
    }
    
    public Window setSize(Dimension size) {
        this.size = size;
        BufferedImage img = new BufferedImage((int) size.getWidth(), (int) size.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage img2 = new BufferedImage((int) size.getWidth(), (int) size.getHeight(), BufferedImage.TYPE_INT_ARGB);
        if (this.canvas == null) {
            Graphics2D g = img.createGraphics();
            g.setPaint(Color.WHITE);
            g.fillRect(0, 0, img.getWidth(null), img.getHeight(null));
            g.dispose();
            this.canvas = img;
        } else {
            Graphics2D g = img.createGraphics();
            g.setPaint(Color.WHITE);
            g.fillRect(0, 0, img.getWidth(null), img.getHeight(null));
            g.drawImage(this.canvas, 0, 0, null);
            g.dispose();
            this.canvas = img;
        }
        Graphics2D g = img2.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g.fillRect(0, 0, img2.getWidth(null), img2.getHeight(null));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g.dispose();
        this.cptCanvas = img2;
        return this;
    }
    
    public Window setSize(int width, int height) {
        return this.setSize(new Dimension(width, height));
    }
    
    public Dimension getSize() {
        return this.size;
    }
    
    public Rectangle getBounds() {
        Dimension size = this.getSize();
        return new Rectangle((int) size.getWidth(), (int) size.getHeight());
    }
    
    public Window setPosition(Point pos) {
        this.position = pos;
        return this;
    }
    
    public Point getPosition() {
        return this.position;
    }
    
    public Window setLocation(Point pos) {
        this.position = pos;
        return this;
    }
    
    public Window setLocation(int x, int y) {
        this.position = new Point(x, y);
        return this;
    }
    
    public Point getLocation() {
        return this.position;
    }
    
    public Graphics2D getGraphics() {
        Graphics2D g = this.canvas.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setPaint(Color.BLACK);
        g.setFont(EnderX.getFont("Geneva", 12));
        return g;
    }
    
    public Window setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public Window setClosable(boolean close) {
        this.close = close;
        return this;
    }
    
    public boolean canClose() {
        return this.close;
    }
    
    public Window setResizable(boolean resize) {
        this.resize = resize;
        return this;
    }
    
    public boolean canResize() {
        return this.resize;
    }
    
    public Window setRollable(boolean roll) {
        this.roll = roll;
        return this;
    }
    
    public boolean canRoll() {
        return this.roll;
    }
    
    public BufferedImage getCanvas() {
        return this.canvas;
    }
    
    public BufferedImage getComponentCanvas() {
        return this.cptCanvas;
    }
    
    public Window close() {
        this.canClose = true;
        this.onClose();
        System.gc();
        return this;
    }
    
    public Window stopClose() {
        this.canClose = false;
        return this;
    }
    
    public boolean shouldClose() {
        return this.canClose;
    }
    
    public boolean destroyed() {
        return this.canClose;
    }
    
    public void onClose() {}
    
    public Window setDragging(boolean dragging) {
        this.dragging = dragging;
        return this;
    }
    
    public boolean isDragging() {
        return this.dragging;
    }
    
    public Window setDragOrigin(Point origin) {
        this.dragOrigin = origin;
        return this;
    }
    
    public Point getDragOrigin() {
        return this.dragOrigin;
    }
    
    public Window setRoll(boolean rolled) {
        this.rolled = rolled;
        return this;
    }
    
    public boolean isRolled() {
        return this.rolled;
    }
    
    public ArrayList<Menu> getMenus() {
        return this.menus;
    }
    
    public Window add(java.awt.Component component) {
        this.components.add(component);
        if (Component.class.isAssignableFrom(component.getClass()))
            ((Component) component).setParentWindow(this);
        return this;
    }
    
    public Window remove(java.awt.Component component) {
        this.components.remove(component);
        return this;
    }
    
    public ArrayList<java.awt.Component> getComponents() {
        return this.components;
    }
    
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
    
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    
    public void onResize() {}
    
    public Window setResizing(boolean resizing) {
        this.resizing = resizing;
        return this;
    }
    
    public boolean isResizing() {
        return this.resizing;
    }
    
    public Window setResizeOrigin(Point origin) {
        this.resizeOrigin = origin;
        return this;
    }
    
    public Point getResizeOrigin() {
        return this.resizeOrigin;
    }
    
    public Window setMaximized(boolean maximized) {
        this.maximized = maximized;
        return this;
    }
    
    public boolean isMaximized() {
        return this.maximized;
    }
    
    public Window setOriginalSize(Dimension originalSize) {
        this.originalSize = originalSize;
        return this;
    }
    
    public Dimension getOriginalSize() {
        return this.originalSize;
    }
    
    public Window setOriginalPosition(Point pos) {
        this.originalPosition = pos;
        return this;
    }
    
    public Point getOriginalPosition() {
        return this.originalPosition;
    }
    
    public boolean requestComponentFocus(java.awt.Component component) {
        if (component != null) {
            boolean giveFocus = false;
            for (java.awt.Component cpt : this.getComponents())
                if (cpt == component)
                    giveFocus = true;
            if (giveFocus)
                for (java.awt.Component cpt : this.getComponents())
                    if (Component.class.isAssignableFrom(cpt.getClass()))
                        ((Component) cpt).removeFocus();
            return giveFocus;
        } else {
            for (java.awt.Component cpt : this.getComponents())
                if (Component.class.isAssignableFrom(cpt.getClass()))
                    ((Component) cpt).removeFocus();
            return true;
        }
    }
    
    public Window addKeyListener(KeyListener keyListener) {
        this.keyListeners.add(keyListener);
        return this;
    }
    
    public Window removeKeyListener(KeyListener keyListener) {
        this.keyListeners.remove(keyListener);
        return this;
    }
    
    public ArrayList<KeyListener> getKeyListeners() {
        return this.keyListeners;
    }
    
    public Window addMouseListener(MouseListener mouseListener) {
        this.mouseListeners.add(mouseListener);
        return this;
    }
    
    public Window removeMouseListener(MouseListener mouseListener) {
        this.mouseListeners.remove(mouseListener);
        return this;
    }
    
    public ArrayList<MouseListener> getMouseListeners() {
        return this.mouseListeners;
    }
    
    public Window addMouseMotionListener(MouseMotionListener mouseMotionListener) {
        this.mouseMotionListeners.add(mouseMotionListener);
        return this;
    }
    
    public Window removeMouseMotionListener(MouseMotionListener mouseMotionListener) {
        this.mouseMotionListeners.remove(mouseMotionListener);
        return this;
    }
    
    public ArrayList<MouseMotionListener> getMouseMotionListeners() {
        return this.mouseMotionListeners;
    }
    
    public Window addComponentListener(ComponentListener componentListener) {
        this.componentListeners.add(componentListener);
        return this;
    }
    
    public Window removeComponentListener(ComponentListener componentListener) {
        this.componentListeners.remove(componentListener);
        return this;
    }
    
    public ArrayList<ComponentListener> getComponentListeners() {
        return this.componentListeners;
    }
    
    public Window addWindowListener(WindowListener windowListener) {
        this.windowListeners.add(windowListener);
        return this;
    }
    
    public Window removeWindowListener(WindowListener windowListener) {
        this.windowListeners.remove(windowListener);
        return this;
    }
    
    public ArrayList<WindowListener> getWindowListeners() {
        return this.windowListeners;
    }
    
    public Window setVisible(boolean visible) {
        if (!this.visible && visible) {
            if (this.getPosition() == null || (this.getPosition().getX() == 0 && this.getPosition().getY() == 0)) {
                try {
                    Dimension size = EnderX.getScreenSize();
                    int maxX = (int) size.getWidth() - (int) this.getSize().getWidth() - 70;
                    int maxY = (int) size.getHeight() - (int) this.getSize().getHeight() - 70;
                    Random r = new Random();
                    Point point = new Point(35 + r.nextInt(maxX), 35 + r.nextInt(maxY));
                    this.setPosition(point);
                } catch (Exception e) {
                    e.printStackTrace();
                    this.setPosition(new Point(20, 20));
                }
            }
        }
        
        this.visible = visible;
        if (visible) {
            for (ComponentListener l : this.getComponentListeners())
                l.componentShown(new ComponentEvent(EnderX.getComponent(), ComponentEvent.COMPONENT_SHOWN));
        } else {
            for (ComponentListener l : this.getComponentListeners())
                l.componentHidden(new ComponentEvent(EnderX.getComponent(), ComponentEvent.COMPONENT_HIDDEN));
        }
        return this;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
}

