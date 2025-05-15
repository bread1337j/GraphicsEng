package org.lwjgl.Graphics.Objects;

public class Circle extends AObject{
    private static final int[] indices = new int[]{2, 1, 0, 0, 1, 3};
    private Circle(float x1, float y1, float z1, float r1, float g1, float b1, float a1,
                float x2, float y2, float z2, float r2, float g2, float b2, float a2,
                float x3, float y3, float z3, float r3, float g3, float b3, float a3,
                float x4, float y4, float z4, float r4, float g4, float b4, float a4,
                   float rad, float c_x, float c_y) {
        this.vertices = new float[]{
                x1, y1, z1, r1, g1, b1, a1, c_x, c_y, rad, 1,
                x2, y2, z2, r2, g2, b2, a2, c_x, c_y, rad, 1,
                x3, y3, z3, r3, g3, b3, a3, c_x, c_y, rad, 1,
                x4, y4, z4, r4, g4, b4, a4, c_x, c_y, rad, 1
        };
    }

    public Circle(float x, float y, float z, float rad, float r, float g, float b, float a) {
        this(x-rad, y+rad, z, r, g, b, a,
                x+rad, y-rad, z, r, g, b, a,
                x-rad, y-rad, z, r, g, b, a,
                x+rad, y+rad, z, r, g, b, a,
                rad * rad, x, y);
    }


    @Override
    public int[] loadObj(float[] vertexArr, int[] elementArr, int pointer, int eboPointer, int indice) {
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
        return 44;
    }

    @Override
    public int getIndiceNum() {
        return 4;
    }
}

