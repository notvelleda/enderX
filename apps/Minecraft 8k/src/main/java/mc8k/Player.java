package mc8k;

import java.awt.event.KeyEvent;

public class Player
extends Entity {
    public static Player instance = new Player();
    public static final int KEY_UP = 0;
    public static final int KEY_DOWN = 1;
    public static final int KEY_LEFT = 2;
    public static final int KEY_RIGHT = 3;
    public static final int KEY_JUMP = 4;
    private boolean[] keys = new boolean[10];

    public Player() {
        this.init();
    }

    public Player(Level level) {
        super(level);
        this.init();
    }

    public void init() {
        this.heightOffset = 1.62f;
        this.health = 20;
    }

    @Override
    public void setLevel(Level level) {
        this.level = level;
        this.resetPos();
    }

    @Override
    public void resetPos() {
        this.setPos((float)this.level.spawnX + 0.5f, (float)this.level.spawnY + 0.5f, (float)this.level.spawnZ + 0.5f);
    }

    public void setKey(int n, boolean bl) {
        int n2 = -1;
        if (n == KeyEvent.VK_W) {
            n2 = 0;
        }
        if (n == KeyEvent.VK_S) {
            n2 = 1;
        }
        if (n == KeyEvent.VK_D) {
            n2 = 2;
        }
        if (n == KeyEvent.VK_A) {
            n2 = 3;
        }
        if (n == KeyEvent.VK_SPACE) {
            n2 = 4;
        }
        if (n == KeyEvent.VK_CONTROL) {
            n2 = 5;
        }
        if (n == KeyEvent.VK_F && !this.keys[n2 = 6] && bl) {
            this.flying = !this.flying;
        }
        if (n == KeyEvent.VK_SHIFT) {
            n2 = 7;
        }
        if (n2 >= 0) {
            this.keys[n2] = bl;
        }
    }

    public void releaseAllKeys() {
        for (int i = 0; i < 10; ++i) {
            this.keys[i] = false;
        }
    }

    @Override
    public void tick() {
        float f;
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        float f2 = 0.0f;
        float f3 = 0.0f;
        
        float f4 = f = this.keys[5] ? 0.17f : 0.1f;
        if (this.flying) {
            f = 0.04f;
        }
        if (this.flying && this.keys[5]) {
            f = 0.1f;
        }
        if (!this.onGround && !this.flying) {
            f = 0.027f;
        }
        if (this.keys[0]) {
            f3 -= 1.0f;
        }
        if (this.keys[1]) {
            f3 += 1.0f;
        }
        if (this.keys[2]) {
            f2 -= 1.0f;
        }
        if (this.keys[3]) {
            f2 += 1.0f;
        }
        if (this.keys[4]) {
            if (this.onGround) {
                this.yd = 0.42f;
            }
        }
        if (this.flying) {
            if (this.keys[4]) {
                this.yd = 0.5f;
            }
            if (this.keys[7]) {
                this.yd = -0.5f;
            }
            this.moveRelative(f2, f3, f);
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.91f;
            if (!this.keys[4] && !this.keys[7]) {
                this.yd = 0.0f;
            }
            this.zd *= 0.91f;
            if (this.onGround) {
                this.xd *= 0.7f;
                this.zd *= 0.7f;
            }
        } else {
            this.moveRelative(f2, f3, f);
            this.move(this.xd, this.yd, this.zd);
            this.xd *= 0.91f;
            this.yd *= 0.98f;
            this.zd *= 0.91f;
            this.yd = (float)((double)this.yd - 0.08);
            if (this.onGround) {
                this.xd *= 0.6f;
                this.zd *= 0.6f;
            }
        }
    }
}

