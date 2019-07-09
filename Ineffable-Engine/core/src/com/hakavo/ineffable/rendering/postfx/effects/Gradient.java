package com.hakavo.ineffable.rendering.postfx.effects;
import com.badlogic.gdx.graphics.Pixmap;
import com.hakavo.ineffable.rendering.postfx.filters.Test;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.hakavo.ineffable.rendering.postfx.Effect;


public class Gradient extends Effect {
    protected static Test filter;
    
    public Gradient() {
        filter=new Test();
    }
    
    @Override
    public void create(int width,int height,Pixmap.Format format) {
    }
    @Override
    public void render(FrameBuffer src,FrameBuffer dest) {
        filter.setOutput(dest).render();
    }

    @Override
    public void dispose() {
    }
}