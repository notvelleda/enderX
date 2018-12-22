package mc8k;

import java.util.ArrayList;
import java.util.Random;

public class Level {
    public int[] blocks;
    private int[] lightDepths;
    public int width;
    public int height;
    public int depth;
    public int spawnX;
    public int spawnY;
    public int spawnZ;
    public String name = "A Nice World";
    public String creator = "Steve";
    public long createTime;
    
    public void setData(int w, int h, int d, int[] blocks) {
        this.width = w;
        this.height = h;
        this.depth = d;
        this.blocks = blocks;
        this.lightDepths = new int[w * d];
        this.calcLightDepths(0, 0, w, d);
    }
    
    public void calcLightDepths(int n, int n2, int n3, int n4) {
        for (int i = n; i < n + n3; ++i) {
            for (int j = n2; j < n2 + n4; ++j) {
                int n5;
                int n6 = this.lightDepths[i + j * this.width];
                for (n5 = this.depth - 1; n5 > 0 && !this.isLightBlocker(i, n5, j); --n5) {}
                this.lightDepths[i + j * this.width] = n5 + 1;
            }
        }
    }
    
    public boolean isLightBlocker(int n, int n2, int n3) {
        Tile tile = Tile.tiles[this.getTile(n, n2, n3)];
        if (tile == null) {
            return false;
        }
        return tile.blocksLight();
    }
    
    /*public boolean setTile(int n, int n2, int n3, int n4) {
        if (n < 0 || n2 < 0 || n3 < 0 || n >= this.width || n2 >= this.depth || n3 >= this.height) {
            return false;
        }
        if (n4 == this.getTile(n, n2, n3)) {
            return false;
        }
        n2 = this.height - (n2 + 1);
        this.blocks[(n2 * this.height + n3) * this.width + n] = n4;
        this.calcLightDepths(n, n3, 1, 1);
        return true;
    }
    
    public int getTile(int n, int n2, int n3) {
        if (n < 0 || n2 < 0 || n3 < 0 || n >= this.width || n2 >= this.depth || n3 >= this.height) {
            return 0;
        }
        n2 = this.height - (n2 + 1);
        return this.blocks[(n2 * this.height + n3) * this.width + n];
    }*/
    
    public boolean setTile(int x, int y, int z, int id) {
        if (x < 0 || y < 0 || z < 0 || x >= 64 || y >= 64 || z >= 64)
            return false;
        if (id == this.getTile(x, y, z))
            return false;
        this.blocks[x + (64 - (y + 1)) * 64 + z * 4096] = id;
        this.calcLightDepths(x, z, 1, 1);
        return true;
    }
    
    public int getTile(int x, int y, int z) {
        if (x < 0 || y < 0 || z < 0 || x >= 64 || y >= 64 || z >= 64)
            return 0;
        return this.blocks[x + (64 - (y + 1)) * 64 + z * 4096];
    }

    public boolean isTileOutOfBounds(int n, int n2, int n3) {
        if (n < 0 || n2 < 0 || n3 < 0 || n >= this.width || n2 >= this.depth || n3 >= this.height) {
            return true;
        }
        return false;
    }
    
    public boolean isSolidTile(int n, int n2, int n3) {
        Tile tile = Tile.tiles[this.getTile(n, n2, n3)];
        if (tile == null) {
            return false;
        }
        return tile.isSolid();
    }

    public boolean isTransparentTile(int n, int n2, int n3) {
        Tile tile = Tile.tiles[this.getTile(n, n2, n3)];
        if (tile == null) {
            return false;
        }
        return tile.blocksLight();
    }
    
    public boolean isLit(int n, int n2, int n3) {
        if (n < 0 || n2 < 0 || n3 < 0 || n >= this.width || n2 >= this.depth || n3 >= this.height) {
            return true;
        }
        if (n2 >= this.lightDepths[n + n3 * this.width]) {
            return true;
        }
        return false;
    }
    
    public ArrayList<AABB> getCubes(AABB aABB) {
        ArrayList<AABB> arrayList = new ArrayList<AABB>();
        int n = (int)Math.floor(aABB.x0);
        int n2 = (int)Math.floor(aABB.x1 + 1.0f);
        int n3 = (int)Math.floor(aABB.y0);
        int n4 = (int)Math.floor(aABB.y1 + 1.0f);
        int n5 = (int)Math.floor(aABB.z0);
        int n6 = (int)Math.floor(aABB.z1 + 1.0f);
        for (int i = n; i < n2; ++i) {
            for (int j = n3; j < n4; ++j) {
                for (int k = n5; k < n6; ++k) {
                    AABB aABB2;
                    if (i >= 0 && j >= 0 && k >= 0 && i < this.width && j < this.depth && k < this.height) {
                        AABB aABB3;
                        Tile tile = Tile.tiles[this.getTile(i, j, k)];
                        if (tile == null || (aABB3 = tile.getAABB(i, j, k)) == null) continue;
                        arrayList.add(aABB3);
                        continue;
                    }
                    if (i >= 0 && j >= 0 && k >= 0 && i < this.width && k < this.height || (aABB2 = Tile.bedrock.getAABB(i, j, k)) == null) continue;
                    arrayList.add(aABB2);
                }
            }
        }
        return arrayList;
    }
}
