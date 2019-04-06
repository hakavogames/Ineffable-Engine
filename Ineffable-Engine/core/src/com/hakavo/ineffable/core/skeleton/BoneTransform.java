package com.hakavo.ineffable.core.skeleton;
import com.badlogic.gdx.math.*;
import com.hakavo.ineffable.core.*;

public class BoneTransform {
    public Vector2 position;
    public Vector2 pivot;
    public Vector2 scale;
    public float rotation;
    private final Transform transform=new Transform();
    
    public BoneTransform() {
        this(new Vector2(),new Vector2(),0);
    }
    public BoneTransform(Vector2 position,Vector2 pivot,float rotation) {
        this(position,pivot,new Vector2(1,1),rotation);
    }
    public BoneTransform(Vector2 position,Vector2 pivot,Vector2 scale,float rotation) {
        this.position=position;
        this.pivot=pivot;
        this.rotation=rotation;
        this.scale=scale;
        update();
    }
    public BoneTransform setPosition(Vector2 position) {
        this.position=position;
        update();
        return this;
    }
    public BoneTransform setPivot(Vector2 pivot) {
        this.pivot=pivot;
        update();
        return this;
    }
    public BoneTransform setRotation(float rotation) {
        this.rotation=rotation;
        update();
        return this;
    }
    public Vector2 getPosition() {
        return position;
    }
    public Vector2 getPivot() {
        return pivot;
    }
    public Vector2 getScale() {
        return scale;
    }
    public float getRotation() {
        return rotation;
    }
    public void update() {
        Matrix3 out=transform.matrix;
        out.idt();
        out.translate(position);
        out.translate(pivot);
        out.scale(scale);
        out.rotate(rotation);
        out.translate(-pivot.x,-pivot.y);
    }
    public Transform getTransform() {
        return transform;
    }
    public BoneTransform cpy() {
        return new BoneTransform(new Vector2(position),new Vector2(pivot),rotation);
    }
}