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

public class AboutMenu
extends Menu {
    private int tickDelay = 30;

    public void render(Bitmap target) {
        target.fill(0, 0, 160, 120, 0);
        target.draw("About", 60, 8, Art.getCol(16777215));
        String[] lines = new String[]{"Prelude of the Chambered", "by Markus Persson.", "Made Aug 2011 for the", "21'st Ludum Dare compo.", "", "This game is freeware,", "and was made from scratch", "in just 48 hours."};
        int i = 0;
        while (i < lines.length) {
            target.draw(lines[i], 4, 28 + i * 8, Art.getCol(10526880));
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

