package org.lwjgl;

import org.joml.Vector2f;
import org.lwjgl.Content.TestScene;
import org.lwjgl.Graphics.Camera;
import org.lwjgl.Graphics.Scene;
import org.lwjgl.Input.Keyboard;
import org.lwjgl.Input.Mouse;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow; //memory address number to the window
    private float r, g, b, a;
    private boolean fadeToBlack = false;
    int dtBufferCount = 64;
    int dtBuffer = 0;
    private static Window window = null;
    private static Scene currentScene = null;
    private Window(){
        this.width = 800;
        this.height = 600;
        this.title = "Skibidi gaming";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public String getTitle(){
        return title;
    }


    public void setWidth(int n){
        this.width = n;
    }
    public void setHeight(int n){
        this.height = n;
    }
    public void setTitle(String s){
        this.title = s;
        glfwSetWindowTitle(this.get().glfwWindow, s);
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }

        return Window.window;
    }

    public static void changeScene(Scene scene){
        currentScene = scene;
        currentScene.init();
    }

    public static Scene getScene(){
        return currentScene;
    }

    public void run(){
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }


    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        //init glfw

        if(!glfwInit()){
            throw new IllegalStateException("Unable to init glfw");
        }
        // config glfw
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // create window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);

        if(glfwWindow == NULL){
            throw new IllegalStateException("failed to make glfw window");
        }

        glfwSetCursorPosCallback(glfwWindow, Mouse::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, Mouse::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, Mouse::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, Keyboard::keyCallback);


        glfwMakeContextCurrent(glfwWindow);
        //vsync jumpscare
        glfwSwapInterval(1);

        //make window visible
        glfwShowWindow(glfwWindow);
        //bindings. very important.
        GL.createCapabilities();
        Scene testScene = new TestScene();
        changeScene(testScene);
    }
    long dt = 0;
    public void loop(){
        while(!glfwWindowShouldClose(glfwWindow)){
            glfwPollEvents();
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);
            if(dtBufferCount < 0){
                dtBuffer -= dt;
                dtBufferCount++;
            }
            dt = System.currentTimeMillis();



            //Do things here

            //glViewport(0, 0, 1920, 1080); //really bad but temporary solution
            IntBuffer w = BufferUtils.createIntBuffer(1);
            IntBuffer h = BufferUtils.createIntBuffer(1);
            glfwGetWindowSize(glfwWindow, w, h);
            glViewport((int) Window.getScene().camera().position.x, (int) Window.getScene().camera().position.y, w.get(0), h.get(0)); //this is also temporary and really bad. one of these days I will make this only be called on window resize and that day will be great
            if(dt > 0){
                currentScene.update(dt);
            }


            //stop doing things here
            //divine intellect code organization fr



            dt = System.currentTimeMillis() - dt;
            dtBuffer += dt; dtBufferCount--;
            setTitle(String.valueOf(1000/Math.max(1, (dtBuffer/64)) + "FPS"));
            glfwSwapBuffers(glfwWindow);
        }
    }


}