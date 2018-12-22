package mc8k;

import java.util.ArrayList;

public class Entity {
    protected Level level;
    public float xo;
    public float yo;
    public float zo;
    public float x;
    public float y;
    public float z;
    public float xd;
    public float yd;
    public float zd;
    public float yRot;
    public float xRot;
    public AABB bb;
    public boolean onGround = false;
    public boolean horizontalCollision = false;
    public boolean removed = false;
    protected float heightOffset = 0.0f;
    protected float bbWidth = 0.6f;
    protected float bbHeight = 1.8f;
    public int health = 5;
    public int oldhealth = 5;
    public int armorp = 0;
    public int breath = 10;
    public float fallsy = 0.0f;
    public boolean falling = false;
    public boolean flying = false;
    public boolean asdf = false;

    public Entity() {
    }

    public Entity(Level level) {
        this.level = level;
        this.resetPos();
    }

    public void setLevel(Level level) {
        this.level = level;
        this.resetPos();
    }

    protected void resetPos() {
        float f = (float)Math.random() * (float)(this.level.width - 2) + 1.0f;
        float f2 = this.level.depth + 10;
        float f3 = (float)Math.random() * (float)(this.level.height - 2) + 1.0f;
        this.setPos(f, f2, f3);
    }

    public void remove() {
        this.removed = true;
    }

    protected void setSize(float f, float f2) {
        this.bbWidth = f;
        this.bbHeight = f2;
    }

    protected void setPos(float f, float f2, float f3) {
        this.x = f;
        this.y = f2;
        this.z = f3;
        float f4 = this.bbWidth / 2.0f;
        float f5 = this.bbHeight / 2.0f;
        this.bb = new AABB(f - f4, f2 - f5, f3 - f4, f + f4, f2 + f5, f3 + f4);
    }

    public void turn(float f, float f2) {
        this.yRot = (float)((double)this.yRot + (double)f * 0.15);
        this.xRot = (float)((double)this.xRot - (double)f2 * 0.15);
        if (this.xRot < -90.0f) {
            this.xRot = -90.0f;
        }
        if (this.xRot > 90.0f) {
            this.xRot = 90.0f;
        }
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
    }

    public boolean isFree(float f, float f2, float f3) {
        AABB aABB = this.bb.cloneMove(f, f2, f3);
        ArrayList<AABB> arrayList = this.level.getCubes(aABB);
        if (arrayList.size() > 0) {
            return false;
        }
        return true;
    }

    public void move(float f, float f2, float f3) {
        int n;
        float f4 = f;
        float f5 = f2;
        float f6 = f3;
        ArrayList<AABB> arrayList = this.level.getCubes(this.bb.expand(f, f2, f3));
        for (n = 0; n < arrayList.size(); ++n) {
            f2 = arrayList.get(n).clipYCollide(this.bb, f2);
        }
        this.bb.move(0.0f, f2, 0.0f);
        for (n = 0; n < arrayList.size(); ++n) {
            f = arrayList.get(n).clipXCollide(this.bb, f);
        }
        this.bb.move(f, 0.0f, 0.0f);
        for (n = 0; n < arrayList.size(); ++n) {
            f3 = arrayList.get(n).clipZCollide(this.bb, f3);
        }
        this.bb.move(0.0f, 0.0f, f3);
        this.horizontalCollision = f4 != f || f6 != f3;
        boolean bl = this.onGround;
        this.onGround = f5 != f2 && f5 < 0.0f;
        boolean bl2 = this.onGround;
        if (f4 != f) {
            this.xd = 0.0f;
        }
        if (f5 != f2) {
            this.yd = 0.0f;
        }
        if (f6 != f3) {
            this.zd = 0.0f;
        }
        if (!bl && bl2 && !this.flying) {
            float f7 = this.bb.y0 + this.heightOffset - this.fallsy;
            this.falling = false;
            if (f7 <= -4.0f) {
                this.health = (int)((float)this.health - (Math.abs(f7) - 3.0f));
            }
        }
        if (bl && !bl2 && !this.flying) {
            this.fallsy = this.y;
        }
        this.falling = true;
        if (this.falling && this.y > this.fallsy && !this.flying) {
            this.fallsy = this.y;
        }
        this.x = (this.bb.x0 + this.bb.x1) / 2.0f;
        this.y = this.bb.y0 + this.heightOffset;
        this.z = (this.bb.z0 + this.bb.z1) / 2.0f;
    }

    public void moveRelative(float f, float f2, float f3) {
        float f4 = f * f + f2 * f2;
        if (f4 < 0.01f) {
            return;
        }
        f4 = f3 / (float)Math.sqrt(f4);
        float f5 = (float)Math.sin(Math.toRadians(this.xRot));
        float f6 = (float)Math.cos(Math.toRadians(this.xRot));
        this.xd += (f *= f4) * f6 - (f2 *= f4) * f5;
        this.zd += f2 * f6 + f * f5;
    }

    public boolean isLit() {
        int n = (int)this.x;
        int n2 = (int)this.y;
        int n3 = (int)this.z;
        return this.level.isLit(n, n2, n3);
    }

    public void render(float f) {
    }
}

