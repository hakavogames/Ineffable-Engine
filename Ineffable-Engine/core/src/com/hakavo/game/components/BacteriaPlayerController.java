package com.hakavo.game.components;
import com.hakavo.ineffable.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.*;
import com.hakavo.ineffable.GameServices;
import com.hakavo.ineffable.core.collision.CircleCollider;
import com.hakavo.ineffable.core.physics.*;

public class BacteriaPlayerController extends GameComponent {
    public float maxSpeed,controlRadius;
    private float score;
    protected Transform transform;
    protected RigidBody rigidBody;
    protected SpriteRenderer spriteRenderer;
    protected CircleCollider collider;
    private Vector2 dir=new Vector2();
    private float targetSize;
    
    public BacteriaPlayerController(float maxSpeed,float controlRadius) {
        this.maxSpeed=maxSpeed;
        this.controlRadius=controlRadius;
    }
    
    @Override
    public void start() {
        transform=gameObject.getComponent(Transform.class);
        rigidBody=gameObject.getComponent(RigidBody.class);
        spriteRenderer=gameObject.getComponent(SpriteRenderer.class);
        collider=gameObject.getComponent(CircleCollider.class);
        spriteRenderer.color.set(0.9f,1f,1f,1f);
        spriteRenderer.layer=3;
        targetSize=spriteRenderer.size;
        super.messageListener=new MessageListener() {
            @Override
            public void messageReceived(GameObject sender,String message,Object... parameters) {
                if(message.equals("score")) {
                    score+=(Float)parameters[0];
                    setTargetSize(0.1f+score/500f);
                }
            }
        };
    }
    @Override
    public void update(float delta) {
        dir.set(Gdx.input.getX()-Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2-Gdx.input.getY());
        dir.setLength(Math.min(dir.len(),controlRadius)/controlRadius*maxSpeed/Math.max(delta,0.001f));
        setSize(Interpolation.linear.apply(spriteRenderer.size,targetSize,0.1f));
        rigidBody.getRawVelocity().set(dir);
        GameServices.getCamera().zoom=spriteRenderer.size*10f;
    }
    private void setSize(float size) {
        spriteRenderer.size=size;
        collider.radius=size/2;
    }
    private void setTargetSize(float size) {
        this.targetSize=size;
    }
    public int getScore() {
        return Math.round(score);
    }
}
