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
package com.kozma;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.kozma.core.*;
import com.kozma.core.ParticleSystem.Particle;
import com.kozma.gameobjects.*;
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
        background.getComponent(Transform.class).scale.set(1,1);
        
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
        
        Sprite2D fireSprite=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("fire.png"))));
        GameObject fire=new GameObject();
        fire.name="fire";
        fire.addComponent(new Transform());
        ParticleSystem ps=new ParticleSystem(fireSprite);
        ps.enableBlending=true;
        
        float speed=100;
        for(int i=0;i<500;i++)
        {
            float x=MathUtils.random(-10,10);
            float y=MathUtils.random(-10,10);
            Vector2 dir=new Vector2(x,y).nor();
            ps.particles.add(new Particle(0,0,0.1f,0.1f,
                                          dir.x*speed*MathUtils.random(0.5f,3f),dir.y*speed*MathUtils.random(0.5f,3f),
                                          2f*MathUtils.random(0.5f,1.5f),1));
        }
        
        fire.addComponent(ps);
        
        player.getComponent(SpriteRenderer.class).visible=false;
        background.getComponent(TiledBackground.class).layer=3;
        background.getComponent(Transform.class).position.set(0,32);
        ps.layer=1;
        player.getComponent(SpriteRenderer.class).layer=2;
        //engine.level.addGameObject(background);
        engine.level.addGameObject(player);
        engine.level.addGameObject(fire);
        
        Sprite2D l1=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/bg.png"))));
        Sprite2D l2=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/far-buildings.png"))));
        Sprite2D l3=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/buildings.png"))));
        Sprite2D l4=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/foreground.png"))));
        ParallaxScroller p1=new ParallaxScroller(l1,0);
        ParallaxScroller p2=new ParallaxScroller(l2,0.4f);
        ParallaxScroller p3=new ParallaxScroller(l3,0.7f);
        ParallaxScroller p4=new ParallaxScroller(l4,1f);
        p1.layer=7;
        p2.layer=6; 
        p3.layer=5;
        p4.layer=4;
        GameObject bg=new GameObject();
        bg.name="background";
        bg.addComponents(new Transform(),p1,p2,p3,p4);
        bg.getComponent(Transform.class).scale.set(1,1);
        
        engine.level.addGameObject(bg);
        animation.play();
    }
    @Override
    public void update(float delta) {
        Vector2 pos=((Transform)engine.level.getGameObjectByName("player").getComponent(Transform.class)).position;
        camera.position.set(pos.x,pos.y,0);
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
                transform.position.x-=speed*delta;
            }
            else if(Gdx.input.isKeyPressed(Keys.D))
            {
                spriteRenderer.flipX=false;
                transform.position.x+=speed*delta;
            }
            transform.position.x=MathUtils.round(transform.position.x);
            transform.position.y=MathUtils.round(transform.position.y);
        }
    }
}
