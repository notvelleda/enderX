/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.entities;

import com.mojang.escape.Art;
import com.mojang.escape.entities.Bullet;
import com.mojang.escape.entities.EnemyEntity;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.Player;
import com.mojang.escape.gui.Sprite;
import com.mojang.escape.level.Level;
import java.util.Random;

public class GhostBossEntity
extends EnemyEntity {
    private double rotatePos = 0.0;
    private int shootDelay = 0;

    public GhostBossEntity(double x, double z) {
        super(x, z, 38, Art.getCol(16776960));
        this.x = x;
        this.z = z;
        this.health = 10;
        this.flying = true;
    }

    public void tick() {
        double zd;
        ++this.animTime;
        this.sprite.tex = this.defaultTex + this.animTime / 10 % 2;
        double xd = this.level.player.x + Math.sin(this.rotatePos) * 2.0 - this.x;
        double dd = xd * xd + (zd = this.level.player.z + Math.cos(this.rotatePos) * 2.0 - this.z) * zd;
        this.rotatePos = dd < 1.0 ? (this.rotatePos += 0.04) : this.level.player.rot;
        if (dd < 16.0) {
            dd = Math.sqrt(dd);
            this.xa += (xd /= dd) * 0.006;
            this.za += (zd /= dd) * 0.006;
            if (this.shootDelay > 0) {
                --this.shootDelay;
            } else if (random.nextInt(10) == 0) {
                this.shootDelay = 10;
                this.level.addEntity(new Bullet(this, this.x, this.z, Math.atan2(this.level.player.x - this.x, this.level.player.z - this.z), 0.2, 1, this.defaultColor));
            }
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

