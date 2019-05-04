package com.hakavo.ineffable.rendering.postfx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.GameServices;
import java.util.*;
import com.hakavo.ineffable.assets.*;

public class ColorFilter extends Filter
{
    public float curveR=1,curveG=1,curveB=1;
    public float saturation=1;
    
    public ColorFilter(float saturation) {
        this(1,1,1,saturation);
    }
    public ColorFilter(float cR,float cG,float cB) {
        this(cR,cG,cB,1f);
    }
    public ColorFilter(float cR,float cG,float cB,float saturation)
    {
        this.curveR=cR;
        this.curveG=cG;
        this.curveB=cB;
        this.saturation=saturation;
        samplers.add(new Filter.TextureSampler("u_texture"));
        shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/color"),"shader-color");
    }
    @Override
    public void setUniforms() {
        shader.setUniformf("u_curves",curveR,curveG,curveB);
        shader.setUniformf("u_saturation",saturation);
        
        /*if(Gdx.input.isKeyPressed(Keys.U))curveR-=0.01f;
        if(Gdx.input.isKeyPressed(Keys.I))curveR+=0.01f;
        if(Gdx.input.isKeyPressed(Keys.J))curveG-=0.01f;
        if(Gdx.input.isKeyPressed(Keys.K))curveG+=0.01f;
        if(Gdx.input.isKeyPressed(Keys.N))curveB-=0.01f;
        if(Gdx.input.isKeyPressed(Keys.M))curveB+=0.01f;
        System.out.println(curveR+" "+curveG+" "+curveB);*/
    }
}
