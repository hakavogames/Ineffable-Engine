#version 330

uniform sampler2D u_texture0;
uniform float u_exposure=2;
uniform float u_gamma=2.2;
varying vec2 v_texCoords;

vec3 saturate(vec3 rgb,float adjustment)
{
    const vec3 W = vec3(0.2125, 0.7154, 0.0721);
    vec3 intensity = vec3(dot(rgb, W));
    return mix(intensity, rgb, adjustment);
}

void main() 
{
	vec3 color=texture(u_texture0,v_texCoords).rgb;
	
	color=vec3(1.0)-exp(-color*u_exposure);
	color=pow(color,vec3(u_gamma));
	
	gl_FragColor.rgb=color;
}