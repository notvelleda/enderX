/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.entities;

import com.mojang.escape.Art;
import com.mojang.escape.Sound;
import com.mojang.escape.entities.BatEntity;
import com.mojang.escape.entities.EnemyEntity;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.KeyEntity;
import com.mojang.escape.level.Level;
import java.util.Random;

public class BatBossEntity
extends EnemyEntity {
    public BatBossEntity(int x, int z) {
        super(x, z, 32, Art.getCol(16776960));
        this.x = x;
        this.z = z;
        this.health = 5;
        this.r = 0.3;
        this.flying = true;
    }

    protected void die() {
        Sound.bosskill.play();
        this.level.addEntity(new KeyEntity(this.x, this.z));
    }

    public void tick() {
        super.tick();
        if (random.nextInt(20) == 0) {
            double xx = this.x + (random.nextDouble() - 0.5) * 2.0;
            double zz = this.z + (random.nextDouble() - 0.5) * 2.0;
            BatEntity batEntity = new BatEntity(xx, zz);
            batEntity.level = this.level;
            batEntity.x = -999.0;
            batEntity.z = -999.0;
            if (batEntity.isFree(xx, zz)) {
                this.level.addEntity(batEntity);
            }
        }
    }
}

