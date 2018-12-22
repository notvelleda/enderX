/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.Sound;
import com.mojang.escape.entities.Item;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.SolidBlock;

public class SwitchBlock
extends SolidBlock {
    private boolean pressed = false;

    public SwitchBlock() {
        this.tex = 2;
    }

    public boolean use(Level level, Item item) {
        this.pressed = !this.pressed;
        this.tex = this.pressed ? 3 : 2;
        level.trigger(this.id, this.pressed);
        if (this.pressed) {
            Sound.click1.play();
        } else {
            Sound.click2.play();
        }
        return true;
    }
}

