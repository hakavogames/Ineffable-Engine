package com.hakavo.core.collision;
import com.hakavo.core.*;
import com.hakavo.core.GameComponent;

public abstract class Collider extends GameComponent {
    protected boolean collisionState;
    public CollisionAdapter collisionAdapter;
    
    protected abstract boolean collides(Collider collider);
    public void testCollision(Collider collider) {
        collisionState=collides(collider);
        if(collisionAdapter!=null&&collisionState==true)
            collisionAdapter.onCollision(collider.getGameObject());
    }
}
