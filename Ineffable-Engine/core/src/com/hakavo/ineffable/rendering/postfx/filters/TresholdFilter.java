package com.hakavo.ineffable.rendering.postfx.filters;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.hakavo.ineffable.assets.AssetManager;

public class TresholdFilter extends Filter<TresholdFilter> {
    public float treshold;
    
    public TresholdFilter(float treshold) {
        this.treshold=treshold;
        super.shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/treshold"),"shader-treshold");
        super.input.uniform="u_texture";
    }

    @Override
    public void beforeRender() {
        super.shader.setUniformf("u_treshold",treshold);
    }
}
