package mc8k;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.BufferedImage;
import java.awt.image.LookupOp;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupTable;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.util.Arrays;

public class Font {
    private int[] charWidths = new int[256];
    private BufferedImage img;
    
    public Font(String name) {
        try {
            this.img = ImageIO.read(Font.class.getResourceAsStream(name));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        int w = img.getWidth();
        int h = img.getHeight();
        int[] rawPixels = new int[w * h];
        this.img.getRGB(0, 0, w, h, rawPixels, 0, w);
        int i = 0;
        while (i < 128) {
            int xt = i % 16;
            int yt = i / 16;
            int x = 0;
            boolean emptyColumn = false;
            while (x < 8 && !emptyColumn) {
                int xPixel = xt * 8 + x;
                emptyColumn = true;
                int y = 0;
                while (y < 8 && emptyColumn) {
                    int yPixel = (yt * 8 + y) * w;
                    int pixel = rawPixels[xPixel + yPixel] & 255;
                    if (pixel > 128) {
                        emptyColumn = false;
                    }
                    ++y;
                }
                ++x;
            }
            if (i == 32) {
                x = 4;
            }
            this.charWidths[i] = x;
            ++i;
        }
    }
    
    public void drawShadow(Graphics g, String str, int x, int y, int color) {
        this.draw(g, str, x + 1, y + 1, color, true);
        this.draw(g, str, x, y, color, false);
    }
    
    public void draw(Graphics g, String str, int x, int y, int color) {
        this.draw(g, str, x, y, color, false);
    }
    
    public void draw(Graphics gr, String str, int x, int y, int color, boolean darken) {
        char[] chars = str.toCharArray();
        if (darken) {
            color = (color & 16579836) >> 2;
        }
        int xo = 0;
        int i = 0;
        BufferedImage f = colorize(this.img, new Color(color));
        while (i < chars.length) {
            if (chars[i] == '&') {
                int cc = "0123456789abcdef".indexOf(chars[i + 1]);
                int br = (cc & 8) * 8;
                int b = (cc & 1) * 191 + br;
                int g = ((cc & 2) >> 1) * 191 + br;
                int r = ((cc & 4) >> 2) * 191 + br;
                color = r << 16 | g << 8 | b;
                i += 2;
                if (darken) {
                    color = (color & 16579836) >> 2;
                }
                f = colorize(this.img, new Color(color));
            }
            int ix = chars[i] % 16 * 8;
            int iy = chars[i] / 16 * 8;
            gr.drawImage(f.getSubimage(ix, iy, 8, 8), x + xo, y, null);
            xo += this.charWidths[chars[i]];
            ++i;
        }
    }
    
    private static BufferedImage colorize(BufferedImage image, Color to) {
        Color from = Color.WHITE;
        if (to.equals(from)) {
            return image;
        } else {
            BufferedImageOp lookup = new LookupOp(new ColorMapper(from, to), null);
            return lookup.filter(image, null);
        }
    }

    public int width(String str) {
        char[] chars = str.toCharArray();
        int len = 0;
        int i = 0;
        while (i < chars.length) {
            if (chars[i] == '&') {
                ++i;
            } else {
                len += this.charWidths[chars[i]];
            }
            ++i;
        }
        return len;
    }
}

class ColorMapper extends LookupTable {
    private final int[] from;
    private final int[] to;
    public ColorMapper(Color from, Color to) {
        super(0, 4);
        this.from = new int[] {
            from.getRed(),
            from.getGreen(),
            from.getBlue(),
            from.getAlpha(),
        };
        this.to = new int[] {
            to.getRed(),
            to.getGreen(),
            to.getBlue(),
            to.getAlpha(),
        };
    }

    @Override
    public int[] lookupPixel(int[] src, int[] dest) {
        if (dest == null) {
            dest = new int[src.length];
        }
        int[] newColor = (Arrays.equals(src, from) ? to : src);
        System.arraycopy(newColor, 0, dest, 0, newColor.length);
        return dest;
    }
}
