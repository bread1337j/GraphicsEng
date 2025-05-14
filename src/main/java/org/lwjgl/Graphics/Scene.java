package org.lwjgl.Graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.Graphics.Objects.AObject;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public abstract class Scene {
    protected Camera camera;
    protected int vaoID, vboID, eboID;

    public final int positionsSize = 3;
    public final int colorsSize = 4;
    public final int centerSize = 2;
    public final int typeSize = 1;
    public final int floatSizeBytes = Float.SIZE / 8;
    public final int vertexSizeBytes = (positionsSize + colorsSize + centerSize + typeSize) * floatSizeBytes;

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

    public void uploadArrays(){
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(indicesArray.length);
        elementBuffer.put(indicesArray).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
    }

    public Scene(){}
    public void init(){
        fillArrays();
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(indicesArray.length);
        elementBuffer.put(indicesArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);


        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorsSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, centerSize, GL_FLOAT, false, vertexSizeBytes, (colorsSize+positionsSize) * floatSizeBytes);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, colorsSize, GL_FLOAT, false, vertexSizeBytes, (centerSize+colorsSize+positionsSize) * floatSizeBytes);
        glEnableVertexAttribArray(3);
    }
    public abstract void update(float dt);

    public Camera camera() {
        return camera;
    }
}
