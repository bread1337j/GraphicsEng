package org.lwjgl.Graphics;

import org.lwjgl.Graphics.Objects.AObject;

import java.util.ArrayList;

public abstract class Scene {
    protected Camera camera;

    public ArrayList<AObject> objects = new ArrayList<>();
    protected float[] vertexArray;
    protected int[] indicesArray;
    public void fillArrays(){
        int vertexLen = 0; int indiceLen = 0;
        for(int i=0; i<objects.size(); i++){
            vertexLen+=objects.get(i).getVertexLen();
            indiceLen+=objects.get(i).getIndiceLen();
        }
        vertexArray = new float[vertexLen];
        indicesArray = new int[indiceLen];
        int vertexPointer = 0;
        int elementPointer = 0;
        int indicePointer = 0;
        int[] output;
        for(int i=0; i<objects.size(); i++){
            output = objects.get(i).loadObj(vertexArray, indicesArray, vertexPointer, elementPointer, indicePointer);
            vertexPointer=output[0];
            elementPointer=output[1];
            indicePointer=output[2];
        }
    }


    public Scene(){}
    public void init(){}
    public abstract void update(float dt);

    public Camera camera() {
        return camera;
    }
}
