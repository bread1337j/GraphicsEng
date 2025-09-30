package org.lwjgl.Graphics.Groups;

import org.lwjgl.Graphics.Objects.AObject;
import org.lwjgl.Util.Coordinate;

import java.util.LinkedList;
import java.util.List;

public class CullingChunk {
    public List<AObject> arr;
    public int x;
    public int y;
    public CullingChunk(int x, int y){
        this.x = x;
        this.y = y;
        arr = new LinkedList<>(); //SECOND EVER RECORDED USE CASE THIS IS HUGE
    }
    public CullingChunk(Coordinate cord){
        this.x = cord.cord[0];
        this.y = cord.cord[1];
        arr = new LinkedList<>();
    }

    public String toString(){
        return "(" + this.x + ", " + this.y + ")";
    }


    public void addObj(AObject obj){
        arr.add(obj);
    }

    public AObject inladdObj(AObject obj){ //addobj but for in-line chicanery
        arr.add(obj);
        return obj;
    }
}
