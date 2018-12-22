/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.Sound;
import com.mojang.escape.entities.Bullet;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.Item;
import com.mojang.escape.entities.OgreEntity;
import com.mojang.escape.entities.Player;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.SolidBlock;

public class DoorBlock
extends SolidBlock {
    public boolean open = false;
    public double openness = 0.0;

    public DoorBlock() {
        this.tex = 4;
        this.solidRender = false;
    }

    public boolean use(Level level, Item item) {
        boolean bl = this.open = !this.open;
        if (this.open) {
            Sound.click1.play();
        } else {
            Sound.click2.play();
        }
        return true;
    }

    public void tick() {
        double openLimit;
        super.tick();
        this.openness = this.open ? (this.openness += 0.2) : (this.openness -= 0.2);
        if (this.openness < 0.0) {
            this.openness = 0.0;
        }
        if (this.openness > 1.0) {
            this.openness = 1.0;
        }
        if (this.openness < (openLimit = 0.875) && !this.open && !this.blocksMotion && this.level.containsBlockingEntity((double)this.x - 0.5, (double)this.y - 0.5, (double)this.x + 0.5, (double)this.y + 0.5)) {
            this.openness = openLimit;
            return;
        }
        this.blocksMotion = this.openness < openLimit;
    }

    public boolean blocks(Entity entity) {
        double openLimit = 0.875;
        if (this.openness >= openLimit && entity instanceof Player) {
            return this.blocksMotion;
        }
        if (this.openness >= openLimit && entity instanceof Bullet) {
            return this.blocksMotion;
        }
        if (this.openness >= openLimit && entity instanceof OgreEntity) {
            return this.blocksMotion;
        }
        return true;
    }
}

