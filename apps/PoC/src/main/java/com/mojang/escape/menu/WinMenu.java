/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.menu;

import com.mojang.escape.Art;
import com.mojang.escape.Game;
import com.mojang.escape.Sound;
import com.mojang.escape.entities.Player;
import com.mojang.escape.gui.Bitmap;
import com.mojang.escape.menu.Menu;
import com.mojang.escape.menu.TitleMenu;

public class WinMenu
extends Menu {
    private int tickDelay = 30;
    private Player player;

    public WinMenu(Player player) {
        this.player = player;
    }

    public void render(Bitmap target) {
        target.draw(Art.logo, 0, 10, 0, 65, 160, 23, Art.getCol(16777215));
        int seconds = this.player.time / 60;
        int minutes = seconds / 60;
        String timeString = String.valueOf(minutes) + ":";
        if ((seconds %= 60) < 10) {
            timeString = String.valueOf(timeString) + "0";
        }
        timeString = String.valueOf(timeString) + seconds;
        target.draw("Trinkets: " + this.player.loot + "/12", 40, 45, Art.getCol(9474192));
        target.draw("Time: " + timeString, 40, 55, Art.getCol(9474192));
        if (this.tickDelay == 0) {
            target.draw("-> Continue", 40, target.height - 40, Art.getCol(16777088));
        }
    }

    public void tick(Game game, boolean up, boolean down, boolean left, boolean right, boolean use) {
        if (this.tickDelay > 0) {
            --this.tickDelay;
        } else if (use) {
            Sound.click1.play();
            game.setMenu(new TitleMenu());
        }
    }
}

