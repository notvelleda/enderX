package mc8k;

import java.awt.AlphaComposite;
import java.awt.Event;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Random;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import xyz.pugduddly.enderX.EnderX;
import xyz.pugduddly.enderX.ui.Window;
import xyz.pugduddly.enderX.ui.AcceleratedWindow;
import xyz.pugduddly.enderX.ui.Menu;
import xyz.pugduddly.enderX.ui.MenuItem;

public class MC implements Runnable {
    private int[] M = new int[32767];
    private Window win;
    private int mouseX;
    private int mouseY;
    private boolean created = true;
    public Font font = new Font("/default.png");
    public static float scale = 1f;
    //private static double rotSize = 0;
    private static HashMap<Float, Double> rotSizes = new HashMap<Float, Double>();
    private Timer timer = new Timer(20.0f);
    private int frames = 0;
    private int fps = 0;
    private long lastFpsTime = System.currentTimeMillis();
    public Level level = new Level();
    public Player player;
    public BufferedImage gui = EnderX.loadImage(MC.class.getResource("/gui.png"));
    public Screen screen = null;
    public Inventory inventory = new Inventory(9).populate();
    public BufferedImage terrain;
    public int blocksel = 0;
    public int[] textures;
    public Options options = new Options();
    public LevelIO levelIo = new LevelIO();
    
    //String[] blocknames = {"", "Grass", "Dirt", "", "Stone", "Brick", "", "Log", "Leaves", "", "", "", "", "", "", "", ""};

    public static void main() {
        final MC m = new MC();
        m.win = new Window("Minecraft 8k") {
            @Override
            public void keyPressed(KeyEvent e) {
                m.M[e.getKeyCode()] = 1;
                m.player.setKey(e.getKeyCode(), true);
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                m.M[e.getKeyCode()] = 0;
                m.player.setKey(e.getKeyCode(), false);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                if ((e.getModifiers() & 4) > 0)
                    m.M[1] = 1;
                else
                    m.M[0] = 1;
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if ((e.getModifiers() & 4) > 0)
                    m.M[1] = 0;
                else
                    m.M[0] = 0;
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                m.mouseX = e.getX();
                m.mouseY = e.getY();
            }
        };
        m.win.setResizable(false);
        m.win.setSize(new Dimension((int) (428 * scale), (int) (240 * scale)));
        Menu e = new Menu("File");
        e.addItem(new MenuItem("Quit") {
            @Override
            public void actionPerformed() {
                m.destroy();
            }
        });
        m.win.getMenus().add(e);
        m.win.setVisible(true);
        m.run();
    }
    
    public void destroy() {
        this.win.close();
        this.created = false;
    }
    
    public Graphics getGraphics() {
        return this.win.getGraphics();
    }
    
    public boolean isKeyPressed(int key) {
        if (key < 4) return false;
        return this.M[key] == 1;
    }
    
    public void setKeyPressed(int key, boolean value) {
        if (key < 4) return;
        this.M[key] = value ? 1 : 0;
    }
    
    public boolean isMousePressed(int button) {
        if (button > 1) return false;
        return this.M[button] == 1;
    }
    
    public void setMousePressed(int button, boolean value) {
        if (button > 1) return;
        this.M[button] = value ? 1 : 0;
    }
    
    public void buildBlockThumbnail(Graphics g, BufferedImage tileset, int blocksel, int xPos, int yPos, float scale) {
        float w = scale;
        float h = scale;
        float d = scale;
        int topTex = blocksel - 1;
        int frontTex = 16 + blocksel - 1;
        int rightTex = 16 + blocksel - 1;
        double rotSize = 0;
        
        if (rotSizes.get(scale) == null) {
            double a = 0;
            int t = (int) (w * 16 + d * 16);
            while (a < t) {
                a = Math.abs(rotSize * Math.sin(45)) + Math.abs(rotSize * Math.cos(45));
                rotSize += 0.0001;
            }
            rotSizes.put(scale, rotSize);
        } else {
            rotSize = rotSizes.get(scale);
        }
        
        BufferedImage top = new BufferedImage((int) rotSize, (int) rotSize, BufferedImage.TYPE_INT_ARGB);
        top.setAccelerationPriority(1);
        BufferedImage front = new BufferedImage((int) (w * 32), (int) (h * 16), BufferedImage.TYPE_INT_ARGB);
        front.setAccelerationPriority(1);
        BufferedImage right = new BufferedImage((int) (d * 32), (int) (h * 16), BufferedImage.TYPE_INT_ARGB);
        right.setAccelerationPriority(1);
        
        Graphics2D topg2d = top.createGraphics();
        topg2d.drawImage(tileset.getSubimage((topTex % 16) * 16, (topTex / 16) * 16, 16, 16), 0, 0, (int) rotSize, (int) rotSize, null);
        topg2d.dispose();
        
        Graphics2D frontg2d = front.createGraphics();
        frontg2d.drawImage(tileset.getSubimage((frontTex % 16) * 16, (frontTex / 16) * 16, 16, 16), 0, 0, (int) (w * 32), (int) (h * 16), null);
        frontg2d.setPaint(new Color(0, 0, 0));
        frontg2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        frontg2d.fillRect(0, 0, front.getWidth(), front.getHeight());
        frontg2d.dispose();
        
        Graphics2D rightg2d = right.createGraphics();
        rightg2d.drawImage(tileset.getSubimage((rightTex % 16) * 16, (rightTex / 16) * 16, 16, 16), 0, 0, (int) (d * 32), (int) (h * 16), null);
        rightg2d.setPaint(new Color(0, 0, 0));
        rightg2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f));
        rightg2d.fillRect(0, 0, right.getWidth(), right.getHeight());
        rightg2d.dispose();
        
        AffineTransform at = new AffineTransform();
        
        at.rotate(45.0 * Math.PI / 180.0, top.getWidth() / 2, top.getHeight() / 2);

        AffineTransform translationTransform;
        translationTransform = findTranslation(at, top);
        at.preConcatenate(translationTransform);

        BufferedImageOp bio = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        BufferedImage topTransformed = bio.filter(top, null);
        topTransformed = scale(topTransformed, (int) (w * 16 + d * 16) + 1, topTransformed.getHeight() / 2);
        
        front = scale(front, (int) (w * 16), front.getHeight());
            
        at = AffineTransform.getShearInstance(0, 0.5);
        BufferedImage frontTransformed = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR).filter(front, null);
        
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-right.getWidth(), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        right = scale(op.filter(right, null), (int) (d * 16), right.getHeight());
            
        at = AffineTransform.getShearInstance(0, 0.5);
        BufferedImage bi = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR).filter(right, null);
        tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-bi.getWidth(null), 0);
        op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        bi = op.filter(bi, null);
        BufferedImage rightTransformed = bi;
        
        g.drawImage(topTransformed, xPos, yPos, null);
        g.drawImage(frontTransformed, xPos, yPos + topTransformed.getHeight() / 2 + (scale <= 0.5 ? -1 : 0), null);
        g.drawImage(rightTransformed, xPos + (int) (w * 16 + d * 16) / 2, yPos + topTransformed.getHeight() / 2 + (scale <= 0.5 ? -1 : 0), null);
    }
    
    public static BufferedImage scale(BufferedImage imageToScale, int dWidth, int dHeight) {
        BufferedImage scaledImage = null;
        if (imageToScale != null) {
            scaledImage = new BufferedImage(dWidth, dHeight, imageToScale.getType());
            Graphics2D graphics2D = scaledImage.createGraphics();
            graphics2D.drawImage(imageToScale, 0, 0, dWidth, dHeight, null);
            graphics2D.dispose();
        }
        return scaledImage;
    }
    
    private AffineTransform findTranslation(AffineTransform at, BufferedImage bi) {
        Point2D p2din, p2dout;

        p2din = new Point2D.Double(0.0, 0.0);
        p2dout = at.transform(p2din, null);
        double ytrans = p2dout.getY();

        p2din = new Point2D.Double(0, bi.getHeight());
        p2dout = at.transform(p2din, null);
        double xtrans = p2dout.getX();

        AffineTransform tat = new AffineTransform();
        tat.translate(-xtrans, -ytrans);
        return tat;
    }
    
    public void setScreen(Screen screen) {
        this.screen = screen;
        if (screen != null) {
            float scale = this.options.getScreenSize();
            screen.init(this, (int) (214 * scale), (int) (120 * scale));
        }
    }
    
    public void useBlock(int block) {
        if (this.inventory.contains(block)) {
            this.blocksel = this.inventory.indexOf(block);
        } else {
            this.inventory.push(block);
            this.blocksel = 0;
        }
    }
    
    public void loadTextures(File file) {
        try {
            if (file != null && file.exists())
                this.terrain = EnderX.loadImage(new FileInputStream(file));
            else
                this.terrain = EnderX.loadImage(getClass().getResource("/terrain.png"));
            
            this.textures = new int[(this.terrain.getWidth() + 16) * this.terrain.getHeight()];
            
            int x = 0;
            int y = 0;
            int xOfs = 0;
            for (int i = 0; i < this.textures.length; i ++) {
                int block = i / (16 * 16) / 3;
                if (block == 0) continue; // Is block air?
                
                if (x + xOfs >= this.terrain.getWidth()) break;
                if (y >= this.terrain.getHeight()) break;
                
                this.textures[i] = this.terrain.getRGB(x + xOfs, y);
                
                x ++;
                if (x >= 16) {
                    x = 0;
                    y ++;
                }
                if (y >= this.terrain.getHeight()) {
                    y = 0;
                    xOfs += 16;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void newLevel() {
        this.level = LevelGen.genLevel();
        this.level.spawnX = this.level.width / 2;
        this.level.spawnY = 65;
        this.level.spawnZ = this.level.depth / 2;
        this.player = new Player(this.level);
        this.player.resetPos();
    }

    public void run() {
        try {
            BufferedImage img = new BufferedImage((int) (214 * scale), (int) (120 * scale), 1);
            img.setAccelerationPriority(1);
            int[] pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
            
            this.newLevel();
            
            this.loadTextures(new File(new File(EnderX.getHomeDirectory(), "Minecraft8k Saves"), "terrain.png"));
            
            float xpos = 96.5f;
            float ypos = 65.0f;
            float zpos = 96.5f;
            float f4 = 0.0f;
            float f5 = 0.0f;
            float f6 = 0.0f;
            long l = System.currentTimeMillis();
            int i4 = -1;
            int i5 = 0;
            float f7 = 0.0f;
            float f8 = 0.0f;
            double renderDistance = 20.0;
            while (this.created) {
                float f9 = (float) Math.sin(f7);
                float f10 = (float) Math.cos(f7);
                float f11 = (float) Math.sin(f8);
                float f12 = (float) Math.cos(f8);
                float f13 = 0.0f;
                float f14 = 0.0f;
                int i6 = 0;
                int i7 = 0;
                int i11;
                int i8 = -1;
                for (int i9 = 0; i9 < 214 * scale; i9++) {
                    float f18 = (i9 - 107 * scale) / 90.0f;
                    for (i11 = 0; i11 < 120 * scale; i11++) {
                        float f20 = (i11 - 60 * scale) / 90.0f;
                        float f21 = 1.0f;
                        float f22 = f21 * f12 + f20 * f11;
                        float f23 = f20 * f12 - f21 * f11;
                        float f24 = f18 * f10 + f22 * f9;
                        float f25 = f22 * f10 - f18 * f9;
                        int i16 = 0;
                        int i17 = 255;
                        double d = renderDistance;
                        float f26 = 5.0f;
                        for (int i18 = 0; i18 < 3; i18++) {
                            float f27 = f24;
                            if (i18 == 1) {
                                f27 = f23;
                            }
                            if (i18 == 2) {
                                f27 = f25;
                            }
                            float f28 = 1.0f / (f27 < 0.0f ? -f27 : f27);
                            float f29 = f24 * f28;
                            float f30 = f23 * f28;
                            float f31 = f25 * f28;
                            float f32 = xpos - (int) xpos;
                            if (i18 == 1) {
                                f32 = ypos - (int) ypos;
                            }
                            if (i18 == 2) {
                                f32 = zpos - (int) zpos;
                            }
                            if (f27 > 0.0f) {
                                f32 = 1.0f - f32;
                            }
                            float f33 = f28 * f32;
                            float f34 = xpos + f29 * f32;
                            float f35 = ypos + f30 * f32;
                            float f36 = zpos + f31 * f32;
                            if (f27 < 0.0f) {
                                if (i18 == 0) {
                                    f34 -= 1.0f;
                                }
                                if (i18 == 1) {
                                    f35 -= 1.0f;
                                }
                                if (i18 == 2) {
                                    f36 -= 1.0f;
                                }
                            }
                            while (f33 < d) {
                                int i21 = (int) f34 - 64;
                                int i22 = (int) f35 - 64;
                                int i23 = (int) f36 - 64;
                                if ((i21 < 0) || (i22 < 0) || (i23 < 0) || (i21 >= 64) || (i22 >= 64) || (i23 >= 64)) {
                                    break;
                                }
                                int i24 = i21 + i22 * 64 + i23 * 4096;
                                int i25 = this.level.blocks[i24];
                                if (i25 > 0) {
                                    i6 = (int)((f34 + f36) * 16.0f) & 0xF;
                                    i7 = ((int)(f35 * 16.0f) & 0xF) + 16;
                                    if (i18 == 1) {
                                        i6 = (int)(f34 * 16.0f) & 0xF;
                                        i7 = (int)(f36 * 16.0f) & 0xF;
                                        if (f30 < 0.0f) {
                                            i7 += 32;
                                        }
                                    }
                                    int i26 = 0xFFFFFFFF;
                                    try {
                                        if ((i24 != i4) || ((i6 > 0) && (i7 % 16 > 0) && (i6 < 15) && (i7 % 16 < 15))) {
                                            i26 = this.textures[(i6 + i7 * 16 + i25 * 256 * 3)];
                                        }
                                    } catch (Exception e) {}
                                    if ((f33 < f26) && (i9 == M[2] / 4) && (i11 == M[3] / 4)) {
                                        i8 = i24;
                                        i5 = 1;
                                        if (f27 > 0.0f) {
                                            i5 = -1;
                                        }
                                        i5 <<= 6 * i18;
                                        f26 = f33;
                                    }
                                    if ((i26 & 0xFF000000) != 0) {
                                        i16 = i26 & 0xFFFFFF;
                                        i17 = 255 - (int)(f33 / renderDistance * 255.0f);
                                        i17 = i17 * (255 - (i18 + 2) % 3 * 50) / 255;
                                        d = f33;
                                    }
                                }
                                //if (!this.level.isLit(i21, i22, i23)) i17 /= 2;
                                f34 += f29;
                                f35 += f30;
                                f36 += f31;
                                f33 += f28;
                            }
                        }
                        int i18 = (i16 >> 16 & 0xFF) * i17 / 255;
                        int i19 = (i16 >> 8 & 0xFF) * i17 / 255;
                        int i20 = (i16 & 0xFF) * i17 / 255;
                        pixels[(int)(i9 + i11 * 214 * scale)] = (i18 << 16 | i19 << 8 | i20);
                    }
                }
                i4 = i8;

                //Graphics g = buffer.getGraphics();
                Graphics g = this.getGraphics();
                Graphics g2 = img.getGraphics();

                // Render HUD
                g2.drawImage(this.gui.getSubimage(0, 0, 182, 22), (int) ((214 * scale) / 2 - 182 / 2), (int) (120 * scale - 22), null);
                for (int i = 0; i < this.inventory.size; i++) {
                    this.buildBlockThumbnail(g2, this.terrain, this.inventory.get(i), (int) ((214 * scale) / 2 - 182 / 2) + i * 20 + 3, (int) (120 * scale - 18), 0.5f);
                }
                g2.drawImage(this.gui.getSubimage(0, 22, 24, 22), (int) ((214 * scale) / 2 - 182 / 2) + this.blocksel * 20 - 1, (int) (120 * scale - 23), null);
                
                if (this.player.flying)
                    this.font.drawShadow(g2, "Flying", 2, (int) (120 * scale - 10), 0xFFFFFF);

                if (this.screen != null)
                    this.screen.render(g2, this.M[2] / 4, this.M[3] / 4);

                // Render debug info
                this.font.drawShadow(g2, this.fps + " FPS", 2, 2, 0xFFFFFF);
                /*this.font.drawShadow(g2, this.player.x + ", " + this.player.y + ", " + this.player.z, 2, 12, 0xFFFFFF);
                int lookingAtX = i4 % 64;
                int lookingAtY = (i4 / 64) % 64;
                int lookingAtZ = i4 / (64 * 64);
                this.font.drawShadow(g2, "Looking at: " + lookingAtX + ", " + lookingAtY + ", " + lookingAtZ, 2, 22, 0xFFFFFF);*/

                //g2.dispose();
                g.drawImage(img, 0, 0, (int) (428 * scale), (int) (240 * scale), null);

                for (int i = 0; i < this.timer.ticks; ++i) {
                    if (this.screen == null) {
                        if ((M[0] > 0) && (i4 > 0)) {
                            int lX = i4 % 64;
                            int lY = (i4 / 64) % 64;
                            int lZ = i4 / (64 * 64);
                            this.level.setTile(lX, 64 - (lY + 1), lZ, 0);
                            M[0] = 0;
                        }
                        
                        if ((M[1] > 0) && (i4 > 0)) {
                            int lX = (i4 + i5) % 64;
                            int lY = ((i4 + i5) / 64) % 64;
                            int lZ = (i4 + i5) / (64 * 64);
                            this.level.setTile(lX, 64 - (lY + 1), lZ, this.inventory.get(this.blocksel));
                            M[1] = 0;
                        }
                        
                        if (M[2] > 0) {
                            f13 = (M[2] - 428 * scale) / (214.0f * scale) * 2.0f;
                            f14 = (M[3] - 240 * scale) / (120.0f * scale) * 2.0f;
                            float f15 = (float) Math.sqrt(f13 * f13 + f14 * f14) - 1.2f;
                            if (f15 < 0.0f) {
                                f15 = 0.0f;
                            }
                            if (f15 > 0.0f) {
                                f7 += f13 * f15 / 85.0f;
                                f8 -= f14 * f15 / 85.0f;
                                if (f8 < -1.57f) {
                                    f8 = -1.57f;
                                }
                                if (f8 > 1.57f) {
                                    f8 = 1.57f;
                                }
                            }
                        }
                        
                        this.player.xRot = this.convRotation(f7);
                        this.player.yRot = this.convRotation(f8);

                        this.player.tick();

                        if (this.player.x < 0) this.player.x = 0;
                        if (this.player.x > 64) this.player.x = 64;
                        if (this.player.y < 0) this.player.y = 0;
                        if (this.player.y > 64) this.player.y = 64;
                        if (this.player.z < 0) this.player.z = 0;
                        if (this.player.z > 64) this.player.z = 64;

                        xpos = this.player.x + 64;
                        ypos = (64 - this.player.y) + 64;
                        zpos = this.player.z + 64;

                        if (this.M[KeyEvent.VK_R] == 1) {
                            this.M[KeyEvent.VK_R] = 0;
                            this.player.resetPos();
                        }

                        if (this.M[KeyEvent.VK_B] == 1) {
                            this.M[KeyEvent.VK_B] = 0;
                            this.setScreen(new BlockSelectionScreen());
                        }
                        
                        if (this.M[KeyEvent.VK_ESCAPE] == 1) {
                            this.M[KeyEvent.VK_ESCAPE] = 0;
                            this.setScreen(new PauseScreen());
                        }

                        if (this.M[KeyEvent.VK_1] == 1) {
                            this.M[KeyEvent.VK_1] = 0;
                            this.blocksel = 0;
                        } else if (this.M[KeyEvent.VK_2] == 1) {
                            this.M[KeyEvent.VK_2] = 0;
                            this.blocksel = 1;
                        } else if (this.M[KeyEvent.VK_3] == 1) {
                            this.M[KeyEvent.VK_3] = 0;
                            this.blocksel = 2;
                        } else if (this.M[KeyEvent.VK_4] == 1) {
                            this.M[KeyEvent.VK_4] = 0;
                            this.blocksel = 3;
                        } else if (this.M[KeyEvent.VK_5] == 1) {
                            this.M[KeyEvent.VK_5] = 0;
                            this.blocksel = 4;
                        } else if (this.M[KeyEvent.VK_6] == 1) {
                            this.M[KeyEvent.VK_6] = 0;
                            this.blocksel = 5;
                        } else if (this.M[KeyEvent.VK_7] == 1) {
                            this.M[KeyEvent.VK_7] = 0;
                            this.blocksel = 6;
                        } else if (this.M[KeyEvent.VK_8] == 1) {
                            this.M[KeyEvent.VK_8] = 0;
                            this.blocksel = 7;
                        } else if (this.M[KeyEvent.VK_9] == 1) {
                            this.M[KeyEvent.VK_9] = 0;
                            this.blocksel = 8;
                        }
                    } else {
                        this.screen.tick();
                    }
                }
                
                float mouseScale = ((float) this.win.getSize().getWidth()) / ((float) img.getWidth());
                this.M[2] = (int) (this.mouseX * mouseScale);
                this.M[3] = (int) (this.mouseY * mouseScale);
                
                float oldScale = this.scale;
                this.scale = this.options.getScreenSize();
                if (oldScale != this.scale) {
                    this.win.setSize(new Dimension((int) (428 * this.scale), (int) (240 * this.scale)));
                    img = new BufferedImage((int) (214 * this.scale), (int) (120 * this.scale), 1);
                    pixels = ((DataBufferInt)img.getRaster().getDataBuffer()).getData();
                }
                
                renderDistance = this.options.getRenderDistance();

                this.frames ++;

                while (System.currentTimeMillis() >= this.lastFpsTime + 1000) {
                    this.lastFpsTime += 1000;
                    this.fps = frames;
                    this.frames = 0;
                }

                if (this.win.destroyed())
                    this.destroy();

                this.timer.advanceTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private float convRotation(float rads) {
        float degrees = (float) Math.toDegrees(rads) % 360;
        while (degrees < 0) degrees += 360;
        degrees = (360 - degrees) + 180;
        return degrees % 360;
    }
    
    public boolean switchLevel(String string) {
        File dir = new File(EnderX.getHomeDirectory(), "Minecraft8k Saves");
        dir.mkdirs();
        File file = new File(dir, string);
        boolean bl = false;
        String string2 = this.level.name;
        this.level.name = string;
        try {
            bl = this.levelIo.load(this.level, new FileInputStream(file));
            this.player.resetPos();
        }
        catch (Exception var4_4) {
            bl = false;
        }
        if (!bl) {
            this.level.name = string2;
        }
        return bl;
    }
    
    public void saveLevel(String string) throws FileNotFoundException {
        File dir = new File(EnderX.getHomeDirectory(), "Minecraft8k Saves");
        dir.mkdirs();
        File file = new File(dir, string);
        this.level.name = string;
        this.level.spawnX = (int) this.player.x;
        this.level.spawnY = (int) this.player.y;
        this.level.spawnZ = (int) this.player.z;
        this.levelIo.save(this.level, new FileOutputStream(file));
    }
}
