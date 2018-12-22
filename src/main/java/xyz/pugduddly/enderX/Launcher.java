/*
 * Launcher.java
 * 
 * Copyright 2018 Pugduddly <pugduddly@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 * 
 */

package xyz.pugduddly.enderX;

import java.net.URI;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

public class Launcher {
	private static final int MIN_HEAP = 1024;
    private static final int RECOMMENDED_HEAP = 2048;

    public static void main(String[] args) throws Exception {
        float heapSizeMegs = Runtime.getRuntime().maxMemory() / 1024 / 1024;
        if (heapSizeMegs > MIN_HEAP) {
            System.setProperty("sun.java2d.noddraw", "True");
            System.setProperty("sun.java2d.d3d", "True");
            System.setProperty("sun.java2d.opengl", "True");
            System.setProperty("sun.java2d.ddscale", "True");
            System.setProperty("sun.java2d.translaccel", "True");
            System.setProperty("sun.java2d.pmoffscreen", "False");
            //System.setProperty("sun.java2d.trace", "log");
            EnderX.main(args);
        } else {
            try {
                String pathToJar = Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                ArrayList<String> params = new ArrayList<String>();
                params.add("java");
                params.add("-Xmx" + RECOMMENDED_HEAP + "m");
                params.add("-Dsun.java2d.noddraw=True");
                params.add("-Dsun.java2d.d3d=True");
                params.add("-Dsun.java2d.opengl=True");
                params.add("-Dsun.java2d.ddscale=True");
                params.add("-Dsun.java2d.translaccel=True");
                params.add("-Dsun.java2d.pmoffscreen=False");
                //params.add("-Dsun.java2d.trace=log");
                params.add("-classpath");
                params.add(pathToJar);
                params.add("xyz.pugduddly.enderX.EnderX");
                ProcessBuilder pb = new ProcessBuilder(params);
                Process process = pb.start();
                if (process == null) {
                    throw new Exception("process == null");
                }
                System.exit(0);
            }
            catch (Exception e) {
                e.printStackTrace();
                EnderX.main(args);
            }
        }
    }
}

