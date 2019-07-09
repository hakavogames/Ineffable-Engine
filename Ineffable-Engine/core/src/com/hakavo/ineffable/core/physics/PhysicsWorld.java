package com.hakavo.ineffable.core.physics;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.math.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.CircleCollider;

public class PhysicsWorld extends GameComponent {
    public Array<RigidBody> bodies=new Array<RigidBody>();
    public static final Vector2 gravity=new Vector2(0,-9.81f);
    private final Vector2 normal=new Vector2();
    private Thread thread;
    public float correctionPercent=0.4f,correctionSlop=0.1f;
    
    @Override
    public void start() {
        /*thread=new Thread(new Runnable() {
            @Override
            public void run() {
                long last=System.currentTimeMillis(),targetFPS=60;
                float dt=1000/targetFPS;
                while(true) {
                    updatePhysics(dt/1000);
                    float delay=1000/targetFPS;//-Math.max((dt-(1000f/targetFPS)),0);
                    try{Thread.sleep((int)delay);}catch(InterruptedException ex){}
                    dt=System.currentTimeMillis()-last;
                    last=System.currentTimeMillis();
                }
            }
        });
        thread.setPriority(10);
        thread.start();*/
    }
    private float accumulator;
    public float fps=60;
    private float dt;
    @Override
    public void update(float delta) {
        /*dt=1f/fps;
        accumulator+=delta;
        accumulator=Math.min(accumulator,0.2f);
        while(accumulator>=dt) {
            updatePhysics(dt);
            accumulator-=dt;
        }*/
        updatePhysics(Math.min(delta,0.1f));
        for(RigidBody body : bodies)
            body.updateTransform(0.05f);
    }
    private void updatePhysics(float dt) {
        for(int i=0;i<bodies.size;i++) {
            RigidBody A=bodies.get(i);
            if(A.enableGravity)
                A.addVelocity(gravity.cpy().scl(dt));
            A.getRawVelocity().add(A.forces.x*A.inv_mass*dt,A.forces.y*A.inv_mass*dt);
            for(int j=i+1;j<bodies.size;j++) {
                RigidBody B=bodies.get(j);
                boolean match=A.getCollider().matchTags(B.getCollider());
                if(match&&A.getCollider().collides(B.getCollider())) {
                    A.getCollider().getNormal(B.getCollider(),normal);
                    if(normal.len2()==0)continue;
                    Vector2 rv=new Vector2(B.getRawVelocity()).sub(A.getRawVelocity());
                    float penetrationDepth=normal.len();
                    normal.scl(1f/penetrationDepth);
                    float velAlongNormal=rv.dot(normal);
                    
                    if(velAlongNormal>=0)continue;
                    // Apply impulse
                    float e=Math.min(A.restitution,B.restitution);
                    float x=-(1+e)*velAlongNormal;
                    x/=A.inv_mass+B.inv_mass;
                    
                    Vector2 impulse=new Vector2(normal).scl(x);
                    A.getRawVelocity().sub(impulse.x*A.inv_mass,impulse.y*A.inv_mass);
                    B.getRawVelocity().add(impulse.x*B.inv_mass,impulse.y*B.inv_mass);
                    
                    // Positional correction
                    Vector2 correction=new Vector2(normal);
                    correction.scl(Math.max(penetrationDepth-correctionSlop,0.0f)/(A.mass+B.mass)*correctionPercent);
                    A.position.sub(A.mass*correction.x,A.mass*correction.y);
                    B.position.add(B.mass*correction.x,B.mass*correction.y);
                    
                    // Apply friction
                    Vector2 tangent=new Vector2(normal).scl(-rv.dot(normal)).add(rv).nor();
                    float jt=-rv.dot(tangent);
                    jt=jt/(A.inv_mass+B.inv_mass);
                    float mu=pythagoreanSolve(A.staticFriction,B.staticFriction);
                    Vector2 frictionImpulse=new Vector2();
                    if(Math.abs(jt)<x*mu)
                        frictionImpulse.set(tangent).scl(jt);
                    else {
                        float dynamicFriction=pythagoreanSolve(A.dynamicFriction,B.dynamicFriction);
                        frictionImpulse.set(tangent).scl(-x*dynamicFriction);
                    }
                    A.getRawVelocity().sub(frictionImpulse.x*A.inv_mass,frictionImpulse.y*A.inv_mass);
                    B.getRawVelocity().add(frictionImpulse.x*B.inv_mass,frictionImpulse.y*B.inv_mass);
                }
            }
            bodies.get(i).forces.set(0,0);
            bodies.get(i).position.add(bodies.get(i).getRawVelocity());
        }
    }
    
    private float pythagoreanSolve(float a,float b) {
        return (float)Math.sqrt(a*a+b*b);
    }
}
