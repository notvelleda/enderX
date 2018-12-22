/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.Art;
import com.mojang.escape.Sound;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.Player;
import com.mojang.escape.gui.Sprite;
import com.mojang.escape.level.block.Block;

public class LootBlock
extends Block {
    private boolean taken = false;
    private Sprite sprite = new Sprite(0.0, 0.0, 0.0, 18, Art.getCol(16777088));

    public LootBlock() {
        this.addSprite(this.sprite);
        this.blocksMotion = true;
    }

    public void addEntity(Entity entity) {
        super.addEntity(entity);
        if (!this.taken && entity instanceof Player) {
            this.sprite.removed = true;
            this.taken = true;
            this.blocksMotion = false;
            ++((Player)entity).loot;
            Sound.pickup.play();
        }
    }

    public boolean blocks(Entity entity) {
        if (entity instanceof Player) {
            return false;
        }
        return this.blocksMotion;
    }
}

