package com.kozma.core;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kozma.GameServices;

public class Animation extends GameComponent
{
    public AnimationClip clip;
    protected Sprite2D target;
    private float currentTime,startTime;
    private boolean playing=false;
    
    public Animation() {
        super.name="Animation";
        clip=new AnimationClip();
    }
    @Override
    public void update(float delta) {
        float d=currentTime-startTime;
        float f=clip.duration/clip.frames.size;
        
        if(playing)currentTime=GameServices.getElapsedTime();
        if(!clip.loop&&d>clip.duration)stop();
        else if(target!=null)
            for(int i=clip.frames.size-1;i>=0;i--)
                if(d%clip.duration>=f*i)
                {
                    target.textureRegion=clip.frames.get(i);
                    break;
                }
    }
    @Override
    public void start() {
        currentTime=GameServices.getElapsedTime();
        startTime=currentTime;
    }
    
    public void play() {
        currentTime=GameServices.getElapsedTime();
        startTime=currentTime;
        playing=true;
    }
    public void stop() {
        playing=false;
        currentTime=0;
        startTime=0;
    }
    public void pause() {
        playing=false;
    }
    public void resume() {
        playing=true;
    }
    public boolean isPlaying() {return playing;}
    
    public void setTarget(Sprite2D sprite) {
        this.target=sprite;
    }
    public Sprite2D getTarget() {
        return this.target;
    }
}
