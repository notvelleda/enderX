/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.Art;
import com.mojang.escape.Sound;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.Player;
import com.mojang.escape.gui.Sprite;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.Block;

public class LadderBlock
extends Block {
    private static final int LADDER_COLOR = 14388819;
    public boolean wait;

    public LadderBlock(boolean down) {
        if (down) {
            this.floorTex = 1;
            this.addSprite(new Sprite(0.0, 0.0, 0.0, 11, Art.getCol(14388819)));
        } else {
            this.ceilTex = 1;
            this.addSprite(new Sprite(0.0, 0.0, 0.0, 12, Art.getCol(14388819)));
        }
    }

    public void removeEntity(Entity entity) {
        super.removeEntity(entity);
        if (entity instanceof Player) {
            this.wait = false;
        }
    }

    public void addEntity(Entity entity) {
        super.addEntity(entity);
        if (!this.wait && entity instanceof Player) {
            this.level.switchLevel(this.id);
            Sound.ladder.play();
        }
    }
}

