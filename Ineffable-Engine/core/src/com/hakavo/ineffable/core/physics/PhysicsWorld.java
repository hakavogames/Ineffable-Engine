package com.hakavo.ineffable.core.physics;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.math.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.CircleCollider;

public class PhysicsWorld extends GameComponent {
    public Array<RigidBody> bodies=new Array<RigidBody>();
    public static final Vector2 gravity=new Vector2(0,-9.81f);
    private final Vector2 normal=new Vector2();
    
    @Override
    public void start() {
    }
    @Override
    public void update(float delta) {
        for(int i=0;i<bodies.size;i++) {
            bodies.get(i).addForce(gravity.cpy().scl(bodies.get(i).mass));
            for(int j=i+1;j<bodies.size;j++) {
                RigidBody A=bodies.get(i);
                RigidBody B=bodies.get(j);
                if(A.getCollider().collides(B.getCollider())) {
                    A.getCollider().getNormal(B.getCollider(),normal);
                    if(normal.len2()==0)continue;
                    Vector2 rv=new Vector2(B.velocity).sub(A.velocity);
                    float penetrationDepth=normal.len();
                    normal.scl(1f/penetrationDepth);
                    float velAlongNormal=rv.dot(normal);
                    
                    if(velAlongNormal>=0)continue;
                    // Apply impulse
                    float e=Math.min(A.restitution,B.restitution);
                    float x=-(1+e)*velAlongNormal;
                    x/=A.inv_mass+B.inv_mass;
                    
                    Vector2 impulse=new Vector2(normal).scl(x);
                    A.velocity.sub(impulse.x*A.inv_mass,impulse.y*A.inv_mass);
                    B.velocity.add(impulse.x*B.inv_mass,impulse.y*B.inv_mass);
                    
                    // Positional correction
                    float percent=0.02f,slop=0.01f;
                    Vector2 correction=new Vector2(normal);
                    correction.scl(Math.max(penetrationDepth-slop,0.0f)/(A.inv_mass+B.inv_mass)*percent);
                    A.position.sub(A.inv_mass*correction.x,A.inv_mass*correction.y);
                    B.position.add(B.inv_mass*correction.x,B.inv_mass*correction.y);
                    
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
                    A.velocity.sub(frictionImpulse.x*A.inv_mass,frictionImpulse.y*A.inv_mass);
                    B.velocity.add(frictionImpulse.x*B.inv_mass,frictionImpulse.y*B.inv_mass);
                }
            }
        }
    }
    private float pythagoreanSolve(float a,float b) {
        return (float)Math.sqrt(a*a+b*b);
    }
}
