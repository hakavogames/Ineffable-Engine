#version 330
#define PCF_SIZE 1
// Deferred lighting pass
uniform sampler2D u_color;
uniform sampler2D u_position;
uniform sampler2D u_normal;
uniform sampler2D u_ao;
uniform sampler2D u_material;
//uniform sampler2D u_shadow;

uniform vec3 u_camPos;
/*uniform mat4 u_shadowProj;
uniform vec3 u_shadowPos;
uniform vec2 u_shadowResolution;*/
uniform vec3 u_ambient=vec3(0.4f,0.4f,0.4f);
varying vec2 v_texCoords;

struct LightSource
{
	bool active;
	vec3 diffuse;
	vec4 position;
	float range;
};
struct ShadowMap {
	mat4 proj;
	vec3 pos;
	vec2 res;
	sampler2D map;
};
uniform int u_lights;
uniform int u_csmSize;
uniform LightSource light[100];
uniform ShadowMap u_shadow[64];

float texture2DCompare(sampler2D depths, vec2 uv, float compare){
    float depth = texture2D(depths, uv).r;
    return step(compare, depth);
}
float texture2DShadowLerp(sampler2D depths, vec2 size, vec2 uv, float compare){
    vec2 texelSize = vec2(1.0)/size;
    vec2 f = fract(uv*size+0.5);
    vec2 centroidUV = floor(uv*size+0.5)/size;

    float lb = texture2DCompare(depths, centroidUV+texelSize*vec2(0.0, 0.0), compare);
    float lt = texture2DCompare(depths, centroidUV+texelSize*vec2(0.0, 1.0), compare);
    float rb = texture2DCompare(depths, centroidUV+texelSize*vec2(1.0, 0.0), compare);
    float rt = texture2DCompare(depths, centroidUV+texelSize*vec2(1.0, 1.0), compare);
    float a = mix(lb, lt, f.y);
    float b = mix(rb, rt, f.y);
    float c = mix(a, b, f.x);
    return c;
}
float PCF(sampler2D depths, vec2 size, vec2 uv, float compare){
    float result = 0.0;
    for(int x=-PCF_SIZE; x<=PCF_SIZE; x++){
        for(int y=-PCF_SIZE; y<=PCF_SIZE; y++){
            vec2 off = vec2(x,y)/size;
            result += texture2DShadowLerp(depths, size, uv+off, compare);
        }
    }
    return result/((PCF_SIZE*2+1.0)*(PCF_SIZE*2+1.0));
}

void main()
{
	vec4 texColor=texture2D(u_color,v_texCoords.xy);
	vec4 pos=texture2D(u_position,v_texCoords).rgba;
	vec2 material=texture2D(u_material,v_texCoords).rg;
	vec4 normalTex=texture2D(u_normal,v_texCoords).rgba;
	vec3 normal=normalize(normalTex.rgb*2.0-1.0);
	vec3 viewDir=normalize(u_camPos.xyz-pos.xyz);
	vec3 diff=vec3(0);
	vec3 spec=vec3(0);
	vec4 lightDir=vec4(0);
	
	if(normalTex.a==1.0)
		//gl_FragColor=vec4(0.82,0.93,1,1);
		gl_FragColor=vec4(light[0].diffuse,1);
	else
	{
		
		float roughness=material.r;
		for(int i=0;i<=u_lights;i++)
		{
			lightDir=light[i].position;
			if(!light[i].active)continue;
			if(lightDir.w==0.0)
			{
				vec3 reflectDir=reflect(lightDir.xyz,normal);
				vec3 H=normalize(lightDir.xyz-viewDir);
				float NdH=clamp(dot(H,-normal),0,1);
				float NdL=clamp(dot(normal,-lightDir.xyz),0.0,1.0);
				float k=1.999/(roughness*roughness);
				vec3 specular=min(1,3.0*0.0398*k)*pow(NdH,k)*light[i].diffuse*NdL*step(roughness,0.99);
				vec3 diffuse=NdL*light[i].diffuse;
				
				float fade=1;
				for(int i=0;i<u_csmSize;i++)
				{
					vec4 shadowTransform=u_shadow[i].proj*vec4(pos.xyz,1.0);
					vec3 shadowCoord=(shadowTransform.xyz/shadowTransform.w)*0.5+0.5;
					
					if(!(shadowCoord.x<0.0||shadowCoord.x>1.0||shadowCoord.y<0.0||shadowCoord.y>1.0||
						 shadowCoord.z<0.0||shadowCoord.z>1.0))
					{
						float f=max(dot(lightDir.xyz,normal),0.5)*0.85;
						float dist1=distance(u_shadow[i].pos.xyz,pos.xyz)-f;
						//float dist2=texture(u_shadow[i].map,shadowCoord.xy).r;
							fade=PCF(u_shadow[i].map,u_shadow[i].res,shadowCoord.xy,dist1);
						break;
					}
				}
				diff+=diffuse*fade;spec+=specular*fade;
			}
			else
			{
				vec3 surfaceToLight=light[i].position.xyz-pos.xyz;
				vec3 H=normalize(normalize(surfaceToLight.xyz)+viewDir);
				float NdL=clamp(dot(normal,normalize(surfaceToLight).xyz),0.0,1.0);
				float NdH=clamp(dot(normal,H),0.0,1.0);
				
				float range=max(light[i].range,0.1);
				float dist=pow(1.0-clamp(length(surfaceToLight)/range,0.0,1.0),2);
				
				float k=1.999/(roughness*roughness);
				vec3 specular=min(1,3.0*0.0398*k)*pow(NdH,k)*light[i].diffuse*(1.0-step(NdL,0.001));
				vec3 diffuse=NdL*light[i].diffuse;
				
				diff+=diffuse*dist;
				spec+=specular*dist;
			}
		}
		vec3 ambient=u_ambient;
		texColor.rgb*=ambient*texture2D(u_ao,v_texCoords).r+diff+spec;
		
		gl_FragColor=vec4(texColor.rgb,1.0);
	}
}