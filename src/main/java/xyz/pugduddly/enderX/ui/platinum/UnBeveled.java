package xyz.pugduddly.enderX.ui.platinum;

import java.awt.*;

/**
 * Simple reverse beveled component.
 */
public class UnBeveled extends Component {
    /**
     * Paints the component.
     * @param g The Graphics instance to paint this component on.
     */
    public void paint(Graphics g) {
        Dimension size = this.getPreferredSize();
        g.setClip(-2, -2, (int) size.getWidth() + 4, (int) size.getHeight() + 4);
        g.setColor(Color.BLACK);
        g.drawRect(-1, -1, (int) size.getWidth() + 1, (int) size.getHeight() + 1);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, (int) size.getWidth(), (int) size.getHeight());
        g.setColor(new Color(173, 173, 173));
        g.drawLine(-2, -2, (int) size.getWidth(), -2);
        g.drawLine(-2, -2, -2, (int) size.getHeight());
        g.setColor(Color.WHITE);
        g.drawLine(-1, (int) size.getHeight() + 1, (int) size.getWidth() + 1, (int) size.getHeight() + 1);
        g.drawLine((int) size.getWidth() + 1, -1, (int) size.getWidth() + 1, (int) size.getHeight());
    }
}
