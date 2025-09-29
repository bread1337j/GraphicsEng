package org.lwjgl.Graphics.Objects;

public abstract class AObject { //abstract object
    protected float[] vertices;
    protected float[] center;
    public abstract int[] loadObj(float[] vertexArr, int[] elementArr, int pointer, int eboPointer, int indice);
    public abstract int getIndiceLen();
    public abstract int getVertexLen();
    public abstract int getIndiceNum();
    public abstract float[] getCenter();
}
