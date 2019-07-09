package com.hakavo.ineffable.rendering.postfx.filters;
import com.badlogic.gdx.Gdx;
import com.hakavo.ineffable.assets.AssetManager;

public class VignetteFilter extends Filter<VignetteFilter> {
    
    public VignetteFilter() {
        super.shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/vignette"),"shader-vignette");
        super.input.uniform="u_tex0";
    }

    @Override
    public void beforeRender() {
    }
}
