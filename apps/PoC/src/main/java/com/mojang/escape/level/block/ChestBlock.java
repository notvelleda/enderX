/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.Art;
import com.mojang.escape.Sound;
import com.mojang.escape.entities.Item;
import com.mojang.escape.gui.Sprite;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.Block;

public class ChestBlock
extends Block {
    private boolean open = false;
    private Sprite chestSprite;

    public ChestBlock() {
        this.tex = 1;
        this.blocksMotion = true;
        this.chestSprite = new Sprite(0.0, 0.0, 0.0, 16, Art.getCol(16776960));
        this.addSprite(this.chestSprite);
    }

    public boolean use(Level level, Item item) {
        if (this.open) {
            return false;
        }
        ++this.chestSprite.tex;
        this.open = true;
        level.getLoot(this.id);
        Sound.treasure.play();
        return true;
    }
}

