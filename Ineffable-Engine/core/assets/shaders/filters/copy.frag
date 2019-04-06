#version 330

uniform sampler2D u_tex0;
uniform vec2 u_screenSize;

varying vec2 v_texCoords;

void main()
{
	gl_FragColor=vec4(texture(u_tex0,v_texCoords).rgb,1.0);
}