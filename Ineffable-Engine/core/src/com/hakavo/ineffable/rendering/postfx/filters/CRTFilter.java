package com.hakavo.ineffable.rendering.postfx.filters;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.hakavo.ineffable.assets.AssetManager;

public class CRTFilter extends Filter<CRTFilter> {
    private long start=System.currentTimeMillis();
    private int width,height;
    
    public CRTFilter() {
        super.shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/crt"),"shader-crt");
        super.input.uniform="u_texture";
        width=Gdx.graphics.getWidth();
        height=Gdx.graphics.getHeight();
    }
    
    public void setResolution(int width,int height) {
        this.width=width;
        this.height=height;
    }
    
    @Override
    public void beforeRender() {
        super.shader.setUniformf("u_time",(System.currentTimeMillis()-start)/1000f);
        super.shader.setUniformf("u_screenSize",width,height);
    }
}
