/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.menu;

import com.mojang.escape.Art;
import com.mojang.escape.Game;
import com.mojang.escape.Sound;
import com.mojang.escape.gui.Bitmap;
import com.mojang.escape.menu.Menu;
import com.mojang.escape.menu.TitleMenu;

public class InstructionsMenu
extends Menu {
    private int tickDelay = 30;

    public void render(Bitmap target) {
        target.fill(0, 0, 160, 120, 0);
        target.draw("Instructions", 40, 8, Art.getCol(16777215));
        String[] lines = new String[]{"Use W,A,S,D to move, and", "the arrow keys to turn.", "", "The 1-8 keys select", "items from the inventory", "", "Space uses items"};
        int i = 0;
        while (i < lines.length) {
            target.draw(lines[i], 4, 32 + i * 8, Art.getCol(10526880));
            ++i;
        }
        if (this.tickDelay == 0) {
            target.draw("-> Continue", 40, target.height - 16, Art.getCol(16777088));
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

