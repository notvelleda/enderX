/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.entities;

import com.mojang.escape.Art;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.gui.Sprite;
import java.util.List;

public class Bullet
extends Entity {
    Entity owner;

    public Bullet(Entity owner, double x, double z, double rot, double pow, int sprite, int col) {
        this.r = 0.01;
        this.owner = owner;
        this.xa = Math.sin(rot) * 0.2 * pow;
        this.za = Math.cos(rot) * 0.2 * pow;
        this.x = x - this.za / 2.0;
        this.z = z + this.xa / 2.0;
        this.sprites.add(new Sprite(0.0, 0.0, 0.0, 24 + sprite, Art.getCol(col)));
        this.flying = true;
    }

    public void tick() {
        double xao = this.xa;
        double zao = this.za;
        this.move();
        if (this.xa == 0.0 && this.za == 0.0 || this.xa != xao || this.za != zao) {
            this.remove();
        }
    }

    public boolean blocks(Entity entity, double x2, double z2, double r2) {
        if (entity instanceof Bullet) {
            return false;
        }
        if (entity == this.owner) {
            return false;
        }
        return super.blocks(entity, x2, z2, r2);
    }

    protected void collide(Entity entity) {
    }
}

