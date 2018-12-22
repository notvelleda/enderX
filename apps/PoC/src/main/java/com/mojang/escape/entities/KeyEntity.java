/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.entities;

import com.mojang.escape.Art;
import com.mojang.escape.Sound;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.Player;
import com.mojang.escape.gui.Sprite;
import java.util.List;

public class KeyEntity
extends Entity {
    public static final int COLOR = Art.getCol(65535);
    private Sprite sprite;
    private double y;
    private double ya;

    public KeyEntity(double x, double z) {
        this.x = x;
        this.z = z;
        this.y = 0.5;
        this.ya = 0.025;
        this.sprite = new Sprite(0.0, 0.0, 0.0, 19, COLOR);
        this.sprites.add(this.sprite);
    }

    public void tick() {
        this.move();
        this.y += this.ya;
        if (this.y < 0.0) {
            this.y = 0.0;
        }
        this.ya -= 0.005;
        this.sprite.y = this.y;
    }

    protected void collide(Entity entity) {
        if (entity instanceof Player) {
            Sound.key.play();
            ++((Player)entity).keys;
            this.remove();
        }
    }
}

