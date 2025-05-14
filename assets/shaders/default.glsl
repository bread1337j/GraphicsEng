#type vertex

#version 410 core
#extension GL_ARB_separate_shader_objects : enable

layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aCenter;
layout (location=3) in int aType;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;
out vec2 fCenter;
out int fType;


void main(){
    fColor = aColor;
    fCenter = aCenter;
    fType = aType;
    gl_Position =  uProjection * uView * vec4(aPos, 1.0);
}

#type fragment
#version 410 core
#extension GL_ARB_separate_shader_objects : enable

in vec4 fColor;

out vec4 color;

void main(){
    color = fColor;
}