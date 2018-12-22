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

public class PauseMenu
extends Menu {
    private String[] options = new String[]{"Abort game", "Continue"};
    private int selected = 1;

    public void render(Bitmap target) {
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
    }

    public void tick(Game game, boolean up, boolean down, boolean left, boolean right, boolean use) {
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
                game.setMenu(new TitleMenu());
            }
            if (this.selected == 1) {
                game.setMenu(null);
            }
        }
    }
}

