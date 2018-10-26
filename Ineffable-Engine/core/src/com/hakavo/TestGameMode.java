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

package com.hakavo;
import com.hakavo.gameobjects.Map;
import com.hakavo.core.ParallaxScroller;
import com.hakavo.core.Transform;
import com.hakavo.core.Sprite2D;
import com.hakavo.core.TiledBackground;
import com.hakavo.core.GameComponent;
import com.hakavo.core.ParticleSystem;
import com.hakavo.core.TextRenderer;
import com.hakavo.core.Animation;
import com.hakavo.core.SpriteRenderer;
import com.hakavo.core.AnimationClip;
import com.hakavo.core.GameObject;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.core.ParticleSystem.Particle;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestGameMode implements GameMode {
    Map map;
    OrthographicCamera camera;
    Engine engine;
    
    public TestGameMode() {
    }
    @Override
    public void init(Engine engine) {
        Tileset tileset=new Tileset(Gdx.files.internal("tileset.xml"));
        Sprite2D wall=tileset.tiles.get(tileset.getTileIndexByName("wall")).toSprite();
        this.engine=engine; 
        camera=engine.camera;
        
        GameObject background=new GameObject();
        background.addComponent(new Transform());
        background.addComponent(new TiledBackground(wall));
        background.getComponent(Transform.class).matrix.setToScaling(1,1);
        
        Tileset poses=new Tileset(Gdx.files.internal("Scavengers_SpriteSheet.png"),32);
        Sprite2D sprite=new Sprite2D();
        
        AnimationClip clip=new AnimationClip();
        for(int i=0;i<6;i++)clip.frames.add(poses.tiles.get(i).createTextureRegion());
        clip.duration=1f;
        clip.loop=true;
        Animation animation=new Animation();
        animation.setTarget(sprite);
        animation.clip=clip;
        animation.name="idle";
        
        GameObject player=new GameObject();
        player.name="player";
        player.addComponent(new Transform());
        player.addComponent(new SpriteRenderer(sprite));
        player.addComponent(animation);
        player.addComponent(new PlayerController());
        player.getComponent(SpriteRenderer.class).visible=true;
        
        Sprite2D fireSprite=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("fire.png"))));
        GameObject fire=new GameObject();
        fire.name="fire";
        fire.addComponent(new Transform(0,0,4,4));
        fire.addComponent(new TextRenderer("gelu"));
        ParticleSystem ps=new ParticleSystem(fireSprite);
        fire.getComponent(TextRenderer.class).layer=1;
        ps.enableBlending=true;
        
        fire.addComponent(ps);
        fire.addComponent(new GameComponent() {
            private ParticleSystem ps;
            @Override
            public void start() {
                ps=this.getGameObject().getComponent(ParticleSystem.class);
            }
            @Override
            public void update(float delta) {
                float speed=100;
                if(ps.particles.size<500)
                {
                    float x=MathUtils.random(-10,10);
                    float y=MathUtils.random(5,10);
                    Vector2 dir=new Vector2(x,y).nor();
                    ps.particles.add(new Particle(MathUtils.random(-50,50),MathUtils.random(-50,50),0.1f,0.1f,
                                                  dir.x*speed*MathUtils.random(0.5f,3f),dir.y*speed*MathUtils.random(0.5f,3f),
                                                  2f*MathUtils.random(0.5f,1.5f),2));
                }
            }
        });
        
        background.getComponent(TiledBackground.class).layer=3;
        background.getComponent(Transform.class).matrix.translate(0,32);
        ps.layer=1;
        player.getComponent(SpriteRenderer.class).layer=2;
        //engine.level.addGameObject(background);
        engine.level.addGameObject(player);
        engine.level.addGameObject(fire);
        
        Sprite2D l1=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/trees1.png"))));
        Sprite2D l2=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/trees2.png"))));
        Sprite2D l3=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/trees3.png"))));
        Sprite2D l4=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/mountains.png"))));
        Sprite2D l5=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/clouds.png"))));
        Sprite2D l6=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/background.png"))));
        ParallaxScroller p1=new ParallaxScroller(l1,1);
        ParallaxScroller p2=new ParallaxScroller(l2,0.75f);
        ParallaxScroller p3=new ParallaxScroller(l3,0.6f);
        ParallaxScroller p4=new ParallaxScroller(l4,0.25f);
        ParallaxScroller p5=new ParallaxScroller(l5,0.1f);
        ParallaxScroller p6=new ParallaxScroller(l6,0f);
        p1.layer=5;
        p2.layer=6;
        p3.layer=7;
        p4.layer=8;
        p5.layer=9;
        p6.layer=10;
        GameObject bg=new GameObject();
        bg.name="background";
        bg.addComponents(new Transform(0,0,1,1f),p1,p2,p3,p4,p5,p6);
        
        engine.level.addGameObject(bg);
        animation.play();
    }
    @Override
    public void update(float delta) {
        Vector2 pos=Pools.obtain(Vector2.class);
        GameObject player=engine.level.getGameObjectByName("player");
        if(player!=null)
            player.getComponent(Transform.class).matrix.getTranslation(pos);
        camera.position.set(pos.x,pos.y,0);
        Pools.free(pos);
    }
    @Override
    public void render(OrthographicCamera ui) {
    }
    
    public class PlayerController extends GameComponent {
        public float speed=50;
        private Transform transform;
        private SpriteRenderer spriteRenderer;
        @Override
        public void start() {
            this.transform=super.getGameObject().getComponent(Transform.class);
            this.spriteRenderer=super.getGameObject().getComponent(SpriteRenderer.class);
        }
        @Override
        public void update(float delta) {
            if(Gdx.input.isKeyPressed(Keys.A))
            {
                spriteRenderer.flipX=true;
                transform.matrix.translate(-speed*delta,0);
            }
            else if(Gdx.input.isKeyPressed(Keys.D))
            {
                spriteRenderer.flipX=false;
                transform.matrix.translate(speed*delta,0);
            }
            Vector2 position=Pools.obtain(Vector2.class);
            transform.matrix.getTranslation(position);
            transform.matrix.setToTranslation(MathUtils.round(position.x),MathUtils.round(position.y));
            Pools.free(position);
        }
    }
}
