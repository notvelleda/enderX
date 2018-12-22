/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.level;

import com.mojang.escape.Art;
import com.mojang.escape.Game;
import com.mojang.escape.entities.BatBossEntity;
import com.mojang.escape.entities.BatEntity;
import com.mojang.escape.entities.BossOgre;
import com.mojang.escape.entities.BoulderEntity;
import com.mojang.escape.entities.Entity;
import com.mojang.escape.entities.EyeBossEntity;
import com.mojang.escape.entities.EyeEntity;
import com.mojang.escape.entities.GhostBossEntity;
import com.mojang.escape.entities.GhostEntity;
import com.mojang.escape.entities.Item;
import com.mojang.escape.entities.OgreEntity;
import com.mojang.escape.entities.Player;
import com.mojang.escape.level.block.AltarBlock;
import com.mojang.escape.level.block.BarsBlock;
import com.mojang.escape.level.block.Block;
import com.mojang.escape.level.block.ChestBlock;
import com.mojang.escape.level.block.DoorBlock;
import com.mojang.escape.level.block.FinalUnlockBlock;
import com.mojang.escape.level.block.IceBlock;
import com.mojang.escape.level.block.LadderBlock;
import com.mojang.escape.level.block.LockedDoorBlock;
import com.mojang.escape.level.block.LootBlock;
import com.mojang.escape.level.block.PitBlock;
import com.mojang.escape.level.block.PressurePlateBlock;
import com.mojang.escape.level.block.SolidBlock;
import com.mojang.escape.level.block.SpiritWallBlock;
import com.mojang.escape.level.block.SwitchBlock;
import com.mojang.escape.level.block.TorchBlock;
import com.mojang.escape.level.block.VanishBlock;
import com.mojang.escape.level.block.WaterBlock;
import com.mojang.escape.level.block.WinBlock;
import com.mojang.escape.menu.GotLootMenu;
import com.mojang.escape.menu.Menu;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

public abstract class Level {
    public Block[] blocks;
    public int width;
    public int height;
    private Block solidWall = new SolidBlock();
    public int xSpawn;
    public int ySpawn;
    protected int wallCol = 11783906;
    protected int floorCol = 10264731;
    protected int ceilCol = 10264731;
    protected int wallTex = 0;
    protected int floorTex = 0;
    protected int ceilTex = 0;
    public List<Entity> entities = new ArrayList<Entity>();
    protected Game game;
    public String name = "";
    public Player player;
    private static Map<String, Level> loaded = new HashMap<String, Level>();

    public void init(Game game, String name, int w, int h, int[] pixels) {
        int col;
        int x;
        this.game = game;
        this.player = game.player;
        this.solidWall.col = Art.getCol(this.wallCol);
        this.solidWall.tex = Art.getCol(this.wallTex);
        this.width = w;
        this.height = h;
        this.blocks = new Block[this.width * this.height];
        int y = 0;
        while (y < h) {
            x = 0;
            while (x < w) {
                col = pixels[x + y * w] & 16777215;
                int id = 255 - (pixels[x + y * w] >> 24 & 255);
                Block block = this.getBlock(x, y, col);
                block.id = id;
                if (block.tex == -1) {
                    block.tex = this.wallTex;
                }
                if (block.floorTex == -1) {
                    block.floorTex = this.floorTex;
                }
                if (block.ceilTex == -1) {
                    block.ceilTex = this.ceilTex;
                }
                if (block.col == -1) {
                    block.col = Art.getCol(this.wallCol);
                }
                if (block.floorCol == -1) {
                    block.floorCol = Art.getCol(this.floorCol);
                }
                if (block.ceilCol == -1) {
                    block.ceilCol = Art.getCol(this.ceilCol);
                }
                this.blocks[x + y * w] = block;
                block.level = this;
                block.x = x++;
                block.y = y;
            }
            ++y;
        }
        y = 0;
        while (y < h) {
            x = 0;
            while (x < w) {
                col = pixels[x + y * w] & 16777215;
                this.decorateBlock(x, y, this.blocks[x + y * w], col);
                ++x;
            }
            ++y;
        }
    }

    public void addEntity(Entity e) {
        this.entities.add(e);
        e.level = this;
        e.updatePos();
    }

    public void removeEntityImmediately(Player player) {
        this.entities.remove(player);
        this.getBlock(player.xTileO, player.zTileO).removeEntity(player);
    }

    protected void decorateBlock(int x, int y, Block block, int col) {
        block.decorate(this, x, y);
        if (col == 16776960) {
            this.xSpawn = x;
            this.ySpawn = y;
        }
        if (col == 11162880) {
            this.addEntity(new BoulderEntity(x, y));
        }
        if (col == 16711680) {
            this.addEntity(new BatEntity(x, y));
        }
        if (col == 16711681) {
            this.addEntity(new BatBossEntity(x, y));
        }
        if (col == 16711682) {
            this.addEntity(new OgreEntity(x, y));
        }
        if (col == 16711683) {
            this.addEntity(new BossOgre(x, y));
        }
        if (col == 16711684) {
            this.addEntity(new EyeEntity(x, y));
        }
        if (col == 16711685) {
            this.addEntity(new EyeBossEntity(x, y));
        }
        if (col == 16711686) {
            this.addEntity(new GhostEntity(x, y));
        }
        if (col == 16711687) {
            this.addEntity(new GhostBossEntity(x, y));
        }
        if (col == 1712392 || col == 16711687) {
            block.floorTex = 7;
            block.ceilTex = 7;
        }
        if (col == 13027014) {
            block.col = Art.getCol(10526880);
        }
        if (col == 13026967) {
            block.col = Art.getCol(10526880);
        }
        if (col == 6633984) {
            block.floorCol = Art.getCol(11888128);
            block.floorTex = 25;
        }
        if (col == 9699227) {
            block.col = Art.getCol(2797363);
            block.tex = 8;
        }
    }

    protected Block getBlock(int x, int y, int col) {
        if (col == 9699227) {
            return new SolidBlock();
        }
        if (col == 37632) {
            return new PitBlock();
        }
        if (col == 16777215) {
            return new SolidBlock();
        }
        if (col == 65535) {
            return new VanishBlock();
        }
        if (col == 16777060) {
            return new ChestBlock();
        }
        if (col == 255) {
            return new WaterBlock();
        }
        if (col == 16726530) {
            return new TorchBlock();
        }
        if (col == 5000268) {
            return new BarsBlock();
        }
        if (col == 16738047) {
            return new LadderBlock(false);
        }
        if (col == 10354846) {
            return new LadderBlock(true);
        }
        if (col == 12697933) {
            return new LootBlock();
        }
        if (col == 13027014) {
            return new DoorBlock();
        }
        if (col == 65447) {
            return new SwitchBlock();
        }
        if (col == 37760) {
            return new PressurePlateBlock();
        }
        if (col == 16711685) {
            return new IceBlock();
        }
        if (col == 4144992) {
            return new IceBlock();
        }
        if (col == 13026967) {
            return new LockedDoorBlock();
        }
        if (col == 16759298) {
            return new AltarBlock();
        }
        if (col == 7639847) {
            return new SpiritWallBlock();
        }
        if (col == 1712392) {
            return new Block();
        }
        if (col == 49831) {
            return new FinalUnlockBlock();
        }
        if (col == 86) {
            return new WinBlock();
        }
        return new Block();
    }

    public Block getBlock(int x, int y) {
        if (x < 0 || y < 0 || x >= this.width || y >= this.height) {
            return this.solidWall;
        }
        return this.blocks[x + y * this.width];
    }

    public static void clear() {
        loaded.clear();
    }

    public static Level loadLevel(Game game, String name) {
        if (loaded.containsKey(name)) {
            return loaded.get(name);
        }
        try {
            BufferedImage img = ImageIO.read(Level.class.getResource("/level/" + name + ".png"));
            int w = img.getWidth();
            int h = img.getHeight();
            int[] pixels = new int[w * h];
            img.getRGB(0, 0, w, h, pixels, 0, w);
            Level level = Level.byName(name);
            level.init(game, name, w, h, pixels);
            loaded.put(name, level);
            return level;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Level byName(String name) {
        try {
            name = String.valueOf(name.substring(0, 1).toUpperCase()) + name.substring(1);
            return (Level)Class.forName("com.mojang.escape.level." + name + "Level").newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean containsBlockingEntity(double x0, double y0, double x1, double y1) {
        int xc = (int)Math.floor((x1 + x0) / 2.0);
        int zc = (int)Math.floor((y1 + y0) / 2.0);
        int rr = 2;
        int z = zc - rr;
        while (z <= zc + rr) {
            int x = xc - rr;
            while (x <= xc + rr) {
                List<Entity> es = this.getBlock((int)x, (int)z).entities;
                int i = 0;
                while (i < es.size()) {
                    Entity e = es.get(i);
                    if (e.isInside(x0, y0, x1, y1)) {
                        return true;
                    }
                    ++i;
                }
                ++x;
            }
            ++z;
        }
        return false;
    }

    public boolean containsBlockingNonFlyingEntity(double x0, double y0, double x1, double y1) {
        int xc = (int)Math.floor((x1 + x0) / 2.0);
        int zc = (int)Math.floor((y1 + y0) / 2.0);
        int rr = 2;
        int z = zc - rr;
        while (z <= zc + rr) {
            int x = xc - rr;
            while (x <= xc + rr) {
                List<Entity> es = this.getBlock((int)x, (int)z).entities;
                int i = 0;
                while (i < es.size()) {
                    Entity e = es.get(i);
                    if (!e.flying && e.isInside(x0, y0, x1, y1)) {
                        return true;
                    }
                    ++i;
                }
                ++x;
            }
            ++z;
        }
        return false;
    }

    public void tick() {
        int i = 0;
        while (i < this.entities.size()) {
            Entity e = this.entities.get(i);
            e.tick();
            e.updatePos();
            if (e.isRemoved()) {
                this.entities.remove(i--);
            }
            ++i;
        }
        int y = 0;
        while (y < this.height) {
            int x = 0;
            while (x < this.width) {
                this.blocks[x + y * this.width].tick();
                ++x;
            }
            ++y;
        }
    }

    public void trigger(int id, boolean pressed) {
        int y = 0;
        while (y < this.height) {
            int x = 0;
            while (x < this.width) {
                Block b = this.blocks[x + y * this.width];
                if (b.id == id) {
                    b.trigger(pressed);
                }
                ++x;
            }
            ++y;
        }
    }

    public void switchLevel(int id) {
    }

    public void findSpawn(int id) {
        int y = 0;
        while (y < this.height) {
            int x = 0;
            while (x < this.width) {
                Block b = this.blocks[x + y * this.width];
                if (b.id == id && b instanceof LadderBlock) {
                    this.xSpawn = x;
                    this.ySpawn = y;
                }
                ++x;
            }
            ++y;
        }
    }

    public void getLoot(int id) {
        if (id == 20) {
            this.game.getLoot(Item.pistol);
        }
        if (id == 21) {
            this.game.getLoot(Item.potion);
        }
    }

    public void win() {
        this.game.win(this.player);
    }

    public void lose() {
        this.game.lose(this.player);
    }

    public void showLootScreen(Item item) {
        this.game.setMenu(new GotLootMenu(item));
    }
}

