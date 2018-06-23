package xyz.pugduddly.enderX.ui.platinum;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Image;

public class ImageView extends Component {
    private Image image;
    
    public ImageView(Image image) {
        this.setImage(image);
        this.setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
    }
    
    public void paint(Graphics g) {
        Dimension size = this.getPreferredSize();
        g.drawImage(this.image, 0, 0, (int) size.getWidth(), (int) size.getHeight(), null);
    }
    
    public void setImage(Image image) {
        this.image = image;
    }
    
    public Image getImage() {
        return this.image;
    }
}
