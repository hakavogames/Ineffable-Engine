package com.hakavo.ineffable.core.collision;
import com.hakavo.ineffable.core.GameComponent;

public abstract class Collider extends GameComponent implements GameComponent.Copiable {
    protected boolean collisionState;
    public CollisionAdapter collisionAdapter;
    
    protected abstract boolean collides(Collider collider);
    public void testCollision(Collider collider) {
        collisionState=collides(collider);
        if(collisionAdapter!=null&&collisionState==true)
            collisionAdapter.onCollision(collider.getGameObject());
    }
    @Override
    public <T extends GameComponent> void copyFrom(T copyFrom) {
        super.copyFrom(copyFrom);
        if(copyFrom instanceof Collider)
            this.collisionAdapter=((Collider)copyFrom).collisionAdapter;
    }
}
