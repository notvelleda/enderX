/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape;

import com.mojang.escape.Game;
import com.mojang.escape.InputHandler;
import com.mojang.escape.gui.Screen;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.awt.image.ImageObserver;
import java.awt.image.WritableRaster;
import java.io.PrintStream;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class EscapeComponent
extends Component
implements Runnable {
    private static final long serialVersionUID = 1;
    private static final int WIDTH = 160;
    private static final int HEIGHT = 120;
    private static final int SCALE = 4;
    private boolean running;
    private Thread thread;
    private Game game;
    private Screen screen;
    private BufferedImage img;
    private BufferedImage imgAlt;
    private int[] pixels;
    private InputHandler inputHandler;
    private Cursor emptyCursor;
    private Cursor defaultCursor;
    private boolean hadFocus = false;

    public EscapeComponent() {
        Dimension size = new Dimension(640, 480);
        this.setSize(size);
        this.setPreferredSize(size);
        this.setMinimumSize(size);
        this.setMaximumSize(size);
        this.game = new Game();
        this.screen = new Screen(160, 120);
        this.img = new BufferedImage(160, 120, 1);
        this.imgAlt = new BufferedImage(160, 120, 1);
        this.pixels = ((DataBufferInt)this.img.getRaster().getDataBuffer()).getData();
        this.inputHandler = new InputHandler();
        this.addKeyListener(this.inputHandler);
        this.addFocusListener(this.inputHandler);
        this.addMouseListener(this.inputHandler);
        this.addMouseMotionListener(this.inputHandler);
        this.emptyCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(16, 16, 2), new Point(0, 0), "empty");
        this.defaultCursor = this.getCursor();
    }

    public synchronized void start() {
        if (this.running) {
            return;
        }
        this.running = true;
        this.thread = new Thread(this);
        this.thread.start();
    }

    public synchronized void stop() {
        if (!this.running) {
            return;
        }
        this.running = false;
        try {
            this.thread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    // I had to use a different decompiler as CFR completely fucked up this method
    public void run() {
        long j = System.nanoTime();
        this.requestFocus();
        int i = 0;
        double d = 0.0;
        int i0 = 0;
        while(this.running) {
            InterruptedException a = null;
            long j0 = System.nanoTime();
            long j1 = j0 - j;
            if (j1 < 0L) {
                j1 = 0L;
            }
            if (j1 > 100000000L) {
                j1 = 100000000L;
            }
            d = d + (double)j1 / 1000000000.0;
            j = j0;
            boolean b = false;
            while(d > 0.016666666666666666) {
                this.tick();
                d = d - 0.016666666666666666;
                i0 = i0 + 1;
                if (i0 % 60 != 0) {
                    b = true;
                } else {
                    System.out.println(new StringBuilder(String.valueOf(i)).append(" fps").toString());
                    j = j + 1000L;
                    i = 0;
                    b = true;
                }
            }
            if (b) {
                this.render();
                i = i + 1;
                continue;
            } else {
                try {
                    Thread.sleep(1L);
                    continue;
                } catch(InterruptedException a0) {
                    a = a0;
                }
            }
            a.printStackTrace();
        }
    }

    private void tick() {
        if (this.hasFocus()) {
            this.game.tick(this.inputHandler.keys);
        }
    }
    
    public boolean hasFocus() {
        return true;
    }

    private void render() {
        //BufferStrategy bs;
        if (this.hadFocus != this.hasFocus()) {
            this.hadFocus = !this.hadFocus;
            this.setCursor(this.hadFocus ? this.emptyCursor : this.defaultCursor);
        }
        //if ((bs = this.getBufferStrategy()) == null) {
        //    this.createBufferStrategy(3);
        //    return;
        //}
        this.screen.render(this.game, this.hasFocus());
        int i = 0;
        while (i < 19200) {
            this.pixels[i] = this.screen.pixels[i];
            ++i;
        }
        //Graphics g = bs.getDrawGraphics();
        Graphics g = this.imgAlt.getGraphics();
        //g.fillRect(0, 0, this.img.getWidth(), this.img.getHeight());
        g.drawImage(this.img, 0, 0, null);
        g.dispose();
        //bs.show();
    }
    
    public void paint(Graphics g) {
        g.drawImage(this.imgAlt, 0, 0, this.getWidth(), this.getHeight(), null);
    }

    public static void main(String[] args) {
        EscapeComponent game = new EscapeComponent();
        JFrame frame = new JFrame("Prelude of the Chambered!");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add((Component)game, "Center");
        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
        game.start();
    }
}

