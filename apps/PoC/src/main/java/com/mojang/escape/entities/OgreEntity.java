/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.entities;

import com.mojang.escape.Art;
import com.mojang.escape.entities.Bullet;
import com.mojang.escape.entities.EnemyEntity;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.Player;
import com.mojang.escape.level.Level;
import java.util.Random;

public class OgreEntity
extends EnemyEntity {
    private int shootDelay;

    public OgreEntity(double x, double z) {
        super(x, z, 34, Art.getCol(8562721));
        this.x = x;
        this.z = z;
        this.health = 6;
        this.r = 0.4;
        this.spinSpeed = 0.05;
    }

    protected void hurt(double xd, double zd) {
        super.hurt(xd, zd);
        this.shootDelay = 50;
    }

    public void tick() {
        super.tick();
        if (this.shootDelay > 0) {
            --this.shootDelay;
        } else if (random.nextInt(40) == 0) {
            this.shootDelay = 40;
            this.level.addEntity(new Bullet(this, this.x, this.z, Math.atan2(this.level.player.x - this.x, this.level.player.z - this.z), 0.3, 1, this.defaultColor));
        }
    }
}

