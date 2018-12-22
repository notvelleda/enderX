package com.mojang.ld22;

import com.mojang.ld22.Game;
import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Component;

import xyz.pugduddly.enderX.EnderX;
import xyz.pugduddly.enderX.ui.Window;
import xyz.pugduddly.enderX.ui.Menu;
import xyz.pugduddly.enderX.ui.MenuItem;

public class GameApp {
    private static final long serialVersionUID = 1;

    public static void main() {
        final Game game = new Game();
        Dimension size = new Dimension(480, 360);
        game.setSize(size);
        game.setPreferredSize(size);
        game.setMinimumSize(size);
        game.setMaximumSize(size);
        final Window window = new Window(game.NAME) {
            @Override
            public void onClose() {
                game.stop();
            }
        };
        Menu menu = new Menu("File");
        menu.addItem(new MenuItem("Quit") {
            @Override
            public void actionPerformed() {
                window.close();
                game.stop();
            }
        });
        window.getMenus().add(menu);
        window.setSize(size);
        window.setResizable(false);
        window.add(game);
        //EnderX.getDesktop().getWindowManager().addWindow(window);
        window.setVisible(true);
        game.start();
    }
}

