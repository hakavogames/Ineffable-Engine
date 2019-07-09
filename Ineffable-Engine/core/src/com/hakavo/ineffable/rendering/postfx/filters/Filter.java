package com.hakavo.ineffable.rendering.postfx.filters;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.utils.Array;
import com.hakavo.ineffable.rendering.postfx.utils.FullScreenQuad;

public abstract class Filter<T> {
    protected ShaderProgram shader;
    public final Array<TextureUniform> textures=new Array<TextureUniform>();
    protected TextureUniform input=new TextureUniform(null);
    private FrameBuffer output;
    
    public Filter<T> setInput(Texture texture) {
        this.input.texture=texture;
        return this;
    }
    public Filter<T> setInput(FrameBuffer frameBuffer) {
        setInput(frameBuffer.getColorBufferTexture());
        return this;
    }
    public Filter<T> setOutput(FrameBuffer frameBuffer) {
        this.output=frameBuffer;
        return this;
    }
    
    public ShaderProgram getShader() {
        return shader;
    }
    
    public abstract void beforeRender();
    
    public void render() {
        Mesh mesh=FullScreenQuad.mesh;
        if(mesh==null)mesh=FullScreenQuad.generate();
        
        output.begin();
        shader.begin();
        beforeRender();
        
        if(input.texture!=null)
            input.setUnit(0).bind(shader);
        for(int i=0;i<textures.size;i++)
            textures.get(i).setUnit(i+1).bind(shader);
        Gdx.gl.glActiveTexture(GL30.GL_TEXTURE0);
        
        mesh.render(shader,GL30.GL_TRIANGLES);
        shader.end();
        output.end();
    }
    
    public TextureUniform getTextureUniform(String name) {
        int hash=name.hashCode();
        for(TextureUniform tex : textures)
            if(tex.uniform.hashCode()==hash)
                return tex;
        return null;
    }
    
    public static class TextureUniform {
        public String uniform;
        public Texture texture;
        public int unit;
        public boolean enabled;
        
        public TextureUniform(Texture texture) {
            this("u_texture",texture);
        }
        public TextureUniform(String uniform,Texture texture) {
            this(uniform,texture,0);
        }
        public TextureUniform(String uniform,Texture texture,int unit) {
            this(uniform,texture,unit,true);
        }
        public TextureUniform(String uniform,Texture texture,int unit,boolean enabled) {
            this.uniform=uniform;
            this.texture=texture;
            this.unit=unit;
            this.enabled=enabled;
        }
        public void bind(ShaderProgram shader) {
            texture.bind(unit);
            shader.setUniformi(uniform,unit);
        }
        public TextureUniform setUnit(int unit) {
            this.unit=unit;
            return this;
        }
    }
}
