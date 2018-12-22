/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.Art;
import com.mojang.escape.entities.Bullet;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.gui.Sprite;
import com.mojang.escape.level.block.Block;
import java.util.Random;

public class SpiritWallBlock
extends Block {
    public SpiritWallBlock() {
        this.floorTex = 7;
        this.ceilTex = 7;
        this.blocksMotion = true;
        int i = 0;
        while (i < 6) {
            double x = random.nextDouble() - 0.5;
            double y = (random.nextDouble() - 0.7) * 0.3;
            double z = random.nextDouble() - 0.5;
            this.addSprite(new Sprite(x, y, z, 38 + random.nextInt(2), Art.getCol(2105376)));
            ++i;
        }
    }

    public boolean blocks(Entity entity) {
        if (entity instanceof Bullet) {
            return false;
        }
        return super.blocks(entity);
    }
}

