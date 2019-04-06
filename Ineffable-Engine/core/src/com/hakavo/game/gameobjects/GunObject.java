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

public class GunObject extends GameObject {
    public GunObject() {
        addComponent(new Transform(0,0,0.5f,0.5f));
        addComponent(new SpriteRenderer(new Sprite2D(new TextureRegion(new Texture("sprites/guns/MP 40.png")))));
        addComponent(new GunController());
        addComponent(new SoundEffect(Gdx.files.internal("sounds/mp40.wav")));
    }
    private class GunController extends GameComponent {
        private Transform transform;
        private SpriteRenderer spriteRenderer;
        private SoundEffect soundEffect;
        private Vector2 dir=new Vector2();
        private float lastBullet;
        public float fireRate=0.03f;
        public float recoil=15f;
        
        @Override
        public void start() {
            transform=getGameObject().getComponent(Transform.class);
            soundEffect=getGameObject().getComponent(SoundEffect.class);
            spriteRenderer=getGameObject().getComponent(SpriteRenderer.class);
        }
        
        @Override
        public void update(float delta) {
            Vector2 mouse=new Vector2(Gdx.input.getX(),Gdx.input.getY());
            dir.set(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2).sub(mouse).nor();
            if(dir.x<0)spriteRenderer.flipY=false;
            else spriteRenderer.flipY=true;
            
            transform.matrix.idt();
            transform.matrix.scl(0.5f);
            transform.matrix.rotateRad(getAngle(dir.x,dir.y)-1.84f);
            
            if(Gdx.input.isButtonPressed(Buttons.LEFT)&&GameServices.getElapsedTime()-lastBullet>=fireRate) {
                lastBullet=GameServices.getElapsedTime();
                soundEffect.play();
                
                Bullet bullet=new Bullet(new Vector2(-dir.x,dir.y).rotate(MathUtils.random(-recoil,recoil)).scl(250));
                bullet.getComponent(Transform.class).matrix.translate(this.transform.getPosition(new Vector2()));
                getGameObject().getLevel().addGameObject(bullet);
            }
        }
        public float getAngle(float x,float y)
        {
            return (float)(1.5f*Math.PI-Math.atan2(y,x));
        }
    }
}
