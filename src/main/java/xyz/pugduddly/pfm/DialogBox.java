package xyz.pugduddly.pfm;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.awt.image.RescaleOp;
import java.awt.image.RGBImageFilter;
import java.awt.image.FilteredImageSource;

import xyz.pugduddly.enderX.EnderX;
import xyz.pugduddly.enderX.ui.Window;
import xyz.pugduddly.enderX.ui.Menu;
import xyz.pugduddly.enderX.ui.MenuItem;
import xyz.pugduddly.enderX.ui.platinum.Beveled;
import xyz.pugduddly.enderX.ui.platinum.Label;
import xyz.pugduddly.enderX.ui.platinum.Textbox;
import xyz.pugduddly.enderX.ui.platinum.Button;

public class DialogBox {
    public DialogBox(String text) {
        final Window win = new Window("PFM");
        win.setSize(new Dimension(368, 112));
        win.setResizable(false);
        win.setClosable(false);
        win.setRollable(false);
        Label label = new Label(text);
        label.setLocation(4, 4);
        win.add(label);
        Button button = new Button("OK") {
            @Override
            public void actionPerformed() {
                win.close();
                DialogBox.this.actionPerformed();
            }
        };
        button.setLocation(368 - 69, 112 - 30);
        button.setDefault(true);
        win.add(button);
        Button button2 = new Button("Cancel") {
            @Override
            public void actionPerformed() {
                win.close();
            }
        };
        button2.setLocation(368 - 147, 112 - 30);
        win.add(button2);
        win.setVisible(true);
        //EnderX.getDesktop().getWindowManager().addWindow(win);
    }
    
    public void actionPerformed() {}
}
