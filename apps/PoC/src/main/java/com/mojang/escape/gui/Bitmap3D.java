/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.gui;

import com.mojang.escape.Art;
import com.mojang.escape.Game;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.Player;
import com.mojang.escape.gui.Bitmap;
import com.mojang.escape.gui.Sprite;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.Block;
import com.mojang.escape.level.block.DoorBlock;
import java.util.List;

public class Bitmap3D
extends Bitmap {
    private double[] zBuffer;
    private double[] zBufferWall;
    private double xCam;
    private double yCam;
    private double zCam;
    private double rCos;
    private double rSin;
    private double fov;
    private double xCenter;
    private double yCenter;
    private double rot;

    public Bitmap3D(int width, int height) {
        super(width, height);
        this.zBuffer = new double[width * height];
        this.zBufferWall = new double[width];
    }

    public void render(Game game) {
        int xb;
        Block c;
        int x = 0;
        while (x < this.width) {
            this.zBufferWall[x] = 0.0;
            ++x;
        }
        int i = 0;
        while (i < this.width * this.height) {
            this.zBuffer[i] = 10000.0;
            ++i;
        }
        this.rot = game.player.rot;
        this.xCam = game.player.x - Math.sin(this.rot) * 0.3;
        this.yCam = game.player.z - Math.cos(this.rot) * 0.3;
        this.zCam = -0.2 + Math.sin(game.player.bobPhase * 0.4) * 0.01 * game.player.bob - game.player.y;
        this.xCenter = (double)this.width / 2.0;
        this.yCenter = (double)this.height / 3.0;
        this.rCos = Math.cos(this.rot);
        this.rSin = Math.sin(this.rot);
        this.fov = this.height;
        Level level = game.level;
        int r = 6;
        int xCenter = (int)Math.floor(this.xCam);
        int zCenter = (int)Math.floor(this.yCam);
        int zb = zCenter - r;
        while (zb <= zCenter + r) {
            xb = xCenter - r;
            while (xb <= xCenter + r) {
                c = level.getBlock(xb, zb);
                Block e = level.getBlock(xb + 1, zb);
                Block s = level.getBlock(xb, zb + 1);
                if (c instanceof DoorBlock) {
                    double rr = 0.125;
                    double openness = 1.0 - ((DoorBlock)c).openness * 7.0 / 8.0;
                    if (e.solidRender) {
                        this.renderWall((double)xb + openness, (double)zb + 0.5 - rr, xb, (double)zb + 0.5 - rr, c.tex, (c.col & 16711422) >> 1, 0.0, openness);
                        this.renderWall(xb, (double)zb + 0.5 + rr, (double)xb + openness, (double)zb + 0.5 + rr, c.tex, (c.col & 16711422) >> 1, openness, 0.0);
                        this.renderWall((double)xb + openness, (double)zb + 0.5 + rr, (double)xb + openness, (double)zb + 0.5 - rr, c.tex, c.col, 0.5 - rr, 0.5 + rr);
                    } else {
                        this.renderWall((double)xb + 0.5 - rr, zb, (double)xb + 0.5 - rr, (double)zb + openness, c.tex, c.col, openness, 0.0);
                        this.renderWall((double)xb + 0.5 + rr, (double)zb + openness, (double)xb + 0.5 + rr, zb, c.tex, c.col, 0.0, openness);
                        this.renderWall((double)xb + 0.5 - rr, (double)zb + openness, (double)xb + 0.5 + rr, (double)zb + openness, c.tex, (c.col & 16711422) >> 1, 0.5 - rr, 0.5 + rr);
                    }
                }
                if (c.solidRender) {
                    if (!e.solidRender) {
                        this.renderWall(xb + 1, zb + 1, xb + 1, zb, c.tex, c.col);
                    }
                    if (!s.solidRender) {
                        this.renderWall(xb, zb + 1, xb + 1, zb + 1, c.tex, (c.col & 16711422) >> 1);
                    }
                } else {
                    if (e.solidRender) {
                        this.renderWall(xb + 1, zb, xb + 1, zb + 1, e.tex, e.col);
                    }
                    if (s.solidRender) {
                        this.renderWall(xb + 1, zb + 1, xb, zb + 1, s.tex, (s.col & 16711422) >> 1);
                    }
                }
                ++xb;
            }
            ++zb;
        }
        zb = zCenter - r;
        while (zb <= zCenter + r) {
            xb = xCenter - r;
            while (xb <= xCenter + r) {
                c = level.getBlock(xb, zb);
                int j = 0;
                while (j < c.entities.size()) {
                    Entity e = c.entities.get(j);
                    int i2 = 0;
                    while (i2 < e.sprites.size()) {
                        Sprite sprite = e.sprites.get(i2);
                        this.renderSprite(e.x + sprite.x, 0.0 - sprite.y, e.z + sprite.z, sprite.tex, sprite.col);
                        ++i2;
                    }
                    ++j;
                }
                int i3 = 0;
                while (i3 < c.sprites.size()) {
                    Sprite sprite = c.sprites.get(i3);
                    this.renderSprite((double)xb + sprite.x, 0.0 - sprite.y, (double)zb + sprite.z, sprite.tex, sprite.col);
                    ++i3;
                }
                ++xb;
            }
            ++zb;
        }
        this.renderFloor(level);
    }

    private void renderFloor(Level level) {
        int y = 0;
        while (y < this.height) {
            double yd = ((double)y + 0.5 - this.yCenter) / this.fov;
            boolean floor = true;
            double zd = (4.0 - this.zCam * 8.0) / yd;
            if (yd < 0.0) {
                floor = false;
                zd = (4.0 + this.zCam * 8.0) / (- yd);
            }
            int x = 0;
            while (x < this.width) {
                if (this.zBuffer[x + y * this.width] > zd) {
                    double xd = (this.xCenter - (double)x) / this.fov;
                    double xx = (xd *= zd) * this.rCos + zd * this.rSin + (this.xCam + 0.5) * 8.0;
                    double yy = zd * this.rCos - xd * this.rSin + (this.yCam + 0.5) * 8.0;
                    int xPix = (int)(xx * 2.0);
                    int yPix = (int)(yy * 2.0);
                    int xTile = xPix >> 4;
                    int yTile = yPix >> 4;
                    Block block = level.getBlock(xTile, yTile);
                    int col = block.floorCol;
                    int tex = block.floorTex;
                    if (!floor) {
                        col = block.ceilCol;
                        tex = block.ceilTex;
                    }
                    if (tex < 0) {
                        this.zBuffer[x + y * this.width] = -1.0;
                    } else {
                        this.zBuffer[x + y * this.width] = zd;
                        this.pixels[x + y * this.width] = Art.floors.pixels[(xPix & 15) + tex % 8 * 16 + ((yPix & 15) + tex / 8 * 16) * 128] * col;
                    }
                }
                ++x;
            }
            ++y;
        }
    }

    private void renderSprite(double x, double y, double z, int tex, int color) {
        double xc = (x - this.xCam) * 2.0 - this.rSin * 0.2;
        double yc = (y - this.zCam) * 2.0;
        double zc = (z - this.yCam) * 2.0 - this.rCos * 0.2;
        double xx = xc * this.rCos - zc * this.rSin;
        double yy = yc;
        double zz = zc * this.rCos + xc * this.rSin;
        if (zz < 0.1) {
            return;
        }
        double xPixel = this.xCenter - xx / zz * this.fov;
        double yPixel = yy / zz * this.fov + this.yCenter;
        double xPixel0 = xPixel - (double)this.height / zz;
        double xPixel1 = xPixel + (double)this.height / zz;
        double yPixel0 = yPixel - (double)this.height / zz;
        double yPixel1 = yPixel + (double)this.height / zz;
        int xp0 = (int)Math.ceil(xPixel0);
        int xp1 = (int)Math.ceil(xPixel1);
        int yp0 = (int)Math.ceil(yPixel0);
        int yp1 = (int)Math.ceil(yPixel1);
        if (xp0 < 0) {
            xp0 = 0;
        }
        if (xp1 > this.width) {
            xp1 = this.width;
        }
        if (yp0 < 0) {
            yp0 = 0;
        }
        if (yp1 > this.height) {
            yp1 = this.height;
        }
        zz *= 4.0;
        int yp = yp0;
        while (yp < yp1) {
            double ypr = ((double)yp - yPixel0) / (yPixel1 - yPixel0);
            int yt = (int)(ypr * 16.0);
            int xp = xp0;
            while (xp < xp1) {
                int col;
                double xpr = ((double)xp - xPixel0) / (xPixel1 - xPixel0);
                int xt = (int)(xpr * 16.0);
                if (this.zBuffer[xp + yp * this.width] > zz && (col = Art.sprites.pixels[xt + tex % 8 * 16 + (yt + tex / 8 * 16) * 128]) >= 0) {
                    this.pixels[xp + yp * this.width] = col * color;
                    this.zBuffer[xp + yp * this.width] = zz;
                }
                ++xp;
            }
            ++yp;
        }
    }

    private void renderWall(double x0, double y0, double x1, double y1, int tex, int color) {
        this.renderWall(x0, y0, x1, y1, tex, color, 0.0, 1.0);
    }

    private void renderWall(double x0, double y0, double x1, double y1, int tex, int color, double xt0, double xt1) {
        double xPixel1;
        double xPixel0;
        double p;
        double xc0 = (x0 - 0.5 - this.xCam) * 2.0;
        double yc0 = (y0 - 0.5 - this.yCam) * 2.0;
        double xx0 = xc0 * this.rCos - yc0 * this.rSin;
        double u0 = (-0.5 - this.zCam) * 2.0;
        double l0 = (0.5 - this.zCam) * 2.0;
        double zz0 = yc0 * this.rCos + xc0 * this.rSin;
        double xc1 = (x1 - 0.5 - this.xCam) * 2.0;
        double yc1 = (y1 - 0.5 - this.yCam) * 2.0;
        double xx1 = xc1 * this.rCos - yc1 * this.rSin;
        double u1 = (-0.5 - this.zCam) * 2.0;
        double l1 = (0.5 - this.zCam) * 2.0;
        double zz1 = yc1 * this.rCos + xc1 * this.rSin;
        xt0 *= 16.0;
        xt1 *= 16.0;
        double zClip = 0.2;
        if (zz0 < zClip && zz1 < zClip) {
            return;
        }
        if (zz0 < zClip) {
            p = (zClip - zz0) / (zz1 - zz0);
            zz0 += (zz1 - zz0) * p;
            xx0 += (xx1 - xx0) * p;
            xt0 += (xt1 - xt0) * p;
        }
        if (zz1 < zClip) {
            p = (zClip - zz0) / (zz1 - zz0);
            zz1 = zz0 + (zz1 - zz0) * p;
            xx1 = xx0 + (xx1 - xx0) * p;
            xt1 = xt0 + (xt1 - xt0) * p;
        }
        if ((xPixel0 = this.xCenter - xx0 / zz0 * this.fov) >= (xPixel1 = this.xCenter - xx1 / zz1 * this.fov)) {
            return;
        }
        int xp0 = (int)Math.ceil(xPixel0);
        int xp1 = (int)Math.ceil(xPixel1);
        if (xp0 < 0) {
            xp0 = 0;
        }
        if (xp1 > this.width) {
            xp1 = this.width;
        }
        double yPixel00 = u0 / zz0 * this.fov + this.yCenter;
        double yPixel01 = l0 / zz0 * this.fov + this.yCenter;
        double yPixel10 = u1 / zz1 * this.fov + this.yCenter;
        double yPixel11 = l1 / zz1 * this.fov + this.yCenter;
        double iz0 = 1.0 / zz0;
        double iz1 = 1.0 / zz1;
        double iza = iz1 - iz0;
        double ixt0 = xt0 * iz0;
        double ixta = xt1 * iz1 - ixt0;
        double iw = 1.0 / (xPixel1 - xPixel0);
        int x = xp0;
        while (x < xp1) {
            double pr = ((double)x - xPixel0) * iw;
            double iz = iz0 + iza * pr;
            if (this.zBufferWall[x] <= iz) {
                this.zBufferWall[x] = iz;
                int xTex = (int)((ixt0 + ixta * pr) / iz);
                double yPixel0 = yPixel00 + (yPixel10 - yPixel00) * pr - 0.5;
                double yPixel1 = yPixel01 + (yPixel11 - yPixel01) * pr;
                int yp0 = (int)Math.ceil(yPixel0);
                int yp1 = (int)Math.ceil(yPixel1);
                if (yp0 < 0) {
                    yp0 = 0;
                }
                if (yp1 > this.height) {
                    yp1 = this.height;
                }
                double ih = 1.0 / (yPixel1 - yPixel0);
                int y = yp0;
                while (y < yp1) {
                    double pry = ((double)y - yPixel0) * ih;
                    int yTex = (int)(16.0 * pry);
                    this.pixels[x + y * this.width] = Art.walls.pixels[xTex + tex % 8 * 16 + (yTex + tex / 8 * 16) * 128] * color;
                    this.zBuffer[x + y * this.width] = 1.0 / iz * 4.0;
                    ++y;
                }
            }
            ++x;
        }
    }

    public void postProcess(Level level) {
        int i = 0;
        while (i < this.width * this.height) {
            double zl = this.zBuffer[i];
            if (zl < 0.0) {
                int xx = (int)Math.floor((double)(i % this.width) - this.rot * 512.0 / 6.283185307179586) & 511;
                int yy = i / this.width;
                this.pixels[i] = Art.sky.pixels[xx + yy * 512] * 4473941;
            } else {
                int xp = i % this.width;
                int yp = i / this.width * 14;
                double xx = ((double)(i % this.width) - (double)this.width / 2.0) / (double)this.width;
                int col = this.pixels[i];
                int brightness = (int)(300.0 - zl * 6.0 * (xx * xx * 2.0 + 1.0));
                if ((brightness = brightness + (xp + yp & 3) * 4 >> 4 << 4) < 0) {
                    brightness = 0;
                }
                if (brightness > 255) {
                    brightness = 255;
                }
                int r = col >> 16 & 255;
                int g = col >> 8 & 255;
                int b = col & 255;
                r = r * brightness / 255;
                g = g * brightness / 255;
                b = b * brightness / 255;
                this.pixels[i] = r << 16 | g << 8 | b;
            }
            ++i;
        }
    }
}

