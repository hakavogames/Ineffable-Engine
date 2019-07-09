package com.hakavo.game.ui;
import com.badlogic.gdx.graphics.Color;
import com.hakavo.ineffable.ui.*;
import com.badlogic.gdx.math.Interpolation;

public class Title extends Label implements EventHandler {
    private float time,duration;
    private Interpolation interpolation;
    protected TitleListener listener;
    
    public Title() {
        super("","pixeltype");
        super.setScale(4);
        super.visible=false;
        
        GUI.setEventHandler("title",this);
    }
    
    @Override
    public void onUpdate(float delta) {
        if(time>=duration) {
            super.style.foreground.a=0;
            super.visible=false;
            return;
        }
        super.visible=true;
        time+=delta;
        float f;
        if(time<=duration/2)f=interpolation.apply(0,1,time*2/duration);
        else f=interpolation.apply(1,0,(time-duration/2)/(duration/2));
        super.style.foreground.a=Math.min(f,1f);
        
        Bounds parent=super.getParent().bounds;
        super.bounds.setPosition(parent.width/2-super.bounds.width/2,parent.height/2-super.bounds.height/2);
        if(time>=duration&&listener!=null)listener.onTitleFinished();
    }
    
    public void show(String text) {
        show(text,3f);
    }
    public void show(String text,float duration) {
        show(text,duration,5f);
    }
    public void show(String text,float duration,float scale) {
        show(text,duration,scale,Color.WHITE);
    }
    public void show(String text,float duration,float scale,Color color) {
        show(text,duration,scale,color,Interpolation.exp5);
    }
    public void show(String text,float duration,float scale,Color color,Interpolation interpolation) {
        show(text,duration,scale,color,Interpolation.exp5,null);
    }
    public void show(String text,float duration,float scale,Color color,Interpolation interpolation,TitleListener listener) {
        this.duration=duration;
        this.interpolation=interpolation;
        this.listener=listener;
        super.setText(text);
        super.setScale(scale);
        super.style.foreground.set(color.r,color.g,color.b,0f);
        time=0;
    }

    @Override
    public void handleMessage(Object[] parameters) {
        switch(parameters.length) {
            case 1:
                show((String)parameters[0]);
                break;
            case 2:
                show((String)parameters[0],((Number)parameters[1]).floatValue());
                break;
            case 3:
                show((String)parameters[0],((Number)parameters[1]).floatValue(),((Number)parameters[2]).floatValue());
                break;
            case 4:
                show((String)parameters[0],((Number)parameters[1]).floatValue(),((Number)parameters[2]).floatValue(),(Color)parameters[3]);
                break;
            case 5:
                show((String)parameters[0],((Number)parameters[1]).floatValue(),((Number)parameters[2]).floatValue(),(Color)parameters[3],(Interpolation)parameters[4]);
                break;
            case 6:
                show((String)parameters[0],((Number)parameters[1]).floatValue(),((Number)parameters[2]).floatValue(),(Color)parameters[3],(Interpolation)parameters[4],(TitleListener)parameters[5]);
                break;
            default:
                break;
        }
    }
}
