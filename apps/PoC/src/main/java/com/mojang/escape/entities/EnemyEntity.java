/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.entities;

import com.mojang.escape.Art;
import com.mojang.escape.Sound;
import com.mojang.escape.entities.Bullet;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.Item;
import com.mojang.escape.entities.Player;
import com.mojang.escape.gui.PoofSprite;
import com.mojang.escape.gui.Sprite;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.Block;
import java.util.List;
import java.util.Random;

public class EnemyEntity
extends Entity {
    protected Sprite sprite;
    protected double rot;
    protected double rota;
    protected int defaultTex;
    protected int defaultColor;
    protected int hurtTime = 0;
    protected int animTime = 0;
    protected int health = 3;
    protected double spinSpeed = 0.1;
    protected double runSpeed = 1.0;

    public EnemyEntity(double x, double z, int defaultTex, int defaultColor) {
        this.x = x;
        this.z = z;
        this.defaultColor = defaultColor;
        this.defaultTex = defaultTex;
        this.sprite = new Sprite(0.0, 0.0, 0.0, 32, defaultColor);
        this.sprites.add(this.sprite);
        this.r = 0.3;
    }

    public void tick() {
        if (this.hurtTime > 0) {
            --this.hurtTime;
            if (this.hurtTime == 0) {
                this.sprite.col = this.defaultColor;
            }
        }
        ++this.animTime;
        this.sprite.tex = this.defaultTex + this.animTime / 10 % 2;
        this.move();
        if (this.xa == 0.0 || this.za == 0.0) {
            this.rota += random.nextGaussian() * random.nextDouble() * 0.3;
        }
        this.rota += random.nextGaussian() * random.nextDouble() * this.spinSpeed;
        this.rot += this.rota;
        this.rota *= 0.8;
        this.xa *= 0.8;
        this.za *= 0.8;
        this.xa += Math.sin(this.rot) * 0.004 * this.runSpeed;
        this.za += Math.cos(this.rot) * 0.004 * this.runSpeed;
    }

    public boolean use(Entity source, Item item) {
        if (this.hurtTime > 0) {
            return false;
        }
        if (item != Item.powerGlove) {
            return false;
        }
        this.hurt(Math.sin(source.rot), Math.cos(source.rot));
        return true;
    }

    protected void hurt(double xd, double zd) {
        this.sprite.col = Art.getCol(16711680);
        this.hurtTime = 15;
        double dd = Math.sqrt(xd * xd + zd * zd);
        this.xa += xd / dd * 0.2;
        this.za += zd / dd * 0.2;
        Sound.hurt2.play();
        --this.health;
        if (this.health <= 0) {
            int xt = (int)(this.x + 0.5);
            int zt = (int)(this.z + 0.5);
            this.level.getBlock(xt, zt).addSprite(new PoofSprite(this.x - (double)xt, 0.0, this.z - (double)zt));
            this.die();
            this.remove();
            Sound.kill.play();
        }
    }

    protected void die() {
    }

    protected void collide(Entity entity) {
        if (entity instanceof Bullet) {
            Bullet bullet = (Bullet)entity;
            if (bullet.owner.getClass() == this.getClass()) {
                return;
            }
            if (this.hurtTime > 0) {
                return;
            }
            entity.remove();
            this.hurt(entity.xa, entity.za);
        }
        if (entity instanceof Player) {
            ((Player)entity).hurt(this, 1);
        }
    }
}

