/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level;

import com.mojang.escape.Game;
import com.mojang.escape.entities.Item;
import com.mojang.escape.level.Level;

public class OverworldLevel
extends Level {
    public OverworldLevel() {
        this.ceilTex = -1;
        this.floorCol = 5276243;
        this.floorTex = 24;
        this.wallCol = 10526880;
        this.name = "The Island";
    }

    public void switchLevel(int id) {
        if (id == 1) {
            this.game.switchLevel("start", 1);
        }
        if (id == 2) {
            this.game.switchLevel("crypt", 1);
        }
        if (id == 3) {
            this.game.switchLevel("temple", 1);
        }
        if (id == 5) {
            this.game.switchLevel("ice", 1);
        }
    }

    public void getLoot(int id) {
        super.getLoot(id);
        if (id == 1) {
            this.game.getLoot(Item.cutters);
        }
    }
}

