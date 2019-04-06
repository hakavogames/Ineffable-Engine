package com.hakavo.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.game.ai.*;
import com.hakavo.game.ai.pathfinding.*;
import com.hakavo.game.components.CharacterController;
import com.hakavo.game.mechanics.DialogueSystem;
import com.hakavo.game.mechanics.DialogueSystem.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.assets.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.BoxCollider;
import com.hakavo.ineffable.core.skeleton.*;
import com.hakavo.ineffable.gameobjects.Map;

public class PrisonGameMode implements GameMode {
    protected Engine engine;
    
    @Override
    public void init(Engine engine) {
        this.engine=engine;
        //engine.camera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        float height=250;
        engine.camera.setToOrtho(false,(float)Gdx.graphics.getWidth()/Gdx.graphics.getHeight()*height,height);
        AssetManager.loadAsset("texture",Gdx.files.internal("sprites/Adventurer Sprite Sheet v1.1.png"),"spritesheet",false);
        AssetManager.loadAsset("texture",Gdx.files.internal("sprites/pixel.png"),"pixel",false);
        
        addMap();
        
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
        
        DialogueSystem dialogueSystem=new DialogueSystem(true,0,0.5f,1.2f);
        
        Dialogue dial1=new Dialogue("greeting","Hey there",1.2f,true);
        dial1.choices.add(new Choice("yay"));
        Dialogue dial2=new Dialogue("yay","This thing really works, yay!",2f,true);
        dialogueSystem.dialogues.addAll(dial1,dial2);
        
        Joint player=new Joint("player");
        player.addComponent(new Transform(256,32));
        player.addComponent(new BoxCollider(-8,-14,16,7));
        player.addComponent(new SpriteRenderer(new Sprite2D()));
        player.addComponents(run,idle);
        player.addComponent(new CharacterController());
        player.addComponent(new AnimationController());
        player.addComponent(new PlayerController());
        
        GameObject text=new GameObject("text");
        text.addComponent(new Transform(0,20,1f,1f).setRelative(player.getComponent(Transform.class)));
        text.addComponent(new TextRenderer("",new Color(1,1,1,1)));
        text.addComponent(dialogueSystem);
        player.addGameObject(text);
        
        GameObject guard=new GameObject("guard");
        guard.addComponent(new Transform(19*24,14*24,24,24));
        guard.addComponent(new SpriteRenderer(new Sprite2D("pixel")));
        guard.addComponent(new AgentController());
        guard.addComponent(new GuardController());
        
        engine.getLevel().addGameObject(player);
        engine.getLevel().addGameObject(guard);
        player.getComponent(AnimationController.class).play("run");
    }
    public void addMap() {
        Map map=new Map(new Tileset(Gdx.files.internal("tilesets/prison.xml")),
                        Gdx.files.internal("maps/tutorial/map.xml"));
        map.getComponent(Transform.class).matrix.scl(1.5f);
        engine.getLevel().addGameObject(map);
    }
    Vector2 mark=new Vector2();
    @Override
    public void update(float delta) {
        Vector2 pos=Pools.obtain(Vector2.class);
        engine.getLevel().getGameObjectByName("player").getComponent(Transform.class).matrix.getTranslation(pos);
        if(Gdx.input.isKeyJustPressed(Keys.F))mark.set(pos);
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
        
        public float speed=200;
        private final Vector2 dir=new Vector2();
        
        @Override
        public void start() {
            animation=gameObject.getComponent(AnimationController.class);
            character=gameObject.getComponent(CharacterController.class);
            spriteRenderer=gameObject.getComponent(SpriteRenderer.class);
        }
        @Override
        public void update(float delta) {
            dir.set(0,0);
            if(Gdx.input.isKeyPressed(Keys.A))dir.x=-1;
            else if(Gdx.input.isKeyPressed(Keys.D))dir.x=1;
            if(Gdx.input.isKeyPressed(Keys.W))dir.y=1;
            else if(Gdx.input.isKeyPressed(Keys.S))dir.y=-1;
            dir.nor().scl(delta*speed);
            if(dir.x!=0||dir.y!=0) {
                character.move(dir);
                animation.setAnimation("run");
            }
            else animation.setAnimation("idle");
            
            if(Gdx.input.isKeyJustPressed(Keys.F)) {
                setDialogue("greeting");
            }
        }
        private void setDialogue(String name) {
            ((Joint)gameObject).getGameObjectByName("text").getComponent(DialogueSystem.class).setDialogue(name);
        }
    }

    private static class GuardController extends GameComponent {
        protected AgentController ai;
        protected GameObject player;
        protected Map map;
        private AIMover task=new AIMover();
        
        @Override
        public void start() {
            ai=gameObject.getComponent(AgentController.class);
            player=gameObject.getLevel().getGameObjectByName("player");
            map=gameObject.getLevel().getGameObject(Map.class);
        }
        @Override
        public void update(float delta) {
            if(Gdx.input.isKeyJustPressed(Keys.F)&&ai.isIdle()) {
                Vector2 start=gameObject.getComponent(Transform.class).getPosition(Pools.obtain(Vector2.class));
                Vector2 scale=gameObject.getComponent(Transform.class).getScale(Pools.obtain(Vector2.class));
                Vector2 end=player.getComponent(Transform.class).getPosition(Pools.obtain(Vector2.class));
                
                start.scl(1f/scale.x,1f/scale.y).set(Math.round(start.x),Math.round(start.y)).scl(scale);
                
                AIPath path=map.getComponent(Map.MapCollider.class).findPath(start,end);
                task.set(path,2,24);
                ai.assignTask(task);
                
                Pools.free(start);
                Pools.free(end);
                Pools.free(scale);
            }
        }
    }
    private static class AIMover extends Task {
        protected Transform target;
        public AIPath path;
        public float speed,scale;
        
        public AIMover() {
        }
        public AIMover(AIPath path,float speed,float scale) {
            set(path,speed,scale);
        }
        public void set(AIPath path,float speed,float scale) {
            this.path=path;
            this.speed=speed;
            this.scale=scale;
            System.out.println("Path: "+path);
        }

        @Override
        public void onTaskAssigned() {
            target=super.parent.getGameObject().getComponent(Transform.class);
        }
        @Override
        public void onTaskPerform(float delta) {
            if(path==null) {
                super.complete();
                return;
            }
            
            int step=(int)((GameServices.getElapsedTime()-(super.beginDelay+super.beginTime))*speed);
            float duration=path.getLength()/speed;
            if(step>=path.getLength())
                super.complete();
            else if(step<path.getLength()-1) {
                Vector2 pos=Pools.obtain(Vector2.class);
                Vector2 t=Pools.obtain(Vector2.class);
                Vector2 scale=Pools.obtain(Vector2.class);
                target.getScale(scale);
                
                float d=GameServices.getElapsedTime()-(super.beginDelay+super.beginTime);
                float f=(d%duration-((float)step/speed))*speed;
                
                pos.set(path.getX(step)*this.scale,path.getY(step)*this.scale);
                t.set(path.getX(step+1)*this.scale,path.getY(step+1)*this.scale);
                pos.lerp(t,f);
                target.matrix.idt();
                target.matrix.translate(pos);
                target.matrix.scale(scale);
                
                Pools.free(pos);
                Pools.free(scale);
                Pools.free(t);
            }
        }
        @Override
        public void onTaskCompleted() {
        }
        @Override
        public void onTaskReset() {
        }
    }
}
