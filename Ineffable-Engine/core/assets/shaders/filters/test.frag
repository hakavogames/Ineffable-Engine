#version 330

uniform float u_time;
uniform vec2 u_screenSize;
varying vec2 v_texCoords;

vec3 saturate(vec3 rgb,float adjustment)
{
    const vec3 W = vec3(0.2125, 0.7154, 0.0721);
    vec3 intensity = vec3(dot(rgb, W));
    return mix(intensity, rgb, adjustment);
}

void main()
{
	vec2 uv=v_texCoords;
	vec3 col=0.5+0.5*cos(u_time+uv.xyx+vec3(0,2,4));
	col=saturate(col,0.6);
	gl_FragColor=vec4(col,1.0);
	//gl_FragColor=vec4(1.0);
}