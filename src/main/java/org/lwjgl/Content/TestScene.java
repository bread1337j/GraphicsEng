package org.lwjgl.Content;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.Graphics.Assets.Shader;
import org.lwjgl.Graphics.Assets.Texture;
import org.lwjgl.Graphics.Camera;
import org.lwjgl.Graphics.Objects.AObject;
import org.lwjgl.Graphics.Objects.Circle;
import org.lwjgl.Graphics.Objects.Rect;
import org.lwjgl.Graphics.Objects.Triangle;
import org.lwjgl.Graphics.Scene;
import org.lwjgl.Input.Keyboard;
import org.lwjgl.Input.Mouse;
import org.lwjgl.Window;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class TestScene extends Scene {




    AObject tri = new Triangle(
            0.5f, -0.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0,
            -0.5f, 0.5f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0,
            0.5f, 0.5f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0
    );

    AObject rect = new Circle(
            -0.0f, -1.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0,
            -1.5f, -0.0f, 0.0f,      0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0,
            -0.0f, -0.0f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 0,
            -1.5f, -1.5f, 0.0f,     1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0,
            0.1f, -0.75f, -0.75f
    );

    AObject rect2 = new Circle(0.0f, 0.0f, 1.0f, 0.5f, 1.0f, 1.0f, 0.0f, 0.0f, -0.5f, -0.5f, 1);
    AObject tri2 = new Triangle(
            0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.5f, 0.5f, 1
    );

    Texture tx;



    public TestScene(){

    }
    int[] sigma;
    @Override
    public void init() {
        super.init();
        //glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
        this.camera = new Camera(new Vector2f(0, 0));
        shader = new Shader("assets/shaders/default.glsl");
        shader.compile();

        tx = new Texture("assets/textures/Test.png");
        bindTexture(0, tx);
        //bindTexture(1, tx);
        //objects.add(tri); objects.add(tri2); objects.add(rect); objects.add(rect2);
        objects.add(tri); objects.add(tri2); objects.add(rect2); objects.add(rect);
        fillArrays();
        uploadArrays();




        //System.out.println(Window.getScene().camera().getProjectionMatrix());
        //System.out.println(Window.getScene().camera().getViewMatrix());
    }

    @Override
    public void update(float dt){
        bindTexture(0, tx);
        fillArrays();
        uploadArrays();
        //this.camera().position.add(new Vector2f(-0.0001f * dt, 0.0f * dt));

        shader.use();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);


        if(Keyboard.isKeyPressed(GLFW_KEY_W)){
            camera.position.add(new Vector2f(0.0f, -0.01f));
        } if (Keyboard.isKeyPressed(GLFW_KEY_A)) {
            camera.position.add(new Vector2f(0.01f, 0.0f));
        } if (Keyboard.isKeyPressed(GLFW_KEY_S)) {
            camera.position.add(new Vector2f(0.0f, 0.01f));
        } if (Keyboard.isKeyPressed(GLFW_KEY_D)) {
            camera.position.add(new Vector2f(-0.01f, 0.0f));
        }


        camera.adjustProjection();



        glDrawElements(GL_TRIANGLES, indicesArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        shader.detach();
    }




}
