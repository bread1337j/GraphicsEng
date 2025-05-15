#type vertex

#version 410 core
#extension GL_ARB_separate_shader_objects : enable

layout (location=0) in vec3 aPos;
layout (location=1) in vec4 aColor;
layout (location=2) in vec2 aCenter;
layout (location=3) in float aRadius; //terminal lobotomy
layout (location=4) in float aType;

uniform mat4 uProjection;
uniform mat4 uView;

out vec2 fPos;
out vec4 fColor;
out vec2 fCenter;
out float fRadius;
out float fType;


void main(){
    fPos = vec2(aPos);
    fColor = aColor;
    fCenter = aCenter; //vec2(uProjection * uView * vec4(aCenter, 0.0, 0.0));
    fRadius = aRadius;
    fType = aType;
    gl_Position = uProjection * uView * vec4(aPos, 1.0);
}

#type fragment
#version 410 core
#extension GL_ARB_separate_shader_objects : enable

in vec2 fPos;
in vec4 fColor;
in vec2 fCenter;
in float fRadius;
in float fType;

/*
Types reference:
0 = took normal pills
1 = circle
2 = squiggle
>2 = mental illness
*/

out vec4 color;


//float sendHelp(vec2 pos, vec2 center, float rad){
//  return ;
//}


void main(){

    if(fType == 1){
        color = vec4(
        fColor.r, fColor.g, fColor.b,
        pow(fPos.x - fCenter.x, 2) + pow(fPos.y - fCenter.y, 2) < fRadius? 1 :
        pow(fPos.x - fCenter.x, 2) + pow(fPos.y - fCenter.y, 2) < fRadius+0.001? 0.5 : 0); //this sucks but I cant make it not suck because im too stupid
    }else{
        color = fColor;
    }
}