package com.hakavo.core.collision;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.core.Transform;

public class CircleCollider extends Collider {
    public Transform transform;
    private Vector2 position;
    private float transformedRadius;
    public float x,y,radius;
    
    public CircleCollider() {
    }
    public CircleCollider(float x,float y) {
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
    public float getTransformedRadius() {
        return transformedRadius;
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
    protected boolean collides(Collider collider) {
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
        
        return false;
    }
    
    @Override
    public String toString() {
        return "["+position.x+","+position.y+","+transformedRadius+"]";
    }
}
