#version 330

uniform sampler2D u_texture;
uniform sampler2D u_blur;
uniform float treshold=0.4;
uniform float intensity=4.0;

varying vec2 v_texCoords;

float luma(vec3 color) {
	return dot(color,vec3(0.299,0.587,0.114));
}

void main()
{
	vec3 rgb=texture(u_texture,v_texCoords).rgb;
	vec3 blur=texture(u_blur,v_texCoords).rgb;
	
	vec3 color=mix(rgb,blur,clamp(luma(rgb)-treshold,0.0,1.0)*intensity);
	gl_FragColor=vec4(color,1.0);
}