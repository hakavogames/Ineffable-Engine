package com.hakavo.ineffable.rendering.postfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.assets.*;
import com.hakavo.ineffable.rendering.Renderer;

public class Test extends Filter
{
    public Test()
    {
        shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/test"),"shader-test");
    }
    @Override
    public void setUniforms() {
        shader.setUniformf("u_time",GameServices.getElapsedTime()/2f);
    }
}
