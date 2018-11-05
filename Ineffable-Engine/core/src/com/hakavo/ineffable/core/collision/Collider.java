package com.hakavo.ineffable.core.collision;
import com.hakavo.ineffable.core.GameComponent;

public abstract class Collider extends GameComponent implements GameComponent.Copiable {
    protected boolean collisionState;
    public boolean active=true;
    protected CollisionAdapter collisionAdapter;
    
    private boolean foo=false;
    protected abstract boolean collides(Collider collider);
    public void testCollision(Collider collider) {
        collisionState=collides(collider);
        if(collisionAdapter!=null&&collisionState==true&&active&&collider.active)
        {
            if(foo==false)collisionAdapter.onCollisionEnter(collider);
            collisionAdapter.onCollision(collider);
            foo=true;
        }
        else if(foo==true&&collisionState==false&&collisionAdapter!=null)
            collisionAdapter.onCollisionExit(collider);
        foo=collisionState;
    }
    public void setCollisionAdapter(CollisionAdapter collisionAdapter) {
        this.collisionAdapter=collisionAdapter;
        collisionAdapter.parent=this;
    }
    public CollisionAdapter getCollsionAdapter() {
        return collisionAdapter;
    }
    @Override
    public <T extends GameComponent> void copyFrom(T copyFrom) {
        super.copyFrom(copyFrom);
        if(copyFrom instanceof Collider)
            this.collisionAdapter=((Collider)copyFrom).collisionAdapter;
    }
}
