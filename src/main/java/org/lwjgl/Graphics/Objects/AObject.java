package org.lwjgl.Graphics.Objects;

public abstract class AObject { //abstract object
    protected float[] vertices;
    protected int[] indices; //rects for example have 2 1 0 0 1 3

    public abstract int[] loadObj(float[] vertexArr, int[] elementArr, int pointer, int eboPointer);
}
