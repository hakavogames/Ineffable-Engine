package com.hakavo.ineffable.rendering.postfx.utils;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.utils.Disposable;

public class PingPongFrameBuffer implements Disposable {
    public final FrameBuffer texture1,texture2;
    private int buffer;
    
    public PingPongFrameBuffer(int width,int height,Format format) {
        this(width,height,format,false);
    }
    public PingPongFrameBuffer(int width,int height,Format format,boolean hasDepth) {
        texture1=new FrameBuffer(format,width,height,hasDepth);
        texture2=new FrameBuffer(format,width,height,hasDepth);
    }
    
    public void next() {
        if(buffer==0)buffer=1;
        else buffer=0;
    }
    public void reset() {
        buffer=0;
    }
    public FrameBuffer getLast() {
        if(buffer==0)return texture2;
        return texture1;
    }
    public FrameBuffer getNext() {
        return getLast();
    }
    public FrameBuffer getCurrent() {
        if(buffer==0)return texture1;
        return texture2;
    }

    @Override
    public void dispose() {
        texture1.dispose();
        texture2.dispose();
    }
    
    public void setFilter(TextureFilter minFilter,TextureFilter magFilter) {
        texture1.getColorBufferTexture().setFilter(minFilter,magFilter);
        texture2.getColorBufferTexture().setFilter(minFilter,magFilter);
    }
}
