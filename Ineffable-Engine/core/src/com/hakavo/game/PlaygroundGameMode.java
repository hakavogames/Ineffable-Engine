package com.hakavo.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.game.components.CharacterController;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.assets.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.BoxCollider;
import com.hakavo.ineffable.core.skeleton.*;
import com.hakavo.ineffable.gameobjects.Map;

public class PlaygroundGameMode implements GameMode {
    protected Engine engine;
    
    @Override
    public void init(Engine engine) {
        this.engine=engine;
        //engine.camera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        float height=250;
        engine.camera.setToOrtho(false,(float)Gdx.graphics.getWidth()/Gdx.graphics.getHeight()*height,height);
        AssetManager.loadAsset("texture",Gdx.files.internal("sprites/Adventurer Sprite Sheet v1.1.png"),"spritesheet",false);
        AssetManager.loadAsset("texture",Gdx.files.internal("sprites/iron_sword.png"),"sword",false);
        AssetManager.loadAsset("texture",Gdx.files.internal("sprites/pixel.png"),"pixel",false);
        
        addMap();
        
        SkeletalSpriteRenderer sword=new SkeletalSpriteRenderer();
        Bone pivot=new Bone("pivot",new SpriteRenderer(new Sprite2D((Texture)AssetManager.getAsset("sword"))));
        pivot.getBoneTransform().position.set(12,-2);
        pivot.getBoneTransform().pivot.set(-8,-8);
        pivot.getBoneTransform().rotation=20;
        Bone flame=new Bone("flame");
        flame.getBoneTransform().position.set(-3,-3);
        pivot.addBone(flame);
        sword.setSkeleton(pivot);
        
        Bone idleA=pivot.cpy();
        Bone idleB=pivot.cpy();
        idleB.getBoneTransform().rotation=27;
        
        Bone hit=pivot.cpy();
        hit.getBoneTransform().rotation=-65;
        SkeletalAnimation sword_idle=new SkeletalAnimation("idle",true,new KeyFrame(idleA,0.8f,Interpolation.pow2),
                                                                       new KeyFrame(idleB,0.8f,Interpolation.pow2));
        SkeletalAnimation sword_hit=new SkeletalAnimation("hit",false,
                new KeyFrame(idleA,0.1f,Interpolation.pow2),
                new KeyFrame(hit,0.4f,Interpolation.pow2)
        );
        
        SkeletalAnimationController swordAnimController=new SkeletalAnimationController(sword_idle,sword_hit);
        
        AnimationClip runClip=new AnimationClip();
        runClip.duration=0.75f;
        runClip.loop=true;
        for(int i=0;i<8;i++)
            runClip.frames.add(new TextureRegion((Texture)AssetManager.getAsset("spritesheet"),
                                              i*32,32,32,32));
        AnimationClip idleClip=new AnimationClip();
        idleClip.duration=3f;
        idleClip.loop=true;
        for(int i=0;i<13;i++)
            idleClip.frames.add(new TextureRegion((Texture)AssetManager.getAsset("spritesheet"),
                                              i*32,0,32,32));
        Animation run=new Animation("run",runClip);
        Animation idle=new Animation("idle",idleClip);
        
        GameObject swordObj=new GameObject("sword",
                new Transform(0,0),
                sword,
                swordAnimController,
                new ParticleSystem(new Sprite2D("pixel")),
                new SwordController()
        );
        
        Joint player=new Joint();
        player.name="player";
        player.addGameObject(swordObj);
        player.addComponent(new Transform(0,0));
        player.addComponent(new BoxCollider(-8,-16,8,32));
        player.addComponent(new SpriteRenderer(new Sprite2D()));
        player.addComponents(run,idle);
        player.addComponent(new CharacterController());
        player.addComponent(new AnimationController());
        player.addComponent(new PlayerController());
        swordObj.getComponent(Transform.class).setRelative(player.getComponent(Transform.class));
        
        engine.getLevel().addGameObject(player);
        player.getComponent(AnimationController.class).play("run");
    }
    public void addMap() {
        Map map=new Map(new Tileset(Gdx.files.internal("tilesets/prison.xml")),
                        Gdx.files.internal("maps/tutorial/map.xml"));
        map.getComponent(Transform.class).matrix.scl(1.5f);
        engine.getLevel().addGameObject(map);
    }
    @Override
    public void update(float delta) {
        Vector2 pos=Pools.obtain(Vector2.class);
        engine.getLevel().getGameObjectByName("player").getComponent(Transform.class).matrix.getTranslation(pos);
        engine.camera.position.x+=(pos.x-engine.camera.position.x)*0.1f;
        engine.camera.position.y+=(pos.y-engine.camera.position.y)*0.1f;
        Pools.free(pos);
    }
    @Override
    public void renderGui(OrthographicCamera camera) {
    }
    public static class PlayerController extends GameComponent {
        protected AnimationController animation;
        protected CharacterController character;
        protected SpriteRenderer spriteRenderer;
        
        protected GameObject sword;
        private Transform swordTrans;
        
        public float speed=50;
        private final Vector2 dir=new Vector2();
        
        @Override
        public void start() {
            animation=gameObject.getComponent(AnimationController.class);
            character=gameObject.getComponent(CharacterController.class);
            spriteRenderer=gameObject.getComponent(SpriteRenderer.class);
            sword=((Joint)gameObject).getGameObjectByName("sword");
            swordTrans=sword.getComponent(Transform.class);
        }
        @Override
        public void update(float delta) {
            dir.set(0,0);
            if(Gdx.input.isKeyPressed(Keys.A))dir.x=-delta*speed;
            else if(Gdx.input.isKeyPressed(Keys.D))dir.x=delta*speed;
            if(Gdx.input.isKeyPressed(Keys.W))dir.y=delta*speed;
            else if(Gdx.input.isKeyPressed(Keys.S))dir.y=-delta*speed;
            
            dir.nor();
            if(dir.x!=0||dir.y!=0) {
                character.move(dir);
                animation.setAnimation("run");
                
                //if(dir.x<0)swordTrans.matrix.setToTranslation(-16,0);
                //else swordTrans.matrix.setToTranslation(0,0);
                //if(dir.x<0)sword.getComponent(SkeletalAnimationController.class).setInvert(true);
                //else sword.getComponent(SkeletalAnimationController.class).setInvert(false);
                if(dir.x<0) {
                    //swordTrans.matrix.setToTranslation(-12,-2);
                    swordTrans.matrix.setToScaling(-1,1);
                }
                else {
                    //swordTrans.matrix.setToTranslation(12,-2);
                    swordTrans.matrix.setToScaling(1,1);
                }
            }
            else animation.setAnimation("idle");
            
            if(Gdx.input.isKeyJustPressed(Keys.F))
                sendMessage(sword,"hit");
        }
        public boolean right() {
            return !spriteRenderer.flipX;
        }
    }
    public static class SwordController extends GameComponent {
        protected SkeletalAnimationController anim;
        protected SkeletalSpriteRenderer renderer;
        protected ParticleSystem particles;
        protected Transform transform;
        private float accumulator;
        
        @Override
        public void start() {
            renderer=gameObject.getComponent(SkeletalSpriteRenderer.class);
            transform=gameObject.getComponent(Transform.class);
            particles=gameObject.getComponent(ParticleSystem.class);
            particles.isTransformDependent=false;
            particles.enableBlending=true;
            
            anim=gameObject.getComponent(SkeletalAnimationController.class);
            anim.play("idle");
            setMessageListener(new MessageListener() {
                @Override
                public void messageReceived(GameObject sender,String message,Object... parameters) {
                    if(message.equals("hit")) {
                        if(anim.getCurrentAnimation().name.equals("idle"))anim.play("hit");
                    }
                }
            });
        }
        @Override
        public void update(float delta) {
            if(!anim.isPlaying())anim.play("idle");
            accumulator+=delta;
            if(accumulator>0.1f) {
                accumulator=0;
                Vector2 vel=Pools.obtain(Vector2.class).set(MathUtils.random(-5.5f,5.5f),MathUtils.random(4,10)).nor().scl(30);
                Matrix3 trans=Pools.obtain(Matrix3.class);
                renderer.getSkeleton().getBone("flame").getBoneTransform().getTransform().calculateMatrix(trans);
                //trans.translate(4,4).translate(renderer.getSkeleton().getBoneTransform().getPivot());
                Vector2 pos=Pools.obtain(Vector2.class);trans.getTranslation(pos);
                if(trans.val[0]<0)pos.x-=16;
                
                ParticleSystem.Particle fire=new ParticleSystem.Particle(MathUtils.random(-7,7),MathUtils.random(-5,2),5,5,vel.x,vel.y,1,2);
                fire.color.set(0.8f,0.4f,0.3f,0.75f);
                fire.position.set(pos);
                particles.particles.add(fire);
                Pools.free(vel);
                Pools.free(pos);
                Pools.free(trans);
            }
        }
    }
}
