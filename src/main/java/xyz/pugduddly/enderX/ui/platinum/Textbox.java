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

public class Textbox extends Component implements KeyListener, MouseListener {
    private String string = "";
    
    public Textbox() {
        //this.setFont(EnderX.getFont("Geneva", 12));
        this.setFont(EnderX.getFont("px_sans_nouveaux", 8));
        this.setForeground(Color.BLACK);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.setPreferredSize(new Dimension(158, 24));
    }
    
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
        while (stringWidth > d.getWidth() - 6) {
            resize = true;
            text = text.substring(1, text.length());
            stringWidth = g.getFontMetrics().stringWidth(text);
        }
        int stringHeight = g.getFontMetrics().getHeight();
        if (resize) {
            g.drawString(text, (int) d.getWidth() - 4 - stringWidth, (int) (d.getHeight() / 2 - stringHeight / 1.6) + stringHeight);
            if (this.hasFocus() && System.currentTimeMillis() % 1000 < 500)
                g.drawLine((int) d.getWidth() - 4, 3, (int) d.getWidth() - 4, (int) d.getHeight() - 3);
        } else {
            g.drawString(text, 4, (int) (d.getHeight() / 2 - stringHeight / 1.6) + stringHeight);
            if (this.hasFocus() && System.currentTimeMillis() % 1000 < 500)
                g.drawLine(stringWidth + 4, 3, stringWidth + 4, (int) d.getHeight() - 3);
        }
    }
    
    public String getString() {
        return this.string;
    }
    
    public void setString(String string) {
        this.string = string;
    }
    
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    
    public void mouseClicked(MouseEvent e) {
        this.requestFocus();
    }
    
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
    
    public void keyTyped(KeyEvent e) {
        if (this.hasFocus()) {
            char keyChar = e.getKeyChar();
            if (keyChar == 8 && this.string.length() > 0)
                this.string = this.string.substring(0, this.string.length() - 1);
            else if (keyChar == 8)
                Toolkit.getDefaultToolkit().beep();
            else if (keyChar >= 32)
                this.string += keyChar;
        }
    }
}
