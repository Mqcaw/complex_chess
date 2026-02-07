package me.jdfunk.app.render;

public class PieceRenderState {
    private boolean overrideActive = false;
    private int overrideX;
    private int overrideY;

    public void setOverride(int x, int y) {
        overrideActive = true;
        overrideX = x;
        overrideY = y;
    }

    public void clearOverride() {
        overrideActive = false;
    }

    public boolean hasOverride() { return overrideActive; }

    public int getOverrideX() { return overrideX; }
    public int getOverrideY() { return overrideY; }
}