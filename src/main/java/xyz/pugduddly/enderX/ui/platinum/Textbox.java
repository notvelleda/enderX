package xyz.pugduddly.enderX.ui.platinum;

import java.awt.Point;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import xyz.pugduddly.enderX.EnderX;
import xyz.pugduddly.enderX.ui.Component;

/**
 * Simple textbox component.
 */
public class Textbox extends Component implements KeyListener, MouseListener {
    //private String string = "";
    private StringBuilder stringBuilder = new StringBuilder();
    private int cursorPos = 0;
    
    /**
     * Creates a new Textbox instance.
     */
    public Textbox() {
        //this.setFont(EnderX.getFont("Geneva", 12));
        this.setFont(EnderX.getFont("px_sans_nouveaux", 8));
        this.setForeground(Color.BLACK);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.setPreferredSize(new Dimension(158, 24));
    }
    
    /**
     * Paints the component.
     * @param g The Graphics instance to paint this component on.
     */
    public void paint(Graphics g) {
        Dimension d = this.getPreferredSize();
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, (int) d.getWidth() - 1, (int) d.getHeight() - 1);
        g.setColor(Color.WHITE);
        g.fillRect(1, 1, (int) d.getWidth() - 2, (int) d.getHeight() - 2);
        g.setColor(this.getForeground());
        
        String text = this.getString();
        int stringWidth = g.getFontMetrics().stringWidth(text);
        boolean resize = false;
        int resizeLength = 0;
        while (stringWidth > d.getWidth() - 6) {
            resize = true;
            text = text.substring(1, text.length());
            stringWidth = g.getFontMetrics().stringWidth(text);
            resizeLength ++;
        }
        int newCursorPos = this.cursorPos - resizeLength;
        if (newCursorPos < 0) {
            text = this.getString();
            int offset = newCursorPos - 1;
            while (resizeLength + offset < 0) offset ++;
            text = text.substring(resizeLength + offset, text.length() + offset);
            stringWidth = g.getFontMetrics().stringWidth(text);
            resizeLength += offset;
            newCursorPos = this.cursorPos - resizeLength;
            resize = false;
        }
        int cursorX = g.getFontMetrics().stringWidth(text.substring(0, newCursorPos));
        int stringHeight = g.getFontMetrics().getHeight();
        
        if (resize) {
            g.drawString(text, (int) d.getWidth() - 4 - stringWidth, (int) (d.getHeight() / 2 - stringHeight / 1.6) + stringHeight);
            if (this.hasFocus() && System.currentTimeMillis() % 1000 < 500)
                if (this.cursorPos == text.length() - 1)
                    g.drawLine((int) d.getWidth() - 4, 3, (int) d.getWidth() - 4, (int) d.getHeight() - 4);
                else
                    g.drawLine((int) d.getWidth() - stringWidth + cursorX - 4, 3, (int) d.getWidth() - stringWidth + cursorX - 4, (int) d.getHeight() - 4);
        } else {
            g.drawString(text, 4, (int) (d.getHeight() / 2 - stringHeight / 1.6) + stringHeight);
            if (this.hasFocus() && System.currentTimeMillis() % 1000 < 500)
                g.drawLine(cursorX + 4, 3, cursorX + 4, (int) d.getHeight() - 4);
        }
    }
    
    /**
     * Gets the current String to be drawn.
     * @return the string.
     */
    public String getString() {
        return this.stringBuilder.toString();
    }
    
    /**
     * Set the String to be drawn.
     * @param string The string.
     */
    public void setString(String string) {
        this.stringBuilder = new StringBuilder(string);
        this.cursorPos = string.length();
    }
    
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    
    public void mouseClicked(MouseEvent e) {
        this.requestFocus();
    }
    
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    
    public void keyPressed(KeyEvent e) {
        if (this.hasFocus()) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (this.cursorPos > 0) {
                    this.cursorPos --;
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    this.cursorPos = 0;
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (this.cursorPos < this.stringBuilder.length()) {
                    this.cursorPos ++;
                } else {
                    Toolkit.getDefaultToolkit().beep();
                    this.cursorPos = this.stringBuilder.length();
                }
            }
        }
    }
    
    public void keyReleased(KeyEvent e) {}
    
    public void keyTyped(KeyEvent e) {
        if (this.hasFocus()) {
            char keyChar = e.getKeyChar();
            if (keyChar == 8 && this.stringBuilder.length() > 0 && this.cursorPos > 0) {
                this.stringBuilder.deleteCharAt(this.cursorPos - 1);
                this.cursorPos --;
            } else if (keyChar == 8) {
                Toolkit.getDefaultToolkit().beep();
                this.cursorPos = 0;
            } else if (keyChar >= 32) {
                this.stringBuilder.insert(this.cursorPos, keyChar);
                this.cursorPos ++;
            }
        }
    }
}
