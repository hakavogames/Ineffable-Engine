package com.hakavo.game.components;

import com.badlogic.gdx.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.core.GameComponent.Copiable;

public class CharacterController extends GameComponent implements Copiable {
    private SpriteRenderer spriteRenderer;
    private Transform transform;
    private BoxCollider collider;
    public float health=100;
    public boolean invertX;
    
    public CharacterController() {
    }
    public CharacterController(boolean invertX) {
        this.invertX=invertX;
    }
    
    @Override
    public void start() {
        spriteRenderer=getGameObject().getComponent(SpriteRenderer.class);
        transform=getGameObject().getComponent(Transform.class);
        collider=getGameObject().getComponent(BoxCollider.class);
        
        this.setMessageListener(new MessageListener() {
            @Override
            public void messageReceived(GameObject sender,String message,Object... parameters) {
                if(message.equals("damage"))
                    health-=(Float)parameters[0];
                else if(message.equals("kill"))
                    health=0;
            }
        });
    }
    @Override
    public void update(float delta) {
        //collider.setSize(spriteRenderer.sprite.textureRegion.getRegionWidth(),spriteRenderer.sprite.textureRegion.getRegionHeight()/2f);
        if(health<=0)getGameObject().destroy();
    }
    
    public void move(float dirX,float dirY) {
        if(dirX>0f)
            spriteRenderer.flipX=invertX;
        else if(dirX<0f)
            spriteRenderer.flipX=!invertX;
        
        Matrix3 previous=Pools.obtain(Matrix3.class).set(transform.matrix);
        transform.matrix.translate(dirX,0);
        Array<Collider> objectsHit=collider.cast(new Vector2(dirX,0),1,1);
        if(objectsHit.size>0)transform.matrix.set(previous);
        previous.set(transform.matrix);
        transform.matrix.translate(0,dirY);
        objectsHit=collider.cast(new Vector2(0,dirY),1,1);
        if(objectsHit.size>0)transform.matrix.set(previous);
        Pools.free(previous);
    }
    public void move(Vector2 direction) {
        move(direction.x,direction.y);
    }
    
    @Override
    public CharacterController cpy() {
        return new CharacterController(this.invertX);
    }
}
