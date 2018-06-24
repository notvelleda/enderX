package xyz.pugduddly.enderX.ui.platinum;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Image;

/**
 * Simple image component.
 */
public class ImageView extends Component {
    private Image image;
    
    /**
     * Creates a new ImageView instance.
     * @param image The image to display.
     */
    public ImageView(Image image) {
        this.setImage(image);
        this.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
    }
    
    /**
     * Paints the component.
     * @param g The Graphics instance to paint this component on.
     */
    public void paint(Graphics g) {
        Dimension size = this.getPreferredSize();
        g.drawImage(this.image, 0, 0, (int) size.getWidth(), (int) size.getHeight(), null);
    }
    
    /**
     * Sets the Image to be displayed.
     * @param image The Image to be displayed.
     */
    public void setImage(Image image) {
        this.image = image;
    }
    
    /**
     * Gets the Image that is displayed.
     * @return the Image to display.
     */
    public Image getImage() {
        return this.image;
    }
}
