package mc8k;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Screen {
    protected MC inst;
    public int width;
    public int height;
    public ArrayList<Button> buttons = new ArrayList<Button>();
    private boolean lastMouseState = false;

    public void render(Graphics g, int n, int n2) {
    }

    public void init(MC mc2, int n, int n2) {
        this.inst = mc2;
        this.width = n;
        this.height = n2;
        this.buttons = new ArrayList<Button>();
        this.init();
    }

    public void init() {
    }
    
    public void fill(Graphics g, int x, int y, int w, int h, Color color) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.fillRect(x, y, w, h);
    }
    
    public void fill(Graphics g, int x, int y, int w, int h, int color) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(color));
        g2d.fillRect(x, y, w, h);
    }

    public void drawCenteredString(Graphics g, String string, int n, int n2, int n3) {
        Font font = this.inst.font;
        font.drawShadow(g, string, n - font.width(string) / 2, n2, n3);
    }

    public void drawString(Graphics g, String string, int n, int n2, int n3) {
        Font font = this.inst.font;
        font.drawShadow(g, string, n, n2, n3);
    }

    public void tick() {
    }
    
    public void renderButtons(Graphics g, int mouseX, int mouseY) {
        boolean thisMouseState = this.inst.isMousePressed(0);
        for (Button button : this.buttons) {
            button.render(g, mouseX, mouseY);
            if (!thisMouseState && lastMouseState && button.isPressed(mouseX, mouseY)) {
                this.buttonPressed(button);
            }
        }
        this.lastMouseState = thisMouseState;
    }
    
    public void buttonPressed(Button button) {}
}

