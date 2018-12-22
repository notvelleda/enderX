/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.entities;

import com.mojang.escape.Art;
import com.mojang.escape.entities.EnemyEntity;

public class BatEntity
extends EnemyEntity {
    public BatEntity(double x, double z) {
        super(x, z, 32, Art.getCol(8545902));
        this.x = x;
        this.z = z;
        this.health = 2;
        this.r = 0.3;
        this.flying = true;
    }
}

