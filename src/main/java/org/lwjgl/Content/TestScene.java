package org.lwjgl.Content;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.Graphics.Assets.Shader;
import org.lwjgl.Graphics.Camera;
import org.lwjgl.Graphics.Objects.AObject;
import org.lwjgl.Graphics.Objects.Rect;
import org.lwjgl.Graphics.Objects.Triangle;
import org.lwjgl.Graphics.Scene;
import org.lwjgl.Window;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class TestScene extends Scene {
    private Shader shader;

    private int vertexID, fragmentID;
    private float[] vertexArray = {
            //position              //color
            0.5f, -0.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f, //Bottom right  0
            -0.5f, 0.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f, //Top left      1
            0.5f, 0.5f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f, //Top right     2
    };

    private int[] elementArray;

    private int vaoID, vboID, eboID;

    public TestScene(){

    }
    int[] sigma;
    @Override
    public void init() {

        this.camera = new Camera(new Vector2f(0, 0));

        shader = new Shader("assets/shaders/default.glsl");
        shader.compile();

        AObject tri = new Triangle(
                0.5f, -0.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f,
                -0.5f, 0.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f,
                0.5f, 0.5f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f
                );

        AObject rect = new Rect(
                -0.0f, -1.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f,
                -1.5f, -0.0f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f,
                -0.0f, -0.0f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f,
                -1.5f, -1.5f, 0.0f,     1.0f, 0.0f, 0.0f, 0.0f
                );

        AObject rect2 = new Rect(-5.0f, 0.0f, 1.0f, 15.0f, 10.0f, 0.0f, 1.0f, 0.0f, 0.0f);
        AObject tri2 = new Triangle(
                0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f
        );

        vertexArray = new float[tri.getVertexLen() * 2 + rect.getVertexLen() * 2];
        elementArray = new int[tri.getIndiceLen() * 2 + rect.getIndiceLen() * 2];
        int vertexPointer = 0;
        int elementPointer = 0;
        int indice = 0;
        sigma = tri.loadObj(vertexArray, elementArray, vertexPointer, elementPointer, indice);
        vertexPointer = sigma[0];
        elementPointer = sigma[1];
        indice = sigma[2];
        sigma = tri2.loadObj(vertexArray, elementArray, vertexPointer, elementPointer, indice);
        vertexPointer = sigma[0];
        elementPointer = sigma[1];
        indice = sigma[2];
        sigma = rect.loadObj(vertexArray, elementArray, vertexPointer, elementPointer, indice);
        vertexPointer = sigma[0];
        elementPointer = sigma[1];
        indice = sigma[2];
        sigma = rect2.loadObj(vertexArray, elementArray, vertexPointer, elementPointer, indice);
        System.out.println(Arrays.toString(sigma));
        System.out.println(Arrays.toString(elementArray));
        vertexPointer = sigma[0];
        elementPointer = sigma[1];
        indice = sigma[2];


        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionsSize = 3;
        int colorsSize = 4;
        int floatSizeBytes = Float.SIZE / 8;
        int vertexSizeBytes = (positionsSize + colorsSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorsSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

        System.out.println(Window.getScene().camera().getProjectionMatrix());
        System.out.println(Window.getScene().camera().getViewMatrix());
    }

    @Override
    public void update(float dt){


        //this.camera().position.add(new Vector2f(-0.0001f * dt, 0.0f * dt));

        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        camera.position.add(new Vector2f(0.01f, 0.0f * dt));
        camera.adjustProjection();
        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        shader.detach();
    }


}
