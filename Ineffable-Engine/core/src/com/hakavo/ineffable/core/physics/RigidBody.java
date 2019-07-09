package com.hakavo.ineffable.core.physics;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;

public class RigidBody extends GameComponent implements GameComponent.Copiable {
    protected Transform transform;
    public Vector2 position;
    private final Vector2 velocity=new Vector2();
    private Collider collider;
    private float delta=1;
    public Vector2 forces=new Vector2();
    public boolean enableGravity=true;
    
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
        this(null,position,density,restitution,0.5f,0.5f);
    }
    public RigidBody(Vector2 position,float density,float restitution,float staticFriction,float dynamicFriction) {
        this(null,position,density,restitution,staticFriction,dynamicFriction);
    }
    public RigidBody(Collider collider,Vector2 position,float density,float restitution,float staticFriction,float dynamicFriction) {
        this.density=density;
        this.position=position;
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
        gameObject.getLevel().getComponent(PhysicsWorld.class).bodies.add(this);
        updateTransform(1f);
    }
    @Override
    public void update(float delta) {
        this.delta=delta;
    }
    public void updateTransform(float alpha) {
        Vector2 scale=transform.matrix.getScale(Pools.obtain(Vector2.class));
        Vector2 translation=transform.matrix.getTranslation(Pools.obtain(Vector2.class));
        transform.matrix.setToTranslation(translation.x*alpha+position.x*(1f-alpha),
                                          translation.y*alpha+position.y*(1f-alpha));
        transform.matrix.scale(scale);
        Pools.free(scale);
        Pools.free(translation);
    }
    @Override
    public void onDestroy() {
        if(gameObject.getLevel().hasComponent(PhysicsWorld.class))
            gameObject.getLevel().getComponent(PhysicsWorld.class).bodies.removeValue(this,true);
    }
    public void addForce(Vector2 force) {
        addForce(force.x,force.y);
    }
    public void addForce(float x,float y) {
        forces.add(x,y);
    }
    public void addVelocity(Vector2 velocity) { // units per second
        addVelocity(velocity.x,velocity.y);
    }
    public void addVelocity(float x,float y) {
        forces.add(x*mass,y*mass);
    }
    public Vector2 getVelocity(Vector2 out) {
        return out.set(velocity.x*delta,velocity.y*delta);
    }
    public Vector2 getRawVelocity() {
        return velocity;
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
    public float getDeltaTime() {
        return delta;
    }
    
    public Collider getCollider() {
        return collider;
    }
    public void setCollider(Collider collider) {
        this.collider=collider;
    }
    
    @Override
    public RigidBody cpy() {
        RigidBody result=new RigidBody(density);
        result.dynamicFriction=this.dynamicFriction;
        result.staticFriction=this.staticFriction;
        result.restitution=this.restitution;
        result.copyFrom(this);
        return result;
    }
}