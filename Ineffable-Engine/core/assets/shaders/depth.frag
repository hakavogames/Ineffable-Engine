#version 330
uniform sampler2D u_diffuse;
uniform vec4 noDiffuse;

uniform vec3 u_cameraPosition;
uniform float u_far;
uniform float u_near;

varying vec4 v_position;
varying vec2 v_texCoords;

void main()
{
	//if(texture(u_diffuse,v_texCoords).a+noDiffuse.a<0.4)discard;
	
	float dist=distance(v_position.xyz,u_cameraPosition.xyz);
	gl_FragColor.r=dist;
}