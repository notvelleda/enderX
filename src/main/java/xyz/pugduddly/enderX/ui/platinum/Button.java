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
import java.awt.image.BufferedImage;

import xyz.pugduddly.enderX.EnderX;
import xyz.pugduddly.enderX.ui.Component;

/**
 * Standard Mac OS 9-like Button.
 */
public class Button extends Component implements KeyListener, MouseListener {
    private String string = "";
    private int width = -1;
    private static BufferedImage[] defaultTex;
    private static BufferedImage[] pressedTex;
    private static BufferedImage[] focusTex;
    private boolean pressed = false;
    private boolean isDefault = false;
    
    static {
        defaultTex = new BufferedImage[] {
            EnderX.loadBase64Image("R0lGODlhBAAUAPIGAAAAACEhIXNzc62trb29vd7e3v///wAAACH5BAEAAAcALAAAAAAEABQAAAMceBdwQCUQY0Ch1pS8b9dc6IngaJaaVAwOIShMAgA7"),
            EnderX.loadBase64Image("R0lGODlhAQAUAPIEAAAAAHNzc62trd7e3v///wAAAAAAAAAAACH5BAAAAAAALAAAAAABABQAAAMICEPT/k8EkAAAOw=="),
            EnderX.loadBase64Image("R0lGODlhBAAUAPIGAAAAACEhIXNzc62trb29vd7e3v///wAAACH5BAEAAAcALAAAAAAEABQAAAMcCHFXBMe4UIYA9WZsue5bCI5fCQydJQTCdShMAgA7")
        };
        
        pressedTex = new BufferedImage[] {
            EnderX.loadBase64Image("R0lGODlhBAAUAPIAAAAAACEhITIyMz8/QExMTVxcXX5+gAAAACH5BAEAAAcALAAAAAAEABQAAAMaeBdwICLA8goVtt6Mt9dgF3JkEQyWQxgKkwAAOw=="),
            EnderX.loadBase64Image("R0lGODlhAQAUAPEAAAAAADIyM1xcXX5+gCH5BAAAAAAALAAAAAABABQAAAIGRISpKwMFADs="),
            EnderX.loadBase64Image("R0lGODlhBAAUAPIAAAAAACEhITIyMz8/QExMTVxcXX5+gAAAACH5BAEAAAcALAAAAAAEABQAAAMaCHEnA6cUEqQBFpeb+9bc54UkaG5GYFyHwiQAOw==")
        };
        
        focusTex = new BufferedImage[] {
            EnderX.loadBase64Image("R0lGODlhBwAaAPMAAAAAACEhIXNzc4SEhK2trb29vc7Ozt7e3v///wAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAAkALAAAAAAHABoAAARAMMkAqgQnn4QP+UFGCBQ2AsVhBgWCmJgLH4jqxTaBzzWf7zfaT+grBnvHIZLACp4KJMOHBOAUPp/LQMCVJEqACAA7"),
            EnderX.loadBase64Image("R0lGODlhAQAaAPIAAAAAAHNzc62trd7e3v///wAAAAAAAAAAACH5BAAAAAAALAAAAAABABoAAAMKCCNK8zBGEQAFCQA7"),
            EnderX.loadBase64Image("R0lGODlhBwAaAPMAAAAAACEhIXNzc4SEhK2trb29vc7Ozt7e3v///wAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAAkALAAAAAAHABoAAARCEMiQ6rkGVMJJ0UAgcENwfKMAIGdAqMcLyLFKy7cN47sO1LNe8Mcj+oC5IdJWfAlcKkFUFhoNnp2rRirVJCShSiICADs=")
        };
    }
    
    /**
     * Creates a new Button instance.
     * @param string The string to draw on the button.
     */
    public Button(String string) {
        this.setFont(EnderX.getFont("Charcoal", 12));
        this.setForeground(Color.BLACK);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.setString(string);
        this.setForeground(Color.BLACK);
        this.setPreferredSize(new Dimension(60, 20));
    }
    
    /**
     * Paints the component.
     * @param g The Graphics instance to paint this component on.
     */
    public void paint(Graphics g) {
        if (this.width == -1) {
            this.width = Math.max(10 + g.getFontMetrics().stringWidth(this.string) + 10, 60);
            this.setPreferredSize(new Dimension(this.width, 20));
        }
        
        if (/*this.hasFocus() || */this.isDefault) {
            g.drawImage(focusTex[0], -3, -3, null);
            for (int i = 0; i < this.width - 8; i ++)
                g.drawImage(focusTex[1], 4 + i, -3, null);
            g.drawImage(focusTex[2], this.width - 4, -3, null);
        } else if (!this.pressed) {
            g.drawImage(defaultTex[0], 0, 0, null);
            for (int i = 0; i < this.width - 8; i ++)
                g.drawImage(defaultTex[1], 4 + i, 0, null);
            g.drawImage(defaultTex[2], this.width - 4, 0, null);
        }
        
        if (this.pressed) {
            g.drawImage(pressedTex[0], 0, 0, null);
            for (int i = 0; i < this.width - 8; i ++)
                g.drawImage(pressedTex[1], 4 + i, 0, null);
            g.drawImage(pressedTex[2], this.width - 4, 0, null);
        }
        
        int stringWidth = g.getFontMetrics().stringWidth(this.string);
        if (this.pressed)
            g.drawString(this.string, this.width / 2 - stringWidth / 2 + 1, 15);
        else
            g.drawString(this.string, this.width / 2 - stringWidth / 2, 14);
    }
    
    /**
     * Gets the current String to be drawn.
     * @return the string.
     */
    public String getString() {
        return this.string;
    }
    
    /**
     * Set the String to be drawn.
     * @param string The string.
     */
    public void setString(String string) {
        this.string = string;
        this.width = -1;
    }
    
    public void mousePressed(MouseEvent e) {
        this.pressed = true;
    }
    
    public void mouseReleased(MouseEvent e) {
        this.pressed = false;
    }
    
    public void mouseClicked(MouseEvent e) {
        this.pressed = false;
        this.actionPerformed();
    }
    
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && (this.hasFocus() || this.isDefault))
            this.pressed = true;
    }
    
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER && (this.hasFocus() || this.isDefault))
            this.pressed = false;
    }
    
    public void keyTyped(KeyEvent e) {
        char keyChar = e.getKeyChar();
        if (keyChar == '\n' && (/*this.hasFocus() || */this.isDefault))
            this.mouseClicked(null);
    }
    
    /**
     * Sets whether this button is the default button for the window.
     */
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    /**
     * Called when the button is pressed.
     */
    public void actionPerformed() {}
}
