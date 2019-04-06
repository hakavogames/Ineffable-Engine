package com.hakavo.ineffable.core.physics;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.*;
import com.badlogic.gdx.math.*;

public class RigidBody extends GameComponent implements GameComponent.Copiable {
    protected Transform transform;
    public Vector2 position;
    public Vector2 velocity;
    private Collider collider;
    
    public float mass,inv_mass;
    public float density;
    public float restitution;
    public float staticFriction,dynamicFriction;
    
    public RigidBody() {
        this(1f);
    }
    public RigidBody(float density) {
        this(new Vector2(),density);
    }
    public RigidBody(Vector2 position,float density) {
        this(position,density,0.2f);
    }
    public RigidBody(Vector2 position,float density,float restitution) {
        this(position,new Vector2(),density,restitution);
    }
    public RigidBody(Vector2 position,float density,float restitution,float staticFriction,float dynamicFriction) {
        this(position,new Vector2(),density,restitution,staticFriction,dynamicFriction);
    }
    public RigidBody(Vector2 position,Vector2 velocity,float density,float restitution) {
        this(null,position,velocity,density,restitution,0.5f,0.5f);
    }
    public RigidBody(Vector2 position,Vector2 velocity,float density,float restitution,float staticFriction,float dynamicFriction) {
        this(null,position,velocity,density,restitution,staticFriction,dynamicFriction);
    }
    public RigidBody(Collider collider,Vector2 position,Vector2 velocity,float density,float restitution,float staticFriction,float dynamicFriction) {
        this.density=density;
        this.position=position;
        this.velocity=velocity;
        this.collider=collider;
        this.restitution=restitution;
        this.staticFriction=staticFriction;
        this.dynamicFriction=dynamicFriction;
    }
    @Override
    public void start() {
        if(collider==null)collider=gameObject.getComponent(Collider.class);
        computeMass(density);
        transform=gameObject.getComponent(Transform.class);
        transform.matrix.setToTranslation(position);
        gameObject.getLevel().getComponent(PhysicsWorld.class).bodies.add(this);
    }
    @Override
    public void update(float delta) {
        position.add(velocity.x*delta,velocity.y*delta);
        transform.matrix.setToTranslation(position);
    }
    @Override
    public void onDestroy() {
        gameObject.getLevel().getComponent(PhysicsWorld.class).bodies.removeValue(this,true);
    }
    public void addForce(Vector2 force) {
        velocity.add(force.x*inv_mass,force.y*inv_mass);
    }
    public void computeMass(float density) {
        mass=density*collider.getVolume();
        inv_mass=(mass==0f) ? 0f : 1f/mass;
        this.density=density;
    }
    public void setFriction(float staticFriction,float dynamicFriction) {
        this.staticFriction=staticFriction;
        this.dynamicFriction=dynamicFriction;
    }
    
    public Collider getCollider() {
        return collider;
    }
    public void setCollider(Collider collider) {
        this.collider=collider;
    }
    
    @Override
    public RigidBody cpy() {
        return new RigidBody(density);
    }
}