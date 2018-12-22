/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape;

import com.mojang.escape.gui.Bitmap;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

public class Art {
    public static Bitmap walls = Art.loadBitmap("/tex/walls.png");
    public static Bitmap floors = Art.loadBitmap("/tex/floors.png");
    public static Bitmap sprites = Art.loadBitmap("/tex/sprites.png");
    public static Bitmap font = Art.loadBitmap("/tex/font.png");
    public static Bitmap panel = Art.loadBitmap("/tex/gamepanel.png");
    public static Bitmap items = Art.loadBitmap("/tex/items.png");
    public static Bitmap sky = Art.loadBitmap("/tex/sky.png");
    public static Bitmap logo = Art.loadBitmap("/gui/logo.png");

    public static Bitmap loadBitmap(String fileName) {
        try {
            BufferedImage img = ImageIO.read(Art.class.getResource(fileName));
            int w = img.getWidth();
            int h = img.getHeight();
            Bitmap result = new Bitmap(w, h);
            img.getRGB(0, 0, w, h, result.pixels, 0, w);
            int i = 0;
            while (i < result.pixels.length) {
                int in = result.pixels[i];
                int col = (in & 15) >> 2;
                if (in == -65281) {
                    col = -1;
                }
                result.pixels[i] = col;
                ++i;
            }
            return result;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int getCol(int c) {
        int r = c >> 16 & 255;
        int g = c >> 8 & 255;
        int b = c & 255;
        r = r * 85 / 255;
        g = g * 85 / 255;
        b = b * 85 / 255;
        return r << 16 | g << 8 | b;
    }
}

