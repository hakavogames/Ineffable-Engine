package com.hakavo.ineffable.rendering.postfx;

import com.badlogic.gdx.Gdx;
import com.hakavo.ineffable.assets.AssetManager;

public class Mix extends Filter {
    public Mix()
    {
        samplers.add(new TextureSampler("u_texture0"));
        samplers.add(new TextureSampler("u_texture1"));
        shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/mix"),"shader-mix");
    }
}
