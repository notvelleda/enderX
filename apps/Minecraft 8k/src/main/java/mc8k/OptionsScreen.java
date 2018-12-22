package mc8k;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class OptionsScreen extends Screen {
    private Screen parent;
    private Options options;
    
    public OptionsScreen(Screen parent, Options options) {
        super();
        this.parent = parent;
        this.options = options;
    }
    
    public void init() {
        this.buttons = new ArrayList<Button>();
        for (int i = 0; i < this.options.numOptions; i ++) {
            this.buttons.add(new Button(i + 1, this.width / 2 - 100, this.height / 4 + i * 24, 200, 20, this.options.getOption(i), this.inst));
        }
        this.buttons.add(new Button(0, this.width / 2 - 100, this.height / 4 + this.options.numOptions * 24, 200, 20, "Back", this.inst));
    }
    
    public void buttonPressed(Button button) {
        if (button.id == 0) {
            this.options.save();
            this.inst.setScreen(this.parent);
        }
        if (button.id == 1) {
            this.options.screenSize ++;
            if (this.options.screenSize < 1) this.options.screenSize = 3;
            if (this.options.screenSize > 3) this.options.screenSize = 1;
        }
        if (button.id == 2) {
            this.options.renderDistance ++;
            if (this.options.renderDistance < 1) this.options.renderDistance = 4;
            if (this.options.renderDistance > 4) this.options.renderDistance = 1;
        }
        if (button.id > 0) this.inst.setScreen(this);
    }
    
    public void render(Graphics g, int mouseX, int mouseY) {
        this.fill(g, 0, 0, this.width, this.height, new Color(0, 0, 0, 127));
        this.drawCenteredString(g, "Options", this.width / 2, this.height / 4 - 18, 16777215);
        this.renderButtons(g, mouseX, mouseY);
        
        if (this.inst.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            this.inst.setKeyPressed(KeyEvent.VK_ESCAPE, false);
            this.inst.setScreen(null);
        }
    }
}
