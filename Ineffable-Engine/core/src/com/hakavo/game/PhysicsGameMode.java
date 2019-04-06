package com.hakavo.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.hakavo.game.components.CharacterController;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.assets.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.*;
import com.hakavo.ineffable.core.physics.*;
import com.hakavo.ineffable.core.skeleton.*;

public class PhysicsGameMode implements GameMode {
    protected Engine engine;
    
    @Override
    public void init(Engine engine) {
        this.engine=engine;
        //engine.camera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        engine.camera.setToOrtho(false,1600,900);
        engine.camera.position.sub(engine.camera.viewportWidth/2,engine.camera.viewportHeight/2,0);
        AssetManager.loadAsset("texture",Gdx.files.internal("sprites/red_ball.png"),"redball",false);
        Sprite2D ballSprite=new Sprite2D((Texture)AssetManager.getAsset("redball"));
        
        PhysicsWorld.gravity.set(0,0);
        RigidBody body=new RigidBody(new Vector2(-400,0),2f);
        body.velocity.set(500,0);
        
        GameObject player=new GameObject();
        player.name="player";
        player.addComponent(new Transform());
        player.addComponent(new SpriteRenderer(ballSprite,100));
        player.addComponent(new CircleCollider(50));
        player.addComponent(body);
        
        for(int i=1;i<=10;i++) {
            for(float f=-50*(i-1);f<=50*(i-1);f+=50)
            {
                RigidBody enemyBody=new RigidBody(new Vector2(50*i,f+200),1f);
                enemyBody.velocity.set(0,0);
                GameObject enemy=new GameObject();
                enemy.addComponent(new Transform());
                enemy.addComponent(new SpriteRenderer(ballSprite,50));
                enemy.addComponent(new CircleCollider(25f));
                enemy.addComponent(enemyBody);
                enemy.getComponent(SpriteRenderer.class).color.set(0.6f,0.8f,1,1f);
                engine.getLevel().addGameObject(enemy);
            }
        }
        
        GameObject b1=new GameObject();
        b1.addComponent(new Transform());
        b1.addComponent(new BoxCollider(0,0,1500,50));
        b1.addComponent(new RigidBody(0f));
        b1.getComponent(RigidBody.class).position.set(-750,-300);
        GameObject b2=new GameObject();
        b2.addComponent(new Transform());
        b2.addComponent(new BoxCollider(0,0,1500,50));
        b2.addComponent(new RigidBody(0f));
        b2.getComponent(RigidBody.class).position.set(-750,580);
        
        GameObject b3=new GameObject();
        b3.addComponent(new Transform());
        b3.addComponent(new BoxCollider(-1000,-700,100,1400));
        b3.addComponent(new RigidBody(0f));
        GameObject b4=new GameObject();
        b4.addComponent(new Transform());
        b4.addComponent(new BoxCollider(1000,-700,100,1400));
        b4.addComponent(new RigidBody(0f));
        
        engine.getLevel().addGameObject(player);
        engine.getLevel().addGameObject(b1);
        engine.getLevel().addGameObject(b2);
        engine.getLevel().addGameObject(b3);
        engine.getLevel().addGameObject(b4);
    }
    @Override
    public void update(float delta) {
        if(Gdx.input.isKeyPressed(Keys.R))engine.loadGameMode(new PhysicsGameMode());
        if(Gdx.input.isButtonPressed(Buttons.LEFT)) {
            RigidBody rb=engine.getLevel().getGameObjectByName("player").getComponent(RigidBody.class);
            
            Vector3 v=engine.camera.getPickRay(Gdx.input.getX(),Gdx.input.getY()).origin;
            Vector2 o=new Vector2(v.x,v.y).sub(rb.position).scl(0.05f);
            rb.velocity.add(o);
        }
    }
    @Override
    public void renderGui(OrthographicCamera camera) {
    }
    public static class PlayerController extends GameComponent {
        protected AnimationController animation;
        protected CharacterController character;
        public float speed=50;
        private Vector2 dir=new Vector2();
        
        @Override
        public void start() {
            animation=gameObject.getComponent(AnimationController.class);
            character=gameObject.getComponent(CharacterController.class);
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
            }
            else animation.setAnimation("idle");
        }
    }
}
