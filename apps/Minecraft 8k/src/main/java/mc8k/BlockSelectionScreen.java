package mc8k;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class BlockSelectionScreen extends Screen {
    public BlockSelectionScreen() {
        super();
    }
    
    public void render(Graphics g, int mouseX, int mouseY) {
        int boxW = 220;
        int boxH = 110;
        int boxX = this.width / 2 - boxW / 2;
        int boxY = this.height / 2 - boxH / 2;
        this.fill(g, boxX, boxY, boxW, boxH, new Color(0, 0, 0, 127));
        this.drawCenteredString(g, "Select block", boxX + boxW / 2, boxY + 8, 0xFFFFFF);
        
        int numBlocks = this.inst.terrain.getWidth() / 16;
        for (int i = 0; i < numBlocks; i ++) {
            int blockX = (int) (boxX + 10 + (i % 9) * 23);
            int blockY = (int) (boxY + 30 + (i / 9) * 27);
            if (mouseX >= blockX - 2 && mouseX < blockX + 16 + 2 &&
                mouseY >= blockY - 2 && mouseY < blockY + 16 + 2) {
                this.inst.buildBlockThumbnail(g, this.inst.terrain, i + 1, blockX - 4, blockY - 4, 0.75f);
                if (this.inst.isMousePressed(0)) {
                    this.inst.setMousePressed(0, false);
                    this.inst.setScreen(null);
                    this.inst.useBlock(i + 1);
                }
            } else {
                this.inst.buildBlockThumbnail(g, this.inst.terrain, i + 1, blockX, blockY, 0.5f);
            }
        }
        
        if (this.inst.isKeyPressed(KeyEvent.VK_B)) {
            this.inst.setKeyPressed(KeyEvent.VK_B, false);
            this.inst.setScreen(null);
        }
        
        if (this.inst.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            this.inst.setKeyPressed(KeyEvent.VK_ESCAPE, false);
            this.inst.setScreen(null);
        }
    }
}
