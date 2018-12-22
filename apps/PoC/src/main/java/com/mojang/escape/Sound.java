/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape;

import java.io.PrintStream;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
    public static Sound altar = Sound.loadSound("/snd/altar.wav");
    public static Sound bosskill = Sound.loadSound("/snd/bosskill.wav");
    public static Sound click1 = Sound.loadSound("/snd/click.wav");
    public static Sound click2 = Sound.loadSound("/snd/click2.wav");
    public static Sound hit = Sound.loadSound("/snd/hit.wav");
    public static Sound hurt = Sound.loadSound("/snd/hurt.wav");
    public static Sound hurt2 = Sound.loadSound("/snd/hurt2.wav");
    public static Sound kill = Sound.loadSound("/snd/kill.wav");
    public static Sound death = Sound.loadSound("/snd/death.wav");
    public static Sound splash = Sound.loadSound("/snd/splash.wav");
    public static Sound key = Sound.loadSound("/snd/key.wav");
    public static Sound pickup = Sound.loadSound("/snd/pickup.wav");
    public static Sound roll = Sound.loadSound("/snd/roll.wav");
    public static Sound shoot = Sound.loadSound("/snd/shoot.wav");
    public static Sound treasure = Sound.loadSound("/snd/treasure.wav");
    public static Sound crumble = Sound.loadSound("/snd/crumble.wav");
    public static Sound slide = Sound.loadSound("/snd/slide.wav");
    public static Sound cut = Sound.loadSound("/snd/cut.wav");
    public static Sound thud = Sound.loadSound("/snd/thud.wav");
    public static Sound ladder = Sound.loadSound("/snd/ladder.wav");
    public static Sound potion = Sound.loadSound("/snd/potion.wav");
    private Clip clip;

    public static Sound loadSound(String fileName) {
        Sound sound = new Sound();
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(Sound.class.getResource(fileName));
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            sound.clip = clip;
        }
        catch (Exception e) {
            //System.out.println(e);
            e.printStackTrace();
        }
        return sound;
    }

    public void play() {
        try {
            if (this.clip != null) {
                new Thread(){

                    /*
                     * WARNING - Removed try catching itself - possible behaviour change.
                     */
                    public void run() {
                        Clip clip = Sound.this.clip;
                        synchronized (clip) {
                            Sound.this.clip.stop();
                            Sound.this.clip.setFramePosition(0);
                            Sound.this.clip.start();
                        }
                    }
                }.start();
            }
        }
        catch (Exception e) {
            //System.out.println(e);
            e.printStackTrace();
        }
    }

}

