/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level;

import com.mojang.escape.Game;
import com.mojang.escape.entities.Item;
import com.mojang.escape.level.Level;

public class DungeonLevel
extends Level {
    public DungeonLevel() {
        this.wallCol = 12994900;
        this.floorCol = 9325137;
        this.ceilCol = 9325137;
        this.name = "The Dungeons";
    }

    public void init(Game game, String name, int w, int h, int[] pixels) {
        super.init(game, name, w, h, pixels);
        super.trigger(6, true);
        super.trigger(7, true);
    }

    public void switchLevel(int id) {
        if (id == 1) {
            this.game.switchLevel("start", 2);
        }
    }

    public void getLoot(int id) {
        super.getLoot(id);
        if (id == 1) {
            this.game.getLoot(Item.powerGlove);
        }
    }

    public void trigger(int id, boolean pressed) {
        super.trigger(id, pressed);
        if (id == 5) {
            super.trigger(6, !pressed);
        }
        if (id == 4) {
            super.trigger(7, !pressed);
        }
    }
}

