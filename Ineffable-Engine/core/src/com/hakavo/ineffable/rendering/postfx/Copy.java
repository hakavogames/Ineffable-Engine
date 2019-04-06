package com.hakavo.ineffable.rendering.postfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.hakavo.ineffable.assets.AssetManager;
import com.hakavo.ineffable.rendering.*;

public class Copy extends Filter
{
    public Copy()
    {
        samplers.add(new TextureSampler("u_tex0"));
        shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/copy"),"shader-copy");
    }
}
