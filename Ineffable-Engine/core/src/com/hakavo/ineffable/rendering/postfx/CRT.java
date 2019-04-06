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

public class CRT extends Filter
{
    public CRT()
    {
        samplers.add(new Filter.TextureSampler("u_texture"));
        shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/crt"),"shader-crt");
    }
    @Override
    public void setUniforms() {
        shader.setUniformf("u_time",GameServices.getElapsedTime());
    }
}
