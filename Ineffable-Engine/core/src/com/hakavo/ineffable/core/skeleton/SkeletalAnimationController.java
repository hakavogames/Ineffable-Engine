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

package com.hakavo.ineffable.core.skeleton;

import com.hakavo.ineffable.core.*;
import com.badlogic.gdx.utils.*;

public class SkeletalAnimationController extends GameComponent implements GameComponent.Copiable {
    protected Bone target;
    protected int currentAnim;
    public Array<SkeletalAnimation> animations=new Array<SkeletalAnimation>();
    
    public SkeletalAnimationController(Bone target,SkeletalAnimation... animations) {
        this(target);
        addAnimations(animations);
    }
    public SkeletalAnimationController(SkeletalAnimation... animations) {
        this(null,animations);
    }
    public SkeletalAnimationController(Bone target) {
        setTarget(target);
    }
    public SkeletalAnimationController() {
    }
    
    @Override
    public void update(float delta) {
        if(currentAnim<animations.size)
            animations.get(currentAnim).update(delta);
    }
    @Override
    public void start() {
        if(target==null) {
            SkeletalSpriteRenderer spriteRenderer=gameObject.getComponent(SkeletalSpriteRenderer.class);
            if(spriteRenderer!=null)
                setTarget(spriteRenderer.getSkeleton());
        }
        for(int i=0;i<animations.size;i++) {
            animations.get(i).setGameObject(super.getGameObject());
            animations.get(i).start();
        }
        animations.addAll(gameObject.getComponents(SkeletalAnimation.class));
    }
    
    public void addAnimations(SkeletalAnimation... anims) {
        for(SkeletalAnimation animation : anims) {
            animation.setGameObject(super.getGameObject());
            animations.add(animation);
        }
    }
    public void setTarget(Bone target) {
        this.target=target;
        for(int i=0;i<animations.size;i++)
            animations.get(i).setTarget(target);
    }
    public Bone getTarget() {
        return target;
    }
    public int getAnimationIndexByName(String name) {
        int index=-1;
        for(int i=0;i<animations.size;i++)
            if(animations.get(i).name.equals(name))
                {index=i;break;}
        return index;
    }
    public SkeletalAnimation getAnimationByName(String name) {
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
    public void setAnimation(int index) {
        if(currentAnim!=index||(currentAnim==index&&!isPlaying()))play(index);
    }
    public void setAnimation(String name) {
        setAnimation(getAnimationIndexByName(name));
    }
    public void setInvert(boolean invert) {
        for(SkeletalAnimation anim : animations)
            anim.setInvert(invert);
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
    public SkeletalAnimation getCurrentAnimation() {
        return animations.get(currentAnim);
    }
    
    @Override
    public SkeletalAnimationController cpy() {
        SkeletalAnimationController out=new SkeletalAnimationController();
        for(SkeletalAnimation anim : animations)
            out.animations.add(anim.cpy());
        return out;
    }
}
