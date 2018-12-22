package mc8k;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Button {
    public int x;
    public int y;
    public int width;
    public int height;
    public String msg;
    public int id;
    public BufferedImage gui;
    public Font font;
    public MC inst;
    public boolean disabled = false;

    public Button(int n, int n2, int n3, int n4, int n5, String string, MC mc2) {
        this.id = n;
        this.x = n2;
        this.y = n3;
        this.width = n4;
        this.height = n5;
        this.msg = string;
        this.inst = mc2;
        this.font = this.inst.font;
        this.gui = this.inst.gui;
    }

    public Button(int n, int n2, int n3, int n4, int n5, String string, MC mc2, boolean bl) {
        this(n, n2, n3, n4, n5, string, mc2);
        this.disabled = bl;
    }

    public void render(Graphics g, int n, int n2) {
        int yOfs = 0;
        if (!this.disabled) yOfs += 20;
        if (this.isPressed(n, n2)) yOfs += 20;
        
        g.drawImage(this.gui.getSubimage(0, 46 + yOfs, this.width - 2, 20), this.x, this.y, this.width - 2, this.height, null);
        g.drawImage(this.gui.getSubimage(198, 46 + yOfs, 2, 20), this.x + this.width - 2, this.y, 2, this.height, null);
        
        int color = 14737632;
        if (this.isPressed(n, n2)) color = 16777120;
        if (this.disabled) color = 8421504;
        this.font.drawShadow(g, this.msg, this.x + this.width / 2 - this.font.width(this.msg) / 2, this.y + 6, color);
    }

    public boolean isPressed(int n, int n2) {
        return n >= this.x && n2 >= this.y && n < this.x + this.width && n2 < this.y + this.height && !this.disabled;
    }
}
