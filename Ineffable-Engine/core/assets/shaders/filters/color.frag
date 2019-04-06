#version 330

uniform sampler2D u_texture;
uniform float u_saturation=1.0;
uniform vec3 u_curves=vec3(1.0,1.0,1.0);

varying vec2 v_texCoords;

vec3 saturate(vec3 rgb,float adjustment)
{
    const vec3 W=vec3(0.2125,0.7154,0.0721);
    vec3 intensity=vec3(dot(rgb,W));
    return mix(intensity,rgb,adjustment);
}
void main()
{
	vec3 rgb=texture(u_texture,v_texCoords).rgb;
	rgb=saturate(rgb,u_saturation);
	rgb.r=pow(rgb.r,u_curves.r);
	rgb.g=pow(rgb.g,u_curves.g);
	rgb.b=pow(rgb.b,u_curves.b);
	
	gl_FragColor=vec4(rgb,1.0);
}