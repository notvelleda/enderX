/*
 * PlatinumDesktop.java
 * 
 * Copyright 2018 Pugduddly <pugduddly@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 * 
 */

package xyz.pugduddly.enderX.ui.platinum;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;

import java.lang.management.ManagementFactory;
import com.sun.management.*;

import com.mojang.nbt.NBTBase;
import com.mojang.nbt.NBTTagCompound;
import com.mojang.nbt.CompressedStreamTools;

import xyz.pugduddly.enderX.EnderX;
import xyz.pugduddly.enderX.EnderXComponent;
import xyz.pugduddly.enderX.ui.Desktop;
import xyz.pugduddly.enderX.ui.Menu;
import xyz.pugduddly.enderX.ui.MenuItem;
import xyz.pugduddly.enderX.ui.Window;
import xyz.pugduddly.enderX.ui.WindowManager;

import xyz.pugduddly.enderX.ui.platinum.Beveled;
import xyz.pugduddly.enderX.ui.platinum.UnBeveled;
import xyz.pugduddly.enderX.ui.platinum.Label;
import xyz.pugduddly.enderX.ui.platinum.Textbox;
import xyz.pugduddly.enderX.ui.platinum.Button;
import xyz.pugduddly.enderX.ui.platinum.ImageView;

import xyz.pugduddly.pfm.PFM;

import com.jcraft.weirdx.WeirdX;

public class PlatinumDesktop implements Desktop {
    public static final Color BG_COLOR = new Color(49, 66, 123);
    public static BufferedImage[] corners;
    public static BufferedImage[] menubar;
    public static BufferedImage[] window;
    public static BufferedImage[] windowInactive;
    private static DateFormat timeFormat = new SimpleDateFormat("K:mm a");
    
    private Menu enderMenu;
    private ArrayList<Menu> menus = new ArrayList<Menu>();
    private WindowManager windowmanager = new WindowManager();
    private Window lastClickedWindow;
    private BufferedImage image;
    private Graphics2D graphics;
    private boolean isMousePressed = false;
    private boolean isMouseReleased = false;
    private int scrWidth, scrHeight;
    private Point dragPoint;
    private Dimension resize;
    private NBTTagCompound config = new NBTTagCompound();
    private BufferedImage wallpaper;
    private Window rootWindow;
    private boolean rootWindowFocused;
    private Window prevTopWindow;
    private boolean prevRootFocused = false;
    
    private boolean use2xUpscaling = false;
    
	public PlatinumDesktop() {
        corners = new BufferedImage[] {
            EnderX.loadBase64Image("R0lGODlhBQAFAIABAAAAAP///yH5BAEKAAEALAAAAAAFAAUAAAIIhG8RqaD9QgEAOw=="),
            EnderX.loadBase64Image("R0lGODlhBQAFAIABAAAAAP///yH5BAEKAAEALAAAAAAFAAUAAAIIhB1xqcD6QAEAOw=="),
            EnderX.loadBase64Image("R0lGODlhBQAFAIABAAAAAP///yH5BAEKAAEALAAAAAAFAAUAAAIIRB6GoMnHYgEAOw=="),
            EnderX.loadBase64Image("R0lGODlhBQAFAIABAAAAAP///yH5BAEKAAEALAAAAAAFAAUAAAIIjAOHB4kNWwEAOw==")
        };
        menubar = new BufferedImage[] {
            EnderX.loadBase64Image("R0lGODdhEAAUAMIFAAAAAFJSUpycnK2trd7e3v///////////ywAAAAAEAAUAAADNAiqMVQwwtUiufiyl7sOnNcBQyF6gXl25eqp7gXHIV3Tc5y7+9qfP1HwZcMIjsikcsFsMhMAOw=="),
            EnderX.loadBase64Image("R0lGODdhAQAUAKEDAAAAAJycnN7e3v///ywAAAAAAQAUAAACBZyOqRdQADs="),
            EnderX.loadBase64Image("R0lGODdhAQAUAKEEAAAAAAoAjDMzmWRmzywAAAAAAQAUAAACBZyOqRdQADs="),
            EnderX.loadBase64Image("R0lGODdhEAAUAOMKAAAAACEhIVJSUnNzc4yMjJycnK2trb29vc7Ozt7e3v///////////////////////ywAAAAAEAAUAAAEPVDJKZMRAWiQuvfIQQjbZ3YiyZ1murKfGMDnRbfD/SGE/hU+DzCYGAaNPqROeWPSnDAoq0CtWq/UjXarjQAAOw==")
        };
        window = new BufferedImage[] {
            // -- Top (0) -- //
            EnderX.loadBase64Image("R0lGODlhBQAWAPIFAAAAAAEBAZWVlaOjo87Ozv///wAAAAAAACH5BAAAAAAALAAAAAAFABYAAAMaGLrVFYVIKAmdEddrc98eJ4YkaGroRwjXkAAAOw=="),
            EnderX.loadBase64Image("R0lGODlhAQAWAPEDAAAAAJycnM7Ozv///yH5BAAAAAAAIf8LSW1hZ2VNYWdpY2sNZ2FtbWE9MC40NTQ1NQAsAAAAAAEAFgAAAgbEhKmbAQUAOw=="),
            EnderX.loadBase64Image("R0lGODlhAQAWAPEBAM7Ozv///wAAAAAAACH5BAEAAAIALAAAAAABABYAAAIHlBWQqw1nCgA7"),
            EnderX.loadBase64Image("R0lGODlhAQAWAPECAHNzc87Ozv///wAAACH5BAEAAAMALAAAAAABABYAAAIHnCeQqx1nCgA7"),
            EnderX.loadBase64Image("R0lGODlhAQAWAPEAAHNzc87OzgAAAAAAACH5BAEAAAIALAAAAAABABYAAAIHlBWQqx1nCgA7"),
            EnderX.loadBase64Image("R0lGODlhBgAWAPIDAAAAAJycnM7Ozv///wAAAAAAAAAAAAAAACH5BAEAAAQALAAAAAAGABYAAAMeCKo0LoCIGdQUFdy8LfUYyH0a2ZUhOqZna74sOZAJADs="),
            
            // -- Roll (6) -- //
            EnderX.loadBase64Image("R0lGODlhBQAXAPIEAAAAAAEBAZycnM7Ozv///wAAAAAAAAAAACH5BAEAAAUALAAAAAAFABcAAAMdGLrUFGRIKAedEddrc98eJ4YkaGqoNQjsohTFkgAAOw=="),
            EnderX.loadBase64Image("R0lGODlhAQAXAPEDAAAAAJycnM7Ozv///yH5BAAAAAAALAAAAAABABcAAAIHxISpmwFQAAA7"),
            EnderX.loadBase64Image("R0lGODlhBgAXAPIDAAAAAJycnM7Ozv///wAAAAAAAAAAAAAAACH5BAEAAAQALAAAAAAGABcAAAMgCKo0LoCIGdQUFdy8LfUYyH0a2ZUhOqZna75pIGdLbScAOw=="),
            
            // -- Buttons (9) -- //
            // Close
            EnderX.loadBase64Image("R0lGODlhDQANAPUqAAUFBQcHBwsLCwwMDA0NDQ8PDxAQEBQUFIODg4qKiouLi4yMjI2NjY6Ojo+Pj5aWlpeXl5qamqmpqaurq6ysrK+vr7q6uru7u729vb+/v8zMzNDQ0NHR0dTU1NjY2NnZ2dra2tvb29zc3ODg4OLi4uXl5enp6fHx8fT09Pj4+P///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACsALAAAAAANAA0AAAZwQIRjSHQ8homV4yAwOJ8GgkDlmH5Eoo/2YwpQDZ/HgyLJWDgJwlf0qEg0Fg3noXYYRBSKWaMpJaZ2H2UZfCQkDnVgZhwaWCl/Xx9nGiQiJyhVkR0chigoKmkqdCYJRQ4JEWppAVICrq9eKyqztLWzQQA7"),
            EnderX.loadBase64Image("R0lGODlhDQANAPMJACIiIkRERFVVVWZmZnd3d4iIiJmZmaqqqru7u////wAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAAoALAAAAAANAA0AAAQ+sMhJpSog6w0SDoEgDANBYB4QjuWJYiJpSkYHy65Rpzit24CW77DDzAq6AxGYMygRRYBPRwVyOAlFYsvtbiMAOw=="),
            // Maximize
            EnderX.loadBase64Image("R0lGODlhDQANAPMIACEhIYyMjJycnK2trb29vc7Ozt7e3u/v7////wAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAAkALAAAAAANAA0AAARAMMhJZQog6w0QRkVYAGKHFYIwDABRfKc6EORrkitBFwaM6yND78YBGA6+got3RN5EwsPB86xMiEUNIoHoer/dCAA7"),
            EnderX.loadBase64Image("R0lGODlhDQANAPMJACIiIkRERFVVVWZmZnd3d4iIiJmZmaqqqru7u////wAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAAoALAAAAAANAA0AAARBsMhJpSog6w0SDoEgDABBYB4QjuSJYuIwEIBkdLBs1sad6idAz4fhCA/Ekqt3QOJKNkMTkYwOe0/jJqFIeL9gbwQAOw=="),
            // Roll
            EnderX.loadBase64Image("R0lGODlhDQANAPMIACEhIYyMjJycnK2trb29vc7Ozt7e3u/v7////wAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAAkALAAAAAANAA0AAAQ5MMhJZQog6w0QRkUohh1WCMIwEETxmenauiVne0Ahh4bx2hxcYdY7HH6i4gEnrExqQA0igahar9UIADs="),
            EnderX.loadBase64Image("R0lGODlhDQANAPMJACIiIkRERFVVVWZmZnd3d4iIiJmZmaqqqru7u////wAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAAoALAAAAAANAA0AAAQ8sMhJpSog6w0SDoEgDANBYB4QjuWJYiJpSkaHcVwqu0Zv4znMrNA7HGopnsGIQGJovagTuEkoEtisFhsBADs="),
            
            // -- Sides (15) -- //
            // Left
            EnderX.loadBase64Image("R0lGODlhBgABAPEDAAAAAJycnM7Ozv///yH5BAAAAAAAIf8LSW1hZ2VNYWdpY2sNZ2FtbWE9MC40NTQ1NQAsAAAAAAYAAQAAAgTEJAEFADs="),
            // Right
            EnderX.loadBase64Image("R0lGODlhBwABAPEDAAAAAJycnM7Ozv///yH5BAAAAAAAIf8LSW1hZ2VNYWdpY2sNZ2FtbWE9MC40NTQ1NQAsAAAAAAcAAQAAAgTEJAFQADs="),
            // Bottom Left
            EnderX.loadBase64Image("R0lGODlhBgAHAPIDAAAAAJycnM7Ozv///wAAAAAAAAAAAAAAACH5BAEAAAQAIf8LSW1hZ2VNYWdpY2sNZ2FtbWE9MC40NTQ1NQAsAAAAAAYABwAAAxAIIxKgTAwYKQMt6McBIV0CADs="),
            // Bottom Middle
            EnderX.loadBase64Image("R0lGODlhAQAHAPEDAAAAAJycnM7Ozv///yH5BAAAAAAAIf8LSW1hZ2VNYWdpY2sNZ2FtbWE9MC40NTQ1NQAsAAAAAAEABwAAAgTEJAFQADs="),
            // Bottom Right
            EnderX.loadBase64Image("R0lGODlhBwAHAPEDAAAAAJycnM7Ozv///yH5BAAAAAAALAAAAAAHAAcAAAIPxCQBhrntAFQg2CCW3nwVADs="),
            // Bottom Right w/ Resize
            EnderX.loadBase64Image("R0lGODlhFgAWAPIFAAAAAHNzc5ycnK2trc7Ozv///wAAAAAAACH5BAAAAAAAIf8LSW1hZ2VNYWdpY2sNZ2FtbWE9MC40NTQ1NQAsAAAAABYAFgAAA1gIutxQhIhVqr0YShq7/92kaGAZiU9ZmSjZQQHLeTCxfi1NxDU+2zsgz5UTGnun2YAXRCZHOmbMB+0sjaBiVFb1XLkpE/cmzirKYhGaK2i733C4Y06v2x0JADs=")
        };
        windowInactive = new BufferedImage[] {
            // -- Top (0) -- //
            EnderX.loadBase64Image("R0lGODlhFQAWAPEDAHd3d5mZmczMzP///yH5BAAAAAAALAAAAAAVABYAAAI3hI+pMO0PIRO02msn3lfz731bKHZDyZEopa4t+paxOIPnauK5XtXpzRP4RreA8YhMGjWBhRNQAAA7"),
            EnderX.loadBase64Image("R0lGODlhAQAWAPEDAHd3d5mZmczMzP///yH5BAAAAAAALAAAAAABABYAAAIGxISpmwEFADs="),
            EnderX.loadBase64Image("R0lGODlhFgAWAPIEAFJSUnd3d5mZmczMzP///wAAAAAAAAAAACH5BAEAAAUALAAAAAAWABYAAANCGLrcXiTKSecIZejNOxcB4I0eKJLoYKboyo7u28XyRtdqiH/6rt01oEz4IrKMKWSr51OSTIKodEoV2EKO7ILwCyUAADs="),
            
            // -- Roll (3) -- //
            EnderX.loadBase64Image("R0lGODlhFQAXAPIDAFJSUnd3d5mZmczMzP///wAAAAAAAAAAACH5BAEAAAUALAAAAAAVABcAAAM+GLrcHiTKSSkcOOutL/+bB47i+JVmSKQgymLuG7NzWps3ub4q3/uZXGsHHAhPA4FyyWwyH1BHoQCoWq/YawIAOw=="),
            EnderX.loadBase64Image("R0lGODlhAQAXAPIDAFJSUnd3d5mZmczMzP///wAAAAAAAAAAACH5BAAAAAAALAAAAAABABcAAAMIGDTc/k0EkAAAOw=="),
            EnderX.loadBase64Image("R0lGODlhFgAXAPIDAFJSUnd3d5mZmczMzP///wAAAAAAAAAAACH5BAEAAAUALAAAAAAWABcAAANEGLrcXiTKSecIZejNOxcB4I0eKJLoYKboyo7u28XyRtdqiH/6rt01oEz4IrKMKWSr51OSTIKodEqlhhzYLGDL7Xq/2wQAOw=="),
            
            // -- Sides (6) -- //
            EnderX.loadBase64Image("R0lGODlhBgABAPEDAHd3d5mZmczMzP///yH5BAAAAAAALAAAAAAGAAEAAAIExCQBBQA7"),
            EnderX.loadBase64Image("R0lGODlhBwABAPIDAFJSUnd3d5mZmczMzP///wAAAAAAAAAAACH5BAAAAAAALAAAAAAHAAEAAAMFGDQjAQkAOw=="),
            EnderX.loadBase64Image("R0lGODlhBgAHAPIEAFJSUnd3d5mZmczMzP///wAAAAAAAAAAACH5BAEAAAUALAAAAAAGAAcAAAMRGDQjoWwQGCkLTejHQwEgmAAAOw=="),
            EnderX.loadBase64Image("R0lGODlhAQAHAPIEAFJSUnd3d5mZmczMzP///wAAAAAAAAAAACH5BAAAAAAALAAAAAABAAcAAAMFGDQjAQkAOw=="),
            EnderX.loadBase64Image("R0lGODlhBwAHAPIAAFJSUnd3d5mZmczMzP///wAAAAAAAAAAACH5BAAAAAAALAAAAAAHAAcAAAMTGDQjAYs5wOSr7Ym9X/ggII5iAgA7"),
            EnderX.loadBase64Image("R0lGODlhFgAWAPIDAFJSUnd3d5mZmczMzP///wAAAAAAAAAAACH5BAAAAAAALAAAAAAWABYAAANJGLrcQWOIAB65OOco6eNgGE4VJJ4c+aGoarKiC5/yPHqvHdX6jvepH7BTGhJXQF5PqSPllhSjDyAlCq7YrFbrcXi9gLB4TC6HEwA7")
        };
        this.enderMenu = new Menu(EnderX.icon);
        this.enderMenu.addItem(new MenuItem("About This Computer") {
            @Override
            public void actionPerformed() {
                Window win = new Window("About This Computer");
                win.setResizable(false);
                win.setSize(new Dimension(466, 252));
                
                Beveled beveled = new Beveled();
                beveled.setPreferredSize(new Dimension(466, 167));
                win.add(beveled);
                
                UnBeveled unBeveled = new UnBeveled();
                unBeveled.setLocation(6, 6);
                unBeveled.setPreferredSize(new Dimension(454, 90));
                win.add(unBeveled);
                
                ImageView imageView = new ImageView(EnderX.loadImage(getClass().getResource("/banner.png")));
                imageView.setLocation(6, 6);
                win.add(imageView);
                
                Label versionLabel1 = new Label("Version:");
                versionLabel1.setLocation(8, 104);
                versionLabel1.setBold(true);
                win.add(versionLabel1);
                
                Label versionLabel2 = new Label("enderX " + EnderX.VERSION + ", WeirdX " + WeirdX.releaseNumber);
                versionLabel2.setLocation(63, 104);
                win.add(versionLabel2);
                
                Label memoryLabel1 = new Label("Built-in Memory:");
                memoryLabel1.setLocation(8, 120);
                memoryLabel1.setBold(true);
                win.add(memoryLabel1);
                
                Label memoryLabel2 = new Label((getMaxMemory() / 1024 / 1024) + " MB");
                memoryLabel2.setLocation(100, 120);
                win.add(memoryLabel2);
                
                Label memoryLabel3 = new Label("Virtual Memory:");
                memoryLabel3.setLocation(8, 136);
                memoryLabel3.setBold(true);
                win.add(memoryLabel3);
                
                Label memoryLabel4 = new Label((getVirtualMemory() / 1024 / 1024) + " MB");
                memoryLabel4.setLocation(100, 136);
                win.add(memoryLabel4);
                
                Label copyrightLabel = new Label("© Pugduddly 2018-");
                copyrightLabel.setLocation(350, 148);
                win.add(copyrightLabel);
                
                //EnderX.getDesktop().getWindowManager().addWindow(win);
                win.setVisible(true);
            }
            
            public long getMaxMemory() {
                return Runtime.getRuntime().maxMemory();
            }

            public long getUsedMemory() {
                return getMaxMemory() - getFreeMemory();
            }

            public long getTotalMemory() {
                return Runtime.getRuntime().totalMemory();
            }

            public long getFreeMemory() {
                return Runtime.getRuntime().freeMemory();
            }
            
            public long getVirtualMemory() {
                OperatingSystemMXBean osMBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
                return osMBean.getTotalSwapSpaceSize();
            }
        });
        this.enderMenu.addItem(new MenuItem("-"));
        this.enderMenu.addItem(new MenuItem("Control Panel") {
            @Override
            public void actionPerformed() {
                Window win = new Window("Control Panel");
                win.setResizable(false);
                win.setSize(new Dimension(320, 252));
                
                Beveled beveled = new Beveled();
                beveled.setPreferredSize(new Dimension(320, 252));
                win.add(beveled);
                
                Label wallpaperLabel1 = new Label("Wallpaper:");
                wallpaperLabel1.setLocation(16, 16);
                wallpaperLabel1.setBold(true);
                win.add(wallpaperLabel1);
                
                String wallpaperName = EnderX.getDesktop().getWallpaper().getName();
                if (wallpaperName.length() == 0) wallpaperName = "None";
                final Label wallpaperLabel2 = new Label(wallpaperName);
                wallpaperLabel2.setLocation(86, 16);
                win.add(wallpaperLabel2);
                
                Button clearWallpaperButton = new Button("Clear") {
                    @Override
                    public void actionPerformed() {
                        wallpaperLabel2.setString("None");
                        EnderX.getDesktop().setWallpaper(null);
                    }
                };
                clearWallpaperButton.setLocation(246, 14);
                win.add(clearWallpaperButton);
                
                Label resolutionLabel = new Label("Screen Resolution:");
                resolutionLabel.setLocation(16, 48);
                resolutionLabel.setBold(true);
                win.add(resolutionLabel);
                
                win.add(this.createResolutionButton(640, 480, 16, 64));
                win.add(this.createResolutionButton(800, 600, 88, 64));
                win.add(this.createResolutionButton(1024, 768, 160, 64));
                
                Button customResolutionButton = new Button("Custom") {
                    @Override
                    public void actionPerformed() {
                        final Window win = new Window("Set Custom Resolution");
                        win.setSize(new Dimension(368, 112));
                        win.setResizable(false);
                        win.setClosable(false);
                        win.setRollable(false);
                        Label widthLabel = new Label("Width:");
                        widthLabel.setLocation(4, 8);
                        win.add(widthLabel);
                        final Textbox widthTextbox = new Textbox();
                        widthTextbox.setLocation(48, 2);
                        win.add(widthTextbox);
                        Label heightLabel = new Label("Height:");
                        heightLabel.setLocation(4, 36);
                        win.add(heightLabel);
                        final Textbox heightTextbox = new Textbox();
                        heightTextbox.setLocation(48, 30);
                        win.add(heightTextbox);
                        Button button = new Button("OK") {
                            @Override
                            public void actionPerformed() {
                                int width, height;
                                try {
                                    width = Math.max(Integer.parseInt(widthTextbox.getString()), 512);
                                } catch (NumberFormatException e) {
                                    EnderX.alert("Width is not a number!");
                                    return;
                                }
                                try {
                                    height = Math.max(Integer.parseInt(heightTextbox.getString()), 342);
                                } catch (NumberFormatException e) {
                                    EnderX.alert("Height is not a number!");
                                    return;
                                }
                                EnderX.setScreenSize(new Dimension(width, height));
                                win.close();
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
                    }
                };
                customResolutionButton.setLocation(239, 64);
                win.add(customResolutionButton);
                
                Label scaleLabel = new Label("2x upscaling (useful for HiDPI displays)");
                scaleLabel.setLocation(16, 96);
                scaleLabel.setBold(true);
                win.add(scaleLabel);
                
                Button scaleOnButton = new Button("On") {
                    @Override
                    public void actionPerformed() {
                        PlatinumDesktop.this.use2xUpscaling(true);
                    }
                };
                scaleOnButton.setLocation(16, 112);
                win.add(scaleOnButton);
                
                Button scaleOffButton = new Button("Off") {
                    @Override
                    public void actionPerformed() {
                        PlatinumDesktop.this.use2xUpscaling(false);
                    }
                };
                scaleOffButton.setLocation(96, 112);
                win.add(scaleOffButton);
                
                //EnderX.getDesktop().getWindowManager().addWindow(win);
                win.setVisible(true);
            }
            
            private Button createResolutionButton(final int w, final int h, int buttonX, int buttonY) {
                Button button = new Button(w + "x" + h) {
                    @Override
                    public void actionPerformed() {
                        System.out.println("Set resolution " + w + "x" + h);
                        EnderX.setScreenSize(new Dimension(w, h));
                    }
                };
                button.setLocation(buttonX, buttonY);
                return button;
            }
        });
        
        this.loadConfig();
    }
    
    public void loadConfig() {
        try {
            File configFile = new File(EnderX.getHomeDirectory(), "config.dat");
            if (configFile.exists())
                this.config = CompressedStreamTools.readCompressed(new FileInputStream(configFile));
            else
                this.saveConfig();
            File file = new File(this.config.getString("Wallpaper"));
            if (file.exists())
                this.wallpaper = EnderX.loadImage(new FileInputStream(file));
            this.scrWidth = this.config.getInteger("DisplayWidth");
            this.scrHeight = this.config.getInteger("DisplayHeight");
            if (this.scrWidth == 0) this.scrWidth = 512;
            if (this.scrHeight == 0) this.scrHeight = 342;
            EnderX.setScreenSize(new Dimension(this.scrWidth, this.scrHeight));
            this.use2xUpscaling(this.config.getBoolean("2xUpscaling"));
        } catch (Exception e) {
            System.out.println("Failed to load config: " + e);
        }
    }
    
    public void saveConfig() {
        try {
            File configFile = new File(EnderX.getHomeDirectory(), "config.dat");
            CompressedStreamTools.writeCompressed(this.config, new FileOutputStream(configFile));
        } catch (Exception e) {
            System.out.println("Failed to save config: " + e);
        }
    }
    
    public void setWallpaper(File file) {
        try {
            if (file == null) {
                this.wallpaper = null;
                this.config.setString("Wallpaper", "");
            } else {
                this.wallpaper = EnderX.loadImage(new FileInputStream(file));
                this.config.setString("Wallpaper", file.toString());
            }
            saveConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setScreenSize(Dimension size) {
        try {
            this.scrWidth = (int) size.getWidth();
            this.scrHeight = (int) size.getHeight();
            
            this.image = new BufferedImage(this.scrWidth, this.scrHeight, BufferedImage.TYPE_INT_RGB);
            this.graphics = image.createGraphics();
            this.graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
            this.rootWindow = PFM.getRootWindow(this.scrWidth, this.scrHeight);
            
            this.config.setInteger("DisplayWidth", this.scrWidth);
            this.config.setInteger("DisplayHeight", this.scrHeight);
            saveConfig();
            
            System.gc();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if (this.use2xUpscaling) {
            Dimension d = EnderX.getScreenSize();
            Dimension d2 = new Dimension((int) d.getWidth() * 2, (int) d.getHeight() * 2);
            EnderX.getComponent().setSize(d2);
            EnderX.getComponent().setPreferredSize(d2);
            Frame frame = ((java.awt.Frame) EnderX.getContainer());
            frame.setSize(d2);
        }
    }
    
    public void use2xUpscaling(boolean b) {
        this.use2xUpscaling = b;
        this.config.setBoolean("2xUpscaling", this.use2xUpscaling);
        this.saveConfig();
        
        if (this.use2xUpscaling) {
            Dimension d = EnderX.getScreenSize();
            Dimension d2 = new Dimension((int) d.getWidth() * 2, (int) d.getHeight() * 2);
            EnderX.getComponent().setSize(d2);
            EnderX.getComponent().setPreferredSize(d2);
            Frame frame = ((java.awt.Frame) EnderX.getContainer());
            frame.setSize(d2);
        }
    }
    
    public File getWallpaper() {
        try {
            return new File(this.config.getString("Wallpaper"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private void drawWallpaper() {
        this.graphics.setPaint(BG_COLOR);
        this.graphics.fillRect(0, 0, this.scrWidth, this.scrHeight);
        
        if (this.wallpaper != null) {
            Dimension wallpaperSize = new Dimension(this.wallpaper.getWidth(), this.wallpaper.getHeight());
            Dimension screenSize = new Dimension(this.scrWidth, this.scrHeight);
            Dimension scaledDimension = getScaledDimension(wallpaperSize, screenSize);
            int width = (int) scaledDimension.getWidth();
            int height = (int) scaledDimension.getHeight();
            this.graphics.drawImage(this.wallpaper, this.scrWidth / 2 - width / 2, this.scrHeight / 2 - height / 2, width, height, null);
        }
    }
    
    private void renderAllWindows(Point mouse) {
        ArrayList<Window> windowsToClose = new ArrayList<Window>();
        
        try {
            for (Window window : this.getWindowManager().getWindows()) {
                if (window.isVisible()) {
                    this.drawComponents(window);
                    if (this.getWindowManager().isWindowOnTop(window) && !this.rootWindowFocused)
                        this.drawWindow(this.graphics, window, mouse);
                    else
                        this.drawInactiveWindow(this.graphics, window, mouse);
                }
                if (window.shouldClose())
                    windowsToClose.add(window);
            }
        } catch (ConcurrentModificationException e) {
            System.out.println(e);
        }
        
        for (Window window : windowsToClose) {
            this.getWindowManager().removeWindow(window);
            for (WindowListener l : window.getWindowListeners())
                l.windowClosed(new WindowEvent((java.awt.Window) EnderX.getContainer(), WindowEvent.WINDOW_CLOSED));
        }
    }
    
    public void drawMenubar(Point mouse) {
        this.graphics.drawImage(menubar[0], 0, 0, null);
        for (int i = 16; i < this.scrWidth - 16; i ++) {
            this.graphics.drawImage(menubar[1], i, 0, null);
        }
        this.graphics.drawImage(menubar[3], this.scrWidth - 16, 0, null);
        
        /*if (this.getWindowManager().getTopWindow() != this.prevTopWindow ||
            this.rootWindowFocused != this.prevRootFocused ||
            this.menus.size() == 0) {
            updateMenus();
        }*/
        updateMenus();
        
        int x = 10;
        for (Menu menu : this.menus)
            x = this.drawMenu(this.graphics, menu, x, this.scrWidth, mouse);
        
        String time = timeFormat.format(new Date());
        int stringWidth = this.graphics.getFontMetrics().stringWidth(time);
        
        this.graphics.setColor(Color.BLACK);
        this.graphics.drawString(time, this.scrWidth - stringWidth - 16, 14);
        
        if (!isOverMenu(mouse)) {
            Window win = this.getWindowManager().getTopWindow();
            if (win != null && this.dragPoint != null && win.isDragging()) {
                int width = (int) win.getSize().getWidth();
                int height = (int) win.getSize().getHeight();
                if (win.isRolled()) height = -6;
                invertRect((int) this.dragPoint.getX(), (int) Math.max(this.dragPoint.getY(), 20), width + 6 + 6, height + 22 + 6);
            }
            if (win != null && this.resize != null && win.isResizing()) {
                int width = (int) this.resize.getWidth();
                int height = (int) this.resize.getHeight();
                invertRect((int) win.getPosition().getX(), (int) win.getPosition().getY(), width + 6 + 6, height + 22 + 6);
            }
        }
    }
    
    private void allocImage() {
        this.image = new BufferedImage(this.scrWidth, this.scrHeight, BufferedImage.TYPE_INT_RGB);
        this.graphics = image.createGraphics();
        this.graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            
        this.rootWindow = PFM.getRootWindow(this.scrWidth, this.scrHeight);
    }
    
    public void paint(Graphics2D g, Point mouse) {
        boolean isMouseReleased = this.isMouseReleased;
        if (this.image == null) {
            this.allocImage();
        }
        
        if (this.use2xUpscaling) mouse = new Point((int) mouse.getX() / 2, (int) mouse.getY() / 2);
        
        // Draw background
        this.drawWallpaper();
        
        // do stuff lmao
        this.graphics.setColor(Color.BLACK);
        this.graphics.setFont(EnderX.getFont("Charcoal", 12));
        
        this.drawComponents(this.rootWindow);
        //this.graphics.drawImage(this.rootWindow.getCanvas(), 0, 0, null);
        this.graphics.drawImage(this.rootWindow.getComponentCanvas(), 0, 0, null);
        
        this.renderAllWindows(mouse);
        
        this.drawMenubar(mouse);
        
        // Draw the obligatory corners
        this.graphics.drawImage(corners[0], 0, 0, null);
        this.graphics.drawImage(corners[1], this.scrWidth - 5, 0, null);
        this.graphics.drawImage(corners[2], 0, this.scrHeight - 5, null);
        this.graphics.drawImage(corners[3], this.scrWidth - 5, this.scrHeight - 5, null);
        
        if (isMouseReleased == this.isMouseReleased && this.isMouseReleased == true)
            this.isMouseReleased = false;
        
        if (this.use2xUpscaling)
            g.drawImage(this.image, 0, 0, this.scrWidth * 2, this.scrHeight * 2, null);
        else
            g.drawImage(this.image, 0, 0, null);
        
        this.prevTopWindow = this.getWindowManager().getTopWindow();
        this.prevRootFocused = this.rootWindowFocused;
    }
    
    private void updateMenus() {
        this.menus.clear();
        this.menus.add(this.enderMenu);
        
        Window topWin = this.getWindowManager().getTopWindow();
        if (topWin != null && !this.rootWindowFocused)
            this.menus.addAll(topWin.getMenus());
        else if (this.rootWindow != null)
            this.menus.addAll(this.rootWindow.getMenus());
    }
    
    public void newWindowAdded(Window window) {
        this.rootWindowFocused = false;
    }
    
    private void invertRect(int x, int y, int w, int h) {
        for (int i = x; i < x + w; i ++) {
            for (int j = y; j < y + h; j ++) {
                if ((i > x && i <= x + w - 2 && j > y && j <= y + h - 2) || i < 0 || j < 0 || i >= this.scrWidth || j >= this.scrHeight) continue;
                int rgb = this.image.getRGB(i, j);
                int r = 255 - ((rgb >> 16) & 0xFF);
                int g = 255 - ((rgb >> 8) & 0xFF);
                int b = 255 - ((rgb >> 0) & 0xFF);
                rgb = r;
                rgb = (rgb << 8) + g;
                rgb = (rgb << 8) + b;
                this.image.setRGB(i, j, rgb);
            }
        }
    }
    
    private void drawWindow(Graphics2D g, Window win, Point mouse) {
        int x = (int) win.getPosition().getX();
        int y = (int) win.getPosition().getY();
        int width = (int) win.getSize().getWidth() + 2;
        int height = (int) win.getSize().getHeight();
        String title = win.getTitle();
        int titleWidth = 5 + g.getFontMetrics().stringWidth(title) + 5;
        int numLeftButtons = (win.canClose() ? 1 : 0);
        int numRightButtons = (win.canResize() ? 1 : 0) + (win.canRoll() ? 1 : 0);
        if (win.isRolled())
            g.drawImage(window[6], x, y, null);
        else
            g.drawImage(window[0], x, y, null);
        for (int i = 0; i < width; i ++) {
            if (win.isRolled())
                g.drawImage(window[7], x + 5 + i, y, null);
            else
                g.drawImage(window[1], x + 5 + i, y, null);
            if (i == numLeftButtons * 16 || i == width / 2 + titleWidth / 2)
                g.drawImage(window[2], x + 5 + i, y, null);
            else if ((i > numLeftButtons * 16 && i < width / 2 - titleWidth / 2) || (i > width / 2 + titleWidth / 2 && i < width - numRightButtons * 16 - 1))
                g.drawImage(window[3], x + 5 + i, y, null);
            else if (i == width / 2 - titleWidth / 2 || i == width - numRightButtons * 16 - 1)
                g.drawImage(window[4], x + 5 + i, y, null);
        }
        if (win.isRolled())
            g.drawImage(window[8], x + 5 + width, y, null);
        else
            g.drawImage(window[5], x + 5 + width, y, null);
        g.setColor(Color.BLACK);
        g.drawString(title, x + 5 + width / 2 - titleWidth / 2 + 5, y + 14);
        if (win.canClose()) {
            int buttonX = x + 4;
            int buttonY = y + 4;
            if (this.lastClickedWindow == win && (this.isMousePressed || this.isMouseReleased) && mouse.getX() >= buttonX && mouse.getX() < buttonX + 13 && mouse.getY() >= buttonY && mouse.getY() < buttonY + 13) {
                g.drawImage(window[10], buttonX, buttonY, null);
                if (this.isMouseReleased) {
                    for (WindowListener l : win.getWindowListeners())
                        l.windowClosing(new WindowEvent((java.awt.Window) EnderX.getContainer(), WindowEvent.WINDOW_CLOSING));
                    win.close();
                }
            } else {
                g.drawImage(window[9], buttonX, buttonY, null);
            }
        }
        if (win.canResize()) {
            int buttonX = x + width - (-- numRightButtons) * 16 - 7;
            int buttonY = y + 4;
            if (this.lastClickedWindow == win && (this.isMousePressed || this.isMouseReleased) && mouse.getX() >= buttonX && mouse.getX() < buttonX + 13 && mouse.getY() >= buttonY && mouse.getY() < buttonY + 13) {
                g.drawImage(window[12], buttonX, buttonY, null);
                if (this.isMouseReleased) {
                    if (win.isMaximized()) {
                        win.setMaximized(false);
                        win.setSize(win.getOriginalSize());
                        win.setPosition(win.getOriginalPosition());
                        win.onResize();
                        for (ComponentListener l : win.getComponentListeners())
                            l.componentResized(new ComponentEvent(EnderX.getComponent(), ComponentEvent.COMPONENT_RESIZED));
                    } else {
                        win.setMaximized(true);
                        win.setOriginalSize(win.getSize());
                        win.setOriginalPosition(win.getPosition());
                        win.setSize(new Dimension(this.scrWidth - 45, this.scrHeight - 60));
                        win.setPosition(new Point(15, 20));
                        win.onResize();
                        for (ComponentListener l : win.getComponentListeners())
                            l.componentResized(new ComponentEvent(EnderX.getComponent(), ComponentEvent.COMPONENT_RESIZED));
                    }
                }
            } else {
                g.drawImage(window[11], buttonX, buttonY, null);
            }
        }
        if (win.canRoll()) {
            int buttonX = x + width - (-- numRightButtons) * 16 - 7;
            int buttonY = y + 4;
            if (this.lastClickedWindow == win && (this.isMousePressed || this.isMouseReleased) && mouse.getX() >= buttonX && mouse.getX() < buttonX + 13 && mouse.getY() >= buttonY && mouse.getY() < buttonY + 13) {
                g.drawImage(window[14], buttonX, buttonY, null);
                if (this.isMouseReleased)
                    win.setRoll(!win.isRolled());
            } else {
                g.drawImage(window[13], buttonX, buttonY, null);
            }
        }
        if (win.isRolled()) return;
        g.drawImage(win.getCanvas(), x + 6, y + 22, null);
        g.drawImage(win.getComponentCanvas(), x + 6, y + 22, null);
        for (int i = 0; i < height; i ++) {
            g.drawImage(window[15], x, y + 22 + i, null);
            g.drawImage(window[16], x + 4 + width, y + 22 + i, null);
        }
        g.drawImage(window[17], x, y + 22 + height, null);
        for (int i = 0; i < width - 2; i ++) {
            g.drawImage(window[18], x + 6 + i, y + 22 + height, null);
        }
        if (win.canResize())
            g.drawImage(window[20], x + width - 11, y + height + 7, null);
        else
            g.drawImage(window[19], x + 4 + width, y + 22 + height, null);
    }
    
    private void drawInactiveWindow(Graphics2D g, Window win, Point mouse) {
        int x = (int) win.getPosition().getX();
        int y = (int) win.getPosition().getY();
        int width = (int) win.getSize().getWidth() + 2;
        int height = (int) win.getSize().getHeight();
        String title = win.getTitle();
        int titleWidth = 5 + g.getFontMetrics().stringWidth(title) + 5;
        if (win.isRolled())
            g.drawImage(windowInactive[3], x, y, null);
        else
            g.drawImage(windowInactive[0], x, y, null);
        for (int i = 0; i < width - 16; i ++) {
            if (win.isRolled())
                g.drawImage(windowInactive[4], x + 5 + i, y, null);
            else
                g.drawImage(windowInactive[1], x + 5 + i, y, null);
        }
        if (win.isRolled())
            g.drawImage(windowInactive[5], x + width - 11, y, null);
        else
            g.drawImage(windowInactive[2], x + width - 11, y, null);
        g.setColor(new Color(0x737373));
        g.drawString(title, x + 5 + width / 2 - titleWidth / 2 + 5, y + 14);
        if (win.isRolled()) return;
        g.drawImage(win.getCanvas(), x + 6, y + 22, null);
        g.drawImage(win.getComponentCanvas(), x + 6, y + 22, null);
        for (int i = 0; i < height; i ++) {
            g.drawImage(windowInactive[6], x, y + 22 + i, null);
            g.drawImage(windowInactive[7], x + 4 + width, y + 22 + i, null);
        }
        g.drawImage(windowInactive[8], x, y + 22 + height, null);
        for (int i = 0; i < width - 2; i ++) {
            g.drawImage(windowInactive[9], x + 6 + i, y + 22 + height, null);
        }
        if (win.canResize())
            g.drawImage(windowInactive[11], x + width - 11, y + height + 7, null);
        else
            g.drawImage(windowInactive[10], x + 4 + width, y + 22 + height, null);
    }
    
    private void drawComponents(Window window) {
        BufferedImage bi = window.getComponentCanvas();
        Graphics2D g = bi.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR));
        g.fillRect(0, 0, bi.getWidth(null), bi.getHeight(null));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        for (Component component : window.getComponents()) {
            g.setColor(component.getForeground());
            g.setFont(component.getFont());
            g.translate(component.getX(), component.getY());
            Dimension size = component.getPreferredSize();
            g.setClip(0, 0, (int) size.getWidth(), (int) size.getHeight());
            component.paint((Graphics) g);
            component.paintAll((Graphics) g);
            g.translate(-component.getX(), -component.getY());
        }
        g.dispose();
    }
    
    public boolean isMouseIn(Window win, Point mouse) {
        if (win.isRolled()) return false;
        Dimension size = win.getSize();
        Point pos = win.getPosition();
        if (mouse.getX() >= pos.getX() + 6 && mouse.getX() < pos.getX() + 6 + size.getWidth() &&
            mouse.getY() >= pos.getY() + 22 && mouse.getY() < pos.getY() + 22 + size.getHeight()) {
            return true;
        }
        return false;
    }
    
    public boolean isMouseIn(Window win, Component cpt, Point mouse) {
        Point pos = win.getPosition();
        if (mouse.getX() >= pos.getX() + cpt.getX() + 6 && mouse.getX() < pos.getX() + cpt.getX() + 6 + cpt.getPreferredSize().getWidth() &&
            mouse.getY() >= pos.getY() + cpt.getY() + 22 && mouse.getY() < pos.getY() + cpt.getY() + 22 + cpt.getPreferredSize().getHeight()) {
            return true;
        }
        return false;
    }
    
    public boolean isMouseIn(Component cpt, Point mouse) {
        if (mouse.getX() >= cpt.getX() && mouse.getX() < cpt.getX() + cpt.getPreferredSize().getWidth() &&
            mouse.getY() >= cpt.getY() && mouse.getY() < cpt.getY() + cpt.getPreferredSize().getHeight()) {
            return true;
        }
        return false;
    }
    
    private int drawMenu(Graphics2D g, Menu menu, int x, int scrWidth, Point mouse) {
        int itemWidth;
        if (menu.getName() == null && menu.getImage() != null) {
            Image img = menu.getImage();
            Dimension imgSize = new Dimension(img.getWidth(null), img.getHeight(null));
            Dimension boundary = new Dimension(scrWidth, 18);
            Dimension newSize = getScaledDimension(imgSize, boundary);
            itemWidth = 8 + (int) newSize.getWidth() + 8 + 8;
        } else {
            itemWidth = 8 + g.getFontMetrics().stringWidth(menu.getName()) + 8;
        }
        
        boolean isSelected = menu.isSelected();
        
        if (this.isMousePressed && mouse.x >= x && mouse.x < x + itemWidth && mouse.y < 20) {
            menu.select();
            isSelected = true;
        } else {
            menu.deselect();
        }
        
        if (isSelected) {
            for (int i = x; i < x + itemWidth; i ++)
                g.drawImage(menubar[2], i, 0, null);
            g.setColor(Color.WHITE);
            if (menu.getName() == null && menu.getImage() != null) {
                Image img = menu.getImage();
                Dimension imgSize = new Dimension(img.getWidth(null) * 4, img.getHeight(null) * 4);
                Dimension boundary = new Dimension(scrWidth, 16);
                Dimension newSize = getScaledDimension(imgSize, boundary);
                g.drawImage(img, x + 8, 2, (int) newSize.getWidth(), (int) newSize.getHeight(), null);
            } else {
                g.drawString(menu.getName(), x + 8, 14);
            }
            
            int y = 0;
            int width = 98;
            g.setColor(Color.BLACK);
            for (MenuItem item : menu.getItems()) {
                y += 20;
                if (item.getName().equals("-")) {
                    y -= 12;
                } else {
                    int width2 = 20 + g.getFontMetrics().stringWidth(item.getName()) + 16;
                    if (width2 > width) width = width2;
                }
            }
            
            g.setColor(new Color(0x212121));
            g.drawRect(x + 1, 20, width, y + 1);
            g.setColor(Color.BLACK);
            g.drawRect(x, 19, width, y + 1);
            g.setColor(new Color(0xdddddd));
            g.fillRect(x + 1, 20, width - 1, y - 1);
            g.setColor(Color.WHITE);
            g.drawLine(x + 1, 20, x + 1, y + 19);
            g.drawLine(x + 1, 20, x + width - 1, 20);
            g.setColor(new Color(0x999999));
            g.drawLine(x + width - 1, 20, x + width - 1, y + 19);
            g.drawLine(x + 1, y + 19, x + width - 1, y + 19);
            
            y = 0;
            int count = 0;
            for (MenuItem item : menu.getItems()) {
                y += 20;
                if (item.getName().equals("-")) {
                    g.setColor(new Color(0x858585));
                    g.drawLine(x + 1, y + 3, x + width - 1, y + 3);
                    g.setColor(Color.WHITE);
                    g.drawLine(x + 1, y + 4, x + width - 1, y + 4);
                    y -= 12;
                } else {
                    g.setColor(Color.BLACK);
                    if (item.isDisabled()) {
                        g.setColor(new Color(119,119,119));
                    } else if (mouse.x >= x && mouse.x < x + width && mouse.y >= y && mouse.y < 20 + y) {
                        g.setColor(new Color(0x333399));
                        g.fillRect(x + 2, y, width - 3, 20);
                        g.setColor(new Color(0x6466cf));
                        g.drawLine(x + 1, y, x + 1, y + 19);
                        if (count == 0)
                            g.drawLine(x + 1, y, x + width - 2, y);
                        g.setColor(new Color(0x0a008c));
                        g.drawLine(x + width - 1, y, x + width - 1, y + 19);
                        if (count == menu.getItems().size() - 1)
                            g.drawLine(x + 2, y + 19, x + width - 1, y + 19);
                        g.setColor(Color.WHITE);
                    }
                    g.drawString(item.getName(), x + 20, y + 14);
                }
                count ++;
            }
            
            if (mouse.x >= x && mouse.x < x + width && mouse.y >= 20 && mouse.y < 20 + y) {
                if (this.isMousePressed) {
                    menu.select();
                }
            }
        } else {
            g.setColor(Color.BLACK);
            if (menu.getName() == null && menu.getImage() != null) {
                Image img = menu.getImage();
                Dimension imgSize = new Dimension(img.getWidth(null) * 4, img.getHeight(null) * 4);
                Dimension boundary = new Dimension(scrWidth, 16);
                Dimension newSize = getScaledDimension(imgSize, boundary);
                g.drawImage(img, x + 8, 2, (int) newSize.getWidth(), (int) newSize.getHeight(), null);
            } else {
                g.drawString(menu.getName(), x + 8, 14);
            }
        }
        
        return x + itemWidth;
    }
    
    private boolean menuClick(Point mouse) {
        boolean propagate = true;
        int x = 10;
        Graphics2D g = this.graphics;
        
        for (Menu menu : this.menus) {
            int itemWidth;
            if (menu.getName() == null && menu.getImage() != null) {
                Image img = menu.getImage();
                Dimension imgSize = new Dimension(img.getWidth(null), img.getHeight(null));
                Dimension boundary = new Dimension(scrWidth, 18);
                Dimension newSize = getScaledDimension(imgSize, boundary);
                itemWidth = 8 + (int) newSize.getWidth() + 8 + 8;
            } else {
                itemWidth = 8 + g.getFontMetrics().stringWidth(menu.getName()) + 8;
            }
            
            boolean isSelected = menu.isSelected();
            
            if (this.isMousePressed && mouse.x >= x && mouse.x < x + itemWidth && mouse.y < 20) {
                menu.select();
                isSelected = true;
                propagate = false;
            } else {
                menu.deselect();
            }
            
            if (isSelected) {
                int width = 98;
                int y = 0;
                g.setColor(Color.BLACK);
                for (MenuItem item : menu.getItems()) {
                    y += 20;
                    if (item.getName().equals("-")) {
                        y -= 12;
                    } else {
                        int width2 = 20 + g.getFontMetrics().stringWidth(item.getName()) + 16;
                        if (width2 > width) width = width2;
                    }
                }
                
                int count = 0;
                if (mouse.x >= x && mouse.x < x + width && mouse.y >= 20 && mouse.y < 20 + y) {
                    if (this.isMousePressed) {
                        menu.select();
                        propagate = false;
                    } else {
                        y = 0;
                        for (MenuItem item : menu.getItems()) {
                            y += 20;
                            if (item.getName().equals("-")) {
                                y -= 12;
                            } else {
                                if (!item.isDisabled() && mouse.x >= x && mouse.x < x + width && mouse.y >= y && mouse.y < 20 + y) {
                                    item.actionPerformed();
                                }
                            }
                            count ++;
                        }
                    }
                }
            }
            
            x += itemWidth;
        }
        
        return propagate && mouse.getX() > 20;
    }
    
    private boolean isOverMenu(Point mouse) {
        boolean propagate = true;
        int x = 10;
        Graphics2D g = this.graphics;
        if (g == null) return false;
        
        for (Menu menu : this.menus) {
            int itemWidth;
            if (menu.getName() == null && menu.getImage() != null) {
                Image img = menu.getImage();
                Dimension imgSize = new Dimension(img.getWidth(null), img.getHeight(null));
                Dimension boundary = new Dimension(scrWidth, 18);
                Dimension newSize = getScaledDimension(imgSize, boundary);
                itemWidth = 8 + (int) newSize.getWidth() + 8 + 8;
            } else {
                itemWidth = 8 + g.getFontMetrics().stringWidth(menu.getName()) + 8;
            }
            
            boolean isSelected = menu.isSelected();
            
            if (this.isMousePressed && mouse.x >= x && mouse.x < x + itemWidth && mouse.y < 20) {
                isSelected = true;
                propagate = false;
            }
            
            if (isSelected) {
                propagate = false;
                int width = 98;
                int y = 0;
                g.setColor(Color.BLACK);
                for (MenuItem item : menu.getItems()) {
                    y += 20;
                    if (item.getName().equals("-")) {
                        y -= 12;
                    } else {
                        int width2 = 20 + g.getFontMetrics().stringWidth(item.getName()) + 16;
                        if (width2 > width) width = width2;
                    }
                }
                
                int count = 0;
                if (mouse.x >= x && mouse.x < x + width && mouse.y >= 20 && mouse.y < 20 + y) {
                    if (this.isMousePressed) {
                        propagate = false;
                    }
                }
            }
            
            x += itemWidth;
        }
        
        return !propagate;
    }
    
    private static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }
    
    public WindowManager getWindowManager() {
        return this.windowmanager;
    }
    
    public void keyPressed(KeyEvent e) {
        Window win = this.getWindowManager().getTopWindow();
        if (win != null && !this.rootWindowFocused) {
            KeyEvent evt = new KeyEvent(EnderXComponent.inst, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), e.getModifiers(), e.getKeyCode(), e.getKeyChar(), e.getKeyLocation());
            win.keyPressed(evt);
            for (KeyListener l : win.getKeyListeners())
                l.keyPressed(evt);
            for (Component cpt : win.getComponents())
                for (KeyListener l : cpt.getKeyListeners())
                    l.keyPressed(evt);
        }
        
        if (this.rootWindowFocused) {
            this.rootWindow.keyPressed(e);
            for (KeyListener l : this.rootWindow.getKeyListeners())
                l.keyPressed(e);
            for (Component cpt : this.rootWindow.getComponents())
                for (KeyListener l : cpt.getKeyListeners())
                    l.keyPressed(e);
        }
    }
    
    public void keyReleased(KeyEvent e) {
        Window win = this.getWindowManager().getTopWindow();
        if (win != null && !this.rootWindowFocused) {
            KeyEvent evt = new KeyEvent(EnderXComponent.inst, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), e.getModifiers(), e.getKeyCode(), e.getKeyChar(), e.getKeyLocation());
            win.keyReleased(evt);
            for (KeyListener l : win.getKeyListeners())
                l.keyReleased(evt);
            for (Component cpt : win.getComponents())
                for (KeyListener l : cpt.getKeyListeners())
                    l.keyReleased(evt);
        }
        
        if (this.rootWindowFocused) {
            this.rootWindow.keyReleased(e);
            for (KeyListener l : this.rootWindow.getKeyListeners())
                l.keyReleased(e);
            for (Component cpt : this.rootWindow.getComponents())
                for (KeyListener l : cpt.getKeyListeners())
                    l.keyReleased(e);
        }
    }
    
    public void keyTyped(KeyEvent e) {
        Window win = this.getWindowManager().getTopWindow();
        if (win != null && !this.rootWindowFocused) {
            KeyEvent evt = new KeyEvent(EnderXComponent.inst, KeyEvent.KEY_TYPED, System.currentTimeMillis(), e.getModifiers(), e.getKeyCode(), e.getKeyChar(), e.getKeyLocation());
            win.keyTyped(evt);
            for (KeyListener l : win.getKeyListeners())
                l.keyTyped(e);
            for (Component cpt : win.getComponents())
                for (KeyListener l : cpt.getKeyListeners())
                    l.keyTyped(evt);
        }
        
        if (this.rootWindowFocused) {
            this.rootWindow.keyTyped(e);
            for (KeyListener l : this.rootWindow.getKeyListeners())
                l.keyTyped(e);
            for (Component cpt : this.rootWindow.getComponents())
                for (KeyListener l : cpt.getKeyListeners())
                    l.keyTyped(e);
        }
    }
    
    public void mousePressed(MouseEvent e) {
        Point mouse = e.getPoint();
        if (this.use2xUpscaling) mouse = new Point((int) mouse.getX() / 2, (int) mouse.getY() / 2);
        
        this.isMousePressed = true;
        
        if (menuClick(mouse)) {
            ArrayList<Window> windows = this.getWindowManager().getWindows();
            boolean hasFoundWindow = false;
            this.rootWindowFocused = true;
            // Iterate over windows in reverse
            for (int i = windows.size() - 1; i >= 0; i --) {
                Window window = windows.get(i);
                if (!window.isVisible()) continue;
                Point pos = window.getPosition();
                Dimension size = window.getSize();
                int width = (int) size.getWidth();
                int height = (int) size.getHeight();
                if (window.isRolled()) height = -7;
                if (mouse.getX() >= pos.getX() && mouse.getX() < pos.getX() + width + 6 + 7 && e.getY() > pos.getY() &&
                    mouse.getY() >= pos.getY() && mouse.getY() < pos.getY() + height + 22 + 7) {
                    this.lastClickedWindow = window;
                    this.getWindowManager().moveToTop(window);
                    hasFoundWindow = true;
                    this.rootWindowFocused = false;
                    break;
                }
            }
            
            if (this.rootWindowFocused && e.getY() > 20) {
                MouseEvent evt = new MouseEvent(EnderXComponent.inst, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), e.getModifiers(), (int) mouse.getX(), (int) mouse.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
                this.rootWindow.mousePressed(evt);
                for (MouseListener l : this.rootWindow.getMouseListeners())
                    l.mousePressed(evt);
                for (Component cpt : this.rootWindow.getComponents())
                    if (isMouseIn(cpt, e.getPoint()))
                        for (MouseListener l : cpt.getMouseListeners())
                            l.mousePressed(evt);
            } else {
                Window win = this.lastClickedWindow;
                if (win != null) {
                    int x = (int) win.getPosition().getX();
                    int y = (int) win.getPosition().getY();
                    int width = (int) win.getSize().getWidth() + 2;
                    int height = (int) win.getSize().getHeight();
                    if (win.isRolled()) height = -7;
                    int numLeftButtons = (win.canClose() ? 1 : 0);
                    int numRightButtons = (win.canResize() ? 1 : 0) + (win.canRoll() ? 1 : 0);
                    boolean startDragging = false;
                    boolean startResizing = false;
                    if (mouse.getX() >= x && mouse.getX() < x + 5 + width + 5 && mouse.getY() >= y && mouse.getY() < y + 22) {
                        startDragging = true;
                    }
                    if (win.canResize() && !win.isRolled() && mouse.getX() >= x + 5 + width + 5 - 22 && mouse.getX() < x + 5 + width + 5 && mouse.getY() >= y + height && mouse.getY() < y + height + 22 + 7) {
                        startResizing = true;
                    }
                    if (win.canClose()) {
                        int buttonX = x + 4;
                        int buttonY = y + 4;
                        if (mouse.getX() >= buttonX && mouse.getX() < buttonX + 13 && mouse.getY() >= buttonY && mouse.getY() < buttonY + 13)
                            startDragging = false;
                    }
                    if (win.canResize()) {
                        int buttonX = x + width - (-- numRightButtons) * 16 - 7;
                        int buttonY = y + 4;
                        if (mouse.getX() >= buttonX && mouse.getX() < buttonX + 13 && mouse.getY() >= buttonY && mouse.getY() < buttonY + 13)
                            startDragging = false;
                    }
                    if (win.canRoll()) {
                        int buttonX = x + width - (-- numRightButtons) * 16 - 7;
                        int buttonY = y + 4;
                        if (mouse.getX() >= buttonX && mouse.getX() < buttonX + 13 && mouse.getY() >= buttonY && mouse.getY() < buttonY + 13)
                            startDragging = false;
                    }
                    if (startDragging && !win.isDragging() && !isOverMenu(mouse)) {
                        win.setDragging(true);
                        win.setDragOrigin(mouse);
                        this.dragPoint = win.getPosition();
                    }
                    if (startResizing && !win.isResizing() && !isOverMenu(mouse)) {
                        win.setResizing(true);
                        win.setResizeOrigin(mouse);
                        this.resize = win.getSize();
                    }
                    if (this.isMouseIn(win, mouse)) {
                        Point pos = win.getPosition();
                        MouseEvent evt = new MouseEvent(EnderXComponent.inst, MouseEvent.MOUSE_PRESSED, System.currentTimeMillis(), e.getModifiers(), (int) (mouse.getX() - (pos.getX() + 6)), (int) (mouse.getY() - (pos.getY() + 22)), e.getClickCount(), e.isPopupTrigger(), e.getButton());
                        win.mousePressed(evt);
                        for (MouseListener l : win.getMouseListeners())
                            l.mousePressed(evt);
                        for (Component cpt : win.getComponents())
                            if (isMouseIn(win, cpt, mouse))
                                for (MouseListener l : cpt.getMouseListeners())
                                    l.mousePressed(evt);
                    }
                }
            }
        }
    }
    
    public void mouseReleased(MouseEvent e) {
        Point mouse = e.getPoint();
        if (this.use2xUpscaling) mouse = new Point((int) mouse.getX() / 2, (int) mouse.getY() / 2);
        
        this.isMousePressed = false;
        this.isMouseReleased = true;
        Window top = this.getWindowManager().getTopWindow();
        if (top != null && top.isDragging() && !this.rootWindowFocused) {
            top.setDragging(false);
            if (this.dragPoint != null) {
                top.setPosition(new Point((int) this.dragPoint.getX(), (int) Math.max(this.dragPoint.getY(), 20)));
                for (ComponentListener l : top.getComponentListeners())
                    l.componentResized(new ComponentEvent(EnderX.getComponent(), ComponentEvent.COMPONENT_MOVED));
            }
        }
        if (top != null && top.isResizing() && !this.rootWindowFocused) {
            top.setResizing(false);
            if (this.resize != null) {
                top.setSize(this.resize);
                top.onResize();
                for (ComponentListener l : top.getComponentListeners())
                    l.componentResized(new ComponentEvent(EnderX.getComponent(), ComponentEvent.COMPONENT_RESIZED));
            }
        }
        if (top != null && !this.rootWindowFocused) {
            if (this.isMouseIn(top, mouse)) {
                Point pos = top.getPosition();
                MouseEvent evt = new MouseEvent(EnderXComponent.inst, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), e.getModifiers(), (int) (mouse.getX() - (pos.getX() + 6)), (int) (mouse.getY() - (pos.getY() + 22)), e.getClickCount(), e.isPopupTrigger(), e.getButton());
                top.mouseReleased(evt);
                for (MouseListener l : top.getMouseListeners())
                    l.mouseReleased(evt);
                for (Component cpt : top.getComponents())
                    if (isMouseIn(top, cpt, mouse))
                        for (MouseListener l : cpt.getMouseListeners())
                            l.mouseReleased(evt);
            }
        }
        if (this.rootWindowFocused && mouse.getY() > 20) {
            MouseEvent evt = new MouseEvent(EnderXComponent.inst, MouseEvent.MOUSE_RELEASED, System.currentTimeMillis(), e.getModifiers(), (int) mouse.getX(), (int) mouse.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
            this.rootWindow.mouseReleased(evt);
            for (MouseListener l : this.rootWindow.getMouseListeners())
                l.mouseReleased(evt);
            for (Component cpt : this.rootWindow.getComponents())
                if (isMouseIn(cpt, mouse))
                    for (MouseListener l : cpt.getMouseListeners())
                        l.mouseReleased(evt);
        }
        menuClick(mouse);
    }
    
    public void mouseClicked(MouseEvent e) {
        Point mouse = e.getPoint();
        if (this.use2xUpscaling) mouse = new Point((int) mouse.getX() / 2, (int) mouse.getY() / 2);
        
        Window win = this.getWindowManager().getTopWindow();
        if (win != null && !this.rootWindowFocused) {
            if (this.isMouseIn(win, mouse)) {
                Point pos = win.getPosition();
                MouseEvent evt = new MouseEvent(EnderXComponent.inst, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), e.getModifiers(), (int) (mouse.getX() - (pos.getX() + 6)), (int) (mouse.getY() - (pos.getY() + 22)), e.getClickCount(), e.isPopupTrigger(), e.getButton());
                win.mouseClicked(evt);
                for (MouseListener l : win.getMouseListeners())
                    l.mouseClicked(evt);
                boolean hasClickedComponent = false;
                for (Component cpt : win.getComponents())
                    if (isMouseIn(win, cpt, mouse)) {
                        cpt.requestFocus();
                        for (MouseListener l : cpt.getMouseListeners())
                            l.mouseClicked(evt);
                        hasClickedComponent = true;
                    }
                if (!hasClickedComponent)
                    win.requestComponentFocus(null);
            }
        }
        
        if (this.rootWindowFocused && mouse.getY() > 20) {
            MouseEvent evt = new MouseEvent(EnderXComponent.inst, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), e.getModifiers(), (int) mouse.getX(), (int) mouse.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
            this.rootWindow.mouseClicked(evt);
            for (MouseListener l : this.rootWindow.getMouseListeners())
                l.mouseClicked(evt);
            boolean hasClickedComponent = false;
            for (Component cpt : this.rootWindow.getComponents())
                if (isMouseIn(cpt, mouse)) {
                    cpt.requestFocus();
                    for (MouseListener l : cpt.getMouseListeners())
                        l.mouseClicked(evt);
                    hasClickedComponent = true;
                }
            if (!hasClickedComponent)
                this.rootWindow.requestComponentFocus(null);
        }
    }
    
    public void mouseMoved(MouseEvent e) {
        Point mouse = e.getPoint();
        if (this.use2xUpscaling) mouse = new Point((int) mouse.getX() / 2, (int) mouse.getY() / 2);
        
        Window win = this.getWindowManager().getTopWindow();
        if (win != null && !this.rootWindowFocused) {
            if (this.isMouseIn(win, mouse)) {
                Point pos = win.getPosition();
                MouseEvent evt = new MouseEvent(EnderXComponent.inst, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), e.getModifiers(), (int) (mouse.getX() - (pos.getX() + 6)), (int) (mouse.getY() - (pos.getY() + 22)), e.getClickCount(), e.isPopupTrigger(), e.getButton());
                win.mouseMoved(evt);
                for (MouseMotionListener l : win.getMouseMotionListeners())
                    l.mouseMoved(evt);
                for (Component cpt : win.getComponents())
                    if (isMouseIn(win, cpt, mouse))
                        for (MouseMotionListener l : cpt.getMouseMotionListeners())
                            l.mouseMoved(evt);
            }
        }
        
        if (this.rootWindowFocused) {
            MouseEvent evt = new MouseEvent(EnderXComponent.inst, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), e.getModifiers(), (int) mouse.getX(), (int) mouse.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
            this.rootWindow.mouseMoved(evt);
            for (MouseMotionListener l : this.rootWindow.getMouseMotionListeners())
                l.mouseMoved(evt);
            for (Component cpt : this.rootWindow.getComponents())
                if (isMouseIn(cpt, mouse))
                    for (MouseMotionListener l : cpt.getMouseMotionListeners())
                        l.mouseMoved(evt);
        }
    }
    
    public void mouseDragged(MouseEvent e) {
        Point mouse = e.getPoint();
        if (this.use2xUpscaling) mouse = new Point((int) mouse.getX() / 2, (int) mouse.getY() / 2);
        
        if (!isOverMenu(mouse)) {
            if (this.rootWindowFocused) {
                MouseEvent evt = new MouseEvent(EnderXComponent.inst, MouseEvent.MOUSE_DRAGGED, System.currentTimeMillis(), e.getModifiers(), (int) mouse.getX(), (int) mouse.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
                this.rootWindow.mouseDragged(evt);
                for (MouseMotionListener l : this.rootWindow.getMouseMotionListeners())
                    l.mouseDragged(evt);
                for (Component cpt : this.rootWindow.getComponents())
                    if (isMouseIn(cpt, mouse))
                        for (MouseMotionListener l : cpt.getMouseMotionListeners())
                            l.mouseDragged(evt);
            } else {
                Window win = this.getWindowManager().getTopWindow();
                if (win != null && win.isDragging()) {
                    Point origin = win.getDragOrigin();
                    Point newpos = new Point((int) (this.dragPoint.getX() + mouse.getX() - origin.getX()), (int) (this.dragPoint.getY() + mouse.getY() - origin.getY()));
                    this.dragPoint = newpos;
                    win.setDragOrigin(mouse);
                }
                if (win != null && win.isResizing()) {
                    Point origin = win.getResizeOrigin();
                    Dimension newdim = new Dimension((int) (this.resize.getWidth() + mouse.getX() - origin.getX()), (int) (this.resize.getHeight() + mouse.getY() - origin.getY()));
                    this.resize = newdim;
                    win.setResizeOrigin(mouse);
                }
                
                if (win != null) {
                    if (this.isMouseIn(win, mouse)) {
                        Point pos = win.getPosition();
                        MouseEvent evt = new MouseEvent(EnderXComponent.inst, MouseEvent.MOUSE_DRAGGED, System.currentTimeMillis(), e.getModifiers(), (int) (mouse.getX() - (pos.getX() + 6)), (int) (mouse.getY() - (pos.getY() + 22)), e.getClickCount(), e.isPopupTrigger(), e.getButton());
                        win.mouseDragged(evt);
                        for (MouseMotionListener l : win.getMouseMotionListeners())
                            l.mouseDragged(evt);
                        for (Component cpt : win.getComponents())
                            if (isMouseIn(win, cpt, mouse))
                                for (MouseMotionListener l : cpt.getMouseMotionListeners())
                                    l.mouseDragged(evt);
                    }
                }
            }
        }
    }
}

