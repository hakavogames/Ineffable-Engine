package com.hakavo.ineffable.rendering.postfx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.rendering.Renderer;

public abstract class Filter
{
    public boolean active=true;
    public Vector2 resolution=new Vector2(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    protected String screenSize="u_screenSize";
    //protected String texture0="u_tex0",texture1="u_tex1",texture2="u_tex2",texture3="u_tex3",texture4="u_tex4";
    //protected Texture input0,input1,input2,input3,input4;
    //protected boolean useTex0=false,useTex1=false,useTex2=false,useTex3=false,useTex4=false;
    protected Array<TextureSampler> samplers=new Array<TextureSampler>();
    protected ShaderProgram shader;
    protected Renderer renderer;
    private final Matrix4 projection=new OrthographicCamera(1,1).combined;
    private final Texture blankTexture=new Texture(1,1,Pixmap.Format.RGB565);
    
    /*public void bind(Texture input0,Texture input1,Texture input2,Texture input3,Texture input4)
    {
        this.input0=input0;
        this.input1=input1;
        this.input2=input2;
        this.input3=input3;
        this.input4=input4;
    }*/
    public void bind(Texture... inputs) {
        for(int i=0;i<Math.min(inputs.length,samplers.size);i++) {
            samplers.get(i).texture=inputs[i];
        }
    }
    public void setUniforms() {
    }
    public void render(FrameBuffer out)
    {
        if(out!=null)out.begin();
        SpriteBatch sb=GameServices.getSpriteBatch();
        sb.begin();
        sb.setShader(shader);
        
        shader.setUniformf(screenSize,resolution.x,resolution.y);
        shader.setUniformMatrix("u_projection",projection);
        for(TextureSampler sampler : samplers) {
            if(!sampler.enabled||sampler.texture==null)continue;
            shader.setUniformi(sampler.uniform,sampler.texture.getTextureObjectHandle());
            Gdx.gl.glActiveTexture(GL30.GL_TEXTURE0+sampler.texture.getTextureObjectHandle());
            Gdx.gl.glBindTexture(GL30.GL_TEXTURE_2D,sampler.texture.getTextureObjectHandle());
        }
        Gdx.gl.glActiveTexture(GL30.GL_TEXTURE0);
        setUniforms();
        sb.draw(blankTexture,-1,-1,2,2,0,0,1,1,false,true);
        sb.end();
        if(out!=null)out.end();
    }
    public ShaderProgram getShader() {
        return shader;
    }
    public TextureSampler getSampler(String uniform) {
        for(TextureSampler sampler : samplers)
            if(sampler.uniform.equals(uniform))
                return sampler;
        return null;
    }
    public static class TextureSampler {
        public boolean enabled=true;
        public Texture texture;
        public String uniform="u_texture";
        
        public TextureSampler(String uniform,Texture texture,boolean enabled) {
            this.uniform=uniform;
            this.enabled=enabled;
            this.texture=texture;
        }
        public TextureSampler(String uniform) {
            this(uniform,null,true);
        }
    }
}
