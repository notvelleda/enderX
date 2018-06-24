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

/**
 * A Window. That's it.
 */
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
    
    /**
     * Creates a new Window instance.
     * @param title The title of the Window.
     */
    public Window(String title) {
        this.title = title;
        this.setSize(new Dimension(320, 240));
        this.setPosition(null);
        EnderX.getDesktop().getWindowManager().addWindow(this);
    }
    
    /**
     * Sets the size of a Window.
     * @param size The size.
     * @return the Window object, for convenience.
     */
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
    
    /**
     * Alternative to {@link #setSize(Dimension)}.
     * @param width The width.
     * @param height The height.
     * @return the Window object, for convenience.
     */
    public Window setSize(int width, int height) {
        return this.setSize(new Dimension(width, height));
    }
    
    /**
     * Gets the size of a Window.
     * @return the size of the Window.
     */
    public Dimension getSize() {
        return this.size;
    }
    
    /**
     * Alternative to {@link #getSize()}.
     * @return the size of the Window.
     */
    public Rectangle getBounds() {
        Dimension size = this.getSize();
        return new Rectangle((int) size.getWidth(), (int) size.getHeight());
    }
    
    /**
     * Sets the position of a Window.
     * @param pos The new position of the Window.
     * @return the Window object, for convenience.
     */
    public Window setPosition(Point pos) {
        this.position = pos;
        return this;
    }
    
    /**
     * Gets the position of a Window.
     * @return the position of the Window.
     */
    public Point getPosition() {
        return this.position;
    }
    
    /**
     * Alternative to {@link #setPosition(Point)}
     * @param pos The new position of the Window.
     * @return the Window object, for convenience.
     */
    public Window setLocation(Point pos) {
        this.position = pos;
        return this;
    }
    
    /**
     * Alternative to {@link #setPosition(Point)}
     * @param x The new X position of the Window.
     * @param y The new Y position of the Window.
     * @return the Window object, for convenience.
     */
    public Window setLocation(int x, int y) {
        this.position = new Point(x, y);
        return this;
    }
    
    /**
     * Alternative to {@link #getPosition()}
     * @return the Position of the Window.
     */
    public Point getLocation() {
        return this.position;
    }
    
    /**
     * Creates a graphics context for this Window.
     * @return a graphics context for this Window.
     */
    public Graphics2D getGraphics() {
        Graphics2D g = this.canvas.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setPaint(Color.BLACK);
        g.setFont(EnderX.getFont("Geneva", 12));
        return g;
    }
    
    /**
     * Sets the title of a Window.
     * @param title The new title of the Window.
     * @return the Window object, for convenience.
     */
    public Window setTitle(String title) {
        this.title = title;
        return this;
    }
    
    /**
     * Gets the title of a Window.
     * @return the title of the Window.
     */
    public String getTitle() {
        return this.title;
    }
    
    /**
     * Sets whether this Window is closable by the user.
     * @param close `true` if the Window is closable, `false` otherwise.
     * @return the Window object, for convenience.
     */
    public Window setClosable(boolean close) {
        this.close = close;
        return this;
    }
    
    /**
     * Gets whether this Window is closable by the user.
     * @return `true` if the Window is closable, `false` otherwise.
     */
    public boolean canClose() {
        return this.close;
    }
    
    /**
     * Sets whether this Window is resizable by the user.
     * @param resize `true` if the Window is resizable, `false` otherwise.
     * @return the Window object, for convenience.
     */
    public Window setResizable(boolean resize) {
        this.resize = resize;
        return this;
    }
    
    /**
     * Gets whether this Window is resizable by the user.
     * @return `true` if the Window is resizable by the user, `false` other
     */
    public boolean canResize() {
        return this.resize;
    }
    
    /**
     * Sets whether this Window is able to be rolled up by the user.
     * @param roll `true` if the Window is able to be rolled up, `false` otherwise.
     * @return the Window object, for convenience.
     */
    public Window setRollable(boolean roll) {
        this.roll = roll;
        return this;
    }
    
    /**
     * Gets whether this Window is able to be rolled up by the user.
     * @return `true` if the Window is able to be rolled up by the user, `false` other
     */
    public boolean canRoll() {
        return this.roll;
    }
    
    /**
     * Get the BufferedImage that {@link #getGraphics()} is drawing to.
     * @return the BufferedImage.
     */
    public BufferedImage getCanvas() {
        return this.canvas;
    }
    
    /**
     * Get the BufferedImage that the current Desktop should be drawing Components to.
     * @return the BufferedImage.
     */
    public BufferedImage getComponentCanvas() {
        return this.cptCanvas;
    }
    
    /**
     * Closes the Window.
     * @return the Window object, for convenience.
     */
    public Window close() {
        this.canClose = true;
        this.onClose();
        System.gc();
        return this;
    }
    
    /**
     * Attempt to cancel a Window from closing. This may not work.
     * @return the Window object, for convenience.
     */
    public Window stopClose() {
        this.canClose = false;
        return this;
    }
    
    /**
     * Get whether this Window should be removed from the current WindowManager.
     * @return `true` if this Window should be removed, `false` otherwise.
     */
    public boolean shouldClose() {
        return this.canClose;
    }
    
    /**
     * Get whether this Window has been removed from the current WindowManager.
     * @return `true` if this Window has been removed, `false` otherwise.
     */
    public boolean destroyed() {
        return this.canClose;
    }
    
    /**
     * Called when this Window is closed with {@link #close()}.
     */
    public void onClose() {}
    
    /**
     * Used by {@link xyz.pugduddly.enderX.ui.platinum.PlatinumDesktop}. Don't use it, please.
     */
    public Window setDragging(boolean dragging) {
        this.dragging = dragging;
        return this;
    }
    
    /**
     * Gets if Window is being dragged by the user.
     * @return `true` if the Window is being dragged, `false` otherwise.
     */
    public boolean isDragging() {
        return this.dragging;
    }
    
    /**
     * Used by {@link xyz.pugduddly.enderX.ui.platinum.PlatinumDesktop}. Don't use it, please.
     */
    public Window setDragOrigin(Point origin) {
        this.dragOrigin = origin;
        return this;
    }
    
    /**
     * Used by {@link xyz.pugduddly.enderX.ui.platinum.PlatinumDesktop}. Don't use it, please.
     */
    public Point getDragOrigin() {
        return this.dragOrigin;
    }
    
    /**
     * Sets whether a Window is rolled up.
     * @param rolled `true` if the Window is rolled up, `false` otherwise.
     * @return the Window object, for convenience.
     */
    public Window setRoll(boolean rolled) {
        this.rolled = rolled;
        return this;
    }
    
    /**
     * Gets whether a Window is rolled up.
     * @return `true` if the Window is rolled up, `false` otherwise.
     */
    public boolean isRolled() {
        return this.rolled;
    }
    
    /**
     * Get the ArrayList of menus associated with this Window.
     * @return the ArrayList of menus associated with this Window.
     * @see Menu MenuItem
     */
    public ArrayList<Menu> getMenus() {
        return this.menus;
    }
    
    /**
     * Appends the specified component to this Window.
     * @param component The Component to be added.
     * @return the Window object, for convenience.
     * @see java.awt.Component xyz.pugduddly.enderX.ui.Component
     */
    public Window add(java.awt.Component component) {
        this.components.add(component);
        if (Component.class.isAssignableFrom(component.getClass()))
            ((Component) component).setParentWindow(this);
        return this;
    }
    
    /**
     * Removes the specified component from this Window.
     * @param component The Component to be removed.
     * @return the Window object, for convenience.
     * @see java.awt.Component xyz.pugduddly.enderX.ui.Component
     */
    public Window remove(java.awt.Component component) {
        this.components.remove(component);
        return this;
    }
    
    /**
     * Gets all the components in this Window.
     * @return an array of all the components in this Window
     */
    public java.awt.Component[] getComponents() {
        return this.components.toArray(new java.awt.Component[this.components.size()]);
    }
    
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
    
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    
    /**
     * Called when this Window is resized.
     */
    public void onResize() {}
    
    /**
     * Used by {@link xyz.pugduddly.enderX.ui.platinum.PlatinumDesktop}. Don't use it, please.
     */
    public Window setResizing(boolean resizing) {
        this.resizing = resizing;
        return this;
    }
    
    /**
     * Gets whether this Window is being resized.
     * @return `true` if the Window is being resized, `false` otherwise.
     */
    public boolean isResizing() {
        return this.resizing;
    }
    
    /**
     * Used by {@link xyz.pugduddly.enderX.ui.platinum.PlatinumDesktop}. Don't use it, please.
     */
    public Window setResizeOrigin(Point origin) {
        this.resizeOrigin = origin;
        return this;
    }
    
    /**
     * Used by {@link xyz.pugduddly.enderX.ui.platinum.PlatinumDesktop}. Don't use it, please.
     */
    public Point getResizeOrigin() {
        return this.resizeOrigin;
    }
    
    /**
     * Used by {@link xyz.pugduddly.enderX.ui.platinum.PlatinumDesktop}. Don't use it, please.
     */
    public Window setMaximized(boolean maximized) {
        this.maximized = maximized;
        return this;
    }
    
    /**
     * Gets whether this Window is maximized.
     * @return `true` if the Window is maximized, `false` otherwise.
     */
    public boolean isMaximized() {
        return this.maximized;
    }
    
    /**
     * Used by {@link xyz.pugduddly.enderX.ui.platinum.PlatinumDesktop}. Don't use it, please.
     */
    public Window setOriginalSize(Dimension originalSize) {
        this.originalSize = originalSize;
        return this;
    }
    
    /**
     * Gets the original size of the Window, if maximized
     * @return the original size of the Window
     * @see java.awt.Dimension
     */
    public Dimension getOriginalSize() {
        return this.originalSize;
    }
    
    /**
     * Used by {@link xyz.pugduddly.enderX.ui.platinum.PlatinumDesktop}. Don't use it, please.
     */
    public Window setOriginalPosition(Point pos) {
        this.originalPosition = pos;
        return this;
    }
    
    /**
     * Gets the original position of the Window, if maximized
     * @return the original position of the Window
     * @see java.awt.Dimension
     */
    public Point getOriginalPosition() {
        return this.originalPosition;
    }
    
    /**
     * Requests focus to be given to a Component. The component must be a subclass of {@link xyz.pugduddly.enderX.ui.Component}.
     * @param component The component to give focus to.
     * @return whether the component has sucessfully been given focus.
     * @see java.awt.Component xyz.pugduddly.enderX.ui.Component
     */
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
    
    /**
     * Adds the specified key listener to receive key events from this window. If keyListener is null, no exception is thrown and no action is performed.
     * @param keyListener the key listener.
     * @return the Window object, for convenience.
     * @see java.awt.KeyListener
     */
    public Window addKeyListener(KeyListener keyListener) {
        if (keyListener == null) return null;
        this.keyListeners.add(keyListener);
        return this;
    }
    
    /**
     * Removes the specified key listener so that it no longer receives events from this window.
     * This method performs no function, nor does it throw an exception, if the listener specified by the argument was not previously added to this window.
     * If keyListener is null, no exception is thrown and no action is performed.
     * @param keyListener the key listener.
     * @return the Window object, for convenience.
     * @see java.awt.KeyListener
     */
    public Window removeKeyListener(KeyListener keyListener) {
        if (keyListener == null) return null;
        this.keyListeners.remove(keyListener);
        return this;
    }
    
    /**
     * Returns an array of all the key listeners registered on this window.
     * @return all of this window's `KeyListener`s or an empty array if no key listeners are currently registered
     * @see java.awt.KeyListener
     */
    public KeyListener[] getKeyListeners() {
        return this.keyListeners.toArray(new KeyListener[this.keyListeners.size()]);
    }
    
    /**
     * Adds the specified mouse listener to receive key events from this window. If mouseListener is null, no exception is thrown and no action is performed.
     * @param mouseListener the mouse listener.
     * @return the Window object, for convenience.
     * @see java.awt.MouseListener
     */
    public Window addMouseListener(MouseListener mouseListener) {
        if (mouseListener == null) return null;
        this.mouseListeners.add(mouseListener);
        return this;
    }
    
    /**
     * Removes the specified mouse listener so that it no longer receives events from this window.
     * This method performs no function, nor does it throw an exception, if the listener specified by the argument was not previously added to this window.
     * If mouseListener is null, no exception is thrown and no action is performed.
     * @param mouseListener the mouse listener.
     * @return the Window object, for convenience.
     * @see java.awt.MouseListener
     */
    public Window removeMouseListener(MouseListener mouseListener) {
        if (mouseListener == null) return null;
        this.mouseListeners.remove(mouseListener);
        return this;
    }
    
    /**
     * Returns an array of all the mouse listeners registered on this window.
     * @return all of this window's `MouseListener`s or an empty array if no mouse listeners are currently registered
     * @see java.awt.MouseListener
     */
    public MouseListener[] getMouseListeners() {
        return this.mouseListeners.toArray(new MouseListener[this.mouseListeners.size()]);
    }
    
    /**
     * Adds the specified mouse motion listener to receive key events from this window. If mouseMotionListener is null, no exception is thrown and no action is performed.
     * @param mouseMotionListener the mouse motion listener.
     * @return the Window object, for convenience.
     * @see java.awt.MouseMotionListener
     */
    public Window addMouseMotionListener(MouseMotionListener mouseMotionListener) {
        if (mouseMotionListener == null) return null;
        this.mouseMotionListeners.add(mouseMotionListener);
        return this;
    }
    
    /**
     * Removes the specified mouse motion listener so that it no longer receives events from this window.
     * This method performs no function, nor does it throw an exception, if the listener specified by the argument was not previously added to this window.
     * If mouseMotionListener is null, no exception is thrown and no action is performed.
     * @param mouseMotionListener the mouse motion listener.
     * @return the Window object, for convenience.
     * @see java.awt.MouseMotionListener
     */
    public Window removeMouseMotionListener(MouseMotionListener mouseMotionListener) {
        if (mouseMotionListener == null) return null;
        this.mouseMotionListeners.remove(mouseMotionListener);
        return this;
    }
    
    /**
     * Returns an array of all the mouse motion listeners registered on this window.
     * @return all of this window's `MouseMotionListener`s or an empty array if no mouse motion listeners are currently registered
     * @see java.awt.MouseMotionListener
     */
    public MouseMotionListener[] getMouseMotionListeners() {
        return this.mouseMotionListeners.toArray(new MouseMotionListener[this.mouseMotionListeners.size()]);
    }
    
    /**
     * Adds the specified component listener to receive key events from this window.
     * If componentListener is null, no exception is thrown and no action is performed.
     * @param componentListener the component listener.
     * @return the Window object, for convenience.
     * @see java.awt.ComponentListener
     */
    public Window addComponentListener(ComponentListener componentListener) {
        if (componentListener == null) return null;
        this.componentListeners.add(componentListener);
        return this;
    }
    
    /**
     * Removes the specified component listener so that it no longer receives events from this window.
     * This method performs no function, nor does it throw an exception, if the listener specified by the argument was not previously added to this window.
     * If componentListener is null, no exception is thrown and no action is performed.
     * @param componentListener the component listener.
     * @return the Window object, for convenience.
     * @see java.awt.ComponentListener
     */
    public Window removeComponentListener(ComponentListener componentListener) {
        if (componentListener == null) return null;
        this.componentListeners.remove(componentListener);
        return this;
    }
    
    /**
     * Returns an array of all the component listeners registered on this window.
     * @return all of this window's `ComponentListener`s or an empty array if no component listeners are currently registered
     * @see java.awt.ComponentListener
     */
    public ComponentListener[] getComponentListeners() {
        return this.componentListeners.toArray(new ComponentListener[this.componentListeners.size()]);
    }
    
    /**
     * Adds the specified window listener to receive key events from this window.
     * If windowListener is null, no exception is thrown and no action is performed.
     * @param windowListener the window listener.
     * @return the Window object, for convenience.
     * @see java.awt.WindowListener
     */
    public Window addWindowListener(WindowListener windowListener) {
        if (windowListener == null) return null;
        this.windowListeners.add(windowListener);
        return this;
    }
    
    /**
     * Removes the specified window listener so that it no longer receives events from this window.
     * This method performs no function, nor does it throw an exception, if the listener specified by the argument was not previously added to this window.
     * If windowListener is null, no exception is thrown and no action is performed.
     * @param windowListener the window listener.
     * @return the Window object, for convenience.
     * @see java.awt.WindowListener
     */
    public Window removeWindowListener(WindowListener windowListener) {
        if (windowListener == null) return null;
        this.windowListeners.remove(windowListener);
        return this;
    }
    
    /**
     * Returns an array of all the window listeners registered on this window.
     * @return all of this window's `WindowListener`s or an empty array if no window listeners are currently registered
     * @see java.awt.WindowListener
     */
    public WindowListener[] getWindowListeners() {
        return this.windowListeners.toArray(new WindowListener[this.windowListeners.size()]);
    }
    
    /**
     * Shows or hides this component depending on the value of parameter visible.
     * @param visible if `true`, shows this window; otherwise, hides this window
     * @return the Window object, for convenience.
     * @see #isVisible()
     */
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
    
    /**
     * Gets whether this Window should be visible.
     * @return `true` if the window is visible, `false` otherwise
     * @see #setVisible(boolean)
     */
    public boolean isVisible() {
        return this.visible;
    }
    
    /**
     * Removes all the components from this window.
     * @return the Window object, for convenience.
     */
    public Window removeAll() {
        this.components.clear();
        return this;
    }
}

