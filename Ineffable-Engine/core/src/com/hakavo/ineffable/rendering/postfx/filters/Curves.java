package com.hakavo.ineffable.rendering.postfx.filters;
import com.badlogic.gdx.Gdx;
import com.hakavo.ineffable.assets.AssetManager;

public class Curves extends Filter {
    public float saturation;
    public float powRed,powGreen,powBlue;
    
    public Curves() {
        this(1);
    }
    public Curves(float saturation) {
        this(1,1,1,saturation);
    }
    public Curves(float powRed,float powGreen,float powBlue) {
        this(powRed,powGreen,powBlue,1);
    }
    public Curves(float powRed,float powGreen,float powBlue,float saturation) {
        this.powRed=powRed;
        this.powGreen=powGreen;
        this.powBlue=powBlue;
        this.saturation=saturation;
        shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/color"),"shader-color");
    }
    
    public void setGamma(float gamma) {
        powRed=gamma;
        powGreen=gamma;
        powBlue=gamma;
    }
    
    @Override
    public void beforeRender() {
        shader.setUniformf("u_saturation",saturation);
        shader.setUniformf("u_curves",powRed,powGreen,powBlue);
    }
}
