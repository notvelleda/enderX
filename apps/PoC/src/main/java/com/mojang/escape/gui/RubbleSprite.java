/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.gui;

import com.mojang.escape.gui.Sprite;

public class RubbleSprite
extends Sprite {
    private double xa = Math.random() - 0.5;
    private double ya = Math.random();
    private double za = Math.random() - 0.5;

    public RubbleSprite() {
        super(Math.random() - 0.5, Math.random() * 0.8, Math.random() - 0.5, 2, 5592405);
    }

    public void tick() {
        this.x += this.xa * 0.03;
        this.y += this.ya * 0.03;
        this.z += this.za * 0.03;
        this.ya -= 0.1;
        if (this.y < 0.0) {
            this.y = 0.0;
            this.xa *= 0.8;
            this.za *= 0.8;
            if (Math.random() < 0.04) {
                this.removed = true;
            }
        }
    }
}

