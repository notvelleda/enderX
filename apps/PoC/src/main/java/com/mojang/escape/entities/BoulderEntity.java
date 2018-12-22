/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.entities;

import com.mojang.escape.Art;
import com.mojang.escape.Sound;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.Item;
import com.mojang.escape.gui.Sprite;
import java.util.List;

public class BoulderEntity
extends Entity {
    public static final int COLOR = Art.getCol(11510419);
    private Sprite sprite;
    private double rollDist = 0.0;

    public BoulderEntity(int x, int z) {
        this.x = x;
        this.z = z;
        this.sprite = new Sprite(0.0, 0.0, 0.0, 16, COLOR);
        this.sprites.add(this.sprite);
    }

    public void tick() {
        this.rollDist += Math.sqrt(this.xa * this.xa + this.za * this.za);
        this.sprite.tex = 8 + ((int)(this.rollDist * 4.0) & 1);
        double xao = this.xa;
        double zao = this.za;
        this.move();
        if (this.xa == 0.0 && xao != 0.0) {
            this.xa = (- xao) * 0.3;
        }
        if (this.za == 0.0 && zao != 0.0) {
            this.za = (- zao) * 0.3;
        }
        this.xa *= 0.98;
        this.za *= 0.98;
        if (this.xa * this.xa + this.za * this.za < 1.0E-4) {
            this.za = 0.0;
            this.xa = 0.0;
        }
    }

    public boolean use(Entity source, Item item) {
        if (item != Item.powerGlove) {
            return false;
        }
        Sound.roll.play();
        this.xa += Math.sin(source.rot) * 0.1;
        this.za += Math.cos(source.rot) * 0.1;
        return true;
    }
}

