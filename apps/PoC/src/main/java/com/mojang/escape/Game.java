/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape;

import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.Item;
import com.mojang.escape.entities.Player;
import com.mojang.escape.level.Level;
import com.mojang.escape.level.block.Block;
import com.mojang.escape.level.block.LadderBlock;
import com.mojang.escape.menu.LoseMenu;
import com.mojang.escape.menu.Menu;
import com.mojang.escape.menu.PauseMenu;
import com.mojang.escape.menu.TitleMenu;
import com.mojang.escape.menu.WinMenu;

public class Game {
    public int time;
    public Level level;
    public Player player;
    public int pauseTime;
    public Menu menu;

    public Game() {
        this.setMenu(new TitleMenu());
    }

    public void newGame() {
        Level.clear();
        this.level = Level.loadLevel(this, "start");
        this.player = new Player();
        this.player.level = this.level;
        this.level.player = this.player;
        this.player.x = this.level.xSpawn;
        this.player.z = this.level.ySpawn;
        this.level.addEntity(this.player);
        this.player.rot = 3.541592653589793;
    }

    public void switchLevel(String name, int id) {
        this.pauseTime = 30;
        this.level.removeEntityImmediately(this.player);
        this.level = Level.loadLevel(this, name);
        this.level.findSpawn(id);
        this.player.x = this.level.xSpawn;
        this.player.z = this.level.ySpawn;
        ((LadderBlock)this.level.getBlock((int)this.level.xSpawn, (int)this.level.ySpawn)).wait = true;
        this.player.x += Math.sin(this.player.rot) * 0.2;
        this.player.z += Math.cos(this.player.rot) * 0.2;
        this.level.addEntity(this.player);
    }

    public void tick(boolean[] keys) {
        if (this.pauseTime > 0) {
            --this.pauseTime;
            return;
        }
        ++this.time;
        boolean strafe = keys[17] || keys[18] || keys[65406] || keys[16];
        boolean lk = keys[37] || keys[100];
        boolean rk = keys[39] || keys[102];
        boolean up = keys[87] || keys[38] || keys[104];
        boolean down = keys[83] || keys[40] || keys[98];
        boolean left = keys[65] || strafe && lk;
        boolean right = keys[68] || strafe && rk;
        boolean turnLeft = keys[81] || !strafe && lk;
        boolean turnRight = keys[69] || !strafe && rk;
        boolean use = keys[32];
        int i = 0;
        while (i < 8) {
            if (keys[49 + i]) {
                keys[49 + i] = false;
                this.player.selectedSlot = i;
                this.player.itemUseTime = 0;
            }
            ++i;
        }
        if (keys[27]) {
            keys[27] = false;
            if (this.menu == null) {
                this.setMenu(new PauseMenu());
            }
        }
        if (use) {
            keys[32] = false;
        }
        if (this.menu != null) {
            keys[104] = false;
            keys[38] = false;
            keys[87] = false;
            keys[98] = false;
            keys[40] = false;
            keys[83] = false;
            keys[65] = false;
            keys[68] = false;
            this.menu.tick(this, up, down, left, right, use);
        } else {
            this.player.tick(up, down, left, right, turnLeft, turnRight);
            if (use) {
                this.player.activate();
            }
            this.level.tick();
        }
    }

    public void getLoot(Item item) {
        this.player.addLoot(item);
    }

    public void win(Player player) {
        this.setMenu(new WinMenu(player));
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public void lose(Player player) {
        this.setMenu(new LoseMenu(player));
    }
}

