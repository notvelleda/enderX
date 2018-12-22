package mc8k;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.File;

import xyz.pugduddly.enderX.EnderX;

public class SaveLevelScreen extends Screen {
    public SaveLevelScreen() {
        super();
    }
    
    public void init() {
        this.buttons.add(new Button(1, this.width / 2 - 100, this.height / 4, 98, 20, this.fileExists("slot1.dat") ? "World 1" : "-", this.inst));
        this.buttons.add(new Button(2, this.width / 2 + 2, this.height / 4, 98, 20, this.fileExists("slot2.dat") ? "World 2" : "-", this.inst));
        this.buttons.add(new Button(3, this.width / 2 - 100, this.height / 4 + 24, 98, 20, this.fileExists("slot3.dat") ? "World 3" : "-", this.inst));
        this.buttons.add(new Button(4, this.width / 2 + 2, this.height / 4 + 24, 98, 20, this.fileExists("slot4.dat") ? "World 4" : "-", this.inst));
        this.buttons.add(new Button(5, this.width / 2 - 100, this.height / 4 + 48, 98, 20, this.fileExists("slot5.dat") ? "World 5" : "-", this.inst));
        this.buttons.add(new Button(0, this.width / 2 + 2, this.height / 4 + 48, 98, 20, "Cancel", this.inst));
    }
    
    public void buttonPressed(Button button) {
        try {
            if (button.id == 0) {
                this.inst.setScreen(new PauseScreen());
            }
            if (button.id > 0) {
                this.inst.saveLevel("slot" + button.id + ".dat");
                this.inst.setScreen(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void render(Graphics g, int mouseX, int mouseY) {
        this.fill(g, 0, 0, this.width, this.height, new Color(0, 0, 0, 127));
        this.drawCenteredString(g, "Save level", this.width / 2, this.height / 4 - 18, 16777215);
        this.renderButtons(g, mouseX, mouseY);
        
        if (this.inst.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            this.inst.setKeyPressed(KeyEvent.VK_ESCAPE, false);
            this.inst.setScreen(null);
        }
    }
    
    private boolean fileExists(String string) {
        File file = new File(new File(EnderX.getHomeDirectory(), "Minecraft8k Saves"), string);
        return file.exists() && file.isFile();
    }
}
