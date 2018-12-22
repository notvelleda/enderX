/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.Player;
import com.mojang.escape.level.block.Block;

public class WinBlock
extends Block {
    public void addEntity(Entity entity) {
        super.addEntity(entity);
        if (entity instanceof Player) {
            ((Player)entity).win();
        }
    }
}

