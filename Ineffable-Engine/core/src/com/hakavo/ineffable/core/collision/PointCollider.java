package com.hakavo.ineffable.core.collision;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.ineffable.Engine;
import com.hakavo.ineffable.core.Transform;
import com.hakavo.ineffable.gameobjects.Map;

public class PointCollider extends Collider {
    public Transform transform;
    private final Vector2 position=new Vector2();
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
    public float getVolume() {
        return 1f;
    }
    
    @Override
    public void start() {
        if(getGameObject()!=null)
            transform=this.getGameObject().getComponent(Transform.class);
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
    public Vector2 getNormal(Collider collider,Vector2 out) {
        if(collider instanceof BoxCollider) {
            BoxCollider coll=(BoxCollider)collider;
            float d=Float.MAX_VALUE,t=0;
            t=(coll.getTransformedY()+coll.getTransformedHeight())-position.y;
            if(t>=0&&t<d) {
                d=t;
                out.set(0,-d);
            }
            t=position.y-coll.getTransformedY();
            if(t>=0&&t<d) {
                d=t;
                out.set(0,d);
            }
            t=(coll.getTransformedX()+coll.getTransformedWidth())-position.x;
            if(t>=0&&t<d) {
                d=t;
                out.set(-d,0);
            }
            t=position.x-coll.getTransformedX();
            if(t>=0&&t<d) {
                d=t;
                out.set(d,0);
            }
            return out;
        }
        return out.set(0,0);
    }
    @Override
    public PointCollider cpy() {
        PointCollider pc=new PointCollider(this.x,this.y);
        pc.copyFrom(this);
        return pc;
    }
    @Override
    public boolean collides(Collider collider) {
        if(collider instanceof BoxCollider)
            return ((BoxCollider)collider).contains(getTransformedX(),getTransformedY(),0,0);
        else if(collider instanceof CircleCollider)
            return collider.collides(this);
        else if(collider instanceof Map.MapCollider)
            return collider.collides(this);
        
        return false;
    }
    @Override
    public Array<Collider> cast(Vector2 direction,float step,int samples) {
        Array<Collider> coll=new Array<Collider>();
        PointCollider p=this.cpy();
        p.start();
        
        for(int i=0;i<samples;i++) {
            p.x+=direction.x*step;
            p.y+=direction.y*step;
            p.update(0);
            this.getGameObject().getLevel();
            Array<Collider> tmp=this.getGameObject().getLevel().getComponent(Engine.GameManager.class).testCollision(p);
            for(Collider collider : tmp) {
                if(!collider.equals(this)&&!coll.contains(collider,false))
                    coll.add(collider);
            }
        }
        
        return coll;
    }
    @Override
    public String toString() {
        return "["+getTransformedX()+","+getTransformedY()+"]";
    }

}
