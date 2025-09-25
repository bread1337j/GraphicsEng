package org.lwjgl.Graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.Consts;
import org.lwjgl.Graphics.Assets.Shader;
import org.lwjgl.Graphics.Assets.Texture;
import org.lwjgl.Graphics.Objects.AObject;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

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
    public final int texCordsSize = 2;
    public final int texTypeSize = 1;
    public final int centerSize = 2;
    public final int radiusSize = 1;
    public final int typeSize = 1;
    public final int floatSizeBytes = Float.SIZE / 8;
    public final int vertexSizeBytes = (positionsSize + colorsSize + texCordsSize + texTypeSize + centerSize + typeSize + radiusSize) * floatSizeBytes;


    protected Shader shader;


    public List<AObject> objects = new CopyOnWriteArrayList<>();
    protected float[] vertexArray;
    protected int[] indicesArray;

    protected Texture[] textures;
    protected int[] texSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    protected Thread arrayFiller = new Thread(new Runnable() {
        @Override
        public void run(){
            fillArrays();
            uploadArrays();
        }
    });


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
        if(shader!=null) {
            //System.out.println("aa");
            for (int i = 0; i < textures.length; i++) {
                if (textures[i] != null) {
                    glActiveTexture(GL_TEXTURE0 + i + 1);
                    textures[i].bind(); //atp this is just magic TBH
                    //System.out.println("aa");
                }
            }
            shader.uploadIntArray("uTextures", texSlots);
        }
    }

    public Scene(){}
    public void init(){
        fillArrays();
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        this.textures = new Texture[8];
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

        if(Consts.DEBUG){
            System.out.println("Positions from " + 0 + " to " + positionsSize);
        }

        glVertexAttribPointer(1, colorsSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

        if(Consts.DEBUG){
            System.out.println("Colors from " + positionsSize + " to " + (positionsSize+colorsSize));
        }

        glVertexAttribPointer(2, texCordsSize, GL_FLOAT, false, vertexSizeBytes, (colorsSize + positionsSize) * floatSizeBytes);
        glEnableVertexAttribArray(2);

        if(Consts.DEBUG){
            System.out.println("Textures from " + (colorsSize + positionsSize) + " to " + (colorsSize + positionsSize+texCordsSize));
        }

        glVertexAttribPointer(3, texTypeSize, GL_FLOAT, false, vertexSizeBytes, (colorsSize + texCordsSize + positionsSize) * floatSizeBytes);
        glEnableVertexAttribArray(3);

        if(Consts.DEBUG){
            System.out.println("Texture ids from " + (colorsSize + positionsSize+texCordsSize) + " to " + (colorsSize + positionsSize+texCordsSize+texTypeSize));
        }

        glVertexAttribPointer(4, centerSize, GL_FLOAT, false, vertexSizeBytes, (texTypeSize + texCordsSize + colorsSize+positionsSize) * floatSizeBytes);
        glEnableVertexAttribArray(4);

        if(Consts.DEBUG){
            System.out.println("Centers from " + (colorsSize + positionsSize+texCordsSize+texTypeSize) + " to " + (colorsSize + positionsSize+texCordsSize+texTypeSize+centerSize));
        }

        glVertexAttribPointer(5, radiusSize, GL_FLOAT, false, vertexSizeBytes, (texTypeSize + texCordsSize + centerSize+colorsSize+positionsSize) * floatSizeBytes);
        glEnableVertexAttribArray(5);

        if(Consts.DEBUG){
            System.out.println("Radii from " + (colorsSize + positionsSize+texCordsSize+texTypeSize+centerSize) + " to " + (colorsSize + positionsSize+texCordsSize+texTypeSize+centerSize+radiusSize));
        }

        glVertexAttribPointer(6, typeSize, GL_FLOAT, false, vertexSizeBytes, (texTypeSize + texCordsSize + radiusSize+centerSize+colorsSize+positionsSize) * floatSizeBytes);
        glEnableVertexAttribArray(6);

        if(Consts.DEBUG){
            System.out.println("Shapes from " + (colorsSize + positionsSize+texCordsSize+texTypeSize+centerSize+radiusSize) + " to " + (colorsSize + positionsSize+texCordsSize+texTypeSize+centerSize+radiusSize+typeSize));
        }

    }
    public abstract void update(float dt);

    public Camera camera() {
        return camera;
    }

    public void bindTexture(int n, Texture tex){
        this.textures[n] = tex;
    }

}
