package xyz.pugduddly.enderX.ui.platinum;

import java.awt.*;

import xyz.pugduddly.enderX.EnderX;

/**
 * Text label component.
 */
public class Label extends Component {
    private String string;
    private boolean centerText = false;
    private boolean isBold = false;
    private boolean shouldSetSize = true;
    
    /**
     * Create a new Label instance.
     * @param string The text to be displayed
     */
    public Label(String string) {
        this.setString(string);
        //this.setFont(EnderX.getFont("ScratchySans", 16));
        this.setFont(EnderX.getFont("px_sans_nouveaux", 8));
        this.setForeground(Color.BLACK);
    }
    
    /**
     * Paints the component.
     * @param g The Graphics instance to paint this component on.
     */
    public void paint(Graphics g) {
        String text = this.getString();
        int stringWidth = g.getFontMetrics().stringWidth(text);
        int stringHeight = g.getFontMetrics().getHeight() - 6;
        //int stringHeight = 10;
        if (this.shouldSetSize && !this.centerText) {
            this.setPreferredSize(new Dimension(stringWidth, stringHeight + 2));
        }
        for (int i = 0; i < (this.isBold ? 2 : 1); i ++) {
            if (this.centerText) {
                g.setClip(i - stringWidth / 2, 0, stringWidth, stringHeight + 2);
                g.drawString(text, i - stringWidth / 2, stringHeight);
            } else {
                //g.setClip(i, 0, stringWidth, stringHeight);
                g.drawString(text, i, stringHeight);
            }
        }
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
    }
    
    /**
     * Specify whether the text should be centered.
     */
    public void centerText(boolean center) {
        this.centerText = center;
    }
    
    /**
     * Specify whether the text should be bold.
     */
    public void setBold(boolean bold) {
        this.isBold = bold;
    }
}
