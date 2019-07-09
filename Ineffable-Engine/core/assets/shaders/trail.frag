#version 150

uniform vec4 u_color;
uniform vec4 u_noTexture;
uniform sampler2D u_texture;

varying vec4 v_color;
varying vec2 v_texCoords;

void main()
{
	vec4 color=u_color*v_color*clamp(texture2D(u_texture,v_texCoords)+u_noTexture,0.0,1.0);
	gl_FragColor=color;
}
