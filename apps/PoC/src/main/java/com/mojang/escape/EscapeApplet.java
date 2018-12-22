/*
 * Decompiled with CFR 0_123.
 */
package com.mojang.escape;

import com.mojang.escape.EscapeComponent;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.LayoutManager;

public class EscapeApplet
extends Applet {
    private static final long serialVersionUID = 1;
    private EscapeComponent escapeComponent = new EscapeComponent();

    public void init() {
        this.setLayout(new BorderLayout());
        this.add((Component)this.escapeComponent, "Center");
    }

    public void start() {
        this.escapeComponent.start();
    }

    public void stop() {
        this.escapeComponent.stop();
    }
}

