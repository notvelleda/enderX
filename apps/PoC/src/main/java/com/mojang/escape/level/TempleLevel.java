/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level;

import com.mojang.escape.Game;
import com.mojang.escape.entities.Item;
import com.mojang.escape.level.Level;

public class TempleLevel
extends Level {
    private int triggerMask = 0;

    public TempleLevel() {
        this.floorCol = 9069718;
        this.ceilCol = 9069718;
        this.wallCol = 13610459;
        this.name = "The Temple";
    }

    public void switchLevel(int id) {
        if (id == 1) {
            this.game.switchLevel("overworld", 3);
        }
    }

    public void getLoot(int id) {
        super.getLoot(id);
        if (id == 1) {
            this.game.getLoot(Item.skates);
        }
    }

    public void trigger(int id, boolean pressed) {
        this.triggerMask |= 1 << id;
        if (!pressed) {
            this.triggerMask ^= 1 << id;
        }
        if (this.triggerMask == 14) {
            super.trigger(1, true);
        } else {
            super.trigger(1, false);
        }
    }
}

