package org.lwjgl.Graphics;

public abstract class Scene {
    protected Camera camera;

    public Scene(){}
    public void init(){}
    public abstract void update(float dt);

    public Camera camera() {
        return camera;
    }
}
