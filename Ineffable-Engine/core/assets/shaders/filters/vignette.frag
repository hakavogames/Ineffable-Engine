#version 330

uniform sampler2D u_tex0;

varying vec2 v_texCoords;

const vec2 center=vec2(0.5);
const float vignetteX=0.8,vignetteY=0.25;
const float vignetteIntensity=1;

void main()
{
	vec3 rgb=texture(u_tex0,v_texCoords).rgb;
	
	float d=distance(v_texCoords,center);
	float factor=smoothstep(vignetteX,vignetteY,d);
	rgb=rgb*factor+rgb*(1.0-factor)*(1.0-vignetteIntensity);
	
	gl_FragColor=vec4(rgb,1.0);
}