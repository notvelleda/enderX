package mc8k;

public class Tile {
    public static final Tile[] tiles = new Tile[256];
    public static final boolean[] shouldTick = new boolean[256];
    public static final Tile rock = new Tile(1);
    public static final Tile grass = new Tile(2);
    public static final Tile dirt = new Tile(3);
    public static final Tile cobble = new Tile(4);
    public static final Tile wood = new Tile(5);
    public static final Tile bedrock = new Tile(6);
    public static final Tile sand = new Tile(7);
    public static final Tile gravel = new Tile(8);
    public static final Tile log = new Tile(9);
    public static final Tile leaves = new TransparentTile(10);
    public static final Tile slab = new Tile(11);
    public static final Tile brick = new Tile(12);
    public static final Tile glass = new TransparentTile(13);
    public static final Tile coalore = new Tile(14);
    public static final Tile ironore = new Tile(15);
    public static final Tile goldore = new Tile(16);
    
    public int id;
    
    public Tile(int id) {
        Tile.tiles[id] = this;
        this.id = id;
    }
    
    public final AABB getTileAABB(int n, int n2, int n3) {
        return new AABB(n, n2, n3, n + 1, n2 + 1, n3 + 1);
    }

    public AABB getAABB(int n, int n2, int n3) {
        return new AABB(n, n2, n3, n + 1, n2 + 1, n3 + 1);
    }
    
    public boolean blocksLight() {
        return true;
    }

    public boolean isSolid() {
        return true;
    }
}
