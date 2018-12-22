/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class InputHandler
implements KeyListener,
FocusListener,
MouseListener,
MouseMotionListener {
    public boolean[] keys = new boolean[65536];

    public void mouseDragged(MouseEvent arg0) {
    }

    public void mouseMoved(MouseEvent arg0) {
    }

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    public void focusGained(FocusEvent arg0) {
    }

    public void focusLost(FocusEvent arg0) {
        int i = 0;
        while (i < this.keys.length) {
            this.keys[i] = false;
            ++i;
        }
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code > 0 && code < this.keys.length) {
            this.keys[code] = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code > 0 && code < this.keys.length) {
            this.keys[code] = false;
        }
    }

    public void keyTyped(KeyEvent arg0) {
    }
}

