package com.hakavo.ineffable.rendering.postfx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.GameServices;
import java.util.*;
import com.hakavo.ineffable.assets.*;

public class Noise extends Filter
{
    public float downscale;
    public float intensity;
    public boolean isStatic=false;
    public Noise() {
        this(0.1f);
    }
    public Noise(float intensity) {
        this(intensity,1f);
    }
    public Noise(float intensity,float resolutionDownscale)
    {
        this(intensity,resolutionDownscale,false);
    }
    public Noise(float intensity,float resolutionDownscale,boolean isStatic)
    {
        this.isStatic=isStatic;
        this.intensity=intensity;
        this.downscale=resolutionDownscale;
        samplers.add(new Filter.TextureSampler("u_texture"));
        shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/noise"),"shader-noise");
    }
    @Override
    public void setUniforms() {
        shader.setUniformf("u_time",(isStatic) ? 0f : GameServices.getElapsedTime());
        shader.setUniformf("u_intensity",intensity);
        shader.setUniformf("u_downscale",downscale);
    }
}
