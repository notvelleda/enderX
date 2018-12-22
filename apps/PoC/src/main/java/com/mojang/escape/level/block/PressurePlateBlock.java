/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.Sound;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.Block;

public class PressurePlateBlock
extends Block {
    public boolean pressed = false;

    public PressurePlateBlock() {
        this.floorTex = 2;
    }

    public void tick() {
        super.tick();
        double r = 0.2;
        boolean steppedOn = this.level.containsBlockingNonFlyingEntity((double)this.x - r, (double)this.y - r, (double)this.x + r, (double)this.y + r);
        if (steppedOn != this.pressed) {
            this.pressed = steppedOn;
            this.floorTex = this.pressed ? 3 : 2;
            this.level.trigger(this.id, this.pressed);
            if (this.pressed) {
                Sound.click1.play();
            } else {
                Sound.click2.play();
            }
        }
    }

    public double getFloorHeight(Entity e) {
        if (this.pressed) {
            return -0.02;
        }
        return 0.02;
    }
}

