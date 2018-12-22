package mc8k;

public class TransparentTile extends Tile {
    public TransparentTile(int id) {
        super(id);
    }
    
    public boolean blocksLight() {
        return false;
    }
}
