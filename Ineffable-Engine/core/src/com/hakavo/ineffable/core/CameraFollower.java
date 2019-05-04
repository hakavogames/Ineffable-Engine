package com.hakavo.ineffable.core;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.hakavo.ineffable.GameServices;
import com.hakavo.ineffable.core.*;

public class CameraFollower extends GameComponent {
    public float smoothness;
    protected Transform transform;
    private Vector3 position=new Vector3();
    private Vector2 foo=new Vector2();
    
    public CameraFollower() {
        this(0.1f);
    }
    public CameraFollower(float smoothness) {
        this.smoothness=smoothness;
    }
    
    @Override
    public void start() {
        transform=gameObject.getComponent(Transform.class);
    }
    @Override
    public void update(float delta) {
        transform.getPosition(foo);
        position.set(foo.x,foo.y,GameServices.getCamera().position.z);
        GameServices.getCamera().position.lerp(position,smoothness);
    }
}
