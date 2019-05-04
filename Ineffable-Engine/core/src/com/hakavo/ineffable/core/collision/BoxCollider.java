package com.hakavo.ineffable.core.collision;
import com.hakavo.ineffable.core.Transform;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.ineffable.Engine;
import com.hakavo.ineffable.gameobjects.Map;

public class BoxCollider extends Collider {
    public Transform transform;
    public float x,y,width,height;
    private Vector2 b1=new Vector2(),b2=new Vector2();
    
    public BoxCollider() {
    }
    public BoxCollider(float x,float y,float width,float height) {
        setBounds(x,y,width,height);
    }
    
    public final void setBounds(float x,float y,float width,float height) {
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
    }
    public final void setPosition(float x,float y) {
        setBounds(x,y,this.width,this.height);
    }
    public final void setSize(float width,float height) {
        setBounds(this.x,this.y,width,height);
    }
    public final float getTransformedX() {
        return b1.x;
    }
    public final float getTransformedY() {
        return b1.y;
    }
    public final float getTransformedWidth() {
        return b2.x;
    }
    public final float getTransformedHeight() {
        return b2.y;
    }
    public final boolean isEmpty() {
        return width==0f||height==0f;
    }
    public final Vector2 getCenter(Vector2 out) {
        return out.set(b1.x+b2.x/2,b1.y+b2.y/2);
    }
    @Override
    public final float getVolume() {
        return width*height;
    }
    
    
    public boolean contains(float x,float y,float width,float height) {
        return x>=this.getTransformedX()&&y>=this.getTransformedY()&&
               x+width<=this.getTransformedX()+this.getTransformedWidth()&&y+height<=this.getTransformedY()+this.getTransformedHeight();
    }
    public boolean intersects(float x,float y,float width,float height) {
        return x+width>this.getTransformedX()&&y+height>this.getTransformedY()&&
               x<this.getTransformedX()+this.getTransformedWidth()&&y<this.getTransformedY()+this.getTransformedHeight();
    }
    @Override
    public Vector2 getNormal(Collider collider,Vector2 out) {
        if(collider instanceof CircleCollider) {
            CircleCollider circle=(CircleCollider)collider;
            BoxCollider box=new BoxCollider(circle.getTransformedX()-circle.getTransformedRadius(),
                                            circle.getTransformedY()-circle.getTransformedRadius(),
                                            circle.getTransformedRadius()*2,
                                            circle.getTransformedRadius()*2);
            box.update(0);
            return box.getNormal(this,out);
        }
        else if(collider instanceof BoxCollider) {
            BoxCollider box=(BoxCollider)collider;
            float d=Float.MAX_VALUE,t;
            t=b1.x+b2.x-box.b1.x;
            if(t>=0&&t<d) {
                d=t;
                out.set(d,0);
            }
            t=box.b1.x+box.b2.x-b1.x;
            if(t>=0&&t<d) {
                d=t;
                out.set(-d,0);
            }
            t=b1.y+b2.y-box.b1.y;
            if(t>=0&&t<d) {
                d=t;
                out.set(0,d);
            }
            t=box.b2.y+box.b1.y-b1.y;
            if(t>=0&&t<d) {
                out.set(0,-t);
            }
            return out;
        }
        return out.set(0,0);
    }
    @Override
    public boolean collides(Collider collider) {
        if(collider instanceof BoxCollider) {
            if(isEmpty())return false;
            
            BoxCollider box=(BoxCollider)collider;
            return intersects(box.b1.x,box.b1.y,box.b2.x,box.b2.y)||contains(box.b1.x,box.b1.y,box.b2.x,box.b2.y);
        }
        else if(collider instanceof CircleCollider)
            return collider.collides(this);
        else if(collider instanceof PointCollider)
            return collider.collides(this);
        else if(collider instanceof Map.MapCollider)
            return collider.collides(this);
        
        return false;
    }
    @Override
    public Array<Collider> cast(Vector2 direction,float step,int samples) {
        Array<Collider> coll=new Array<Collider>();
        BoxCollider box=this.cpy();
        box.start();
        
        for(int i=0;i<samples;i++) {
            box.x+=direction.x*step;
            box.y+=direction.y*step;
            box.update(0);
            this.getGameObject().getLevel();
            Array<Collider> tmp=this.getGameObject().getLevel().getComponent(Engine.GameManager.class).testCollision(box);
            for(Collider collider : tmp) {
                if(!collider.equals(this)&&!coll.contains(collider,false))
                    coll.add(collider);
            }
        }
        
        return coll;
    }
    
    @Override
    public void start() {
        transform=this.getGameObject().getComponent(Transform.class);
    }
    @Override
    public void update(float delta) {
        b1.set(this.x,this.y);
        b2.set(width,height);
        if(transform!=null) {
            Matrix3 mat=Pools.obtain(Matrix3.class).idt();
            b1.mul(transform.calculateMatrix(mat));
            mat.getScale(b2);
            b2.scl(width,height);
            Pools.free(mat);
        }
    }
    @Override
    public BoxCollider cpy() {
        BoxCollider col=new BoxCollider(this.x,this.y,this.width,this.height);
        col.copyFrom(this);
        col.start();
        col.update(0);
        return col;
    }
    
    @Override
    public String toString() {
        return "["+getTransformedX()+","+getTransformedY()+","+getTransformedWidth()+","+getTransformedHeight()+"]";
    }

}
