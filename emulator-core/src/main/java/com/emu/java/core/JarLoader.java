package com.emu.java.core;

import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class JarLoader {
    private String mainClassName;
    private String appName;

    public void loadJar(String jarPath) throws Exception {
        try (JarFile jarFile = new JarFile(jarPath)) {
            Manifest manifest = jarFile.getManifest();
            if (manifest != null) {
                Attributes attrs = manifest.getMainAttributes();
                String midlet1 = attrs.getValue("MIDlet-1");
                if (midlet1 != null) {
                    String[] parts = midlet1.split(",");
                    if (parts.length >= 3) {
                        this.appName = parts[0].trim();
                        this.mainClassName = parts[2].trim();
                    }
                }
            }
        }
    }

    public String getMainClassName() { return mainClassName; }
    public String getAppName() { return appName; }
}
