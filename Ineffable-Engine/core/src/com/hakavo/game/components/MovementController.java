package com.hakavo.game.components;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.*;
import com.hakavo.ineffable.core.physics.*;

public class MovementController extends GameComponent implements MessageListener {
    public float speed=1;
    protected RigidBody rigidBody;
    protected BoxCollider collider;
    
    @Override
    public void start() {
        rigidBody=gameObject.getComponent(RigidBody.class);
        collider=gameObject.getComponent(BoxCollider.class);
        super.messageListener=this;
    }
    @Override
    public void update(float delta) {
        rigidBody.getRawVelocity().x=MathUtils.clamp(rigidBody.getRawVelocity().x,-delta*6,delta*6);
        rigidBody.getRawVelocity().y=MathUtils.clamp(rigidBody.getRawVelocity().y,-delta*12,delta*12);
    }

    @Override
    public void messageReceived(GameObject sender,String message,Object... parameters) {
        Vector2 vel=rigidBody.getVelocity(Pools.obtain(Vector2.class));
        if(message.equals("go_left"))
            rigidBody.addVelocity(-speed-Math.max(vel.x,0)/2,0);
        else if(message.equals("go_right"))
            rigidBody.addVelocity(speed+Math.max(-vel.x,0)/2,0);
        else if(message.equals("jump")) {
            Array<Collider> colliders=collider.cast(new Vector2(0,-1),0.05f,1);
            if(colliders.size>=1)rigidBody.addVelocity(0,9f);
        }
        else if(message.equals("set_speed"))
            this.speed=(Float)parameters[0];
        Pools.free(vel);
    }
}
