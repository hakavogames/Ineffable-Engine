package com.hakavo.game.components;
import com.badlogic.gdx.graphics.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.core.*;
import com.badlogic.gdx.math.*;

public class CameraEffects extends GameComponent implements MessageListener {
    public float shakeDuration=0.1f;
    public boolean rotate=true;
    private OrthographicCamera camera;
    private float time=Float.MAX_VALUE;
    private final Vector2 pos=new Vector2();
    private final Vector2 offset=new Vector2();
    private final Vector2 target=new Vector2();
    
    @Override
    public void start() {
        camera=GameServices.getCamera();
        super.messageListener=this;
    }
    @Override
    public void update(float delta) {
        pos.set(camera.position.x-offset.x,camera.position.y-offset.y);
        time+=delta;
        if(time<shakeDuration/2)
            offset.lerp(target,0.1f);
        else offset.scl(0.9f);
        
        camera.position.x=pos.x+offset.x;
        camera.position.y=pos.y+offset.y;
        
        if(Float.isNaN(camera.position.x)||Float.isNaN(camera.position.y)) {
            offset.set(0,0);
            camera.position.set(0,0,camera.position.z);
        }
        
        if(rotate)
            camera.up.set((float)Math.sin(GameServices.getElapsedTime()/1.2f)/80f,1,0).nor();
        else camera.up.set(0,1,0);
    }

    @Override
    public void messageReceived(GameObject sender,String message,Object... parameters) {
        if(message.equals("shake")&&time>=shakeDuration) {
            if(parameters.length==1&&parameters[0] instanceof Vector2)
                target.set(((Vector2)parameters[0]).nor().scl(2.5f));
            else target.setToRandomDirection().scl(2);
            time=0;
        }
    }
}
