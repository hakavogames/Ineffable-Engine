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

package com.hakavo.game;
import com.hakavo.ineffable.core.collision.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.gameobjects.Map;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.ineffable.Engine;
import com.hakavo.ineffable.GameMode;
import com.hakavo.ineffable.GameServices;
import com.hakavo.ineffable.Tileset;
import com.hakavo.ineffable.core.GameComponent.Copiable;
import com.hakavo.ineffable.core.ParticleSystem.Particle;
import java.io.IOException;
import java.util.logging.*;

public class TestGameMode implements GameMode {
    Map map;
    OrthographicCamera camera;
    Engine engine;
    Tileset poses;
    
    public TestGameMode() {
    }
    @Override
    public void init(Engine engine) {
        Tileset tileset=new Tileset(Gdx.files.internal("tileset.xml"));
        Sprite2D wall=tileset.tiles.get(tileset.getTileIndexByName("wall")).toSprite();
        this.engine=engine;
        camera=engine.camera;
        camera.setToOrtho(false,400,225);
        
        GameObject background=new GameObject();
        background.addComponent(new Transform());
        background.addComponent(new TiledBackground(wall));
        background.getComponent(Transform.class).matrix.setToScaling(1,1);
        
        Sprite2D sprite=new Sprite2D();
        
        poses=new Tileset(Gdx.files.internal("Scavengers_SpriteSheet.png"),32);
        AnimationClip clip1=new AnimationClip();
        for(int i=0;i<6;i++)clip1.frames.add(poses.tiles.get(i).createTextureRegion());
        clip1.duration=1f;
        clip1.loop=true;
        
        AnimationClip clip2=new AnimationClip();
        for(int i=46;i<48;i++)clip2.frames.add(poses.tiles.get(i).createTextureRegion());
        clip2.duration=0.6f;
        clip2.loop=true;
        
        Animation idle=new Animation("idle",clip1);
        Animation fart=new Animation("fart",clip2);
        AnimationController animationController=new AnimationController(sprite,idle,fart);
        
        initEnemy();
        
        Sprite2D fireSprite=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("fire.png"))));
        Joint player=new Joint();
        player.name="player";
        player.addComponent(new Transform());
        player.addComponent(new SpriteRenderer(sprite));
        player.addComponent(animationController);
        player.addComponent(new BoxCollider());
        player.addComponent(new PlayerController());
        player.addComponent(new ParticleSystem(fireSprite));
        player.addComponent(new ScoreController());
        
        background.getComponent(TiledBackground.class).layer=3;
        background.getComponent(Transform.class).matrix.translate(0,32);
        player.getComponent(SpriteRenderer.class).layer=2;
        //engine.level.addGameObject(background);
        engine.getLevel().addGameObject(player);
        //engine.level.addGameObject(fire);
        
        Sprite2D l1=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/trees1.png"))));
        Sprite2D l2=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/trees2.png"))));
        Sprite2D l3=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/trees3.png"))));
        Sprite2D l4=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/mountains.png"))));
        Sprite2D l5=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/clouds.png"))));
        Sprite2D l6=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/background.png"))));
        ParallaxScroller p1=new ParallaxScroller(l1,1);
        ParallaxScroller p2=new ParallaxScroller(l2,0.75f);
        ParallaxScroller p3=new ParallaxScroller(l3,0.4f);
        ParallaxScroller p4=new ParallaxScroller(l4,0.15f);
        ParallaxScroller p5=new ParallaxScroller(l5,0.05f);
        ParallaxScroller p6=new ParallaxScroller(l6,0f);
        p1.layer=5;
        p2.layer=6;
        p3.layer=7;
        p4.layer=8;
        p5.layer=9;
        p6.layer=10;
        GameObject bg=new GameObject();
        bg.name="background";
        bg.addComponents(new Transform(0,0,0.4f,0.4f),p1,p2,p3,p4,p5,p6);
        
        engine.getLevel().addGameObject(bg);
    }
    public void initEnemy() {
        Sprite2D enemySprite=new Sprite2D();
        
        AnimationClip idleClip=new AnimationClip();
        for(int i=12;i<18;i++)idleClip.frames.add(poses.tiles.get(i).createTextureRegion());
        idleClip.duration=1f;
        idleClip.loop=true;
        Animation idle=new Animation("idle",idleClip);
        
        AnimationClip hitClip=new AnimationClip();
        for(int i=44;i<46;i++)hitClip.frames.add(poses.tiles.get(i).createTextureRegion());
        hitClip.duration=0.3f;
        hitClip.loop=true;
        Animation hit=new Animation("hit",hitClip);
        
        AnimationController animController=new AnimationController(enemySprite,idle,hit);
        
        GameObject enemy=new GameObject();
        enemy.name="zombie";
        enemy.addComponent(new Transform(150,0));
        enemy.addComponent(new SpriteRenderer(enemySprite,false,false));
        enemy.addComponent(new BoxCollider());
        enemy.addComponent(animController);
        enemy.addComponent(new EnemyBehaviour());
        enemy.addComponent(new EnemyController());
        
        Sprite2D enemyCloneSprite=new Sprite2D();
        GameObject enemyClone=enemy.cpy();
        enemyClone.getComponent(Transform.class).matrix.translate(-80,0);
        enemyClone.getComponent(SpriteRenderer.class).sprite=enemyCloneSprite;
        enemyClone.getComponent(AnimationController.class).setTarget(enemyCloneSprite);
        
        engine.getLevel().addGameObject(enemyClone);
        engine.getLevel().addGameObject(enemy);
    }
    public class EnemyBehaviour extends GameComponent implements GameComponent.Copiable {
        
        private float health=100;
        public void start() {
            this.setMessageListener(new MessageListener() {
                @Override
                public void messageReceived(GameObject sender,String message,Object... args) {
                    if(message.equals("damage"))
                        health-=(Float)args[0];
                }
            });
        }
        public void update(float delta) {
            if(health<=0)
                this.getGameObject().destroy();
        }
        public EnemyBehaviour cpy() {
            EnemyBehaviour eb=new EnemyBehaviour();
            eb.copyFrom(this);
            return eb;
        }
    }
    public class EnemyController extends GameComponent implements GameComponent.Copiable {
        private Transform transform;
        private AnimationController animController;
        private SpriteRenderer spriteRenderer;
        private BoxCollider collider;
        @Override
        public void start() {
            transform=this.getGameObject().getComponent(Transform.class);
            animController=this.getGameObject().getComponent(AnimationController.class);
            spriteRenderer=this.getGameObject().getComponent(SpriteRenderer.class);
            collider=this.getGameObject().getComponent(BoxCollider.class);
            collider.setCollisionAdapter(new CollisionAdapter() {
                @Override
                public void onCollision(Collider collider) {
                    if(gameObject.name.equals("player"))
                    {
                        TextRenderer tr=((Joint)collider.getGameObject()).gameObjects.get(0).getComponent(TextRenderer.class);
                        tr.text="";
                        for(int i=0;i<10;i++)
                            tr.text+=(char)MathUtils.random(255);
                    }
                }
            });
            spriteRenderer.layer=3;
            
            animController.play("idle");
        }
        @Override
        public void update(float delta) {
            collider.setSize(spriteRenderer.sprite.textureRegion.getRegionWidth(),spriteRenderer.sprite.textureRegion.getRegionHeight());
            Vector2 position=new Vector2();
            engine.getLevel().getGameObjectByName("player").getComponent(Transform.class).getPosition(position);
            
            float dist=position.x-transform.getPosition(new Vector2()).x;
            
            if(Math.abs(dist)<=50&&animController.getCurrentAnimation().name.equals("idle"))
                animController.play("hit");
            if(Math.abs(dist)>50&&animController.getCurrentAnimation().name.equals("hit"))
                animController.play("idle");
            
            if(dist>=0)spriteRenderer.flipX=true;
            else spriteRenderer.flipX=false;
        }

        @Override
        public EnemyController cpy() {
            EnemyController ec=new EnemyController();
            ec.copyFrom(this);
            return ec;
        }
    }
    @Override
    public void update(float delta) {
        Vector2 pos=Pools.obtain(Vector2.class);
        GameObject player=engine.getLevel().getGameObjectByName("player");
        if(player!=null)
            player.getComponent(Transform.class).matrix.getTranslation(pos);
        camera.position.set(pos.x,pos.y,0);
        Pools.free(pos);
    }
    @Override
    public void renderGui(OrthographicCamera ui) {
    }

    private static class Arrow extends GameObject {
        Transform transform;
        SpriteRenderer spriteRenderer;
        BoxCollider collider;
        ArrowBehaviour arrowBehaviour;
        
        public Arrow(boolean flipX) {
            Sprite2D arrow=new Sprite2D(new TextureRegion(new Texture("sprites/arrow.png"))); 
            transform=new Transform(0,24,0.5f,0.5f);
            collider=new BoxCollider(0,0,arrow.textureRegion.getRegionWidth(),arrow.textureRegion.getRegionHeight());
            spriteRenderer=new SpriteRenderer(arrow,flipX,false);
            spriteRenderer.layer=2;
            arrowBehaviour=new ArrowBehaviour(flipX);
            
            super.addComponents(transform,spriteRenderer,collider,arrowBehaviour);
        }
        
        private static class ArrowBehaviour extends GameComponent {
            Transform transform;
            BoxCollider collider;
            float spawnTime=0,lifespan=2;
            float direction=1;
            
            public ArrowBehaviour(boolean flipX) {
                if(flipX)direction=-1;
            }
            public void start() {
                transform=super.getGameObject().getComponent(Transform.class);
                collider=super.getGameObject().getComponent(BoxCollider.class);
                spawnTime=GameServices.getElapsedTime();
                
                collider.setCollisionAdapter(new CollisionAdapter() {
                    @Override
                    public void onCollision(Collider collider) {
                        if(gameObject.name.equals("zombie"))
                        {
                            getGameObject().getComponent(ArrowBehaviour.class).sendMessage(collider.getGameObject(),"damage",30f);
                            getGameObject().destroy();
                        }
                    }
                });
            }
            public void update(float delta) {
                if(GameServices.getElapsedTime()-spawnTime>=lifespan){super.getGameObject().destroy();return;}
                
                transform.matrix.translate(direction*400*delta,0);
            }
        }
    }
    
    public class ScoreController extends Renderable {
        private int score;
        
        @Override
        public void start() {
            this.setMessageListener(new MessageListener() {
                public void messageReceived(GameObject sender,String message,Object... parameters) {
                    if(message.equals("modifyScore"))
                        score+=(Integer)parameters[0];
                }
            });
        }
        @Override
        public void onGui(OrthographicCamera gui) {
            BitmapFont font=GameServices.getFonts().getValueAt(0);
            SpriteBatch sb=GameServices.getSpriteBatch();
            
            font.setColor(0,0,0,1);
            font.draw(sb,"Score: "+score,50,50);
        }
        @Override
        public void update(float delta) {
        }
        @Override
        public void render(OrthographicCamera camera) {
        }
    }
    public class PlayerController extends GameComponent implements GameComponent.Copiable {
        public float speed=50;
        private Transform transform;
        private SpriteRenderer spriteRenderer;
        private GameObject tag;
        private ParticleSystem particleSystem;
        private AnimationController animationController;
        private ScoreController scoreController;
        private BoxCollider collider;
        private String text="Press E to shoot";
        
        @Override
        public void start() {
            this.name="PlayerController";
            this.transform=super.getGameObject().getComponent(Transform.class);
            this.spriteRenderer=super.getGameObject().getComponent(SpriteRenderer.class);
            this.particleSystem=super.getGameObject().getComponent(ParticleSystem.class);
            this.animationController=super.getGameObject().getComponent(AnimationController.class);
            this.scoreController=super.getGameObject().getComponent(ScoreController.class);
            this.collider=super.getGameObject().getComponent(BoxCollider.class);
            
            animationController.play("idle");
            tag=new GameObject();
            tag.addComponent(new Transform(0,40,0.25f,0.25f).setRelative(this.transform));
            tag.addComponent(new TextRenderer(""));
            particleSystem.isTransformDependent=false;
            ((Joint)this.gameObject).addGameObject(tag);
        }
        @Override
        public void update(float delta) {
            collider.setSize(spriteRenderer.sprite.textureRegion.getRegionWidth(),spriteRenderer.sprite.textureRegion.getRegionHeight());
            updateText();
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
            if(Gdx.input.isKeyJustPressed(Keys.E))
            {
                Arrow arrow=new Arrow(spriteRenderer.flipX);
                arrow.getComponent(Transform.class).setRelative(transform);
                this.getGameObject().getParent().addGameObject(arrow);
            }
            if(Gdx.input.isKeyPressed(Keys.F))
            {
                if(!animationController.getAnimationByName("fart").isPlaying())
                    animationController.play("fart");
                fart();
            }
            else if(!animationController.getAnimationByName("idle").isPlaying())
                animationController.play("idle");
            
        }
        @Override
        public PlayerController cpy() {
            return new PlayerController();
        }
        public void updateText() {
            float time=GameServices.getElapsedTime();
            tag.getComponent(TextRenderer.class).text=text.substring(0,Math.min((int)(time*20),text.length()));
        }
        public void fart() {
            this.sendMessage(this.getGameObject(),"modifyScore",1);
            if(particleSystem.particles.size<500)
            {
                Vector2 playerPos=Pools.obtain(Vector2.class);
                transform.matrix.getTranslation(playerPos);
                
                float x=MathUtils.random(-10,-7);
                float y=MathUtils.random(-2,2);
                Vector2 dir=new Vector2(x,y).nor().scl(0.4f);
                particleSystem.particles.add(new Particle(MathUtils.random(-40,-20)+playerPos.x,MathUtils.random(-15,-10)+playerPos.y,
                                                          0.15f,0.15f,
                                                          dir.x*speed*MathUtils.random(1.5f,3f),dir.y*speed*MathUtils.random(1.5f,3f),
                                                          2f*MathUtils.random(0.5f,1.5f),2));
                
                Pools.free(playerPos);
            }
        }
    }
}
