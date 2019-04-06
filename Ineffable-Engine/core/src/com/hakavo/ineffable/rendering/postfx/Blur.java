package com.hakavo.ineffable.rendering.postfx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import java.util.*;
import com.hakavo.ineffable.assets.*;

public class Blur extends Filter
{
    public Vector2 direction;
    public float radius=0.02f;
    public int samples=10;
    public Blur()
    {
        direction=new Vector2(0,0);
        samplers.add(new TextureSampler("u_texture"));
        shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/blur"),"shader-blur");
    }
    public Blur(Vector2 direction,float radius,int samples) {
        this();
        this.direction.set(direction);
        this.radius=radius;
        this.samples=samples;
    }
    @Override
    public void setUniforms() {
        shader.setUniformf("u_radius",radius);
        shader.setUniformf("u_direction",direction);
        shader.setUniformf("u_samples",samples);
    }
}
