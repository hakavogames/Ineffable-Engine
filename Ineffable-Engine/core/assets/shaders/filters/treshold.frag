#version 330

uniform sampler2D u_texture;
uniform float u_treshold;
varying vec2 v_texCoords;

float luma(vec3 color)
{
    return dot(color,vec3(0.299,0.587,0.114));
}

void main()
{
	vec2 uv=v_texCoords;
    vec3 col=texture(u_texture,v_texCoords).rgb;
    col*=1.0-step(luma(col),u_treshold);
	gl_FragColor=vec4(col,1.0);
}
