package org.lwjgl.Graphics.Objects;

public class Rect extends AObject {
    private static final int[] indices = new int[]{2, 1, 0, 0, 1, 3};
    public Rect(float x1, float y1, float z1, float r1, float g1, float b1, float a1,
                    float x2, float y2, float z2, float r2, float g2, float b2, float a2,
                    float x3, float y3, float z3, float r3, float g3, float b3, float a3,
                    float x4, float y4, float z4, float r4, float g4, float b4, float a4) {
        this.vertices = new float[]{
                x1, y1, z1, r1, g1, b1, a1, x1, y1, 0,
                x2, y2, z2, r2, g2, b2, a2, x2, y2, 0,
                x3, y3, z3, r3, g3, b3, a3, x3, y3, 0,
                x4, y4, z4, r4, g4, b4, a4, x4, y4, 0 //will have to be rendering a lot of circles to compensate for this chicanery
        };
    }

    public Rect(float x, float y, float z, float w, float h, float r, float g, float b, float a) {
        this(x, y+h, z, r, g, b, a,
                x+w, y, z, r, g, b, a,
                x, y, z, r, g, b, a,
                x+w, y+h, z, r, g, b, a);
    }


    @Override
    public int[] loadObj(float[] vertexArr, int[] elementArr, int pointer, int eboPointer, int indice) {
        //System.out.println("Starting at: " + pointer);
        //System.out.println("Expecting to end at: " + (pointer + vertices.length));
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
        return 40;
    }

    @Override
    public int getIndiceNum() {
        return 4;
    }
}
