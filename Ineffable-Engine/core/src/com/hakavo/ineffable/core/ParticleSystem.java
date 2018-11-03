/*
 * Copyright 2018 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hakavo.ineffable.core;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.hakavo.ineffable.GameServices;
import com.hakavo.ineffable.core.collision.PointCollider;

public class ParticleSystem extends Renderable implements GameComponent.Copiable {
    public Sprite2D sprite;
    public Array<Particle> particles=new Array<Particle>();
    public boolean enableBlending=true;
    public boolean isTransformDependent=true;
    
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
            TextureRegion tr=sprite.textureRegion;
            
            if(enableBlending)
                sb.setColor(particle.color.r,particle.color.g,particle.color.b,
                            particle.color.a*(1f-(float)Math.pow((particle.lifetime/particle.lifespan),particle.decayExponent)));
            
            Matrix3 mat=Pools.obtain(Matrix3.class);
            Matrix4 mat4=Pools.obtain(Matrix4.class);
            mat.idt();
            if(isTransformDependent)transform.calculateMatrix(mat);
            mat.translate(particle.position).scale(particle.scale);
            sb.setTransformMatrix(mat4.set(mat));
            sb.draw(tr,tr.getRegionWidth(),tr.getRegionHeight());
            Pools.free(mat);
            Pools.free(mat4);
        }
    }
    @Override
    public ParticleSystem cpy() {
        ParticleSystem particleSystem=new ParticleSystem(this.sprite);
        particleSystem.copyFrom(this);
        particleSystem.enableBlending=this.enableBlending;
        particleSystem.isTransformDependent=this.isTransformDependent;
        return particleSystem;
    }
    @Override
    public void onGui(OrthographicCamera gui) {
    }
    
    public static class Particle {
        public float lifespan=1; // seconds
        public float lifetime=0; // same unit
        public float decayExponent=1;
        
        public Vector2 position=new Vector2();
        public Vector2 velocity=new Vector2();
        public Vector2 scale=new Vector2(1,1);
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
