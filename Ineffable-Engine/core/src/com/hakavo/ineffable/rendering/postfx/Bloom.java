package com.hakavo.ineffable.rendering.postfx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import java.util.*;
import com.hakavo.ineffable.assets.*;

public class Bloom extends Filter
{
    public Bloom()
    {
        samplers.add(new Filter.TextureSampler("u_texture"));
        samplers.add(new Filter.TextureSampler("u_blur"));
        shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/bloom"),"shader-bloom");
    }
    @Override
    public void setUniforms() {
    }
}
