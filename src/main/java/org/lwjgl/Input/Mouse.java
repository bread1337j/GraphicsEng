package org.lwjgl.Input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Mouse {
    private static Mouse instance;
    private double scrollx,scrolly;
    private double xPos, yPos, lastY, lastX;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;


    private Mouse(){
        this.scrollx = 0.0;
        this.scrolly = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastY = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }
    public static Mouse get(){
        if(Mouse.instance == null){
            Mouse.instance = new Mouse();
        }
        return Mouse.instance;
    }

    public static void mousePosCallback(long window, double xpos, double ypos){
        get().lastX = get().xPos;
        get().lastY = get().yPos;
        get().xPos = xpos;
        get().yPos = ypos;

        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if(action == GLFW_PRESS){
            if(button < get().mouseButtonPressed.length){
                get().mouseButtonPressed[button] = true;
            }
        } else if(action == GLFW_RELEASE){
            if(button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xoffset, double yoffset){
        get().scrollx = xoffset;
        get().scrolly = yoffset;
    }

    public static void endFrame(){
        get().scrollx = 0;
        get().scrolly = 0;
        get().lastX = get().xPos;
        get().lastY = get().yPos;
    }

    public static float getX(){
        return (float)get().xPos;
    }
    public static float getY(){
        return (float)get().yPos;
    }
    public static float getDx(){
        return (float)(get().lastX - get().xPos);
    }
    public static float getDy(){
        return (float)(get().lastY - get().yPos);
    }
    public static float getScrollx(){
        return (float)get().scrollx;
    }
    public static float getScrolly(){
        return (float)get().scrolly;
    }

    public static boolean isDragging(){
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int b){
        if(b>get().mouseButtonPressed.length-1) {
            throw new IllegalArgumentException("Button press not in array mouseButtonPressed in public static boolean mouseButtonDown; tldr kys");
        }
        return get().mouseButtonPressed[b];
    }
}
