/*
 * EnderX.java
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

package xyz.pugduddly.enderX;

import java.util.HashMap;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.net.URL;
import java.net.URLClassLoader;

import java.lang.reflect.Method;

import xyz.pugduddly.enderX.ui.Desktop;
import xyz.pugduddly.enderX.ui.Window;
import xyz.pugduddly.enderX.ui.platinum.Label;
import xyz.pugduddly.enderX.ui.platinum.Button;

import com.jcraft.weirdx.WeirdX;

public class EnderX {
    private static File homeDirectory;
    private static HashMap<String, Font> fonts = new HashMap<String, Font>();
    public static BufferedImage icon = loadBase64Image("R0lGODdhCAAIAMIFAAAAAAoKCvoD9Pn5+fz7+////////////ywAAAAACAAIAAADEgi6G6EOPDlfpGAIoshmYCgqCQA7");
    private static EnderXComponent component;
    private static Frame frame;
    private static Dimension screenSize;
    private static BufferedImage screenBuffer;
    public static final String VERSION = "2.0-beta1";
    
	public static void main(String args[]) {
        frame = new Frame("enderX");
        component = new EnderXComponent();
        setScreenSize(new Dimension(512, 342));
        frame.add(component);
        frame.setResizable(false);
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(
            new WindowAdapter() {
                public void windowClosed(WindowEvent e) {
                    System.exit(0);
                }
                public void windowClosing(WindowEvent e) {
                    ((Frame) e.getWindow()).dispose();
                    System.exit(0);
                }
            }
        );
        frame.pack();
        frame.setVisible(true);
	}
    
    /**
     * Get the directory that all enderX-related files should be stored in.
     * @return the directory that all enderX-related files should be stored in.
     */
    public static File getHomeDirectory() {
        if (homeDirectory == null) {
            String string = "enderX";
            String string2 = System.getProperty("user.home", ".");
            switch (getOSType()) {
                case "unix": 
                case "linux": 
                case "solaris": 
                case "sunos":
                    homeDirectory = new File(string2, '.' + string + '/');
                    break;
                case "win":
                    String string3 = System.getenv("APPDATA");
                    if (string3 != null) {
                        homeDirectory = new File(string3, "." + string + '/');
                        break;
                    }
                    homeDirectory = new File(string2, '.' + string + '/');
                    break;
                case "mac":
                    homeDirectory = new File(string2, "Library/Application Support/" + string);
                    break;
                default:
                    homeDirectory = new File(string2, string + '/');
            }
            if (!homeDirectory.exists() && !homeDirectory.mkdirs()) {
                throw new RuntimeException("The working directory could not be created: " + homeDirectory);
            }
        }
        return homeDirectory;
    }

    private static String getOSType() {
        String string = System.getProperty("os.name").toLowerCase();
        if (string.contains("win")) {
            return "win";
        }
        if (string.contains("mac")) {
            return "mac";
        }
        if (string.contains("solaris")) {
            return "solaris";
        }
        if (string.contains("sunos")) {
            return "sunos";
        }
        if (string.contains("linux")) {
            return "linux";
        }
        if (string.contains("unix")) {
            return "unix";
        }
        return "other";
    }
    
    /**
     * Get a loaded Font.
     * @param name The name of the font.
     * @param size The size of the font.
     * @return the Font object.
     * @see java.awt.Font
     */
    public static Font getFont(String name, int size) {
        return fonts.get(name.toLowerCase() + "#" + size);
    }
    
    /**
     * Add a font to the table of loaded fonts.
     * @param name The name of the font.
     * @param size The size of the font.
     * @param font The font object.
     * @return the Font object, for convenience.
     * @see java.awt.Font
     */
    public static Font addFont(String name, int size, Font font) {
        return fonts.put(name.toLowerCase() + "#" + size, font);
    }
    
    /**
     * Load a font, and then add it to the table of loaded fonts.
     * @param is The InputStream pointing to the font.
     * @param name The name of the font.
     * @param size The size of the font.
     * @return the loaded Font object, for convenience
     * @see java.awt.Font java.io.InputStream
     */
    public static Font loadFont(InputStream is, String name, int size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont((float) size);
            addFont(name, size, font);
            return font;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Load all built-in fonts.
     * This method is called automatically on startup.
     */
    public static void loadBuiltinFonts() {
        System.out.println("Loading fonts...");
        List<String> classNames = new ArrayList<String>();
        try {
            File jarFile = new File(EnderX.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
            ZipInputStream zip = new ZipInputStream(new FileInputStream(jarFile));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().startsWith("fonts")) {
                    String[] pathElements = entry.getName().split("/");
                    int size = Integer.parseInt(pathElements[1]);
                    String name = pathElements[2].substring(0, pathElements[2].lastIndexOf("."));
                    if (loadFont(zip, name, size) != null) {
                        System.out.println("Loaded " + name + " (" + size + ")");
                    } else {
                        System.out.println("Failed to load" + entry.getName());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Done");
    }
    
    /**
     * Convenience method to attempt accelerating an image.
     */
    public static BufferedImage accelerateImage(Image i, int transparency) {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
            BufferedImage bi = gc.createCompatibleImage(i.getWidth(null), i.getHeight(null), transparency);
            bi.setAccelerationPriority(1);
            Graphics2D g = bi.createGraphics();
            g.drawImage(i, 0, 0, null);
            g.dispose();
            i.flush();
            return bi;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static BufferedImage toCompatibleImage(BufferedImage image) {
        // obtain the current system graphical settings
        GraphicsConfiguration gfxConfig = GraphicsEnvironment.
            getLocalGraphicsEnvironment().getDefaultScreenDevice().
            getDefaultConfiguration();

        /*
         * if image is already compatible and optimized for current system 
         * settings, simply return it
         */
        if (image.getColorModel().equals(gfxConfig.getColorModel()))
            return image;

        // image is not optimized, so create a new image that is
        BufferedImage newImage = gfxConfig.createCompatibleImage(
                image.getWidth(), image.getHeight(), image.getTransparency());

        // get the graphics context of the new image to draw the old image on
        Graphics2D g2d = newImage.createGraphics();

        // actually draw the image and dispose of context no longer needed
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();

        // return the new optimized image
        return newImage; 
    }
    
    /**
     * Convenience method to load a BufferedImage from a Base64 string.
     * @return the loaded image, or `null` if unsuccessful.
     * @see java.awt.image.BufferedImage
     */
    public static BufferedImage loadBase64Image(String string) {
        try {
            return toCompatibleImage(javax.imageio.ImageIO.read(new java.io.ByteArrayInputStream(javax.xml.bind.DatatypeConverter.parseBase64Binary(string))));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Convenience method to load a BufferedImage from an InputStream.
     * @return the loaded image, or `null` if unsuccessful.
     * @see java.io.InputStream java.awt.image.BufferedImage
     */
    public static BufferedImage loadImage(InputStream is) {
        try {
            return toCompatibleImage(javax.imageio.ImageIO.read(is));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Convenience method to load a BufferedImage from a URL.
     * @return the loaded image, or `null` if unsuccessful.
     * @see java.net.URL java.awt.image.BufferedImage
     */
    public static BufferedImage loadImage(URL uRL) {
        try {
            return toCompatibleImage(javax.imageio.ImageIO.read(uRL));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Load and run an enderX-compatible application.
     * @param file The file pointing to the application to be run.
     */
    public static void runApp(File file) {
        String mainClass = "";
        try {
            ZipInputStream zip = new ZipInputStream(new FileInputStream(file));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().toLowerCase().endsWith(".mf")) {
                    String[] manifest = convertStreamToString(zip).split("\n");
                    for (int i = 0; i < manifest.length; i ++) {
                        String[] line = manifest[i].split(":");
                        if (line[0].equalsIgnoreCase("Main-Class")) {
                            mainClass = line[1].trim();
                        }
                    }
                }
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Main class is: " + mainClass);
        try {
            URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()});
            Thread.currentThread().setContextClassLoader(loader);
            List<String> classes = getJarClasses(file.getPath());
            for (int j = 0; j < classes.size(); j ++) {
                Class c;
                try {
                    c = loader.loadClass(classes.get(j));
                } catch (Exception e) {
                    System.out.println("Exception while loading class!");
                    e.printStackTrace();
                    continue;
                } catch (Error e) {
                    System.out.println("Exception while loading class!");
                    e.printStackTrace();
                    continue;
                }
                //System.out.println("Loaded class " + c.getName());
                if (c.getName().equals(mainClass)) {
                    try {
                        final Method main = c.getDeclaredMethod("main");
                        new Thread(file.getName()) {
                            public void run() {
                                try {
                                    main.invoke(null, new Object[] {});
                                } catch (Exception e) {
                                    System.out.println("No main class found!");
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    } catch (Exception e) {
                        System.out.println("No main class found!");
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    
    private static List<String> getJarClasses(String filename) {
        List<String> classNames = new ArrayList<String>();
        try {
            ZipInputStream zip = new ZipInputStream(new FileInputStream(filename));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    // This ZipEntry represents a class. Now, what class does it represent?
                    String className = entry.getName().replace('/', '.'); // including ".class"
                    classNames.add(className.substring(0, className.length() - ".class".length()));
                }
            }
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        return classNames;
    }
    
    /**
     * Alert the user.
     * @param string The text to show.
     */
    public static void alert(String string) {
        final Window win = new Window("Alert");
        win.setSize(new Dimension(368, 112));
        win.setResizable(false);
        win.setClosable(false);
        win.setRollable(false);
        Label label = new Label(string);
        label.setLocation(4, 4);
        win.add(label);
        Button button = new Button("OK") {
            @Override
            public void actionPerformed() {
                win.close();
            }
        };
        button.setLocation(368 - 69, 112 - 30);
        button.setDefault(true);
        win.add(button);
        win.setVisible(true);
    }
    
    /**
     * Gets the current Desktop.
     * @return the current Desktop.
     * @see xyz.pugduddly.enderX.ui.Desktop
     */
    public static Desktop getDesktop() {
        return component.desktop;
    }
    
    /**
     * Gets the Component that enderX is displayed in.
     * @return the Component that enderX is displayed in.
     * @see java.awt.Component
     */
    public static Component getComponent() {
        return (Component) component;
    }
    
    /**
     * Gets the Container that enderX is displayed in.
     * @return the Container that enderX is displayed in.
     * @see java.awt.Container
     */
    public static Container getContainer() {
        return (Container) frame;
    }
    
    /**
     * Sets the current screen size.
     * @param size The new screen size.
     * @see java.awt.Dimension
     */
    public static void setScreenSize(Dimension size) {
        component.setScreenSize(size);
        WeirdX.width = (short) size.getWidth();
        WeirdX.height = (short) size.getHeight();
        
        Desktop desktop = getDesktop();
        if (desktop != null)
            desktop.setScreenSize(size);
        
        frame.pack();
        
        screenSize = size;
    }
    
    /**
     * Gets the current screen size.
     * @return the current screen size.
     * @see java.awt.Dimension
     */
    public static Dimension getScreenSize() {
        return component.getPreferredSize();
        //return screenSize;
    }
}
