package com.hakavo.ineffable.rendering.postfx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.utils.Disposable;

public abstract class Effect implements Disposable {
    protected boolean enabled=true;
    private boolean initialized=false;
    
    public abstract void render(FrameBuffer src,FrameBuffer dest);
    public abstract void create(int width,int height,Format format);
    
    public final void init(int width,int height,Format format) {
        if(!initialized) {
            initialized=true;
            create(width,height,format);
        }
    }
    
    public final boolean isEnabled() {
        return enabled;
    }
    public final void setEnabled(boolean enabled) {
        this.enabled=enabled;
    }
}
