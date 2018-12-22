/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.entities;

import com.mojang.escape.Art;
import com.mojang.escape.entities.EnemyEntity;

public class EyeEntity
extends EnemyEntity {
    public EyeEntity(double x, double z) {
        super(x, z, 36, Art.getCol(8711423));
        this.x = x;
        this.z = z;
        this.health = 4;
        this.r = 0.3;
        this.runSpeed = 2.0;
        this.spinSpeed *= 1.5;
        this.flying = true;
    }
}

