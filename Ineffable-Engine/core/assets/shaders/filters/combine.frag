#version 330

uniform sampler2D u_texture0;
uniform sampler2D u_texture1;
uniform float u_tex0Intensity;
uniform float u_tex1Intensity;

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
    vec3 col1=texture(u_texture0,v_texCoords).rgb*u_tex0Intensity;
    vec3 col2=texture(u_texture1,v_texCoords).rgb*u_tex1Intensity;
    //col1*=(1.0-col2);

    gl_FragColor=vec4(col1+col2,1.0);
}
