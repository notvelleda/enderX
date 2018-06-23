/*
 * PFM.java
 * 
 * Copyright 2018 Evan Carlson <pugduddly@gmail.com>
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

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;

import xyz.pugduddly.enderX.EnderX;
import xyz.pugduddly.enderX.ui.Window;
import xyz.pugduddly.enderX.ui.Menu;
import xyz.pugduddly.enderX.ui.MenuItem;
import xyz.pugduddly.enderX.ui.platinum.Beveled;
import xyz.pugduddly.enderX.ui.platinum.Label;
import xyz.pugduddly.enderX.ui.platinum.Textbox;
import xyz.pugduddly.enderX.ui.platinum.Button;

public class PFM {
    public static BufferedImage[] icons;
    public static File clipboard;
    
    static {
        icons = new BufferedImage[] {
            EnderX.loadImage(PFM.class.getResource("/icons/file.png")),
            EnderX.loadImage(PFM.class.getResource("/icons/folder.png")),
            EnderX.loadImage(PFM.class.getResource("/icons/hdd.png")),
            EnderX.loadImage(PFM.class.getResource("/icons/trashcan.png")),
            EnderX.loadImage(PFM.class.getResource("/icons/trashcan_full.png")),
        };
    }
    
    public static void openFile(File file) {
        if (file.equals(EnderX.getHomeDirectory()))
            PFM.main(file, "Hard Disk");
        else if (file.isDirectory())
            PFM.main(file, file.getName());
        else if (getFileExtension(file).equalsIgnoreCase("app"))
            EnderX.runApp(file);
    }
    
    public static Window getRootWindow(int width, int height) {
        return new PFMRootWindow(width, height);
    }
    
    private static boolean isMouseIn(Component cpt, Point mouse) {
        if (mouse.getX() >= cpt.getX() && mouse.getX() < cpt.getX() + cpt.getPreferredSize().getWidth() &&
            mouse.getY() >= cpt.getY() && mouse.getY() < cpt.getY() + cpt.getPreferredSize().getHeight()) {
            return true;
        }
        return false;
    }
    
    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }
    
    public static BufferedImage getAppIcon(File file) {
        try {
            ZipInputStream zip = new ZipInputStream(new FileInputStream(file));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().equals("META-INF/Icon.png")) {
                    return EnderX.loadImage(zip);
                }
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return icons[0];
    }
    
    public static BufferedImage getFolderIcon(File file) {
        try {
            File iconFile = new File(file, "Icon.png");
            if (iconFile.exists() && !iconFile.isDirectory())
                return EnderX.loadImage(new FileInputStream(iconFile));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return icons[1];
    }
    
    private static void updateWindow(final Window window, File folder, boolean fullUpdate, Point mouse) {
        for (Menu menu : window.getMenus())
            for (MenuItem item : menu.getItems()) {
                if (item.getName().equals("Open") || item.getName().equals("Get Info") || item.getName().equals("Duplicate") ||
                    item.getName().equals("Rename") || item.getName().equals("Delete") || item.getName().equals("Copy") ||
                    item.getName().equals("Set as wallpaper"))
                    item.setDisabled(true);
                else if (item.getName().equals("Paste") && PFM.clipboard != null)
                    item.setDisabled(false);
            }
        
        if (fullUpdate) {
            window.getComponents().clear();
            Dimension size = window.getSize();
            
            Beveled b = new Beveled();
            b.setPreferredSize(new Dimension((int) size.getWidth(), 20));
            window.add(b);
            
            int x = 0;
            int y = 20;
            int numItems = 0;
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                File file = listOfFiles[i];
                BufferedImage icon = icons[0];
                if (file.getName().equals("Icon.png")) continue;
                if (file.isDirectory()) icon = getFolderIcon(file);
                if (getFileExtension(file).equalsIgnoreCase("app")) icon = getAppIcon(file);
                FileIcon fileIcon = new FileIcon(icon, file) {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        File file = this.getFile();
                        if (this.selected) {
                            PFM.openFile(file);
                        } else {
                            String ext = getFileExtension(file).toLowerCase();
                            if (ext.equals("png") || ext.equals("jpg") || ext.equals("jpeg") || ext.equals("gif")) {
                                for (Menu menu : window.getMenus())
                                    for (MenuItem item : menu.getItems())
                                        if (item.getName().equals("Set as wallpaper"))
                                            item.setDisabled(false);
                            }
                        }
                        this.selected = !this.selected;
                    }
                };
                fileIcon.setLocation(x, y);
                window.add(fileIcon);
                x += fileIcon.getPreferredSize().getWidth();
                if (x + fileIcon.getPreferredSize().getWidth() > size.getWidth()) {
                    x = 0;
                    y += fileIcon.getPreferredSize().getHeight();
                }
                numItems ++;
            }
            
            Label label = new Label(numItems + " items, " + (folder.getFreeSpace() / 1024 / 1024) + " MB available");
            label.centerText(true);
            label.setLocation((int) size.getWidth() / 2, 5);
            window.add(label);
        } else {
            File selectedFile = null;
            for (Component cpt : window.getComponents()) {
                try {
                    FileIcon icon = (FileIcon) cpt; // Will throw exception if not a FileIcon, thereby skipping it
                    if (!isMouseIn(icon, mouse) || !icon.selected) {
                        icon.selected = false;
                    }
                    if (isMouseIn(icon, mouse))
                        for (Menu menu : window.getMenus())
                            for (MenuItem item : menu.getItems())
                                if (item.getName().equals("Open") || item.getName().equals("Get Info") || item.getName().equals("Duplicate") ||
                                    item.getName().equals("Rename") || item.getName().equals("Delete") || item.getName().equals("Copy"))
                                    item.setDisabled(false);
                } catch (Exception e) {}
            }
        }
    }
    
	public static void main() {
        main(null, "Hard Disk");
    }
    
	public static void main(final File file, String name) {
        if (file == null) {
            main(EnderX.getHomeDirectory(), name);
            return;
        }
        
		final Window window = new Window(name) {
            @Override
            public void mouseClicked(MouseEvent e) {
                PFM.updateWindow(this, file, false, e.getPoint());
            }
            @Override
            public void onResize() {
                PFM.updateWindow(this, file, true, null);
            }
        };
        
        window.setSize(new Dimension(320, 240));
        
        Menu fileMenu = new Menu("File");
        fileMenu.addItem(new MenuItem("New Folder") {
            @Override
            public void actionPerformed() {
                final Window win = new Window("New Folder");
                win.setSize(new Dimension(368, 112));
                win.setResizable(false);
                win.setClosable(false);
                win.setRollable(false);
                Label label = new Label("Please enter the name of the folder to create.");
                label.setLocation(4, 4);
                win.add(label);
                final Textbox textbox = new Textbox();
                textbox.setLocation(8, 20);
                win.add(textbox);
                Button button = new Button("OK") {
                    @Override
                    public void actionPerformed() {
                        win.close();
                        new File(file, textbox.getString()).mkdirs();
                        PFM.updateWindow(window, file, true, null);
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
        });
        fileMenu.addItem(new MenuItem("Open") {
            @Override
            public void actionPerformed() {
                for (Component cpt : window.getComponents()) {
                    try {
                        FileIcon icon = (FileIcon) cpt;
                        if (icon.selected) {
                            PFM.openFile(icon.getFile());
                            break;
                        }
                    } catch (Exception e) {}
                }
            }
        }.setDisabled(true));
        fileMenu.addItem(new MenuItem("Close") {
            @Override
            public void actionPerformed() {
                window.close();
            }
        });
        fileMenu.addItem(new MenuItem("-"));
        fileMenu.addItem(new MenuItem("Get Info") {
            @Override
            public void actionPerformed() {
            }
        }.setDisabled(true));
        fileMenu.addItem(new MenuItem("Duplicate") {
            @Override
            public void actionPerformed() {
                for (Component cpt : window.getComponents()) {
                    try {
                        FileIcon icon = (FileIcon) cpt;
                        if (icon.selected) {
                            String newName = "Copy of " + icon.getFile().getName();
                            if (Paths.get(file.toString(), newName).toFile().exists()) {
                                EnderX.alert(newName + " already exists!");
                            } else if (!icon.getFile().exists()) {
                                EnderX.alert(icon.getFile().getName() + " doesn't exist!");
                            } else {
                                Files.copy(icon.getFile().toPath(), Paths.get(file.toString(), newName));
                                PFM.updateWindow(window, file, true, null);
                            }
                            break;
                        }
                    } catch (Exception e) {}
                }
            }
        }.setDisabled(true));
        
        Menu editMenu = new Menu("Edit");
        editMenu.addItem(new MenuItem("Copy") {
            @Override
            public void actionPerformed() {
                for (Component cpt : window.getComponents()) {
                    try {
                        FileIcon icon = (FileIcon) cpt;
                        if (icon.selected) {
                            PFM.clipboard = icon.getFile();
                            break;
                        }
                    } catch (Exception e) {}
                }
            }
        }.setDisabled(true));
        editMenu.addItem(new MenuItem("Paste") {
            @Override
            public void actionPerformed() {
                try {
                    if (Paths.get(file.toString(), PFM.clipboard.getName()).toFile().exists()) {
                        EnderX.alert(PFM.clipboard.getName() + " already exists!");
                    } else if (!PFM.clipboard.exists()) {
                        EnderX.alert(PFM.clipboard.getName() + " doesn't exist!");
                    } else {
                        Files.copy(PFM.clipboard.toPath(), Paths.get(file.toString(), PFM.clipboard.getName()));
                        PFM.updateWindow(window, file, true, null);
                    }
                } catch (Exception e) {
                    EnderX.alert(e.toString());
                }
            }
        }.setDisabled(true));
        editMenu.addItem(new MenuItem("-"));
        editMenu.addItem(new MenuItem("Rename") {
            @Override
            public void actionPerformed() {
                File selectedFile = null;
                for (Component cpt : window.getComponents()) {
                    try {
                        FileIcon icon = (FileIcon) cpt;
                        if (icon.selected) {
                            selectedFile = icon.getFile();
                            break;
                        }
                    } catch (Exception e) {}
                }
                if (selectedFile == null) return;
                final File fileToRename = selectedFile;
                
                final Window win = new Window("Rename");
                win.setSize(new Dimension(368, 112));
                win.setResizable(false);
                win.setClosable(false);
                win.setRollable(false);
                Label label = new Label("Please enter the new name for " + fileToRename.getName() + ".");
                label.setLocation(4, 4);
                win.add(label);
                final Textbox textbox = new Textbox();
                textbox.setLocation(8, 20);
                win.add(textbox);
                Button button = new Button("OK") {
                    @Override
                    public void actionPerformed() {
                        try {
                            if (!fileToRename.toPath().resolveSibling(textbox.getString()).toFile().exists()) {
                                win.close();
                                Files.move(fileToRename.toPath(), fileToRename.toPath().resolveSibling(textbox.getString()));
                                PFM.updateWindow(window, file, true, null);
                            } else {
                                EnderX.alert(textbox.getString() + " already exists!");
                            }
                        } catch (Exception e) {
                            EnderX.alert(e.toString());
                        }
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
        }.setDisabled(true));
        editMenu.addItem(new MenuItem("Delete") {
            @Override
            public void actionPerformed() {
                File selectedFile = null;
                for (Component cpt : window.getComponents()) {
                    try {
                        FileIcon icon = (FileIcon) cpt;
                        if (icon.selected) {
                            selectedFile = icon.getFile();
                            break;
                        }
                    } catch (Exception e) {}
                }
                if (selectedFile == null) return;
                final File fileToDelete = selectedFile;
                new DialogBox("Are you sure that you want to delete " + selectedFile.getName() + "?") {
                    @Override
                    public void actionPerformed() {
                        fileToDelete.delete();
                        PFM.updateWindow(window, file, true, null);
                    }
                };
            }
        }.setDisabled(true));
        
        Menu specialMenu = new Menu("Special");
        specialMenu.addItem(new MenuItem("Refresh") {
            @Override
            public void actionPerformed() {
                updateWindow(window, file, true, null);
            }
        });
        specialMenu.addItem(new MenuItem("-"));
        specialMenu.addItem(new MenuItem("Set as wallpaper") {
            @Override
            public void actionPerformed() {
                for (Component cpt : window.getComponents()) {
                    try {
                        FileIcon icon = (FileIcon) cpt;
                        if (icon.selected) {
                            EnderX.getDesktop().setWallpaper(icon.getFile());
                        }
                    } catch (Exception e) {}
                }
            }
        }.setDisabled(true));
        
        window.getMenus().add(fileMenu);
        window.getMenus().add(editMenu);
        window.getMenus().add(specialMenu);
        
        updateWindow(window, file, true, null);
        
        window.setVisible(true);
        //EnderX.getDesktop().getWindowManager().addWindow(window);
	}
}

