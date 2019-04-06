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

public class MotionBlur extends Blur {
    public float sensitivity=5;
    public float attenuation=0.3f;
    
    protected Camera camera;
    private Vector2 delta=new Vector2();
    private Vector2 blur=new Vector2();
    
    public void setCamera(Camera camera)
    {
        this.camera=camera;
    }
    @Override
    public void setUniforms()
    {
        delta.set((float)Gdx.input.getDeltaX()/sensitivity,-(float)Gdx.input.getDeltaY()/sensitivity);
        
        blur.x=Interpolation.linear.apply(blur.x,delta.x,attenuation);
        blur.y=Interpolation.linear.apply(blur.y,delta.y,attenuation);
        super.direction.set(blur);
        
        super.setUniforms();
    }
}