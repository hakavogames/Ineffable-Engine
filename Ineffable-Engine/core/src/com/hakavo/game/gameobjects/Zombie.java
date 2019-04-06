package com.hakavo.game.gameobjects;

import com.badlogic.gdx.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import com.hakavo.game.components.*;
import com.hakavo.game.ai.*;
import com.hakavo.ineffable.GameServices;
import com.hakavo.ineffable.Tileset;

public class Zombie extends GameObject {
    public Zombie() {
        super.name="zombie";
        
        Tileset poses=new Tileset(Gdx.files.internal("Scavengers_SpriteSheet.png"),32);
        AnimationClip clip1=new AnimationClip();
        for(int i=12;i<18;i++)clip1.frames.add(poses.tiles.get(i).createTextureRegion());
        clip1.duration=1f;
        clip1.loop=true;
        AnimationClip clip2=new AnimationClip();
        for(int i=44;i<46;i++)clip2.frames.add(poses.tiles.get(i).createTextureRegion());
        clip2.duration=0.3f;
        clip2.loop=true;
        
        SpriteRenderer spriteRenderer=new SpriteRenderer(new Sprite2D());
        
        Animation idle=new Animation("idle",clip1);
        Animation attack=new Animation("attack",clip2);
        AnimationController animationController=new AnimationController(spriteRenderer.sprite,idle,attack);
        animationController.play("idle");
        
        Task chasePlayer=new Task() {
            private GameObject target;
            private CharacterController characterController;
            private AnimationController animationController;
            private Transform transform;
            private float speed=45;
            private float lastHit;
            
            @Override
            public void onTaskAssigned() {
                target=parent.getGameObject().getLevel().getGameObjectByName("player");
                characterController=parent.getGameObject().getComponent(CharacterController.class);
                animationController=parent.getGameObject().getComponent(AnimationController.class);
                transform=parent.getGameObject().getComponent(Transform.class);
            }
            @Override
            public void onTaskPerform(float delta) {
                if(target.getComponent(Transform.class)==null)return;
                Vector2 a=transform.getPosition(Pools.obtain(Vector2.class));
                Vector2 b=target.getComponent(Transform.class).getPosition(Pools.obtain(Vector2.class));
                Vector2 dir=b.cpy().sub(a).nor().scl(speed*delta);
                
                characterController.move(dir);
                
                if(a.dst(b)<48&&!animationController.getCurrentAnimation().name.equals("attack"))
                {
                    animationController.play("attack");
                }
                else if(!animationController.getCurrentAnimation().name.equals("idle")&&a.dst(b)>=48)
                    animationController.play("idle");
                
                if(a.dst(b)<48&&GameServices.getElapsedTime()-lastHit>1.5f)
                {
                    parent.sendMessage(target,"damage",7f);
                    lastHit=GameServices.getElapsedTime();
                }
                
                Pools.free(a);
                Pools.free(b);
            }
            @Override
            public void onTaskCompleted() {
            }
            @Override
            public void onTaskReset() {
            }
            
        };
        AgentController agentController=new AgentController();
        agentController.assignTask(chasePlayer);
        
        BoxCollider collider=new BoxCollider();
        collider.tags.clear();
        collider.tags.add(0,1);
        //collider.ignoreTags.add(5);
        
        this.addComponent(new Transform());
        this.addComponent(new CharacterController(true));
        this.addComponent(agentController);
        this.addComponent(collider);
        this.addComponent(spriteRenderer);
        this.addComponent(animationController);
    }
}
