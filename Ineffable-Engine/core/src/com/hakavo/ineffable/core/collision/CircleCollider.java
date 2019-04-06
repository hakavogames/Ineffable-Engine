package com.hakavo.ineffable.core.collision;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.gameobjects.Map;

public class CircleCollider extends Collider {
    public Transform transform;
    private Vector2 position;
    private float transformedRadius;
    public float x,y,radius;
    
    public CircleCollider(float radius) {
        this.radius=radius;
    }
    public CircleCollider(float x,float y,float radius) {
        this.radius=radius;
        setPosition(x,y);
    }
    public CircleCollider(float x,float y) {
        this(x,y,1);
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
    public float getTransformedRadius() {
        return transformedRadius;
    }
    @Override
    public float getVolume() {
        return MathUtils.PI*radius*radius;
    }
    @Override
    public Vector2 getNormal(Collider collider,Vector2 out) {
        if(collider instanceof BoxCollider)
        {
            /*BoxCollider box=(BoxCollider)collider;
            float deltaX=position.x-Math.max(box.getTransformedX(),Math.min(position.x,box.getTransformedX()+box.getTransformedWidth()));
            float deltaY=position.y-Math.max(box.getTransformedY(),Math.min(position.y,box.getTransformedY()+box.getTransformedHeight()));
            //return (deltaX*deltaX+deltaY*deltaY)<(transformedRadius*transformedRadius);*/
            return collider.getNormal(this,out);
        }
        else if(collider instanceof CircleCollider)
        {
            CircleCollider circle=(CircleCollider)collider;
            out.set(circle.position).sub(position);
            float dist=Vector2.dst(this.getTransformedX(),this.getTransformedY(),circle.getTransformedX(),circle.getTransformedY());
            out.scl((this.getTransformedRadius()+circle.getTransformedRadius())-dist);
            return out;
        }
        
        return out.set(0,0);
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
            Vector2 foo=Pools.obtain(Vector2.class);
            position.mul(transform.calculateMatrix(mat));
            mat.getScale(foo);
            transformedRadius=radius*foo.x;
            Pools.free(mat);
            Pools.free(foo);
        }
    }
    @Override
    public CircleCollider cpy() {
        CircleCollider cc=new CircleCollider(this.x,this.y,this.radius);
        cc.copyFrom(this);
        return cc;
    }
    @Override
    public boolean collides(Collider collider) {
        if(collider instanceof BoxCollider)
        {
            BoxCollider box=(BoxCollider)collider;
            float deltaX=position.x-Math.max(box.getTransformedX(),Math.min(position.x,box.getTransformedX()+box.getTransformedWidth()));
            float deltaY=position.y-Math.max(box.getTransformedY(),Math.min(position.y,box.getTransformedY()+box.getTransformedHeight()));
            return (deltaX*deltaX+deltaY*deltaY)<(transformedRadius*transformedRadius);
        }
        else if(collider instanceof CircleCollider)
        {
            CircleCollider circle=(CircleCollider)collider;
            float dist=Vector2.dst(this.getTransformedX(),this.getTransformedY(),circle.getTransformedX(),circle.getTransformedY());
            return dist<(this.getTransformedRadius()+circle.getTransformedRadius());
        }
        else if(collider instanceof PointCollider)
        {
            PointCollider point=(PointCollider)collider;
            return Vector2.dst(this.getTransformedX(),this.getTransformedY(),
                               point.getTransformedX(),point.getTransformedY())<this.getTransformedRadius();
        }
        else if(collider instanceof Map.MapCollider)
            return collider.collides(this);
        
        return false;
    }
    
    @Override
    public String toString() {
        return "["+position.x+","+position.y+","+transformedRadius+"]";
    }
}
