/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.entities;

import com.mojang.escape.Art;
import com.mojang.escape.Sound;
import com.mojang.escape.entities.EnemyEntity;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.KeyEntity;
import com.mojang.escape.level.Level;

public class EyeBossEntity
extends EnemyEntity {
    public EyeBossEntity(double x, double z) {
        super(x, z, 36, Art.getCol(16776960));
        this.x = x;
        this.z = z;
        this.health = 10;
        this.r = 0.3;
        this.runSpeed = 4.0;
        this.spinSpeed *= 1.5;
        this.flying = true;
    }

    protected void die() {
        Sound.bosskill.play();
        this.level.addEntity(new KeyEntity(this.x, this.z));
    }
}

