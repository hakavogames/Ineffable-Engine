package com.hakavo.ineffable.core.skeleton;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.core.*;

public class SkeletalAnimation extends GameComponent implements GameComponent.Copiable {
    public Array<KeyFrame> keyFrames=new Array<KeyFrame>();
    protected Bone target;
    
    public boolean loop;
    public boolean invert;
    private SkeletalAnimationCallback callback;
    private float duration;
    private float currentTime,startTime;
    private boolean playing;
    
    private Array<Bone> keyA=new Array<Bone>(),keyB=new Array<Bone>(),keyC=new Array<Bone>();
    
    public SkeletalAnimation(String id) {
        this(id,false);
    }
    public SkeletalAnimation(String id,boolean loop) {
        this.loop=loop;
        this.name=id;
    }
    public SkeletalAnimation(String id,boolean loop,KeyFrame... keyframes) {
        this(id,loop);
        keyFrames.addAll(keyframes);
    }
    
    @Override
    public void start() {
        SkeletalSpriteRenderer sprite=getGameObject().getComponent(SkeletalSpriteRenderer.class);
        if(sprite!=null)this.target=sprite.getSkeleton();
    }
    @Override
    public void update(float delta) {
        duration=0;
        for(int i=0;i<keyFrames.size;i++)
            duration+=keyFrames.get(i).duration;
        
        float d=currentTime-startTime;
        
        if(playing)currentTime=GameServices.getElapsedTime();
        if(!loop&&d>=duration)stop();
        else if(target!=null)
            for(int i=keyFrames.size-1;i>=0;i--)
            {
                float s=0;
                for(int j=0;j<i;j++)s+=keyFrames.get(j).duration;
                if(d%duration>=s)
                {
                    float f=(d%duration-s)/keyFrames.get(i).duration;
                    if(i==keyFrames.size-1)interpolate(keyFrames.get(i),keyFrames.get(0),f);
                    else interpolate(keyFrames.get(i),keyFrames.get(i+1),f);
                    break;
                }
            }
    }
    public void setCallback(SkeletalAnimationCallback callback) {
        this.callback=callback;
    }
    private void interpolate(KeyFrame a,KeyFrame b,float f) {
        float c=(invert==false) ? 1f : -1f;
        keyA.clear();
        keyB.clear();
        keyC.clear();
        Interpolation interp=a.interpolation;
        a.skeleton.getBones(keyA);
        b.skeleton.getBones(keyB);
        target.getBones(keyC);
        for(int i=0;i<keyC.size;i++) {
            BoneTransform boneA=keyA.get(i).getBoneTransform();
            BoneTransform boneB=keyB.get(i).getBoneTransform();
            BoneTransform result=keyC.get(i).getBoneTransform();
            result.pivot.set(interp.apply(boneA.pivot.x*c,boneB.pivot.x*c,f),
                             interp.apply(boneA.pivot.y,boneB.pivot.y,f));
            result.position.set(interp.apply(boneA.position.x*c,boneB.position.x*c,f),
                                interp.apply(boneA.position.y,boneB.position.y,f));
            
            result.rotation=interp.apply(boneA.rotation*c,boneB.rotation*c,f);
            result.update();
        }
    }
    public void setTarget(Bone root) {
        this.target=root;
    }
    public void setInvert(boolean invert) {
        this.invert=invert;
    }
    public void play() {
        currentTime=GameServices.getElapsedTime();
        startTime=currentTime;
        playing=true;
        if(callback!=null)callback.onAnimationBegin();
    }
    public void stop() {
        playing=false;
        currentTime=0;
        startTime=0;
        if(callback!=null)callback.onAnimationEnd();
    }
    public void pause() {
        playing=false;
    }
    public void resume() {
        playing=true;
    }
    public boolean isPlaying() {return playing;}
    
    @Override
    public SkeletalAnimation cpy() {
        SkeletalAnimation out=new SkeletalAnimation(this.name,this.loop);
        out.setCallback(this.callback);
        out.keyFrames.addAll(this.keyFrames);
        return out;
    }
}
