/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.entities;

import com.mojang.escape.Art;
import com.mojang.escape.Sound;
import com.mojang.escape.entities.Bullet;
import com.mojang.escape.entities.EnemyEntity;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.KeyEntity;
import com.mojang.escape.level.Level;

public class BossOgre
extends EnemyEntity {
    private int shootDelay;
    private int shootPhase;

    public BossOgre(double x, double z) {
        super(x, z, 34, Art.getCol(16776960));
        this.x = x;
        this.z = z;
        this.health = 10;
        this.r = 0.4;
        this.spinSpeed = 0.05;
    }

    protected void die() {
        Sound.bosskill.play();
        this.level.addEntity(new KeyEntity(this.x, this.z));
    }

    public void tick() {
        super.tick();
        if (this.shootDelay > 0) {
            --this.shootDelay;
        } else {
            this.shootDelay = 5;
            int salva = 10;
            int i = 0;
            while (i < 4) {
                double rot = 1.5707963267948966 * ((double)i + (double)(this.shootPhase / salva % 2) * 0.5);
                this.level.addEntity(new Bullet(this, this.x, this.z, rot, 0.4, 1, this.defaultColor));
                ++i;
            }
            ++this.shootPhase;
            if (this.shootPhase % salva == 0) {
                this.shootDelay = 40;
            }
        }
    }
}

