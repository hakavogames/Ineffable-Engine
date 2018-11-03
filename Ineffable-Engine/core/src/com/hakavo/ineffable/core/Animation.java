/*
 * Copyright 2018 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hakavo.ineffable.core;

import com.hakavo.ineffable.GameServices;

public class Animation extends GameComponent implements GameComponent.Copiable
{
    public AnimationClip clip;
    protected Sprite2D target;
    private float currentTime,startTime;
    private boolean playing=false;
    
    public Animation(String name,AnimationClip animationClip,Sprite2D target) {
        super.name=name;
        this.clip=animationClip;
        setTarget(target);
    }
    public Animation(String name,AnimationClip animationClip) {
        this(name,animationClip,null);
    }
    public Animation(Sprite2D target) {
        this("defaultAnimation",null,target);
    }
    public Animation() {
        this(null);
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
    @Override
    public Animation cpy() {
        Animation anim=new Animation(super.name,clip,this.target);
        anim.copyFrom(this);
        return anim;
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
