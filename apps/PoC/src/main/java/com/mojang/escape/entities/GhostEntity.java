/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.entities;

import com.mojang.escape.Art;
import com.mojang.escape.entities.EnemyEntity;
import com.mojang.escape.entities.Player;
import com.mojang.escape.gui.Sprite;
import com.mojang.escape.level.Level;
import java.util.Random;

public class GhostEntity
extends EnemyEntity {
    private double rotatePos = 0.0;

    public GhostEntity(double x, double z) {
        super(x, z, 38, Art.getCol(16777215));
        this.x = x;
        this.z = z;
        this.health = 4;
        this.r = 0.3;
        this.flying = true;
    }

    public void tick() {
        double zd;
        ++this.animTime;
        this.sprite.tex = this.defaultTex + this.animTime / 10 % 2;
        double xd = this.level.player.x + Math.sin(this.rotatePos) - this.x;
        double dd = xd * xd + (zd = this.level.player.z + Math.cos(this.rotatePos) - this.z) * zd;
        if (dd < 16.0) {
            if (dd < 1.0) {
                this.rotatePos += 0.04;
            } else {
                this.rotatePos = this.level.player.rot;
                this.xa += (random.nextDouble() - 0.5) * 0.02;
                this.za += (random.nextDouble() - 0.5) * 0.02;
            }
            dd = Math.sqrt(dd);
            this.xa += (xd /= dd) * 0.004;
            this.za += (zd /= dd) * 0.004;
        }
        this.move();
        this.xa *= 0.9;
        this.za *= 0.9;
    }

    protected void hurt(double xd, double zd) {
    }

    protected void move() {
        this.x += this.xa;
        this.z += this.za;
    }
}

