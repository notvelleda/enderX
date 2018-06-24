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

public class PFMRootWindow extends Window {
    private File folder;
    
    public PFMRootWindow(int width, int height) {
        super("Root Window");
        this.setSize(new Dimension(width, height));
        
        this.folder = new File(EnderX.getHomeDirectory(), "Desktop");
        if (!this.folder.exists() && !this.folder.mkdirs()) {
            throw new RuntimeException("WTF man! You need free space on your hard drive for your desktop folder!");
        }
        
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
                        new File(PFMRootWindow.this.folder, textbox.getString()).mkdirs();
                        PFMRootWindow.updateWindow(PFMRootWindow.this, PFMRootWindow.this.folder, true, null);
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
                for (Component cpt : PFMRootWindow.this.getComponents()) {
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
        fileMenu.addItem(new MenuItem("Close").setDisabled(true));
        fileMenu.addItem(new MenuItem("-"));
        fileMenu.addItem(new MenuItem("Get Info") {
            @Override
            public void actionPerformed() {
            }
        }.setDisabled(true));
        fileMenu.addItem(new MenuItem("Duplicate") {
            @Override
            public void actionPerformed() {
                for (Component cpt : PFMRootWindow.this.getComponents()) {
                    try {
                        FileIcon icon = (FileIcon) cpt;
                        if (icon.selected) {
                            String newName = "Copy of " + icon.getFile().getName();
                            if (Paths.get(PFMRootWindow.this.folder.toString(), newName).toFile().exists()) {
                                EnderX.alert(newName + " already exists!");
                            } else if (!icon.getFile().exists()) {
                                EnderX.alert(icon.getFile().getName() + " doesn't exist!");
                            } else {
                                Files.copy(icon.getFile().toPath(), Paths.get(PFMRootWindow.this.folder.toString(), newName));
                                PFMRootWindow.updateWindow(PFMRootWindow.this, PFMRootWindow.this.folder, true, null);
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
                for (Component cpt : PFMRootWindow.this.getComponents()) {
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
                    if (Paths.get(PFMRootWindow.this.folder.toString(), PFM.clipboard.getName()).toFile().exists()) {
                        EnderX.alert(PFM.clipboard.getName() + " already exists!");
                    } else if (!PFM.clipboard.exists()) {
                        EnderX.alert(PFM.clipboard.getName() + " doesn't exist!");
                    } else {
                        Files.copy(PFM.clipboard.toPath(), Paths.get(PFMRootWindow.this.folder.toString(), PFM.clipboard.getName()));
                        PFMRootWindow.updateWindow(PFMRootWindow.this, PFMRootWindow.this.folder, true, null);
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
                for (Component cpt : PFMRootWindow.this.getComponents()) {
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
                                PFMRootWindow.updateWindow(PFMRootWindow.this, PFMRootWindow.this.folder, true, null);
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
                for (Component cpt : PFMRootWindow.this.getComponents()) {
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
                        PFMRootWindow.updateWindow(PFMRootWindow.this, PFMRootWindow.this.folder, true, null);
                    }
                };
            }
        }.setDisabled(true));
        
        Menu specialMenu = new Menu("Special");
        specialMenu.addItem(new MenuItem("Refresh") {
            @Override
            public void actionPerformed() {
                PFMRootWindow.updateWindow(PFMRootWindow.this, PFMRootWindow.this.folder, true, null);
            }
        });
        specialMenu.addItem(new MenuItem("-"));
        specialMenu.addItem(new MenuItem("Set as wallpaper") {
            @Override
            public void actionPerformed() {
                for (Component cpt : PFMRootWindow.this.getComponents()) {
                    try {
                        FileIcon icon = (FileIcon) cpt;
                        if (icon.selected) {
                            EnderX.getDesktop().setWallpaper(icon.getFile());
                        }
                    } catch (Exception e) {}
                }
            }
        }.setDisabled(true));
        
        this.getMenus().add(fileMenu);
        this.getMenus().add(editMenu);
        this.getMenus().add(specialMenu);
        
        this.updateWindow(this, this.folder, true, null);
        
        this.close(); // Fix bugs lmao
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
            window.removeAll();
            Dimension size = window.getSize();
            
            FileIcon hardDisk = new FileIcon(PFM.icons[2], EnderX.getHomeDirectory(), "Hard Disk") {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (this.selected) {
                        PFM.openFile(this.getFile());
                    }
                    for (Menu menu : window.getMenus())
                        for (MenuItem item : menu.getItems())
                            if (item.getName().equals("Get Info") || item.getName().equals("Duplicate") ||
                                item.getName().equals("Rename") || item.getName().equals("Delete") || item.getName().equals("Copy"))
                                item.setDisabled(true);
                            else if (item.getName().equals("Open"))
                                item.setDisabled(false);
                    this.selected = !this.selected;
                }
            };
            hardDisk.setLocation((int) size.getWidth() - 80, 24);
            hardDisk.setHighlight(new Color(0xbdc6d6));
            window.add(hardDisk);
            
            int x = 16;
            int y = 24;
            int numItems = 0;
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) {
                File file = listOfFiles[i];
                BufferedImage icon = PFM.icons[0];
                if (file.getName().equals("Icon.png")) continue;
                if (file.isDirectory()) icon = PFM.getFolderIcon(file);
                if (PFM.getFileExtension(file).equalsIgnoreCase("app")) icon = PFM.getAppIcon(file);
                FileIcon fileIcon = new FileIcon(icon, file) {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        File file = this.getFile();
                        if (this.selected) {
                            PFM.openFile(file);
                            for (Menu menu : window.getMenus())
                                for (MenuItem item : menu.getItems())
                                    if (item.getName().equals("Open") || item.getName().equals("Get Info") || item.getName().equals("Duplicate") ||
                                        item.getName().equals("Rename") || item.getName().equals("Delete") || item.getName().equals("Copy"))
                                        item.setDisabled(true);
                        } else {
                            String ext = PFM.getFileExtension(file).toLowerCase();
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
                fileIcon.setHighlight(new Color(0xbdc6d6));
                window.add(fileIcon);
                x += fileIcon.getPreferredSize().getWidth();
                if (x + fileIcon.getPreferredSize().getWidth() > size.getWidth() - 144) {
                    x = 16;
                    y += fileIcon.getPreferredSize().getHeight();
                }
                numItems ++;
            }
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
    
    private static boolean isMouseIn(Component cpt, Point mouse) {
        if (mouse.getX() >= cpt.getX() && mouse.getX() < cpt.getX() + cpt.getPreferredSize().getWidth() &&
            mouse.getY() >= cpt.getY() && mouse.getY() < cpt.getY() + cpt.getPreferredSize().getHeight()) {
            return true;
        }
        return false;
    }
    
    public void mouseClicked(MouseEvent e) {
        updateWindow(this, this.folder, false, e.getPoint());
    }
}
