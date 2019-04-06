#version 330

uniform sampler2D u_tex0;
uniform sampler2D u_tex1;
uniform vec2 u_screenSize;
uniform vec3 u_cameraPosition;
uniform float u_far,u_near;
 
const float blurclamp = 0.015;
uniform float bias = 0.05;
uniform float focus=0.2;

varying vec2 v_texCoords;
 
void main()
{
 
        float aspectratio = u_screenSize.x/u_screenSize.y;
        vec2 aspectcorrect = vec2(1.0,aspectratio);
		
		vec4 position=texture(u_tex1,v_texCoords).xyzw;
        float depth1   = length(position.xyz-u_cameraPosition)/(u_far-u_near);
 
        float factor = ( depth1 - focus );
         
        vec2 dofblur = vec2 (clamp( factor * bias, -blurclamp, blurclamp ));
 
 
        vec4 col = vec4(0.0);
       
		//if(position.w<0)gl_FragColor.rgb=texture2D(u_tex0, v_texCoords).rgb;
		//else
		{
			col += texture2D(u_tex0, v_texCoords);
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.0,0.4 )*aspectcorrect) * dofblur);
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.15,0.37 )*aspectcorrect) * dofblur);
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.29,0.29 )*aspectcorrect) * dofblur);
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.37,0.15 )*aspectcorrect) * dofblur);       
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.4,0.0 )*aspectcorrect) * dofblur);   
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.37,-0.15 )*aspectcorrect) * dofblur);       
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.29,-0.29 )*aspectcorrect) * dofblur);       
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.15,-0.37 )*aspectcorrect) * dofblur);
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.0,-0.4 )*aspectcorrect) * dofblur); 
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.15,0.37 )*aspectcorrect) * dofblur);
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.29,0.29 )*aspectcorrect) * dofblur);
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.37,0.15 )*aspectcorrect) * dofblur); 
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.4,0.0 )*aspectcorrect) * dofblur); 
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.37,-0.15 )*aspectcorrect) * dofblur);       
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.29,-0.29 )*aspectcorrect) * dofblur);       
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.15,-0.37 )*aspectcorrect) * dofblur);
		   
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.15,0.37 )*aspectcorrect) * dofblur*0.9);
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.37,0.15 )*aspectcorrect) * dofblur*0.9);           
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.37,-0.15 )*aspectcorrect) * dofblur*0.9);           
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.15,-0.37 )*aspectcorrect) * dofblur*0.9);
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.15,0.37 )*aspectcorrect) * dofblur*0.9);
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.37,0.15 )*aspectcorrect) * dofblur*0.9);            
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.37,-0.15 )*aspectcorrect) * dofblur*0.9);   
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.15,-0.37 )*aspectcorrect) * dofblur*0.9);   
		   
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.29,0.29 )*aspectcorrect) * dofblur*0.7);
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.4,0.0 )*aspectcorrect) * dofblur*0.7);       
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.29,-0.29 )*aspectcorrect) * dofblur*0.7);   
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.0,-0.4 )*aspectcorrect) * dofblur*0.7);     
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.29,0.29 )*aspectcorrect) * dofblur*0.7);
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.4,0.0 )*aspectcorrect) * dofblur*0.7);     
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.29,-0.29 )*aspectcorrect) * dofblur*0.7);   
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.0,0.4 )*aspectcorrect) * dofblur*0.7);
							 
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.29,0.29 )*aspectcorrect) * dofblur*0.4);
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.4,0.0 )*aspectcorrect) * dofblur*0.4);       
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.29,-0.29 )*aspectcorrect) * dofblur*0.4);   
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.0,-0.4 )*aspectcorrect) * dofblur*0.4);     
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.29,0.29 )*aspectcorrect) * dofblur*0.4);
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.4,0.0 )*aspectcorrect) * dofblur*0.4);     
			col += texture2D(u_tex0, v_texCoords + (vec2( -0.29,-0.29 )*aspectcorrect) * dofblur*0.4);   
			col += texture2D(u_tex0, v_texCoords + (vec2( 0.0,0.4 )*aspectcorrect) * dofblur*0.4);     
                       
			gl_FragColor = col/41.0;
		}
        gl_FragColor.a = 1.0;
}