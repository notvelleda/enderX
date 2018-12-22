/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.Sound;
import com.mojang.escape.entities.BoulderEntity;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.gui.Sprite;
import com.mojang.escape.level.block.Block;

public class PitBlock
extends Block {
    private boolean filled = false;

    public PitBlock() {
        this.floorTex = 1;
        this.blocksMotion = true;
    }

    public void addEntity(Entity entity) {
        super.addEntity(entity);
        if (!this.filled && entity instanceof BoulderEntity) {
            entity.remove();
            this.filled = true;
            this.blocksMotion = false;
            this.addSprite(new Sprite(0.0, 0.0, 0.0, 10, BoulderEntity.COLOR));
            Sound.thud.play();
        }
    }

    public boolean blocks(Entity entity) {
        if (entity instanceof BoulderEntity) {
            return false;
        }
        return this.blocksMotion;
    }
}

