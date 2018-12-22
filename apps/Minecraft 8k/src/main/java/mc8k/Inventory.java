package mc8k;

public class Inventory {
    public int size;
    public int[] items;
    
    public Inventory(int size) {
        this.size = size;
        this.items = new int[size];
    }
    
    public Inventory populate() {
        for (int i = 0; i < this.size; i ++) {
            this.items[i] = i + 1;
        }
        return this;
    }
    
    public int get(int idx) {
        if (idx < 0 || idx >= this.size) return -1;
        return this.items[idx];
    }
    
    public void push(int item) {
        for (int i = this.size - 1; i > 0; i --) {
            this.items[i] = this.items[i - 1];
        }
        this.items[0] = item;
    }
    
    public boolean contains(int block) {
        for (int i = 0; i < this.size; i ++) {
            if (this.items[i] == block) return true;
        }
        return false;
    }
    
    public int indexOf(int block) {
        for (int i = 0; i < this.size; i ++) {
            if (this.items[i] == block) return i;
        }
        return -1;
    }
}
