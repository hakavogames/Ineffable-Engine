package com.hakavo.ineffable.core.collision;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.ineffable.core.Transform;

public class PointCollider extends Collider {
    public Transform transform;
    private Vector2 position;
    public float x,y;
    
    public PointCollider() {
    }
    public PointCollider(float x,float y) {
        setPosition(x,y);
    }
    
    public void setPosition(float x,float y) {
        this.x=x;
        this.y=y;
    }
    public float getTransformedX() {
        return position.x;
    }
    public float getTransformedY() {
        return position.y;
    }
    
    @Override
    public void start() {
        transform=this.getGameObject().getComponent(Transform.class);
        position=new Vector2();
    }
    @Override
    public void update(float delta) {
        position.set(this.x,this.y);
        if(transform!=null) {
            Matrix3 mat=Pools.obtain(Matrix3.class);
            position.mul(transform.calculateMatrix(mat));
            Pools.free(mat);
        }
    }
    @Override
    public PointCollider cpy() {
        PointCollider pc=new PointCollider(this.x,this.y);
        pc.copyFrom(this);
        return pc;
    }
    @Override
    protected boolean collides(Collider collider) {
        if(collider instanceof BoxCollider)
            return ((BoxCollider)collider).contains(getTransformedX(),getTransformedY(),0,0);
        else if(collider instanceof CircleCollider)
            return collider.collides(this);
        
        return false;
    }
    
    @Override
    public String toString() {
        return "["+getTransformedX()+","+getTransformedY()+"]";
    }
}
