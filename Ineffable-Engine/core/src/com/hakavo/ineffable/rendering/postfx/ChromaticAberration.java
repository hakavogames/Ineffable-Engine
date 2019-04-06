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

public class ChromaticAberration extends Filter
{
    public float barrelDistortion;
    public int samples;
    public ChromaticAberration() {
        this(0.2f);
    }
    public ChromaticAberration(float distortion) {
        this(distortion,12);
    }
    public ChromaticAberration(float distortion,int samples)
    {
        this.barrelDistortion=distortion;
        this.samples=samples;
        samplers.add(new TextureSampler("u_texture"));
        shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/chromatic_aberration"),"shader-chromatic_aberration");
    }
    @Override
    public void setUniforms() {
        shader.setUniformf("u_time",GameServices.getElapsedTime());
        shader.setUniformf("u_distortion",barrelDistortion);
        shader.setUniformi("u_samples",samples);
    }
}