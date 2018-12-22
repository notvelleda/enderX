package mc8k;

import java.util.ArrayList;
import java.util.Random;

public class LevelGen {
    private static void setTile(int[] blocks, int x, int y, int z, int id) {
        if (x < 0 || y < 0 || z < 0 || x >= 64 || y >= 64 || z >= 64)
            return;
        blocks[x + (64 - (y + 1)) * 64 + z * 4096] = id;
    }
    
    private static int getTile(int[] blocks, int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= 64 || y >= 64 || z >= 64)
            return 0;
        return blocks[x + (64 - (y + 1)) * 64 + z * 4096];
    }
    
    /*private static void placeTree(int[] blocks, int x, int y, int z) {
        for (int ty = y; ty < y + 7; ty++) {
            setTile(blocks, x, ty, z, 7);
        }
        
        for (int ty = y + 3; ty < y + 8; ty++) {
            for (int ox = -3; ox <= 3; ox++) {
                for (int oz = -3; oz <= 3; oz++) {
                    int d = (ox * ox) + (oz * oz) +
                        (ty - (y + 4)) * (ty - (y + 4));
                    if (d < 11) {
                        setTile(blocks, x + ox, ty, z + oz, 8);
                    }
                }
            }
        }
    }*/
    
    public static boolean placeTree(int[] blocks, int n, int n2, int n3, Random random) {
        int n4;
        int n5;
        int n6;
        int n7;
        int n8 = random.nextInt(3) + 4;
        boolean bl = true;
        for (n7 = n2; n7 <= n2 + 1 + n8; ++n7) {
            n4 = 1;
            if (n7 == n2) {
                n4 = 0;
            }
            if (n7 >= n2 + 1 + n8 - 2) {
                n4 = 2;
            }
            for (n6 = n - n4; n6 <= n + n4 && bl; ++n6) {
                for (n5 = n3 - n4; n5 <= n3 + n4 && bl; ++n5) {
                    if (getTile(blocks, n6, n7, n5) == 0) continue;
                    bl = false;
                }
            }
        }
        if (!bl) {
            return false;
        }
        if (getTile(blocks, n, n2 - 1, n3) == 2) {
            setTile(blocks, n, n2 - 1, n3, 3);
            for (n4 = n2 - 3 + n8; n4 <= n2 + n8; ++n4) {
                n6 = n4 - (n2 + n8);
                n5 = 1 - n6 / 2;
                for (int i = n - n5; i <= n + n5; ++i) {
                    int n9 = i - n;
                    for (n7 = n3 - n5; n7 <= n3 + n5; ++n7) {
                        int n10 = n7 - n3;
                        if (Math.abs(n9) == n5 && Math.abs(n10) == n5 && (random.nextInt(2) == 0 || n6 == 0) || getTile(blocks, i, n4, n7) != 0) continue;
                        setTile(blocks, i, n4, n7, Tile.leaves.id);
                    }
                }
            }
            for (n4 = 0; n4 < n8; ++n4) {
                if (getTile(blocks, n, n2 + n4, n3) != 0) continue;
                setTile(blocks, n, n2 + n4, n3, Tile.log.id);
            }
            return true;
        }
        return false;
    }
    
    private static int[] buildBlocks(Random r) {
        int width = 64;
        int height = 64;
        int depth = 64;
        int[] blocks = new int[262144];
        int[] heightmap1 = new NoiseMap(1).read(width, height);
        int[] heightmap2 = new NoiseMap(1).read(width, height);
        int[] cf = new NoiseMap(2).read(width, height);
        int[] rockMap = new NoiseMap(2).read(width, height);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < depth; z++) {
                    int dh;
                    int dh1 = heightmap1[x + z * width];
                    int dh2 = heightmap2[x + z * width];
                    int cfh = cf[x + z * width];
                    if (cfh < 128) {
                        dh2 = dh1;
                    }
                    if (dh2 > (dh = dh1)) {
                        dh = dh2;
                    } else {
                        dh2 = dh1;
                    }
                    dh = dh / 8 + depth / 3;
                    int rh = rockMap[x + z * width] / 8 + depth / 3;
                    if (rh > dh - 2) {
                        rh = dh - 2;
                    }
                    int id = 0;
                    if (y == dh + 1 && r.nextInt(128) == 16) {
                        placeTree(blocks, x, y, z, r);
                    }
                    if (y == dh) {
                        id = Tile.grass.id; // Grass
                    }
                    if (y < dh) {
                        id = Tile.dirt.id; // Dirt
                    }
                    if (y <= rh) {
                        id = Tile.rock.id; // Stone
                    }
                    
                    if (id != 0)
                        setTile(blocks, x, y, z, id);
                }
            }
        }
        
        return blocks;
    }
    
    private static void carveTunnels(int[] blocks, Random random) {
        int width = 64;
        int height = 64;
        int depth = 64;
        int n = width;
        int n2 = height;
        int n3 = depth;
        int n4 = n * n2 * n3 / 256 / 64;
        for (int i = 0; i < n4; ++i) {
            float f = random.nextFloat() * (float)n;
            float f2 = random.nextFloat() * (float)n3;
            float f3 = random.nextFloat() * (float)n2;
            int n5 = (int)(random.nextFloat() + random.nextFloat() * 150.0f);
            float f4 = (float)((double)random.nextFloat() * 3.141592653589793 * 2.0);
            float f5 = 0.0f;
            float f6 = (float)((double)random.nextFloat() * 3.141592653589793 * 2.0);
            float f7 = 0.0f;
            for (int j = 0; j < n5; ++j) {
                f = (float)((double)f + Math.sin(f4) * Math.cos(f6));
                f3 = (float)((double)f3 + Math.cos(f4) * Math.cos(f6));
                f2 = (float)((double)f2 + Math.sin(f6));
                f4 += f5 * 0.2f;
                f5 *= 0.9f;
                f5 += random.nextFloat() - random.nextFloat();
                f6 += f7 * 0.5f;
                f6 *= 0.5f;
                f7 *= 0.9f;
                f7 += random.nextFloat() - random.nextFloat();
                float f8 = (float)(Math.sin((double)j * 3.141592653589793 / (double)n5) * 2.5 + 1.0);
                for (int k = (int)(f - f8); k <= (int)(f + f8); ++k) {
                    for (int i2 = (int)(f2 - f8); i2 <= (int)(f2 + f8); ++i2) {
                        for (int i3 = (int)(f3 - f8); i3 <= (int)(f3 + f8); ++i3) {
                            int n6;
                            float f9 = (float)k - f;
                            float f10 = (float)i2 - f2;
                            float f11 = (float)i3 - f3;
                            float f12 = f9 * f9 + f10 * f10 * 2.0f + f11 * f11;
                            if (f12 >= f8 * f8 || k < 1 || i2 < 1 || i3 < 1 || k >= width - 1 || i2 >= depth - 1 || i3 >= height - 1 || blocks[n6 = (i2 * height + i3) * width + k] != Tile.rock.id) continue;
                            blocks[n6] = 0;
                        }
                    }
                }
            }
        }
    }
    
    private static void populateOre(int[] blocks, Random random, int n, int n2, int n3) {
        int width = 64;
        int height = 64;
        int depth = 64;
        int n4 = width;
        int n5 = height;
        int n6 = depth;
        int n7 = n4 * n5 * n6 / 256 / 64 * n2 / 100;
        for (int i = 0; i < n7; ++i) {
            float f = random.nextFloat() * (float)n4;
            float f2 = random.nextFloat() * (float)n6;
            float f3 = random.nextFloat() * (float)n5;
            int n8 = (int)((random.nextFloat() + random.nextFloat()) * 75.0f * (float)n2 / 100.0f);
            float f4 = (float)((double)random.nextFloat() * 3.141592653589793 * 2.0);
            float f5 = 0.0f;
            float f6 = (float)((double)random.nextFloat() * 3.141592653589793 * 2.0);
            float f7 = 0.0f;
            for (int j = 0; j < n8; ++j) {
                f = (float)((double)f + Math.sin(f4) * Math.cos(f6));
                f3 = (float)((double)f3 + Math.cos(f4) * Math.cos(f6));
                f2 = (float)((double)f2 + Math.sin(f6));
                f4 += f5 * 0.2f;
                f5 *= 0.9f;
                f5 += random.nextFloat() - random.nextFloat();
                f6 += f7 * 0.5f;
                f6 *= 0.5f;
                f7 *= 0.9f;
                f7 += random.nextFloat() - random.nextFloat();
                float f8 = (float)Math.sin((float)j * 3.1415927f / (float)n8) * (float)n2 / 100.0f + 1.0f;
                for (int k = (int)(f - f8); k <= (int)(f + f8); ++k) {
                    for (int i2 = (int)(f2 - f8); i2 <= (int)(f2 + f8); ++i2) {
                        for (int i3 = (int)(f3 - f8); i3 <= (int)(f3 + f8); ++i3) {
                            float f9 = (float)k - f;
                            float f10 = (float)i2 - f2;
                            float f11 = (float)i3 - f3;
                            float f12 = f9 * f9 + f10 * f10 * 2.0f + f11 * f11;
                            if (f12 >= f8 * f8 || k < 1 || i2 < 1 || i3 < 1 || k >= width - 1 || i2 >= depth - 1 || i3 >= height - 1 || getTile(blocks, k, i2, i3) != Tile.rock.id) continue;
                            setTile(blocks, k, i2, i3, n);
                        }
                    }
                }
            }
        }
    }
    
    public static int getDepth(Level l, int n, int n2, int height) {
        int n3 = height - 1;
        while (l.getTile(n, n3 - 1, n2) == 0 && n3 - 1 > 0) {
            --n3;
        }
        return n3;
    }
    
    public static void setSpawnPoint(Level level) {
        int x, y, z;
        x = y = z = 0;
        int maxTries = 100;
        int tries;
        int width = level.width;
        int height = level.height;
        int depth = level.depth;
        Random r = new Random();
        x = width / 2 + (r.nextInt(128) - 64);
        z = height / 2 + (r.nextInt(128) - 64);
        y = getDepth(level, x, z, height) + 1;
        level.spawnX = x;
        level.spawnY = 65;
        level.spawnZ = z;
    }
    
    public static Level genLevel() {
        Random r = new Random();
        int[] blocks = buildBlocks(r);
        carveTunnels(blocks, r);
        populateOre(blocks, r, Tile.coalore.id, 90, 1);
        populateOre(blocks, r, Tile.ironore.id, 70, 2);
        populateOre(blocks, r, Tile.goldore.id, 50, 3);
        Level l = new Level();
        l.setData(64, 64, 64, blocks);
        setSpawnPoint(l);
        l.createTime = System.currentTimeMillis();
        return l;
    }
}
