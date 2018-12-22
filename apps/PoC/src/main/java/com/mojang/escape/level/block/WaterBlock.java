/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.Art;
import com.mojang.escape.entities.Bullet;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.Item;
import com.mojang.escape.entities.Player;
import com.mojang.escape.level.block.Block;
import java.util.Random;

public class WaterBlock
extends Block {
    int steps = 0;

    public WaterBlock() {
        this.blocksMotion = true;
    }

    public void tick() {
        super.tick();
        --this.steps;
        if (this.steps <= 0) {
            this.floorTex = 8 + random.nextInt(3);
            this.floorCol = Art.getCol(255);
            this.steps = 16;
        }
    }

    public boolean blocks(Entity entity) {
        if (entity instanceof Player && ((Player)entity).getSelectedItem() == Item.flippers) {
            return false;
        }
        if (entity instanceof Bullet) {
            return false;
        }
        return this.blocksMotion;
    }

    public double getFloorHeight(Entity e) {
        return -0.5;
    }

    public double getWalkSpeed(Player player) {
        return 0.4;
    }
}

