package me.jdfunk.app.render;

import java.awt.image.BufferedImage;

public abstract class GameRenderer {

    public abstract void initialize();
    public abstract boolean isInitialized();

    public abstract void drawImage(BufferedImage image, int x, int y, int width, int height);

}
