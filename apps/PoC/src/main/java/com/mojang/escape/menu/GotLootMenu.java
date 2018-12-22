/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.menu;

import com.mojang.escape.Art;
import com.mojang.escape.Game;
import com.mojang.escape.entities.Item;
import com.mojang.escape.gui.Bitmap;
import com.mojang.escape.menu.Menu;

public class GotLootMenu
extends Menu {
    private int tickDelay = 30;
    private Item item;

    public GotLootMenu(Item item) {
        this.item = item;
    }

    public void render(Bitmap target) {
        String str = "You found the " + this.item.name + "!";
        target.scaleDraw(Art.items, 3, target.width / 2 - 24, 2, this.item.icon * 16, 0, 16, 16, Art.getCol(this.item.color));
        target.draw(str, (target.width - str.length() * 6) / 2 + 2, 50, Art.getCol(16777088));
        str = this.item.description;
        target.draw(str, (target.width - str.length() * 6) / 2 + 2, 60, Art.getCol(10526880));
        if (this.tickDelay == 0) {
            target.draw("-> Continue", 40, target.height - 40, Art.getCol(16777088));
        }
    }

    public void tick(Game game, boolean up, boolean down, boolean left, boolean right, boolean use) {
        if (this.tickDelay > 0) {
            --this.tickDelay;
        } else if (use) {
            game.setMenu(null);
        }
    }
}

