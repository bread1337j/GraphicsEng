package org.lwjgl.Graphics.Assets;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int shader;
    private String vertexSource, fragmentSource;
    private String filepath;
    private boolean inUse;

    public Shader(String filepath){
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-z]+)");

            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\n", index);
            String firstPattern = source.substring(index, eol).trim();

            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            }else if(firstPattern.equals("fragment")){
                fragmentSource = splitString[1];
            }else{
                throw new IOException("Unexpected token '"+firstPattern+"'");
            }

            if(secondPattern.equals("vertex")){
                vertexSource = splitString[2];
            }else if(secondPattern.equals("fragment")){
                fragmentSource = splitString[2];
            }else{
                throw new IOException("Unexpected token '"+secondPattern+"'");
            }

        }catch(IOException e){
            e.printStackTrace();
            assert false : "Could not open file for shader '" + filepath + "'";
        }
    }

    public void compile(){
        int vertexID, fragmentID;

        vertexID = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(vertexID, GL_COMPILE_STATUS);
            System.out.println("Error compiling vertex shader '" + filepath + "': " + glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if(success == GL_FALSE){
            int len = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
            System.out.println("Error compiling fragment shader '" + filepath + "': " + glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        shader = glCreateProgram();
        glAttachShader(shader, vertexID);
        glAttachShader(shader, fragmentID);
        glLinkProgram(shader);

        success = glGetProgrami(shader, GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shader, GL_LINK_STATUS);
            System.out.println("Error linking shader '" + filepath + "': " + glGetProgramInfoLog(shader, len));
            assert false : "";
        }

    }

    public void use(){
        if(!inUse){
            glUseProgram(shader);
            inUse = true;
        }
    }

    public void detach(){
        glUseProgram(0);
        inUse = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4){
        int varLocation = glGetUniformLocation(shader, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat3f(String varName, Matrix3f mat3) {
        int varLocation = glGetUniformLocation(shader, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }


    public void uploadVec4f(String varName, Vector4f vec){
        int varLocation = glGetUniformLocation(shader, varName);
        use();
        glUniform4f(varLocation, vec.x, vec.y, vec.z, vec.w);
    }

    public void uploadVec3f(String varName, Vector3f vec) {
        int varLocation = glGetUniformLocation(shader, varName);
        use();
        glUniform3f(varLocation, vec.x, vec.y, vec.z);
    }

    public void uploadVec2f(String varName, Vector2f vec) {
        int varLocation = glGetUniformLocation(shader, varName);
        use();
        glUniform2f(varLocation, vec.x, vec.y);
    }


    public void uploadFloat(String varName, float val){
        int varLocation = glGetUniformLocation(shader, varName);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val){
        int varLocation = glGetUniformLocation(shader, varName);
        use();
        glUniform1i(varLocation, val);
    }

    public void uploadTexture(String varName, int slot){
        int varLocation = glGetUniformLocation(shader, varName);
        use();
        glUniform1i(varLocation, slot);
    }

    public void uploadIntArray(String varName, int[] val){
        int varLocation = glGetUniformLocation(shader, varName);
        use();
        glUniform1iv(varLocation, val);
    }
}
