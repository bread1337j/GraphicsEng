package org.lwjgl.Graphics.Objects;

public class Triangle extends AObject {
    private static final int[] indices = new int[]{0, 1, 2};

    public Triangle(float x1, float y1, float z1, float r1, float g1, float b1, float a1, float t_x1, float t_y1, int t_type1,
                    float x2, float y2, float z2, float r2, float g2, float b2, float a2, float t_x2, float t_y2, int t_type2,
                    float x3, float y3, float z3, float r3, float g3, float b3, float a3, float t_x3, float t_y3, int t_type3) {
        this.vertices = new float[]{
                x1, y1, z1, r1, g1, b1, a1, t_x1+x1, t_y1+y1, t_type1, x1, y1, 0, 0,
                x2, y2, z2, r2, g2, b2, a2, t_x2+x2, t_y2+y2, t_type2, x2, y2,  0, 0,
                x3, y3, z3, r3, g3, b3, a3, t_x3+x3, t_y3+y3, t_type3, x3, y3, 0, 0 //triangle, so the center and type don't matter.
        };
    }

    public Triangle(float c_x, float c_y, float c_z, float w, float h, float r, float g, float b, float a, float t_x, float t_y, int t_type){
        this(
                c_x-w/2, c_y-h/2, c_z-w/2, r, g, b, a, t_x, t_y, t_type,
                c_x+w/2, c_y-h/2, c_z-w/2, r, g, b, a, t_x, t_y, t_type,
                c_x+w/2, c_y+h/2, c_z-w/2, r, g, b, a, t_x, t_y, t_type
        );
    }

    @Override
    public int[] loadObj(float[] vertexArr, int[] elementArr, int pointer, int eboPointer, int indice) {
        for(int i=pointer; i<pointer+vertices.length; i++){
            vertexArr[i] = vertices[i-pointer];
        }
        for(int i=eboPointer; i<eboPointer+indices.length; i++){
            elementArr[i] = indices[i-eboPointer]+eboPointer;
        }
        return new int[]{vertices.length + pointer, indices.length + eboPointer, indice + getIndiceNum()};
    }

    @Override
    public int getIndiceLen() {
        return 3;
    }

    @Override
    public int getVertexLen() {
        return 42;
    }

    @Override
    public int getIndiceNum() {
        return 3;
    }


}
