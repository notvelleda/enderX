/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.entities;

import com.mojang.escape.Sound;
import com.mojang.escape.entities.Bullet;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.Item;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.Block;
import com.mojang.escape.level.block.IceBlock;
import com.mojang.escape.level.block.WaterBlock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player
extends Entity {
    public double bob;
    public double bobPhase;
    public double turnBob;
    public int selectedSlot = 0;
    public int itemUseTime;
    public double y;
    public double ya;
    public int hurtTime = 0;
    public int health = 20;
    public int keys = 0;
    public int loot = 0;
    public boolean dead = false;
    private int deadTime = 0;
    public int ammo = 0;
    public int potions = 0;
    private Block lastBlock;
    public Item[] items = new Item[8];
    boolean sliding = false;
    public int time;

    public Player() {
        this.r = 0.3;
        int i = 0;
        while (i < this.items.length) {
            this.items[i] = Item.none;
            ++i;
        }
    }

    public void tick(boolean up, boolean down, boolean left, boolean right, boolean turnLeft, boolean turnRight) {
        double dd;
        if (this.dead) {
            turnRight = false;
            turnLeft = false;
            right = false;
            left = false;
            down = false;
            up = false;
            ++this.deadTime;
            if (this.deadTime > 120) {
                this.level.lose();
            }
        } else {
            ++this.time;
        }
        if (this.itemUseTime > 0) {
            --this.itemUseTime;
        }
        if (this.hurtTime > 0) {
            --this.hurtTime;
        }
        Block onBlock = this.level.getBlock((int)(this.x + 0.5), (int)(this.z + 0.5));
        double fh = onBlock.getFloorHeight(this);
        if (onBlock instanceof WaterBlock && !(this.lastBlock instanceof WaterBlock)) {
            Sound.splash.play();
        }
        this.lastBlock = onBlock;
        if (this.dead) {
            fh = -0.6;
        }
        if (fh > this.y) {
            this.y += (fh - this.y) * 0.2;
            this.ya = 0.0;
        } else {
            this.ya -= 0.01;
            this.y += this.ya;
            if (this.y < fh) {
                this.y = fh;
                this.ya = 0.0;
            }
        }
        double rotSpeed = 0.05;
        double walkSpeed = 0.03 * onBlock.getWalkSpeed(this);
        if (turnLeft) {
            this.rota += rotSpeed;
        }
        if (turnRight) {
            this.rota -= rotSpeed;
        }
        double xm = 0.0;
        double zm = 0.0;
        if (up) {
            zm -= 1.0;
        }
        if (down) {
            zm += 1.0;
        }
        if (left) {
            xm -= 1.0;
        }
        if (right) {
            xm += 1.0;
        }
        dd = (dd = xm * xm + zm * zm) > 0.0 ? Math.sqrt(dd) : 1.0;
        this.bob *= 0.6;
        this.turnBob *= 0.8;
        this.turnBob += this.rota;
        this.bob += Math.sqrt(xm * (xm /= dd) + zm * (zm /= dd));
        this.bobPhase += Math.sqrt(xm * xm + zm * zm) * onBlock.getWalkSpeed(this);
        boolean wasSliding = this.sliding;
        this.sliding = false;
        if (onBlock instanceof IceBlock && this.getSelectedItem() != Item.skates) {
            if (this.xa * this.xa > this.za * this.za) {
                this.sliding = true;
                this.za = 0.0;
                this.xa = this.xa > 0.0 ? 0.08 : -0.08;
                this.z += ((double)((int)(this.z + 0.5)) - this.z) * 0.2;
            } else if (this.xa * this.xa < this.za * this.za) {
                this.sliding = true;
                this.xa = 0.0;
                this.za = this.za > 0.0 ? 0.08 : -0.08;
                this.x += ((double)((int)(this.x + 0.5)) - this.x) * 0.2;
            } else {
                this.xa -= (xm * Math.cos(this.rot) + zm * Math.sin(this.rot)) * 0.1;
                this.za -= (zm * Math.cos(this.rot) - xm * Math.sin(this.rot)) * 0.1;
            }
            if (!wasSliding && this.sliding) {
                Sound.slide.play();
            }
        } else {
            this.xa -= (xm * Math.cos(this.rot) + zm * Math.sin(this.rot)) * walkSpeed;
            this.za -= (zm * Math.cos(this.rot) - xm * Math.sin(this.rot)) * walkSpeed;
        }
        this.move();
        double friction = onBlock.getFriction(this);
        this.xa *= friction;
        this.za *= friction;
        this.rot += this.rota;
        this.rota *= 0.4;
    }

    public void activate() {
        if (this.dead) {
            return;
        }
        if (this.itemUseTime > 0) {
            return;
        }
        Item item = this.items[this.selectedSlot];
        if (item == Item.pistol) {
            if (this.ammo > 0) {
                Sound.shoot.play();
                this.itemUseTime = 10;
                this.level.addEntity(new Bullet(this, this.x, this.z, this.rot, 1.0, 0, 16777215));
                --this.ammo;
            }
            return;
        }
        if (item == Item.potion) {
            if (this.potions > 0 && this.health < 20) {
                Sound.potion.play();
                this.itemUseTime = 20;
                this.health += 5 + random.nextInt(6);
                if (this.health > 20) {
                    this.health = 20;
                }
                --this.potions;
            }
            return;
        }
        if (item == Item.key) {
            this.itemUseTime = 10;
        }
        if (item == Item.powerGlove) {
            this.itemUseTime = 10;
        }
        if (item == Item.cutters) {
            this.itemUseTime = 10;
        }
        double xa = 2.0 * Math.sin(this.rot);
        double za = 2.0 * Math.cos(this.rot);
        int rr = 3;
        int xc = (int)(this.x + 0.5);
        int zc = (int)(this.z + 0.5);
        ArrayList<Entity> possibleHits = new ArrayList<Entity>();
        int z = zc - rr;
        while (z <= zc + rr) {
            int x = xc - rr;
            while (x <= xc + rr) {
                List<Entity> es = this.level.getBlock((int)x, (int)z).entities;
                int i = 0;
                while (i < es.size()) {
                    Entity e = es.get(i);
                    if (e != this) {
                        possibleHits.add(e);
                    }
                    ++i;
                }
                ++x;
            }
            ++z;
        }
        int divs = 100;
        int i = 0;
        while (i < divs) {
            double xx = this.x + xa * (double)i / (double)divs;
            double zz = this.z + za * (double)i / (double)divs;
            int j = 0;
            while (j < possibleHits.size()) {
                Entity e = (Entity)possibleHits.get(j);
                if (e.contains(xx, zz) && e.use(this, this.items[this.selectedSlot])) {
                    return;
                }
                ++j;
            }
            int xt = (int)(xx + 0.5);
            int zt = (int)(zz + 0.5);
            if (xt != (int)(this.x + 0.5) || zt != (int)(this.z + 0.5)) {
                Block block = this.level.getBlock(xt, zt);
                if (block.use(this.level, this.items[this.selectedSlot])) {
                    return;
                }
                if (block.blocks(this)) {
                    return;
                }
            }
            ++i;
        }
    }

    public boolean blocks(Entity entity, double x2, double z2, double r2) {
        return super.blocks(entity, x2, z2, r2);
    }

    public Item getSelectedItem() {
        return this.items[this.selectedSlot];
    }

    public void addLoot(Item item) {
        if (item == Item.pistol) {
            this.ammo += 20;
        }
        if (item == Item.potion) {
            ++this.potions;
        }
        int i = 0;
        while (i < this.items.length) {
            if (this.items[i] == item) {
                if (this.level != null) {
                    this.level.showLootScreen(item);
                }
                return;
            }
            ++i;
        }
        i = 0;
        while (i < this.items.length) {
            if (this.items[i] == Item.none) {
                this.items[i] = item;
                this.selectedSlot = i;
                this.itemUseTime = 0;
                if (this.level != null) {
                    this.level.showLootScreen(item);
                }
                return;
            }
            ++i;
        }
    }

    public void hurt(Entity enemy, int dmg) {
        if (this.hurtTime > 0 || this.dead) {
            return;
        }
        this.hurtTime = 40;
        this.health -= dmg;
        if (this.health <= 0) {
            this.health = 0;
            Sound.death.play();
            this.dead = true;
        }
        Sound.hurt.play();
        double xd = enemy.x - this.x;
        double zd = enemy.z - this.z;
        double dd = Math.sqrt(xd * xd + zd * zd);
        this.xa -= xd / dd * 0.1;
        this.za -= zd / dd * 0.1;
        this.rota += (random.nextDouble() - 0.5) * 0.2;
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
            this.hurt(entity, 1);
        }
    }

    public void win() {
        this.level.win();
    }
}

