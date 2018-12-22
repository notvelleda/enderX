package mc8k;

public class AABB {
    private float epsilon = 0.0f;
    public float x0;
    public float y0;
    public float z0;
    public float x1;
    public float y1;
    public float z1;

    public AABB(float f, float f2, float f3, float f4, float f5, float f6) {
        this.x0 = f;
        this.y0 = f2;
        this.z0 = f3;
        this.x1 = f4;
        this.y1 = f5;
        this.z1 = f6;
    }

    public AABB expand(float f, float f2, float f3) {
        float f4 = this.x0;
        float f5 = this.y0;
        float f6 = this.z0;
        float f7 = this.x1;
        float f8 = this.y1;
        float f9 = this.z1;
        if (f < 0.0f) {
            f4 += f;
        }
        if (f > 0.0f) {
            f7 += f;
        }
        if (f2 < 0.0f) {
            f5 += f2;
        }
        if (f2 > 0.0f) {
            f8 += f2;
        }
        if (f3 < 0.0f) {
            f6 += f3;
        }
        if (f3 > 0.0f) {
            f9 += f3;
        }
        return new AABB(f4, f5, f6, f7, f8, f9);
    }

    public AABB grow(float f, float f2, float f3) {
        float f4 = this.x0 - f;
        float f5 = this.y0 - f2;
        float f6 = this.z0 - f3;
        float f7 = this.x1 + f;
        float f8 = this.y1 + f2;
        float f9 = this.z1 + f3;
        return new AABB(f4, f5, f6, f7, f8, f9);
    }

    public AABB cloneMove(float f, float f2, float f3) {
        return new AABB(this.x0 + f3, this.y0 + f2, this.z0 + f3, this.x1 + f, this.y1 + f2, this.z1 + f3);
    }

    public float clipXCollide(AABB aABB, float f) {
        float f2;
        if (aABB.y1 <= this.y0 || aABB.y0 >= this.y1) {
            return f;
        }
        if (aABB.z1 <= this.z0 || aABB.z0 >= this.z1) {
            return f;
        }
        if (f > 0.0f && aABB.x1 <= this.x0 && (f2 = this.x0 - aABB.x1 - this.epsilon) < f) {
            f = f2;
        }
        if (f < 0.0f && aABB.x0 >= this.x1 && (f2 = this.x1 - aABB.x0 + this.epsilon) > f) {
            f = f2;
        }
        return f;
    }

    public float clipYCollide(AABB aABB, float f) {
        float f2;
        if (aABB.x1 <= this.x0 || aABB.x0 >= this.x1) {
            return f;
        }
        if (aABB.z1 <= this.z0 || aABB.z0 >= this.z1) {
            return f;
        }
        if (f > 0.0f && aABB.y1 <= this.y0 && (f2 = this.y0 - aABB.y1 - this.epsilon) < f) {
            f = f2;
        }
        if (f < 0.0f && aABB.y0 >= this.y1 && (f2 = this.y1 - aABB.y0 + this.epsilon) > f) {
            f = f2;
        }
        return f;
    }

    public float clipZCollide(AABB aABB, float f) {
        float f2;
        if (aABB.x1 <= this.x0 || aABB.x0 >= this.x1) {
            return f;
        }
        if (aABB.y1 <= this.y0 || aABB.y0 >= this.y1) {
            return f;
        }
        if (f > 0.0f && aABB.z1 <= this.z0 && (f2 = this.z0 - aABB.z1 - this.epsilon) < f) {
            f = f2;
        }
        if (f < 0.0f && aABB.z0 >= this.z1 && (f2 = this.z1 - aABB.z0 + this.epsilon) > f) {
            f = f2;
        }
        return f;
    }

    public boolean intersects(AABB aABB) {
        if (aABB.x1 <= this.x0 || aABB.x0 >= this.x1) {
            return false;
        }
        if (aABB.y1 <= this.y0 || aABB.y0 >= this.y1) {
            return false;
        }
        if (aABB.z1 <= this.z0 || aABB.z0 >= this.z1) {
            return false;
        }
        return true;
    }

    public void move(float f, float f2, float f3) {
        this.x0 += f;
        this.y0 += f2;
        this.z0 += f3;
        this.x1 += f;
        this.y1 += f2;
        this.z1 += f3;
    }
}

