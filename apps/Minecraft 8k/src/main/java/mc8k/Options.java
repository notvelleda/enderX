package mc8k;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import xyz.pugduddly.enderX.EnderX;

public class Options {
    private static final String[] sizeNames = new String[] {"SMALL", "NORMAL", "LARGE"};
    private static final String[] renderDistanceNames = new String[] {"TINY", "SHORT", "NORMAL", "FAR"};
    public int screenSize = 1;
    public int renderDistance = 2;
    public int numOptions = 2;
    public File file;
    
    public Options() {
        File dir = new File(EnderX.getHomeDirectory(), "Minecraft8k Saves");
        dir.mkdirs();
        this.file = new File(dir, "options.txt");
        this.load();
    }
    
    public float getScreenSize() {
        if (this.screenSize == 1) return 1f;
        if (this.screenSize == 2) return 1.5f;
        if (this.screenSize == 3) return 2f;
        return 1f;
    }
    
    public double getRenderDistance() {
        if (this.renderDistance == 1) return 10.0;
        return this.renderDistance * 20;
    }
    
    public String getOption(int option) {
        if (option == 0) {
            return "Screen size: " + sizeNames[this.screenSize - 1];
        }
        if (option == 1) {
            return "Render distance: " + renderDistanceNames[this.renderDistance - 1];
        }
        return "";
    }
    
    public void load() {
        try {
            if (!this.file.exists()) {
                return;
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(this.file));
            String string = "";
            while ((string = bufferedReader.readLine()) != null) {
                String[] arrstring = string.split(":");
                if (arrstring[0].equals("screenSize")) {
                    this.screenSize = Integer.parseInt(arrstring[1]);
                }
                if (arrstring[0].equals("renderDistance")) {
                    this.renderDistance = Integer.parseInt(arrstring[1]);
                }
            }
            bufferedReader.close();
        }
        catch (Exception exception) {
            System.out.println("Failed to load options");
            exception.printStackTrace();
        }
    }
    
    public void save() {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(this.file));
            printWriter.println("screenSize:" + this.screenSize);
            printWriter.println("renderDistance:" + this.renderDistance);
            printWriter.close();
        }
        catch (Exception exception) {
            System.out.println("Failed to save options");
            exception.printStackTrace();
        }
    }
}
