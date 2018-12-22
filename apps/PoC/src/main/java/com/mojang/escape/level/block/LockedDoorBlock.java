/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.entities.Item;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.DoorBlock;

public class LockedDoorBlock
extends DoorBlock {
    public LockedDoorBlock() {
        this.tex = 5;
    }

    public boolean use(Level level, Item item) {
        return false;
    }

    public void trigger(boolean pressed) {
        this.open = pressed;
    }
}

