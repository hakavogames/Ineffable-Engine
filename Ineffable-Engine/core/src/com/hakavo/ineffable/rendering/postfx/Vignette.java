package com.hakavo.ineffable.rendering.postfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.assets.*;
import com.hakavo.ineffable.rendering.Renderer;

public class Vignette extends Filter
{
    public Vignette()
    {
        samplers.add(new TextureSampler("u_tex0"));
        shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/vignette"),"shader-vignette");
    }
}
