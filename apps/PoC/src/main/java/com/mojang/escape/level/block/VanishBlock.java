/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.Sound;
import com.mojang.escape.entities.Item;
import com.mojang.escape.gui.RubbleSprite;
import com.mojang.escape.gui.Sprite;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.SolidBlock;

public class VanishBlock
extends SolidBlock {
    private boolean gone = false;

    public VanishBlock() {
        this.tex = 1;
    }

    public boolean use(Level level, Item item) {
        if (this.gone) {
            return false;
        }
        this.gone = true;
        this.blocksMotion = false;
        this.solidRender = false;
        Sound.crumble.play();
        int i = 0;
        while (i < 32) {
            RubbleSprite sprite = new RubbleSprite();
            sprite.col = this.col;
            this.addSprite(sprite);
            ++i;
        }
        return true;
    }
}

