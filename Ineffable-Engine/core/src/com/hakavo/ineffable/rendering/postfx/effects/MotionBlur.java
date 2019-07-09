package com.hakavo.ineffable.rendering.postfx.effects;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.hakavo.ineffable.GameServices;
import com.hakavo.ineffable.rendering.postfx.Effect;
import com.hakavo.ineffable.rendering.postfx.filters.Blur;

public class MotionBlur extends Effect {
    public float sensitivity;
    public float attenuation;
    
    protected Camera camera;
    private final Vector2 delta=new Vector2();
    private final Vector2 lastPos=new Vector2();
    private final Vector2 blur=new Vector2();
    private final Blur filter;
    
    public MotionBlur() {
        this(0.025f);
    }
    public MotionBlur(float sensitivity) {
        this(sensitivity,0.7f);
    }
    public MotionBlur(float sensitivity,float attenuation) {
        super();
        this.sensitivity=sensitivity;
        this.attenuation=attenuation;
        setCamera(GameServices.getCamera());
        filter=new Blur(blur,8,16);
    }
    public final void setCamera(Camera camera) {
        this.camera=camera;
    }
    @Override
    public void render(FrameBuffer src,FrameBuffer dest) {
        delta.set((camera.position.x-lastPos.x)*sensitivity,-(camera.position.y-lastPos.y)*sensitivity);
        lastPos.set(camera.position.x,camera.position.y);
        
        blur.x=Interpolation.linear.apply(blur.x,delta.x,attenuation);
        blur.y=Interpolation.linear.apply(blur.y,delta.y,attenuation);
        
        filter.setInput(src).setOutput(dest).render();
    }

    @Override
    public void create(int width,int height,Pixmap.Format format) {
    }
    @Override
    public void dispose() {
    }
}
