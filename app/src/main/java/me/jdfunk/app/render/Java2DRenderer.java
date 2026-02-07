package me.jdfunk.app.render;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Java2DRenderer extends GameRenderer{
    private Graphics2D g2d;
    private boolean initialized = false;

    public Java2DRenderer(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

        this.g2d = g2d;
    }

    public void setG2d(Graphics2D g2d) {
        this.g2d = g2d;
    }

    public void setColor(Color color) {
        g2d.setColor(color);
    }

    public void drawRect(int x, int y, int width, int height) {
        g2d.drawRect(x, y, width, height);
    }

    public void fillRect(int x, int y, int width, int height) {
        g2d.fillRect(x, y, width, height);
    }

    public void drawCircle(int x, int y, int radius) {
        g2d.drawOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    public void fillPolygon(Polygon p) {
        g2d.fillPolygon(p);
    }

    public void drawPolygon(Polygon p) {
        g2d.drawPolygon(p);
    }

    //fill circle centered at x,y with radius
    public void fillCircle(int x, int y, int radius) {
        g2d.fillOval(x - radius, y - radius, radius * 2, radius * 2);
    }

    @Override
    public void drawImage(BufferedImage image, int x, int y, int width, int height) {
        g2d.drawImage(image, x, y, width, height, null);
    }

    @Override
    public void initialize() {
        if (!initialized) {
            initialized = true;
        }
    }

    @Override
    public boolean isInitialized() {
        return initialized;
    }
}
