package com.hakavo.ineffable.core.skeleton;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;

public class KeyFrame {
    public Bone skeleton;
    public float duration; // in seconds
    public Interpolation interpolation;
    
    public KeyFrame(Bone skeletonRoot,float duration,Interpolation interpolation) {
        this.skeleton=skeletonRoot;
        this.duration=duration;
        this.interpolation=interpolation;
    }
    public KeyFrame(Bone skeletonRoot,float duration) {
        this(skeletonRoot,duration,Interpolation.linear);
    }
}
