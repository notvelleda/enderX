package xyz.pugduddly.pfm;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.image.RescaleOp;
import java.awt.image.RGBImageFilter;
import java.awt.image.FilteredImageSource;

import java.io.File;

import xyz.pugduddly.enderX.EnderX;
import xyz.pugduddly.enderX.ui.Window;
import xyz.pugduddly.enderX.ui.Menu;
import xyz.pugduddly.enderX.ui.MenuItem;
import xyz.pugduddly.enderX.ui.platinum.Beveled;
import xyz.pugduddly.enderX.ui.platinum.Label;
import xyz.pugduddly.enderX.ui.platinum.Textbox;
import xyz.pugduddly.enderX.ui.platinum.Button;

class DarkenFilter extends RGBImageFilter {
    public DarkenFilter() {
        canFilterIndexColorModel = true;
    }
 
    public int filterRGB(int x, int y, int rgb) {
        int r = ((rgb >> 16) & 0xff) / 2;
        int g = ((rgb >> 8) & 0xff) / 2;
        int b = ((rgb >> 0) & 0xff) / 2;
        return (rgb & 0xff000000) | (r << 16) | (g << 8) | (b << 0);
    }
}

public class FileIcon extends Component implements MouseListener {
    private BufferedImage icon;
    private Image selectedIcon;
    private String name;
    private File file;
    public boolean selected = false;
    private Color highlight = Color.WHITE;
    
    public FileIcon(BufferedImage icon, File file, String name) {
        this.icon = icon;
        this.name = name;
        this.setFont(EnderX.getFont("ScratchySans", 16));
        this.addMouseListener(this);
        this.file = file;
        this.selectedIcon = createImage(new FilteredImageSource(this.icon.getSource(), new DarkenFilter()));
    }
    
    public FileIcon(BufferedImage icon, File file) {
        this(icon, file, file.getName());
    }
    
    public FileIcon(BufferedImage icon, String name) {
        this(icon, null, name);
    }
    
    public Dimension getPreferredSize() {
        return new Dimension(64, 48);
    }
    
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    
    public void mouseClicked(MouseEvent e) {
        this.selected = !this.selected;
    }
    
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    
    public void paint(Graphics g) {
        int nameWidth = g.getFontMetrics().stringWidth(this.name);
        if (nameWidth > 64) {
            String name = this.name;
            while (nameWidth > 64) {
                name = name.substring(0, name.length() - 2);
                nameWidth = g.getFontMetrics().stringWidth(name + "...");
            }
            this.name = name + "...";
        }
        if (this.selected) {
            g.drawImage(this.selectedIcon, 16, 4, 32, 32, null);
            g.setColor(Color.BLACK);
            g.fillRect(64 / 2 - (nameWidth + 4) / 2, 36, nameWidth + 4, 12);
            g.setColor(Color.WHITE);
        } else {
            g.drawImage(this.icon, 16, 4, 32, 32, null);
            g.setColor(this.highlight);
            g.fillRect(64 / 2 - (nameWidth + 4) / 2, 36, nameWidth + 4, 12);
            g.setColor(Color.BLACK);
        }
        g.drawString(this.name, 64 / 2 - nameWidth / 2, 45);
    }
    
    public File getFile() {
        return this.file;
    }
    
    public void setHighlight(Color c) {
        this.highlight = c;
    }
}
