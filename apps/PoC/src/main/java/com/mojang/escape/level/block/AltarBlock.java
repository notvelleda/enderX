/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.Art;
import com.mojang.escape.Sound;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.GhostBossEntity;
import com.mojang.escape.entities.GhostEntity;
import com.mojang.escape.entities.KeyEntity;
import com.mojang.escape.gui.RubbleSprite;
import com.mojang.escape.gui.Sprite;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.Block;

public class AltarBlock
extends Block {
    private boolean filled = false;
    private Sprite sprite;

    public AltarBlock() {
        this.blocksMotion = true;
        this.sprite = new Sprite(0.0, 0.0, 0.0, 20, Art.getCol(14876644));
        this.addSprite(this.sprite);
    }

    public void addEntity(Entity entity) {
        super.addEntity(entity);
        if (!this.filled && (entity instanceof GhostEntity || entity instanceof GhostBossEntity)) {
            entity.remove();
            this.filled = true;
            this.blocksMotion = false;
            this.sprite.removed = true;
            int i = 0;
            while (i < 8) {
                RubbleSprite sprite = new RubbleSprite();
                sprite.col = this.sprite.col;
                this.addSprite(sprite);
                ++i;
            }
            if (entity instanceof GhostBossEntity) {
                this.level.addEntity(new KeyEntity(this.x, this.y));
                Sound.bosskill.play();
            } else {
                Sound.altar.play();
            }
        }
    }

    public boolean blocks(Entity entity) {
        return this.blocksMotion;
    }
}

