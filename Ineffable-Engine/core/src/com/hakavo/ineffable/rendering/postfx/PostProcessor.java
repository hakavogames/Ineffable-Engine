package com.hakavo.ineffable.rendering.postfx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.rendering.*;

public class PostProcessor {
    public Array<Filter> filters;
    protected Renderer renderer;
    public FrameBuffer buf1,buf2;
    protected int currentBuffer=1;
    
    public PostProcessor(Renderer renderer)
    {
        this(renderer,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    }
    public PostProcessor(Renderer renderer,int width,int height)
    {
        this.renderer=renderer;
        buf1=new FrameBuffer(Format.RGBA8888,width,height,false);
        buf2=new FrameBuffer(Format.RGBA8888,width,height,false);
        buf1.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
        buf2.getColorBufferTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
        filters=new Array<Filter>();
    }
    public void render(boolean toRenderBuffer,Texture... inputs)
    {
        Gdx.gl.glDisable(GL30.GL_DEPTH_TEST);
        Gdx.gl.glDisable(GL30.GL_BLEND);
        currentBuffer=1;
        for(int i=0;i<filters.size;i++)
        {
            if(!filters.get(i).active)continue;
            filters.get(i).resolution.set(buf1.getWidth(),buf1.getHeight());
            if(currentBuffer==1)
            {
                currentBuffer=2;
                if(i==0) {
                    filters.get(i).bind(inputs);
                }
                else if(filters.get(i).samplers.size>0)
                    filters.get(i).samplers.get(0).texture=buf2.getColorBufferTexture();
                if(i==filters.size-1&&toRenderBuffer==true)filters.get(i).render(null);
                else filters.get(i).render(buf1);
            }
            else
            {
                currentBuffer=1;
                if(filters.get(i).samplers.size>0)
                    filters.get(i).samplers.get(0).texture=buf1.getColorBufferTexture();
                if(i==filters.size-1&&toRenderBuffer==true)filters.get(i).render(null);
                else filters.get(i).render(buf2);
            }
        }
    }
    public void render(boolean toRenderBuffer) {
        Texture[] array=new Texture[filters.get(0).samplers.size];
        for(int i=0;i<filters.get(0).samplers.size;i++)
            array[i]=filters.get(0).samplers.get(i).texture;
        if(filters.size>0)
            render(toRenderBuffer,array);
    }
    public FrameBuffer getLastFBO()
    {
        if(filters.size>1)
        {
            if(currentBuffer==2)return buf1;
            else return buf2;
        }
        return buf1;
    }
    public void addFilter(Filter filter) {
        filter.renderer=this.renderer;
        filters.add(filter);
    }
    public void addFilters(Filter... filters) {
        for(Filter filter : filters)
            addFilter(filter);
    }
    public int getWidth() {return buf1.getWidth();}
    public int getHeight() {return buf1.getHeight();}
}
