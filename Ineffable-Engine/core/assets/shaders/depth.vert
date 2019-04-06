#version 330
attribute vec3 a_position;
attribute vec2 a_texCoord0;

uniform mat4 u_worldMatrix;
uniform mat4 u_viewMatrix;
uniform mat4 u_projectionMatrix;
uniform vec3 u_cameraPosition;

varying vec4 v_position;
varying vec2 v_texCoords;

void main()
{
	v_position=u_worldMatrix*vec4(a_position.xyz,1.0);
	v_texCoords=a_texCoord0;
	
	gl_Position=u_projectionMatrix*u_viewMatrix*vec4(v_position.xyz,1.0);
}