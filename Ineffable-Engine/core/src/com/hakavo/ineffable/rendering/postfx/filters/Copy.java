package com.hakavo.ineffable.rendering.postfx.filters;
import com.badlogic.gdx.Gdx;
import com.hakavo.ineffable.assets.AssetManager;

public class Copy extends Filter {
    public Copy() {
        shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/copy"),"shader-copy");
        input.uniform="u_tex0";
    }

    @Override
    public void beforeRender() {
    }
}
