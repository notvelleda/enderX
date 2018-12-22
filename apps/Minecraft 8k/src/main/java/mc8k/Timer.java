package mc8k;

public class Timer {
    private static final long NS_PER_SECOND = 1000000000;
    private static final long MAX_NS_PER_UPDATE = 1000000000;
    private static final int MAX_TICKS_PER_UPDATE = 100;
    private float ticksPerSecond;
    private long lastTime;
    public int ticks;
    public float a;
    public float timeScale = 1.0f;
    public float fps = 0.0f;
    public float passedTime = 0.0f;

    public Timer(float f) {
        this.ticksPerSecond = f;
        this.lastTime = System.nanoTime();
    }

    public void advanceTime() {
        long l = System.nanoTime();
        long l2 = l - this.lastTime;
        this.lastTime = l;
        if (l2 < 0) {
            l2 = 0;
        }
        if (l2 > 1000000000) {
            l2 = 1000000000;
        }
        this.fps = 1000000000 / l2;
        this.passedTime += (float)l2 * this.timeScale * this.ticksPerSecond / 1.0E9f;
        this.ticks = (int)this.passedTime;
        if (this.ticks > 100) {
            this.ticks = 100;
        }
        this.passedTime -= (float)this.ticks;
        this.a = this.passedTime;
    }
}

