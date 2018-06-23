package xyz.pugduddly.enderX.ui.platinum;

import java.awt.*;

public class Beveled extends Component {
    public void paint(Graphics g) {
        Dimension size = this.getPreferredSize();
        g.setClip(-1, -1, (int) size.getWidth() + 2, (int) size.getHeight() + 2);
        g.setColor(Color.BLACK);
        g.drawRect(-1, -1, (int) size.getWidth() + 1, (int) size.getHeight() + 1);
        g.setColor(new Color(222, 222, 222));
        g.fillRect(0, 0, (int) size.getWidth() - 1, (int) size.getHeight() - 1);
        g.setColor(Color.WHITE);
        g.drawLine(0, 0, (int) size.getWidth() - 1, 0);
        g.drawLine(0, 0, 0, (int) size.getHeight() - 1);
        g.setColor(new Color(173, 173, 173));
        g.drawLine(1, (int) size.getHeight() - 1, (int) size.getWidth() - 1, (int) size.getHeight() - 1);
        g.drawLine((int) size.getWidth() - 1, 1, (int) size.getWidth() - 1, (int) size.getHeight() - 1);
    }
}
