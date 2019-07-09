package com.hakavo.ineffable.rendering.postfx.effects;
import com.badlogic.gdx.graphics.Pixmap;
import com.hakavo.ineffable.rendering.postfx.filters.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.hakavo.ineffable.rendering.postfx.Effect;


public class Vignette extends Effect {
    protected static VignetteFilter filter;
    
    public Vignette() {
        filter=new VignetteFilter();
    }
    
    @Override
    public void create(int width,int height,Pixmap.Format format) {
    }
    @Override
    public void render(FrameBuffer src,FrameBuffer dest) {
        filter.setInput(src).setOutput(dest).render();
    }

    @Override
    public void dispose() {
    }
}