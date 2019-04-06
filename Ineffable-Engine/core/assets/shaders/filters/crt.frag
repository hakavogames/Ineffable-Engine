#version 330

uniform sampler2D u_texture;
uniform vec2 u_screenSize;
uniform float u_time;

const float offset=0.0005;
const float bDistortion=0.1;

varying vec2 v_texCoords;

vec2 barrelDistortion(vec2 coord,float distortion)
{
	vec2 cc = coord - 0.5;
	float dist = dot(cc, cc) * distortion;
	return (coord + cc * (1.0 + dist) * dist);
}
vec2 scanDistort(vec2 uv)
{
	float amplitude = 0.5;
	float scan1 = clamp(cos(uv.y * 2.0 + u_time), 0.0, 1.0);
	float scan2 = clamp(cos(uv.y * 2.0 + u_time + 4.0) * amplitude, 0.0, 1.0) ;
	float amount = scan1 * scan2 * uv.x;
	uv.x -= 0.05 * amount;
	return uv;
}
vec3 chromaticAberration()
{
	float t=10;
	vec4 c1 = texture(u_texture, barrelDistortion(scanDistort(gl_FragCoord.xy/(u_screenSize.xy+vec2(t * .2,.0))),bDistortion));
	vec4 c2 = texture(u_texture, barrelDistortion(scanDistort(gl_FragCoord.xy/(u_screenSize.xy+vec2(t * .5,.0))),bDistortion));
	vec4 c3 = texture(u_texture, barrelDistortion(scanDistort(gl_FragCoord.xy/(u_screenSize.xy+vec2(t * .9,.0))),bDistortion));
	return vec3(c3.r,c2.g,c1.b);
}

void main()
{
	vec2 uv=barrelDistortion(scanDistort(v_texCoords),bDistortion);
	vec2 barrel=barrelDistortion(v_texCoords,bDistortion);
	
	vec3 color=texture(u_texture,uv).rgb;
	
	//color=chromaticAberration();
	color.r = texture(u_texture,fract(vec2(uv.x+offset,uv.y))).r;
	color.g = texture(u_texture,fract(vec2(uv.x+0.000,uv.y))).g;
	color.b = texture(u_texture,fract(vec2(uv.x-offset,uv.y))).b;
	
	color=clamp(color*0.5+0.5*color*color*1.2,0.0,1.0);
	color*=0.9+0.1*sin(10.0*u_time-uv.y*1000.0);
	color*=0.97+0.03*sin(110.0*u_time);
	
	if(barrel.x-offset<0||barrel.x+offset>1||barrel.y<0||barrel.y>1)color*=0;
	
	gl_FragColor=vec4(color,1.0);
}