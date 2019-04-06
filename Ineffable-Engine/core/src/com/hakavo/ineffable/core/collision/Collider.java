package com.hakavo.ineffable.core.collision;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.hakavo.ineffable.core.GameComponent;

public abstract class Collider extends GameComponent implements GameComponent.Copiable {
    protected boolean collisionState;
    public boolean active=true;
    public Array<Integer> tags=new Array<Integer>();
    public Array<Integer> ignoreTags=new Array<Integer>();
    protected CollisionAdapter collisionAdapter;
    protected Array<Collider> objectsHit=new Array<Collider>();
    
    public Collider() {
        tags.add(0);
    }
    
    public abstract boolean collides(Collider collider);
    public abstract Vector2 getNormal(Collider collider,Vector2 out);
    public abstract float getVolume();
    public void testCollision(Collider collider) {
        collisionState=collides(collider);
        if(!active||!collider.active)collisionState=false;
        
        if(collisionAdapter!=null&&matchTags(collider))
        {
            if(!objectsHit.contains(collider,true)&&collisionState==true) {
                objectsHit.add(collider);
                this.collisionAdapter.onCollisionEnter(collider);
            }
            else if(objectsHit.contains(collider,true)&&collisionState==true)
                this.collisionAdapter.onCollision(collider);
            else if(objectsHit.contains(collider,true)&&collisionState==false) {
                objectsHit.removeValue(collider,true);
                this.collisionAdapter.onCollisionExit(collider);
            }
        }
    }
    public Array<Collider> cast(Vector2 direction,float step,int samples) {
        return new Array<Collider>();
    }
    public boolean matchTags(Collider collider) {
        for(int tag : ignoreTags)
            if(collider.tags.contains(tag,true))
                return false;
        for(int tag : tags)
            if(collider.tags.contains(tag,true))
                return true;
        return false;
    }
    public void setCollisionAdapter(CollisionAdapter collisionAdapter) {
        this.collisionAdapter=collisionAdapter;
        collisionAdapter.parent=this;
    }
    public CollisionAdapter getCollsionAdapter() {
        return collisionAdapter;
    }
    public Array<Collider> getObjectsHit() {
        return objectsHit;
    }
    @Override
    public <T extends GameComponent> void copyFrom(T copyFrom) {
        super.copyFrom(copyFrom);
        if(copyFrom instanceof Collider) {
            tags.clear();
            Collider collider=((Collider)copyFrom);
            this.collisionAdapter=collider.collisionAdapter;
            this.active=collider.active;
            this.tags.addAll(collider.tags);
            this.ignoreTags.addAll(collider.ignoreTags);
        }
    }
}
