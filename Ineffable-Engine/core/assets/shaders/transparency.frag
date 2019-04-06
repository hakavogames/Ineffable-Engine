#version 330

uniform sampler2D u_texture;
uniform sampler2D u_depth;
uniform vec2 u_screenSize;

varying vec4 v_color;
varying vec4 v_position;
varying vec2 v_texCoords;

void main()
{
	vec4 diffuse=v_color*texture(u_texture,v_texCoords);
	float depth=texture2D(u_depth,gl_FragCoord.xy/u_screenSize).r;
	if(diffuse.a<0.2||depth<gl_FragCoord.z)discard;
	
	gl_FragColor=diffuse;
}