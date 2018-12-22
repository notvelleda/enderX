package mc8k;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class PauseScreen extends Screen {
    public PauseScreen() {
        super();
    }
    
    public void init() {
        this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4, 98, 20, "Options...", this.inst));
        this.buttons.add(new Button(1, this.width / 2 + 2, this.height / 4, 98, 20, "Generate level...", this.inst));
        this.buttons.add(new Button(2, this.width / 2 - 100, this.height / 4 + 24, 98, 20, "Save level...", this.inst));
        this.buttons.add(new Button(3, this.width / 2 + 2, this.height / 4 + 24, 98, 20, "Load level...", this.inst));
        this.buttons.add(new Button(4, this.width / 2 - 100, this.height / 4 + 48, 200, 20, "Back to game", this.inst));
    }
    
    public void buttonPressed(Button button) {
        if (button.id == 0) {
            this.inst.setScreen(new OptionsScreen(this, this.inst.options));
        }
        if (button.id == 1) {
            this.inst.setScreen(null);
            this.inst.newLevel();
        }
        if (button.id == 2) {
            this.inst.setScreen(new SaveLevelScreen());
        }
        if (button.id == 3) {
            this.inst.setScreen(new LoadLevelScreen());
        }
        if (button.id == 4) {
            this.inst.setScreen(null);
        }
    }
    
    public void render(Graphics g, int mouseX, int mouseY) {
        this.fill(g, 0, 0, this.width, this.height, new Color(0, 0, 0, 127));
        this.drawCenteredString(g, "Game menu", this.width / 2, this.height / 4 - 18, 16777215);
        this.renderButtons(g, mouseX, mouseY);
        
        if (this.inst.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            this.inst.setKeyPressed(KeyEvent.VK_ESCAPE, false);
            this.inst.setScreen(null);
        }
    }
}
