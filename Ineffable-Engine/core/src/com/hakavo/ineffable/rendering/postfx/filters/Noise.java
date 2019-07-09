package com.hakavo.ineffable.rendering.postfx.filters;

import com.badlogic.gdx.Gdx;
import com.hakavo.ineffable.GameServices;
import com.hakavo.ineffable.assets.AssetManager;

public class Noise extends Filter {
    public float downscale;
    public float intensity;
    public boolean isStatic=false;
    
    public Noise(float intensity) {
        this(intensity,1);
    }
    public Noise(float intensity,float resolutionDownscale) {
        this(intensity,resolutionDownscale,false);
    }
    public Noise(float intensity,float resolutionDownscale,boolean isStatic) {
        this.isStatic=isStatic;
        this.intensity=intensity;
        this.downscale=resolutionDownscale;
        input.uniform="u_texture";
        shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/noise"),"shader-noise");
    }
    @Override
    public void beforeRender() {
        shader.setUniformf("u_time",(isStatic) ? 0f : GameServices.getElapsedTime());
        shader.setUniformf("u_intensity",intensity);
        shader.setUniformf("u_downscale",downscale);
        shader.setUniformf("u_screenSize",super.input.texture.getWidth(),super.input.texture.getHeight());
    }
}
