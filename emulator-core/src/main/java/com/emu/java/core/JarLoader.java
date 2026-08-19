package com.emu.java.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarLoader {
    private String jarPath;
    private String mainClassName;
    private Map<String, String> manifestAttributes = new HashMap<>();

    public void loadJar(String path) throws Exception {
        this.jarPath = path;
        JarFile jarFile = new JarFile(new File(path));
        JarEntry manifestEntry = jarFile.getJarEntry("META-INF/MANIFEST.MF");

        if (manifestEntry != null) {
            InputStream is = jarFile.getInputStream(manifestEntry);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(":")) {
                    String[] parts = line.split(":", 2);
                    manifestAttributes.put(parts[0].trim(), parts[1].trim());
                }
            }
            reader.close();
            is.close();
        }

        String midlet1 = manifestAttributes.get("MIDlet-1");
        if (midlet1 != null && midlet1.contains(",")) {
            String[] tokens = midlet1.split(",");
            if (tokens.length >= 3) {
                this.mainClassName = tokens[2].trim();
            }
        }
        
        if (this.mainClassName == null) {
            this.mainClassName = manifestAttributes.get("MIDlet-Name");
        }
        jarFile.close();
    }

    public String getMainClassName() {
        return mainClassName;
    }

    public String getAppName() {
        return manifestAttributes.get("MIDlet-Name");
    }

    public String getJarPath() {
        return jarPath;
    }
}
