package com.mojang.escape;

import com.mojang.escape.EscapeComponent;
import java.applet.Applet;
import java.awt.Dimension;
import java.awt.Component;

import xyz.pugduddly.enderX.EnderX;
import xyz.pugduddly.enderX.ui.Window;
import xyz.pugduddly.enderX.ui.Menu;
import xyz.pugduddly.enderX.ui.MenuItem;

public class EscapeApp {
    private static final long serialVersionUID = 1;

    public static void main() {
        final EscapeComponent escapeComponent = new EscapeComponent();
        Dimension size = new Dimension(480, 360);
        escapeComponent.setSize(size);
        escapeComponent.setPreferredSize(size);
        escapeComponent.setMinimumSize(size);
        escapeComponent.setMaximumSize(size);
        final Window window = new Window("Prelude of the Chambered") {
            @Override
            public void onClose() {
                escapeComponent.stop();
            }
        };
        Menu menu = new Menu("File");
        menu.addItem(new MenuItem("Quit") {
            @Override
            public void actionPerformed() {
                window.close();
                escapeComponent.stop();
            }
        });
        window.getMenus().add(menu);
        window.setSize(size);
        window.setResizable(false);
        window.add(escapeComponent);
        //EnderX.getDesktop().getWindowManager().addWindow(window);
        window.setVisible(true);
        escapeComponent.start();
    }
}

