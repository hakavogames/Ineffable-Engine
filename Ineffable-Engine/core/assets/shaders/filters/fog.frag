#version 330

uniform sampler2D u_texture0;
uniform sampler2D u_texture1;

uniform vec3 u_fogColor;
uniform float u_start;
uniform float u_end;
uniform float u_near;
uniform float u_far;

varying vec2 v_texCoords;

float linearDepth(float depth)
{
    depth=2.0*depth-1.0;
    float zLinear=2.0*u_near*u_far/(u_far+u_near-depth*(u_far-u_near));
    return zLinear;
}

void main()
{
	vec3 rgb=texture(u_texture1,v_texCoords).rgb;
	
	gl_FragColor=vec4(rgb,1.0);
}