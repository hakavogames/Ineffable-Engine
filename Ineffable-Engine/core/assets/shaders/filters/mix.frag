#version 330

uniform sampler2D u_texture0;
uniform sampler2D u_texture1;
uniform vec2 u_screenSize;

varying vec2 v_texCoords;

void main()
{
	vec4 t1=texture(u_texture0,v_texCoords);
	vec4 t2=texture(u_texture1,v_texCoords);
	
	gl_FragColor=mix(t1,t2,t2.a);
}