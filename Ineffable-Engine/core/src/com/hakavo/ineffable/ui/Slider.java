package com.hakavo.ineffable.ui;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hakavo.ineffable.assets.AssetManager;

public class Slider extends Container {
    public SliderListener listener;
    private float start,end;
    private final boolean vertical;
    private final SliderButton button;
    private final float size,depth;
    
    public Slider(boolean vertical,float size,float depth,float start,float end) {
        super.style.background.set(0.4f,0.4f,0.4f,1f);
        if(!vertical)super.bounds.setSize(size,depth);
        else super.bounds.setSize(depth,size);
        this.start=start;
        this.end=end;
        this.vertical=vertical;
        this.size=size;
        this.depth=depth;
        
        button=new SliderButton(vertical,size/(end-start+1),depth);
        super.add(button);
    }
    public void setLimits(float start,float end) {
        this.start=start;
        this.end=end;
        button.initSize(size/(end-start+1),depth);
    }
    public void scrollPercent(float p) {
        scroll((size-button.bounds.width)/100f*p);
    }
    public void scroll(float amount) {
        if(vertical)scroll(0,amount);
        else scroll(amount,0);
    }
    public float getStart() {
        return start;
    }
    public float getEnd() {
        return end;
    }
    public float getValue() {
        Bounds parent=bounds;
        Bounds bounds=button.bounds;
        if(vertical)
            return start+bounds.y/(parent.height-bounds.height)*(end-start);
        else
            return start+bounds.x/(parent.width-bounds.width)*(end-start);
    }
    private void scroll(float dx,float dy) {
        Bounds parent=bounds;
        Bounds bounds=button.bounds;
        if(vertical) {
            bounds.y-=dy;
            if(bounds.y<0)bounds.y=0;
            else if(bounds.y>parent.height-bounds.height)bounds.y=parent.height-bounds.height;
        }
        else {
            bounds.x+=dx;
            if(bounds.x<0)bounds.x=0;
            else if(bounds.x>parent.width-bounds.width)bounds.x=parent.width-bounds.width;
        }
        if(listener!=null)listener.onChange(getValue());
    }
    
    private static class SliderButton extends Button {
        public boolean vertical;
        
        public SliderButton(boolean isVertical,float width,float height) {
            super();
            super.style.borderWidth=0;
            this.vertical=isVertical;
            initSize(width,height);
            
            super.addEventListener(new EventListener() {
                int x,y;
                @Override
                public void onMouseMoved(int x,int y) {
                    this.x=x;
                    this.y=y;
                }
                @Override
                public void onMouseDragged(int x,int y) {
                    int dx=x-this.x,dy=y-this.y;
                    ((Slider)getParent()).scroll(dx,dy);
                    this.x=x;
                    this.y=y;
                }
            });
        }
        public final void initSize(float width,float height) {
            if(!vertical)super.bounds.setSize(width,height);
            else super.bounds.setSize(height,width);
        }
        /*@Override
        public void onRender(OrthographicCamera camera,SpriteBatch sb) {
            Texture tex=AssetManager.getAsset("gui-blank");
            sb.draw(tex,0,0,bounds.width,bounds.height);
        }*/
    }
}
