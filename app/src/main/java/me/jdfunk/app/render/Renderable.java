package me.jdfunk.app.render;

public interface Renderable {
    //TODO: change to be an annotation, headless versions won't need this.
    void render(GameRenderer renderer);
    void updateImage();
}
