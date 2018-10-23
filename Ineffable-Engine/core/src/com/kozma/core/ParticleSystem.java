package com.kozma.core;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.math.*;
import com.kozma.GameServices;

public class ParticleSystem extends Renderable {
    public Sprite2D sprite;
    public Array<Particle> particles=new Array<Particle>();
    public boolean enableBlending=false;
    
    private Transform transform;
    
    public ParticleSystem(Sprite2D sprite) {
        this.sprite=sprite;
    }
    @Override
    public void start() {
        transform=this.getGameObject().getComponent(Transform.class);
        if(transform==null)transform=new Transform();
    }
    @Override
    public void update(float delta) {
        for(Particle particle : particles)
        {
            particle.position.add(particle.velocity.x*delta,
                                  particle.velocity.y*delta);
            particle.lifetime+=delta;
            if(particle.lifetime>particle.lifespan&&particle.lifespan!=-1)
                particles.removeValue(particle,false);
        }
    }
    @Override
    public void render(OrthographicCamera camera) {
        super.prepareRendering();
        SpriteBatch sb=GameServices.getSpriteBatch();
        for(Particle particle : particles)
        {
            float scaleX=transform.scale.x*particle.scale.x,scaleY=transform.scale.y*particle.scale.y;
            TextureRegion tr=sprite.textureRegion;
            
            if(enableBlending)
                sb.setColor(particle.color.r,particle.color.g,particle.color.b,
                            particle.color.a*(1f-(float)Math.pow((particle.lifetime/particle.lifespan),particle.decayExponent)));
            else sb.setColor(particle.color);
            sb.draw(tr.getTexture(),(transform.position.x+particle.position.x)*scaleX,(transform.position.y+particle.position.y)*scaleY,
                    tr.getRegionWidth()*scaleX,tr.getRegionHeight()*scaleY,
                    tr.getRegionX(),tr.getRegionY(),tr.getRegionWidth(),tr.getRegionHeight(),false,false);
        }
    }
    
    public static class Particle {
        public float lifespan=1; // seconds
        public float lifetime=0; // same unit
        public float decayExponent=1;
        
        public Vector2 position=new Vector2();
        public Vector2 velocity=new Vector2();
        public Vector2 scale=new Vector2();
        public Color color=new Color(1,1,1,1);
        
        public Particle(float x,float y,float sclX,float sclY,float velX,float velY,float lifespan,float decayExponent,float r,float g,float b,float a) {
            position.set(x,y);
            scale.set(sclX,sclY);
            velocity.set(velX,velY);
            this.lifespan=lifespan;
            this.decayExponent=decayExponent;
            color.r=r;
            color.g=g;
            color.b=b;
            color.a=a;
        }
        public Particle(float x,float y,float sclX,float sclY,float velX,float velY,float lifespan,float decayExponent) {
            this(x,y,sclX,sclY,velX,velY,lifespan,decayExponent,1,1,1,1);
        }
        public Particle(float x,float y,float sclX,float sclY,float velX,float velY) {
            this(x,y,sclX,sclY,velX,velY,-1,1);
        }
        public Particle(float x,float y,float sclX,float sclY) {
            this(x,y,sclX,sclY,0,0);
        }
        public Particle(float x,float y) {
            this(x,y,1,1);
        }
        public Particle() {
            this(0,0);
        }
    }
}
