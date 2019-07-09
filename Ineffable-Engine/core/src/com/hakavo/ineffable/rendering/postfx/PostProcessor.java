package com.hakavo.ineffable.rendering.postfx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.rendering.postfx.filters.Copy;
import com.hakavo.ineffable.rendering.postfx.utils.*;

public class PostProcessor implements Disposable {
    protected final PingPongFrameBuffer buffer;
    public final Array<Effect> effects=new Array<Effect>();
    private final Array<Effect> active=new Array<Effect>();
    private final Copy copy=new Copy();
    
    public PostProcessor(int width,int height,Format format) {
        this(width,height,format,false);
    }
    public PostProcessor(int width,int height,Format format,boolean hasDepth) {
        ShaderProgram.pedantic=false;
        buffer=new PingPongFrameBuffer(width,height,format,hasDepth);
    }
    
    public PostProcessor render(FrameBuffer input,FrameBuffer output) {
        boolean depth=Gdx.gl.glIsEnabled(GL30.GL_DEPTH_TEST);
        boolean blend=Gdx.gl.glIsEnabled(GL30.GL_BLEND);
        Gdx.gl.glDisable(GL30.GL_DEPTH_TEST);
        Gdx.gl.glDisable(GL30.GL_BLEND);
        
        Array<Effect> items=active;
        getActiveEffects(items);
        if(items.size==0)return this;
        
        buffer.reset();
        for(int i=0;i<items.size;i++) {
            items.get(i).init(getWidth(),getHeight(),getFormat());
            FrameBuffer src,dest;
            if(i==0)src=input;
            else src=buffer.getLast();
            if(i==items.size-1&&output!=null)dest=output;
            else dest=buffer.getCurrent();
            active.get(i).render(src,dest);
            buffer.next();
        }
        
        if(depth)Gdx.gl.glEnable(GL30.GL_DEPTH_TEST);
        if(blend)Gdx.gl.glEnable(GL30.GL_BLEND);
        
        return this;
    }
    
    public void toRenderBuffer() {
        ShaderProgram shader=copy.getShader();
        Mesh quad=FullScreenQuad.getQuad();
        shader.begin();
        buffer.getLast().getColorBufferTexture().bind(0);
        Gdx.gl.glActiveTexture(GL30.GL_TEXTURE0);
        quad.render(shader,GL30.GL_TRIANGLES);
        shader.end();
    }
    
    private Array<Effect> getActiveEffects(Array<Effect> out) {
        out.clear();
        for(Effect effect : effects)
            if(effect.enabled)
                out.add(effect);
        return out;
    }
    
    public int getWidth() {
        return buffer.texture1.getWidth();
    }
    public int getHeight() {
        return buffer.texture1.getHeight();
    }
    public Format getFormat() {
        return buffer.texture1.getColorBufferTexture().getTextureData().getFormat();
    }
    public PingPongFrameBuffer getBuffer() {
        return buffer;
    }
    
    @Override
    public void dispose() {
        buffer.dispose();
        for(Effect effect : effects)
            effect.dispose();
    }
}
