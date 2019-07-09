package com.hakavo.ineffable.rendering.postfx.filters;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.*;
import com.hakavo.ineffable.assets.AssetManager;

public class Blur extends Filter<Blur> {
    public Vector2 direction;
    public float radius;
    public int samples;
    
    public Blur(Vector2 direction,float radius,int samples) {
        this.direction=direction;
        this.radius=radius;
        this.samples=samples;
        super.shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/blur"),"shader-blur");
        super.input.uniform="u_texture";
    }

    @Override
    public void beforeRender() {
        shader.setUniformf("u_direction",direction);
        shader.setUniformf("u_radius",radius);
        shader.setUniformf("u_samples",samples);
    }
}
