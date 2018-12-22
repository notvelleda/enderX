/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.menu;

import com.mojang.escape.Art;
import com.mojang.escape.Game;
import com.mojang.escape.Sound;
import com.mojang.escape.gui.Bitmap;
import com.mojang.escape.menu.AboutMenu;
import com.mojang.escape.menu.InstructionsMenu;
import com.mojang.escape.menu.Menu;

public class TitleMenu
extends Menu {
    private String[] options = new String[]{"New game", "Instructions", "About"};
    private int selected = 0;
    private boolean firstTick = true;

    public void render(Bitmap target) {
        target.fill(0, 0, 160, 120, 0);
        target.draw(Art.logo, 0, 8, 0, 0, 160, 36, Art.getCol(16777215));
        int i = 0;
        while (i < this.options.length) {
            String msg = this.options[i];
            int col = 9474192;
            if (this.selected == i) {
                msg = "-> " + msg;
                col = 16777088;
            }
            target.draw(msg, 40, 60 + i * 10, Art.getCol(col));
            ++i;
        }
        target.draw("Copyright (C) 2011 Mojang", 5, 111, Art.getCol(3158064));
    }

    public void tick(Game game, boolean up, boolean down, boolean left, boolean right, boolean use) {
        if (this.firstTick) {
            this.firstTick = false;
            Sound.altar.play();
        }
        if (up || down) {
            Sound.click2.play();
        }
        if (up) {
            --this.selected;
        }
        if (down) {
            ++this.selected;
        }
        if (this.selected < 0) {
            this.selected = 0;
        }
        if (this.selected >= this.options.length) {
            this.selected = this.options.length - 1;
        }
        if (use) {
            Sound.click1.play();
            if (this.selected == 0) {
                game.setMenu(null);
                game.newGame();
            }
            if (this.selected == 1) {
                game.setMenu(new InstructionsMenu());
            }
            if (this.selected == 2) {
                game.setMenu(new AboutMenu());
            }
        }
    }
}

