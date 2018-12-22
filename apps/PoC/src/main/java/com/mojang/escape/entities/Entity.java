/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.entities;

import com.mojang.escape.entities.Bullet;
import com.mojang.escape.entities.Item;
import com.mojang.escape.gui.Sprite;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.Block;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Entity {
    protected static final Random random = new Random();
    public List<Sprite> sprites = new ArrayList<Sprite>();
    public double x;
    public double z;
    public double rot;
    public double xa;
    public double za;
    public double rota;
    public double r = 0.4;
    public Level level;
    public int xTileO = -1;
    public int zTileO = -1;
    public boolean flying = false;
    private boolean removed = false;

    public final void updatePos() {
        int xTile = (int)(this.x + 0.5);
        int zTile = (int)(this.z + 0.5);
        if (xTile != this.xTileO || zTile != this.zTileO) {
            this.level.getBlock(this.xTileO, this.zTileO).removeEntity(this);
            this.xTileO = xTile;
            this.zTileO = zTile;
            if (!this.removed) {
                this.level.getBlock(this.xTileO, this.zTileO).addEntity(this);
            }
        }
    }

    public boolean isRemoved() {
        return this.removed;
    }

    public void remove() {
        this.level.getBlock(this.xTileO, this.zTileO).removeEntity(this);
        this.removed = true;
    }

    protected void move() {
        int zSteps;
        int xSteps;
        int i = xSteps = (int)(Math.abs(this.xa * 100.0) + 1.0);
        while (i > 0) {
            double xxa = this.xa;
            if (this.isFree(this.x + xxa * (double)i / (double)xSteps, this.z)) {
                this.x += xxa * (double)i / (double)xSteps;
                break;
            }
            this.xa = 0.0;
            --i;
        }
        int i2 = zSteps = (int)(Math.abs(this.za * 100.0) + 1.0);
        while (i2 > 0) {
            double zza = this.za;
            if (this.isFree(this.x, this.z + zza * (double)i2 / (double)zSteps)) {
                this.z += zza * (double)i2 / (double)zSteps;
                break;
            }
            this.za = 0.0;
            --i2;
        }
    }

    protected boolean isFree(double xx, double yy) {
        int x0 = (int)Math.floor(xx + 0.5 - this.r);
        int x1 = (int)Math.floor(xx + 0.5 + this.r);
        int y0 = (int)Math.floor(yy + 0.5 - this.r);
        int y1 = (int)Math.floor(yy + 0.5 + this.r);
        if (this.level.getBlock(x0, y0).blocks(this)) {
            return false;
        }
        if (this.level.getBlock(x1, y0).blocks(this)) {
            return false;
        }
        if (this.level.getBlock(x0, y1).blocks(this)) {
            return false;
        }
        if (this.level.getBlock(x1, y1).blocks(this)) {
            return false;
        }
        int xc = (int)Math.floor(xx + 0.5);
        int zc = (int)Math.floor(yy + 0.5);
        int rr = 2;
        int z = zc - rr;
        while (z <= zc + rr) {
            int x = xc - rr;
            while (x <= xc + rr) {
                List<Entity> es = this.level.getBlock((int)x, (int)z).entities;
                int i = 0;
                while (i < es.size()) {
                    Entity e = es.get(i);
                    if (e != this && !e.blocks(this, this.x, this.z, this.r) && e.blocks(this, xx, yy, this.r)) {
                        e.collide(this);
                        this.collide(e);
                        return false;
                    }
                    ++i;
                }
                ++x;
            }
            ++z;
        }
        return true;
    }

    protected void collide(Entity entity) {
    }

    public boolean blocks(Entity entity, double x2, double z2, double r2) {
        if (entity instanceof Bullet && ((Bullet)entity).owner == this) {
            return false;
        }
        if (this.x + this.r <= x2 - r2) {
            return false;
        }
        if (this.x - this.r >= x2 + r2) {
            return false;
        }
        if (this.z + this.r <= z2 - r2) {
            return false;
        }
        if (this.z - this.r >= z2 + r2) {
            return false;
        }
        return true;
    }

    public boolean contains(double x2, double z2) {
        if (this.x + this.r <= x2) {
            return false;
        }
        if (this.x - this.r >= x2) {
            return false;
        }
        if (this.z + this.r <= z2) {
            return false;
        }
        if (this.z - this.r >= z2) {
            return false;
        }
        return true;
    }

    public boolean isInside(double x0, double z0, double x1, double z1) {
        if (this.x + this.r <= x0) {
            return false;
        }
        if (this.x - this.r >= x1) {
            return false;
        }
        if (this.z + this.r <= z0) {
            return false;
        }
        if (this.z - this.r >= z1) {
            return false;
        }
        return true;
    }

    public boolean use(Entity source, Item item) {
        return false;
    }

    public void tick() {
    }
}

