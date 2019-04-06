#version 330
attribute vec3 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_worldMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;

varying vec3 v_normal;
varying vec4 v_position;
varying vec2 v_texCoords;
varying vec4 v_color;

void main()
{
	v_position=u_worldMatrix*vec4(a_position.xyz,1.0);
	v_texCoords=a_texCoord0;
	v_color=a_color;
	
	mat4 temp = u_viewMatrix*u_worldMatrix;
	temp[0][0] = 1;
	temp[0][1] = 0;
	temp[0][2] = 0;
 
 
	temp[1][0] = 0;
	temp[1][1] = 1;
	temp[1][2] = 0;
 
 
	temp[2][0] = 0;
	temp[2][1] = 0;
	temp[2][2] = 1;
	
	gl_Position=u_projectionMatrix*temp*vec4(a_position.xyz,1.0);
	v_position.w=gl_Position.w;
}