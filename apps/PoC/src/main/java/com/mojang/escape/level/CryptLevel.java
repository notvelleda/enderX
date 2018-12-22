/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level;

import com.mojang.escape.Game;
import com.mojang.escape.entities.Item;
import com.mojang.escape.level.Level;

public class CryptLevel
extends Level {
    public CryptLevel() {
        this.floorCol = 4210752;
        this.ceilCol = 4210752;
        this.wallCol = 4210752;
        this.name = "The Crypt";
    }

    public void switchLevel(int id) {
        if (id == 1) {
            this.game.switchLevel("overworld", 2);
        }
    }

    public void getLoot(int id) {
        super.getLoot(id);
        if (id == 1) {
            this.game.getLoot(Item.flippers);
        }
    }
}

