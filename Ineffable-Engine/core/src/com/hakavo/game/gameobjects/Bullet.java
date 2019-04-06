package com.hakavo.game.gameobjects;

import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import com.hakavo.game.components.*;
import com.hakavo.game.ai.*;
import com.hakavo.ineffable.*;

public class Bullet extends GameObject {
    public Bullet(Vector2 velocity) {
        super.name="bullet";
        addComponent(new Transform());
        addComponent(new SpriteRenderer(new Sprite2D(new TextureRegion(new Texture("sprites/bullet.png")))));
        addComponent(new CircleCollider(0,0,8f));
        addComponent(new BulletController(velocity));
    }
    private class BulletController extends GameComponent {
        private Transform transform;
        private CircleCollider collider;
        private Vector2 velocity;
        private float startTime;
        public float lifespan=2;
        
        public BulletController(Vector2 velocity) {
            this.velocity=new Vector2(velocity);
        }
        
        @Override
        public void start() {
            startTime=GameServices.getElapsedTime();
            transform=getGameObject().getComponent(Transform.class);
            collider=getGameObject().getComponent(CircleCollider.class);
            
            collider.tags.clear();
            collider.tags.add(0,1,4);
            collider.ignoreTags.add(3,4);
            collider.setCollisionAdapter(new CollisionAdapter() {
                @Override
                public void onCollisionEnter(Collider target) {
                    parent.getGameObject().destroy();
                    if(target.getGameObject().name.equals("zombie"))
                    {
                        //target.getGameObject().destroy();
                        parent.sendMessage(target.getGameObject(),"damage",20f);
                    }
                }
            });
        }
        @Override
        public void update(float delta) {
            if(GameServices.getElapsedTime()-startTime>lifespan)getGameObject().destroy();
            transform.matrix.translate(velocity.x*delta,velocity.y*delta);
        }
    }
}
