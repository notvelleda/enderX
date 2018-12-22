/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.gui;

import com.mojang.escape.Art;
import com.mojang.escape.Game;
import com.mojang.escape.entities.Item;
import com.mojang.escape.entities.Player;
import com.mojang.escape.gui.Bitmap;
import com.mojang.escape.gui.Bitmap3D;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.Block;
import com.mojang.escape.menu.Menu;
import java.util.Random;

public class Screen
extends Bitmap {
    private static final int PANEL_HEIGHT = 29;
    private Bitmap testBitmap;
    private Bitmap3D viewport;
    int time = 0;

    public Screen(int width, int height) {
        super(width, height);
        this.viewport = new Bitmap3D(width, height - 29);
        Random random = new Random();
        this.testBitmap = new Bitmap(64, 64);
        int i = 0;
        while (i < 4096) {
            this.testBitmap.pixels[i] = random.nextInt() * (random.nextInt(5) / 4);
            ++i;
        }
    }

    public void render(Game game, boolean hasFocus) {
        int i;
        if (game.level == null) {
            this.fill(0, 0, this.width, this.height, 0);
        } else {
            int y;
            boolean itemUsed = game.player.itemUseTime > 0;
            Item item = game.player.items[game.player.selectedSlot];
            if (game.pauseTime > 0) {
                this.fill(0, 0, this.width, this.height, 0);
                String[] messages = new String[]{"Entering " + game.level.name};
                y = 0;
                while (y < messages.length) {
                    this.draw(messages[y], (this.width - messages[y].length() * 6) / 2, (this.viewport.height - messages.length * 8) / 2 + y * 8 + 1, 1118481);
                    this.draw(messages[y], (this.width - messages[y].length() * 6) / 2, (this.viewport.height - messages.length * 8) / 2 + y * 8, 5592388);
                    ++y;
                }
            } else {
                this.viewport.render(game);
                this.viewport.postProcess(game.level);
                Block block = game.level.getBlock((int)(game.player.x + 0.5), (int)(game.player.z + 0.5));
                if (block.messages != null && hasFocus) {
                    y = 0;
                    while (y < block.messages.length) {
                        this.viewport.draw(block.messages[y], (this.width - block.messages[y].length() * 6) / 2, (this.viewport.height - block.messages.length * 8) / 2 + y * 8 + 1, 1118481);
                        this.viewport.draw(block.messages[y], (this.width - block.messages[y].length() * 6) / 2, (this.viewport.height - block.messages.length * 8) / 2 + y * 8, 5592388);
                        ++y;
                    }
                }
                this.draw(this.viewport, 0, 0);
                int xx = (int)(game.player.turnBob * 32.0);
                int yy = (int)(Math.sin(game.player.bobPhase * 0.4) * 1.0 * game.player.bob + game.player.bob * 2.0);
                if (itemUsed) {
                    yy = 0;
                    xx = 0;
                }
                xx += this.width / 2;
                yy += this.height - 29 - 45;
                if (item != Item.none) {
                    this.scaleDraw(Art.items, 3, xx, yy, 16 * item.icon + 1, 17 + (itemUsed ? 16 : 0), 15, 15, Art.getCol(item.color));
                }
                if (game.player.hurtTime > 0 || game.player.dead) {
                    double offs = 1.5 - (double)game.player.hurtTime / 30.0;
                    Random random = new Random(111);
                    if (game.player.dead) {
                        offs = 0.5;
                    }
                    int i2 = 0;
                    while (i2 < this.pixels.length) {
                        double xp = ((double)(i2 % this.width) - (double)this.viewport.width / 2.0) / (double)this.width * 2.0;
                        double yp = ((double)(i2 / this.width) - (double)this.viewport.height / 2.0) / (double)this.viewport.height * 2.0;
                        if (random.nextDouble() + offs < Math.sqrt(xp * xp + yp * yp)) {
                            this.pixels[i2] = random.nextInt(5) / 4 * 5570560;
                        }
                        ++i2;
                    }
                }
            }
            this.draw(Art.panel, 0, this.height - 29, 0, 0, this.width, 29, Art.getCol(7368816));
            this.draw("\u00e5", 3, this.height - 26 + 0, 65535);
            this.draw("" + game.player.keys + "/4", 10, this.height - 26 + 0, 16777215);
            this.draw("\u00c4", 3, this.height - 26 + 8, 16776960);
            this.draw("" + game.player.loot, 10, this.height - 26 + 8, 16777215);
            this.draw("\u00c5", 3, this.height - 26 + 16, 16711680);
            this.draw("" + game.player.health, 10, this.height - 26 + 16, 16777215);
            int i3 = 0;
            while (i3 < 8) {
                Item slotItem = game.player.items[i3];
                if (slotItem != Item.none) {
                    this.draw(Art.items, 30 + i3 * 16, this.height - 29 + 2, slotItem.icon * 16, 0, 16, 16, Art.getCol(slotItem.color));
                    if (slotItem == Item.pistol) {
                        String str = "" + game.player.ammo;
                        this.draw(str, 30 + i3 * 16 + 17 - str.length() * 6, this.height - 29 + 1 + 10, 5592405);
                    }
                    if (slotItem == Item.potion) {
                        String str = "" + game.player.potions;
                        this.draw(str, 30 + i3 * 16 + 17 - str.length() * 6, this.height - 29 + 1 + 10, 5592405);
                    }
                }
                ++i3;
            }
            this.draw(Art.items, 30 + game.player.selectedSlot * 16, this.height - 29 + 2, 0, 48, 17, 17, Art.getCol(16777215));
            this.draw(item.name, 26 + (128 - item.name.length() * 4) / 2, this.height - 9, 16777215);
        }
        if (game.menu != null) {
            i = 0;
            while (i < this.pixels.length) {
                this.pixels[i] = (this.pixels[i] & 16579836) >> 2;
                ++i;
            }
            game.menu.render(this);
        }
        if (!hasFocus) {
            i = 0;
            while (i < this.pixels.length) {
                this.pixels[i] = (this.pixels[i] & 16579836) >> 2;
                ++i;
            }
            if (System.currentTimeMillis() / 450 % 2 != 0) {
                String msg = "Click to focus!";
                this.draw(msg, (this.width - msg.length() * 6) / 2, this.height / 3 + 4, 16777215);
            }
        }
    }
}

