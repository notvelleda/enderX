/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.Item;
import com.mojang.escape.entities.Player;
import com.mojang.escape.gui.Sprite;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.SolidBlock;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Block {
    protected static Random random = new Random();
    public boolean blocksMotion = false;
    public boolean solidRender = false;
    public String[] messages;
    public static Block solidWall = new SolidBlock();
    public List<Sprite> sprites = new ArrayList<Sprite>();
    public List<Entity> entities = new ArrayList<Entity>();
    public int tex = -1;
    public int col = -1;
    public int floorCol = -1;
    public int ceilCol = -1;
    public int floorTex = -1;
    public int ceilTex = -1;
    public Level level;
    public int x;
    public int y;
    public int id;

    public void addSprite(Sprite sprite) {
        this.sprites.add(sprite);
    }

    public boolean use(Level level, Item item) {
        return false;
    }

    public void tick() {
        int i = 0;
        while (i < this.sprites.size()) {
            Sprite sprite = this.sprites.get(i);
            sprite.tick();
            if (sprite.removed) {
                this.sprites.remove(i--);
            }
            ++i;
        }
    }

    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public boolean blocks(Entity entity) {
        return this.blocksMotion;
    }

    public void decorate(Level level, int x, int y) {
    }

    public double getFloorHeight(Entity e) {
        return 0.0;
    }

    public double getWalkSpeed(Player player) {
        return 1.0;
    }

    public double getFriction(Player player) {
        return 0.6;
    }

    public void trigger(boolean pressed) {
    }
}

