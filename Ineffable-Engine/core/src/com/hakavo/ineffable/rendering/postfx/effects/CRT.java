package com.hakavo.ineffable.rendering.postfx.effects;
import com.badlogic.gdx.graphics.Pixmap;
import com.hakavo.ineffable.rendering.postfx.filters.*;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.hakavo.ineffable.rendering.postfx.Effect;


public class CRT extends Effect {
    protected static CRTFilter filter;
    
    public CRT() {
        filter=new CRTFilter();
    }
    
    @Override
    public void create(int width,int height,Pixmap.Format format) {
        filter.setResolution(width,height);
    }
    @Override
    public void render(FrameBuffer src,FrameBuffer dest) {
        filter.setInput(src).setOutput(dest).render();
    }

    @Override
    public void dispose() {
    }
}