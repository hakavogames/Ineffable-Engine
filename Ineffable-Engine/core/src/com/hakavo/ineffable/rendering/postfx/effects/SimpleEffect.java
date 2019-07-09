package com.hakavo.ineffable.rendering.postfx.effects;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.hakavo.ineffable.rendering.postfx.Effect;
import com.hakavo.ineffable.rendering.postfx.filters.Filter;

public class SimpleEffect extends Effect {
    public Filter filter;
    
    public SimpleEffect(Filter filter) {
        this.filter=filter;
    }

    @Override
    public void create(int width,int height,Pixmap.Format format) {
    }
    @Override
    public void render(FrameBuffer src,FrameBuffer dest) {
        filter.setInput(src).setOutput(dest).render();
    }
    
    public Filter getFilter() {
        return filter;
    }
    
    @Override
    public void dispose() {
    }
}
