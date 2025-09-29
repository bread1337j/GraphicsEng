package org.lwjgl.Graphics.Objects;

public class Circle extends AObject{
    private static final int[] indices = new int[]{2, 1, 0, 0, 1, 3};
    public Circle(float x1, float y1, float z1, float r1, float g1, float b1, float a1, float t_x1, float t_y1, int t_type1,
                   float x2, float y2, float z2, float r2, float g2, float b2, float a2, float t_x2, float t_y2, int t_type2,
                   float x3, float y3, float z3, float r3, float g3, float b3, float a3, float t_x3, float t_y3, int t_type3,
                   float x4, float y4, float z4, float r4, float g4, float b4, float a4, float t_x4, float t_y4, int t_type4,
                   float rad, float c_x, float c_y) {
        this.vertices = new float[]{
                x1, y1, z1, r1, g1, b1, a1, t_x1+x1, t_y1+y1, t_type1, c_x, c_y, rad, 1,
                x2, y2, z2, r2, g2, b2, a2, t_x2+x2, t_y2+y2, t_type2, c_x, c_y, rad, 1,
                x3, y3, z3, r3, g3, b3, a3, t_x3+x3, t_y3+y3, t_type3, c_x, c_y, rad, 1,
                x4, y4, z4, r4, g4, b4, a4, t_x4+x4, t_y4+y4, t_type4, c_x, c_y, rad, 1
        };
        this.center = new float[]{c_x, c_y};
    }

    public Circle(float x, float y, float z, float rad, float r, float g, float b, float a, float t_x, float t_y, int t_type) {
        this(x-rad, y+rad, z, r, g, b, a, t_x, t_y, t_type,
                x+rad, y-rad, z, r, g, b, a, t_x, t_y, t_type,
                x-rad, y-rad, z, r, g, b, a, t_x, t_y, t_type,
                x+rad, y+rad, z, r, g, b, a, t_x, t_y, t_type,
                rad * rad, x, y);
    }


    @Override
    public int[] loadObj(float[] vertexArr, int[] elementArr, int pointer, int eboPointer, int indice) {
        //System.out.println("I was loaded!");
        for(int i=pointer; i<pointer+vertices.length; i++){
            vertexArr[i] = vertices[i-pointer];
        }
        for(int i=eboPointer; i<eboPointer+indices.length; i++){
            elementArr[i] = indice + indices[i-eboPointer];
        }
        return new int[]{vertices.length + pointer, indices.length + eboPointer, indice + getIndiceNum()};
    }
    @Override
    public int getIndiceLen() {
        return 6;
    }

    @Override
    public int getVertexLen() {
        return 56;
    }

    @Override
    public int getIndiceNum() {
        return 4;
    }

    @Override
    public float[] getCenter(){
        return this.center; //this is better than making a new array every time since it will not be being changed by any functions, but it does also mean that each circle has to keep an extra array. I think it's fine though in the grand scheme of things this is like a drop of water in a tsunami
    }
}

