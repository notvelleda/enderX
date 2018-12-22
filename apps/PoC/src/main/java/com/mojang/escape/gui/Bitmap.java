/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.gui;

import com.mojang.escape.Art;

public class Bitmap {
    public final int width;
    public final int height;
    public final int[] pixels;
    private static final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ.,!?\"'/\\<>()[]{}abcdefghijklmnopqrstuvwxyz_               0123456789+-=*:;\u00d6\u00c5\u00c4\u00e5                      ";

    public Bitmap(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
    }

    public void draw(Bitmap bitmap, int xOffs, int yOffs) {
        int y = 0;
        while (y < bitmap.height) {
            int yPix = y + yOffs;
            if (yPix >= 0 && yPix < this.height) {
                int x = 0;
                while (x < bitmap.width) {
                    int xPix = x + xOffs;
                    if (xPix >= 0 && xPix < this.width) {
                        int src;
                        this.pixels[xPix + yPix * this.width] = src = bitmap.pixels[x + y * bitmap.width];
                    }
                    ++x;
                }
            }
            ++y;
        }
    }

    public void flipDraw(Bitmap bitmap, int xOffs, int yOffs) {
        int y = 0;
        while (y < bitmap.height) {
            int yPix = y + yOffs;
            if (yPix >= 0 && yPix < this.height) {
                int x = 0;
                while (x < bitmap.width) {
                    int xPix = xOffs + bitmap.width - x - 1;
                    if (xPix >= 0 && xPix < this.width) {
                        int src;
                        this.pixels[xPix + yPix * this.width] = src = bitmap.pixels[x + y * bitmap.width];
                    }
                    ++x;
                }
            }
            ++y;
        }
    }

    public void draw(Bitmap bitmap, int xOffs, int yOffs, int xo, int yo, int w, int h, int col) {
        int y = 0;
        while (y < h) {
            int yPix = y + yOffs;
            if (yPix >= 0 && yPix < this.height) {
                int x = 0;
                while (x < w) {
                    int src;
                    int xPix = x + xOffs;
                    if (xPix >= 0 && xPix < this.width && (src = bitmap.pixels[x + xo + (y + yo) * bitmap.width]) >= 0) {
                        this.pixels[xPix + yPix * this.width] = src * col;
                    }
                    ++x;
                }
            }
            ++y;
        }
    }

    public void scaleDraw(Bitmap bitmap, int scale, int xOffs, int yOffs, int xo, int yo, int w, int h, int col) {
        int y = 0;
        while (y < h * scale) {
            int yPix = y + yOffs;
            if (yPix >= 0 && yPix < this.height) {
                int x = 0;
                while (x < w * scale) {
                    int src;
                    int xPix = x + xOffs;
                    if (xPix >= 0 && xPix < this.width && (src = bitmap.pixels[x / scale + xo + (y / scale + yo) * bitmap.width]) >= 0) {
                        this.pixels[xPix + yPix * this.width] = src * col;
                    }
                    ++x;
                }
            }
            ++y;
        }
    }

    public void draw(String string, int x, int y, int col) {
        int i = 0;
        while (i < string.length()) {
            int ch = "ABCDEFGHIJKLMNOPQRSTUVWXYZ.,!?\"'/\\<>()[]{}abcdefghijklmnopqrstuvwxyz_               0123456789+-=*:;\u00d6\u00c5\u00c4\u00e5                      ".indexOf(string.charAt(i));
            if (ch >= 0) {
                int xx = ch % 42;
                int yy = ch / 42;
                this.draw(Art.font, x + i * 6, y, xx * 6, yy * 8, 5, 8, col);
            }
            ++i;
        }
    }

    public void fill(int x0, int y0, int x1, int y1, int color) {
        int y = y0;
        while (y < y1) {
            int x = x0;
            while (x < x1) {
                this.pixels[x + y * this.width] = color;
                ++x;
            }
            ++y;
        }
    }
}

