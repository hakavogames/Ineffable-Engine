#version 330

uniform sampler2D u_texture;

uniform float u_radius=0.008;
uniform float u_samples=10;
uniform vec2 u_direction;

varying vec2 v_texCoords;

void main()
{
	vec2 offset=u_radius*u_direction;
	vec3 color=vec3(0.);
	float inc=1.0/u_samples;
	float weight=0.;
	for (float i=-0.5;i<=0.5;i+=inc)
	{
		color+=texture2D(u_texture,v_texCoords+i*offset).rgb;
		weight+=1.;
	}
	color/=weight;
	
	gl_FragColor=vec4(color,1.0);
}