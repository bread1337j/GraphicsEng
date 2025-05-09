package org.lwjgl.Input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class Keyboard {
    private static Keyboard instance;
    private boolean keyPressed[] = new boolean[350];

    private Keyboard(){

    }

    public static Keyboard get(){
        if(instance == null){
            instance = new Keyboard();
        }
        return instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        if(action == GLFW_PRESS){
            get().keyPressed[key] = true;
        }else if(action == GLFW_RELEASE){
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int key){
        if(key>get().keyPressed.length-1) {
            throw new IllegalArgumentException("Button press not in array keyPressed in public static boolean isKeyPressed; tldr kys");
        }
        return get().keyPressed[key];
    }
}
