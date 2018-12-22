/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.Sound;
import com.mojang.escape.entities.Bullet;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.Item;
import com.mojang.escape.entities.Player;
import com.mojang.escape.gui.Sprite;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.Block;

public class BarsBlock
extends Block {
    private Sprite sprite = new Sprite(0.0, 0.0, 0.0, 0, 2105376);
    private boolean open = false;

    public BarsBlock() {
        this.addSprite(this.sprite);
        this.blocksMotion = true;
    }

    public boolean use(Level level, Item item) {
        if (this.open) {
            return false;
        }
        if (item == Item.cutters) {
            Sound.cut.play();
            this.sprite.tex = 1;
            this.open = true;
        }
        return true;
    }

    public boolean blocks(Entity entity) {
        if (this.open && entity instanceof Player) {
            return false;
        }
        if (this.open && entity instanceof Bullet) {
            return false;
        }
        return this.blocksMotion;
    }
}

