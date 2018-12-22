/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape.entities;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public enum Item {
    none(-1, 16761699, "", ""),
    powerGlove(0, 16761699, "Power Glove", "Smaaaash!!"),
    pistol(1, 15395562, "Pistol", "Pew, pew, pew!"),
    flippers(2, 8174591, "Flippers", "Splish splash!"),
    cutters(3, 13421772, "Cutters", "Snip, snip!"),
    skates(4, 11432191, "Skates", "Sharp!"),
    key(5, 16728128, "Key", "How did you get this?"),
    potion(6, 4915015, "Potion", "Healthy!");
    
    public final int icon;
    public final int color;
    public final String name;
    public final String description;

    private Item(int icon, int color, String name, String description) {
        this.icon = icon;
        this.color = color;
        this.name = name;
        this.description = description;
    }
}

