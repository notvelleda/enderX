/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.gui;

import com.mojang.escape.gui.Sprite;

public class PoofSprite
extends Sprite {
    int life = 20;

    public PoofSprite(double x, double y, double z) {
        super(x, y, z, 5, 2236962);
    }

    public void tick() {
        if (this.life-- <= 0) {
            this.removed = true;
        }
    }
}

