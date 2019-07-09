package com.hakavo.ineffable.rendering.postfx.effects;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.hakavo.ineffable.rendering.postfx.filters.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Vector2;
import com.hakavo.ineffable.rendering.postfx.Effect;
import com.hakavo.ineffable.rendering.postfx.utils.PingPongFrameBuffer;


public class Bloom extends Effect {
    protected Blur hor,ver;
    protected TresholdFilter treshold;
    protected Combine combine;
    private PingPongFrameBuffer buffer;
    private float radius;
    
    public Bloom(float radius,float treshold) {
        this.radius=radius;
        hor=new Blur(Vector2.X,1,1);
        ver=new Blur(Vector2.Y,1,1);
        this.treshold=new TresholdFilter(treshold);
        combine=new Combine(0.8f,0.2f);
    }
    @Override
    public void create(int width,int height,Format format) {
        float w=1f/width;
        float h=1f/height;
        hor.radius=w*radius;hor.samples=(int)radius+1;
        ver.radius=h*radius;ver.samples=(int)radius+1;
        buffer=new PingPongFrameBuffer(width/2,height/2,format);
        //buffer.setFilter(TextureFilter.Linear,TextureFilter.Linear);
    }
    @Override
    public void render(FrameBuffer src,FrameBuffer dest) {
        buffer.reset();
        {
            treshold.setInput(src).setOutput(buffer.getCurrent()).render();
            buffer.next();
            hor.setInput(buffer.getLast()).setOutput(buffer.getCurrent()).render();
            buffer.next();
            ver.setInput(buffer.getLast()).setOutput(buffer.getCurrent()).render();
        }
        combine.setInput(src.getColorBufferTexture(),buffer.getCurrent().getColorBufferTexture())
               .setOutput(dest).render();
    }

    @Override
    public void dispose() {
        buffer.dispose();
    }
}