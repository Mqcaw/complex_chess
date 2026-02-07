package me.jdfunk.app;

import me.jdfunk.api.Engine;

import javax.swing.*;
import java.util.ServiceLoader;

public class Main {
    public static void main(String[] args) {
        //ResourceManager.initialize();
        //System.out.println("JAR path = " + ResourceManager.getJarDirectory());
        SwingUtilities.invokeLater(Window::new);

        Engine engine = ServiceLoader.load(Engine.class).findFirst().orElseThrow(() -> new RuntimeException("No engine runtime found."));

        engine.start();
    }
}