package com.hakavo.game.objects;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.physics.*;
import com.hakavo.ineffable.core.collision.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.ineffable.GameServices;
import com.hakavo.ineffable.assets.AssetManager;
import com.hakavo.ineffable.core.ParticleSystem.Particle;

public class Projectile extends GameObject {
    private Vector2 direction;
    private float projectileLifespan;
    
    public Projectile(Vector2 position,Vector2 dir,Color color) {
        this(position,dir,color,1.5f);
    }
    public Projectile(Vector2 position,Vector2 dir,Color color,float maxDuration) {
        this(position,dir,color,maxDuration,1f);
    }
    public Projectile(Vector2 position,Vector2 dir,Color color,float maxDuration,float density) {
        this.direction=dir;
        this.projectileLifespan=maxDuration;
        addComponent(new Transform());
        addComponent(new BoxCollider(0,0,0.2f,0.2f));
        addComponent(new RigidBody(position,density,0));
        addComponent(new Trail(0.1f,10,color.cpy(),new Color(color.r,color.g,color.b,0f)));
        addComponent(new GameComponent() {
            protected BoxCollider box;
            protected RigidBody rb;
            protected Trail trail;
            public float lifespan;
            private float startTime;
            private boolean destroy;
            @Override
            public void start() {
                lifespan=projectileLifespan;
                trail=gameObject.getComponent(Trail.class);
                box=gameObject.getComponent(BoxCollider.class);
                rb=gameObject.getComponent(RigidBody.class);
                rb.addVelocity(direction);
                trail.layer=-1;
                trail.useTexture=false;
                startTime=GameServices.getElapsedTime();
                box.setCollisionAdapter(new CollisionAdapter() {
                    @Override
                    public void onCollisionEnter(Collider collider) {
                        Vector2 position=Pools.obtain(Vector2.class);
                        position.set(box.getTransformedX(),box.getTransformedY());
                        Vector2 normal=parent.getNormal(collider,Pools.obtain(Vector2.class)).scl(-1);
                        position.add(Math.min(normal.x,0),Math.min(normal.y,0));
                        float len=normal.len();
                        normal.scl(1f/len);
                        Vector2 vec=rb.getRawVelocity().cpy().nor();
                        float f=2*normal.dot(vec);
                        normal.scl(f).sub(vec);
                        addExplosions(position,normal.scl(-1),trail.start);
                        Pools.free(position);
                        Pools.free(normal);
                        AssetManager.getAsset("sound-hit",Sound.class).play(1,MathUtils.random(0.9f,2f),-0.2f);
                        sendMessage(gameObject.getLevel(),"shake",normal);
                        sendMessage(collider.getGameObject(),"damage",len*rb.density);
                        destroy=true;
                    }
                });
            }
            @Override
            public void update(float delta) {
                float f=(GameServices.getElapsedTime()-startTime)/lifespan;
                f=(float)Math.pow(f,10.0);
                //renderer.color.a=1f-f;
                trail.primary.a=1f-f;
                if(f>=1||destroy)gameObject.destroy();
            }
            public void addExplosions(Vector2 position,Vector2 normal,Color color) {
                GameObject g=new GameObject("particle",new Transform(position.x,position.y));
                ParticleSystem ps=new ParticleSystem(new Sprite2D("pixel"));
                ps.isTransformDependent=true;
                for(int i=1;i<=30;i++) {
                    Vector2 vel=new Vector2(normal).rotate(MathUtils.random(-45,45));
                    vel.scl(MathUtils.random(0.5f,3f));
                    ps.particles.add(new Particle(0,0,0.1f,0.1f,vel.x,vel.y,0.5f,3,color.r,color.g,color.b,1));
                }
                g.addComponent(ps);
                g.addComponent(new GameComponent() {
                    protected ParticleSystem ps;
                    @Override
                    public void start() {
                        ps=gameObject.getComponent(ParticleSystem.class);
                    }
                    @Override
                    public void update(float delta) {
                        if(ps.particles.size==0)gameObject.destroy();
                    }
                });
                getGameObject().getLevel().addGameObject(g);
            }
        });
        getComponent(Collider.class).tags.add(2);
        getComponent(Collider.class).ignoreTags.add(2);
    }
}
