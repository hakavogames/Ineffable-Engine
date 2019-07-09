package com.hakavo.ineffable.rendering.postfx.filters;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.hakavo.ineffable.assets.AssetManager;

public class Combine extends Filter<Combine> {
    public float intensityA,intensityB;
    
    public Combine(float intensityA,float intensityB) {
        this.intensityA=intensityA;
        this.intensityB=intensityB;
        super.shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/combine"),"shader-combine");
        super.textures.addAll(new TextureUniform("u_texture0",null,0),
                              new TextureUniform("u_texture1",null,1));
    }
    
    public Combine setInput(Texture tex0,Texture tex1) {
        super.getTextureUniform("u_texture0").texture=tex0;
        super.getTextureUniform("u_texture1").texture=tex1;
        return this;
    }

    @Override
    public void beforeRender() {
        super.shader.setUniformf("u_tex0Intensity",intensityA);
        super.shader.setUniformf("u_tex1Intensity",intensityB);
    }
}
