/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.Sound;
import com.mojang.escape.entities.Item;
import com.mojang.escape.entities.Player;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.SolidBlock;

public class FinalUnlockBlock
extends SolidBlock {
    private boolean pressed = false;

    public FinalUnlockBlock() {
        this.tex = 11;
    }

    public boolean use(Level level, Item item) {
        if (this.pressed) {
            return false;
        }
        if (level.player.keys < 4) {
            return false;
        }
        Sound.click1.play();
        this.pressed = true;
        level.trigger(this.id, true);
        return true;
    }
}

