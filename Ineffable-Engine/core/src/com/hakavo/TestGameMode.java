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
import com.hakavo.gameobjects.*;
import com.hakavo.core.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.core.ParticleSystem.Particle;
import java.io.IOException;
import java.util.logging.*;

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
        
        SoundEffect fart_sound = new SoundEffect(Gdx.files.internal("sounds/sound effects/242004__junkfood2121__fart-01.wav"), 1f, 1);
        fart_sound.name = "fart";
        fart_sound.setLooping(true);
        
        SoundEffects playerSounds = new SoundEffects(fart_sound);
        
        Soundtrack track = new Soundtrack(Gdx.files.internal("sounds/soundtracks/game song.wav"), 1f, 1);
        track.play();
        track.setLooping(true);
        
        Sprite2D fireSprite=new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("fire.png"))));
        Joint player=new Joint();
        player.name="player";
        player.addComponent(new Transform());
        player.addComponent(new SpriteRenderer(sprite));
        player.addComponent(animationController);
        player.addComponent(new PlayerController());
        player.addComponent(new ParticleSystem(fireSprite));
        player.addComponent(new ScoreController());
        player.addComponent(playerSounds);
        
        background.getComponent(TiledBackground.class).layer=3;
        background.getComponent(Transform.class).matrix.translate(0,32);
        player.getComponent(SpriteRenderer.class).layer=2;
        //engine.level.addGameObject(background);
        engine.level.addGameObject(player);
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
        
        engine.level.addGameObject(bg);
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
    
    public class ScoreController extends Renderable {
        private int score;
        
        @Override
        public void start() {
            this.setMessageListener(new MessageListener() {
                public void messageReceived(GameComponent sender,String message,Object... parameters) {
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
    public class PlayerController extends GameComponent {
        public float speed=50;
        private Transform transform;
        private SpriteRenderer spriteRenderer;
        private GameObject tag;
        private ParticleSystem particleSystem;
        private AnimationController animationController;
        private ScoreController scoreController;
        private SoundEffects sounds;
        private String text="Press F to fart";
        
        @Override
        public void start() {
            this.name="PlayerController";
            this.transform=super.getGameObject().getComponent(Transform.class);
            this.spriteRenderer=super.getGameObject().getComponent(SpriteRenderer.class);
            this.particleSystem=super.getGameObject().getComponent(ParticleSystem.class);
            this.animationController=super.getGameObject().getComponent(AnimationController.class);
            this.scoreController=super.getGameObject().getComponent(ScoreController.class);
            this.sounds = super.getGameObject().getComponent(SoundEffects.class);
            
            animationController.play("idle");
            tag=new GameObject();
            tag.addComponent(new Transform(0,40,0.25f,0.25f).setRelative(this.transform));
            tag.addComponent(new TextRenderer(""));
            particleSystem.isTransformDependent=false;
            ((Joint)this.gameObject).addGameObject(tag);
        }
        @Override
        public void update(float delta) {
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
            if(Gdx.input.isKeyJustPressed(Keys.F)) {
                sounds.getSoundByName("fart").play();
                sounds.getSoundByName("fart").setLooping(true);
            }
            if(Gdx.input.isKeyPressed(Keys.F))
            {
                this.sendMessage(scoreController,"modifyScore",1);
                if(!animationController.getAnimationByName("fart").isPlaying())
                    animationController.play("fart");
                float speed=100;
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
            else {
                if(!animationController.getAnimationByName("idle").isPlaying())
                    animationController.play("idle");
                sounds.getSoundByName("fart").stop();
            }
        }
        public void updateText() {
            float time=GameServices.getElapsedTime();
            tag.getComponent(TextRenderer.class).text=text.substring(0,Math.min((int)(time*20),text.length()));
        }
    }
}
