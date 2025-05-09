package org.lwjgl;

import org.lwjgl.Content.TestScene;
import org.lwjgl.Graphics.Scene;
import org.lwjgl.Input.Keyboard;
import org.lwjgl.Input.Mouse;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

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


        //make opengl context current
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
    float dt = 0;
    public void loop(){
        while(!glfwWindowShouldClose(glfwWindow)){
            glfwPollEvents();
            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);
            if(dtBufferCount < 0){
                dtBuffer -= dt;
                dtBufferCount++;
            }
            dt = System.nanoTime();



            //Do things here


            if(dt > 0){
                currentScene.update(dt);
            }


            //stop doing things here
            //divine intellect code organization fr



            dt = System.nanoTime() - dt;
            dtBuffer += dt; dtBufferCount--;
            setTitle(String.valueOf(dtBuffer / 64));
            glfwSwapBuffers(glfwWindow);
        }
    }


}