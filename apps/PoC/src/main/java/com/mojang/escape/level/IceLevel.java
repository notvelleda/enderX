/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level;

import com.mojang.escape.Game;
import com.mojang.escape.entities.Item;
import com.mojang.escape.level.Level;

public class IceLevel
extends Level {
    public IceLevel() {
        this.floorCol = 12114912;
        this.ceilCol = 12114912;
        this.wallCol = 7071999;
        this.name = "The Frost Cave";
    }

    public void switchLevel(int id) {
        if (id == 1) {
            this.game.switchLevel("overworld", 5);
        }
    }

    public void getLoot(int id) {
        super.getLoot(id);
        if (id == 1) {
            this.game.getLoot(Item.skates);
        }
    }
}

