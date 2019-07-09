#version 330

uniform sampler2D u_texture;
uniform float u_intensity=1;
uniform float u_downscale=1;
uniform vec2 u_screenSize;
uniform float u_time;
varying vec2 v_texCoords;

float rand(vec2 co){return fract(sin(dot(co.xy ,vec2(12.9898,78.233)))*43758.5453);}

void main()
{
	vec4 color=texture(u_texture,v_texCoords);
	vec2 uv=floor(gl_FragCoord.xy/u_downscale)/u_screenSize;
	float noise=rand(uv+vec2(u_time))*u_intensity;//-u_intensity/2.0;

	gl_FragColor=color+noise;
	gl_FragColor.a=1.0;
}
