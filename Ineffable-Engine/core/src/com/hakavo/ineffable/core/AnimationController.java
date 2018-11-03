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
import com.badlogic.gdx.utils.*;

public class AnimationController extends GameComponent implements GameComponent.Copiable
{
    protected Sprite2D target;
    protected int currentAnim;
    public Array<Animation> animations=new Array<Animation>();
    
    public AnimationController(Sprite2D target,Animation... animations) {
        this(target);
        this.animations.addAll(animations);
    }
    public AnimationController(Animation... animations) {
        this(null,animations);
    }
    public AnimationController(Sprite2D target) {
        setTarget(target);
    }
    public AnimationController() {
    }
    
    @Override
    public void update(float delta) {
        if(currentAnim<animations.size)
            animations.get(currentAnim).update(delta);
    }
    @Override
    public void start() {
        for(int i=0;i<animations.size;i++)
        {
            animations.get(i).start();
            animations.get(i).setTarget(target);
        }
    }
    @Override
    public AnimationController cpy() {
        AnimationController anim=new AnimationController(this.target);
        anim.copyFrom(this);
        for(int i=0;i<this.animations.size;i++)
            anim.animations.add(this.animations.get(i).cpy());
        return anim;
    }
    
    public void setTarget(Sprite2D target) {
        this.target=target;
    }
    public Sprite2D getTarget() {
        return target;
    }
    public int getAnimationIndexByName(String name) {
        int index=0;
        for(int i=0;i<animations.size;i++)
            if(animations.get(i).name.equals(name))
                {index=i;break;}
        return index;
    }
    public Animation getAnimationByName(String name) {
        return animations.get(getAnimationIndexByName(name));
    }
    
    public void play(int index) {
        if(index<animations.size) {
            animations.get(currentAnim).stop();
            animations.get(index).play();
            currentAnim=index;
        }
    }
    public void play(String name) {
        play(getAnimationIndexByName(name));
    }
    public void stop() {
        if(currentAnim<animations.size)
            animations.get(currentAnim).stop();
    }
    public void pause() {
        if(currentAnim<animations.size)
            animations.get(currentAnim).pause();
    }
    public void resume() {
        if(currentAnim<animations.size)
            animations.get(currentAnim).resume();
    }
    public boolean isPlaying() {
        if(currentAnim<animations.size)
            return animations.get(currentAnim).isPlaying();
        return false;
    }
    public Animation getCurrentAnimation() {
        return animations.get(currentAnim);
    }
}
