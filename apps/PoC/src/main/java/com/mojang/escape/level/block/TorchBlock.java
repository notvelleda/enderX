/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level.block;

import com.mojang.escape.Art;
import com.mojang.escape.gui.Sprite;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.Block;
import java.util.List;
import java.util.Random;

public class TorchBlock
extends Block {
    private Sprite torchSprite = new Sprite(0.0, 0.0, 0.0, 3, Art.getCol(16776960));

    public TorchBlock() {
        this.sprites.add(this.torchSprite);
    }

    public void decorate(Level level, int x, int y) {
        Random random = new Random((x + y * 1000) * 341871231);
        double r = 0.4;
        int i = 0;
        while (i < 1000) {
            int face = random.nextInt(4);
            if (face == 0 && level.getBlock((int)(x - 1), (int)y).solidRender) {
                this.torchSprite.x -= r;
                break;
            }
            if (face == 1 && level.getBlock((int)x, (int)(y - 1)).solidRender) {
                this.torchSprite.z -= r;
                break;
            }
            if (face == 2 && level.getBlock((int)(x + 1), (int)y).solidRender) {
                this.torchSprite.x += r;
                break;
            }
            if (face == 3 && level.getBlock((int)x, (int)(y + 1)).solidRender) {
                this.torchSprite.z += r;
                break;
            }
            ++i;
        }
    }

    public void tick() {
        super.tick();
        if (random.nextInt(4) == 0) {
            this.torchSprite.tex = 3 + random.nextInt(2);
        }
    }
}

