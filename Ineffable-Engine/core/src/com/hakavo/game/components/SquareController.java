package com.hakavo.game.components;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.ineffable.core.*;

public class SquareController extends GameComponent implements MessageListener {
    private float maxHealth;
    public float health;
    protected Transform transform;
    
    public SquareController() {
        initHealth(25);
    }
    public SquareController(float maxHealth) {
        initHealth(maxHealth);
    }
    
    @Override
    public void start() {
        transform=gameObject.getComponent(Transform.class);
        super.messageListener=this;
    }
    @Override
    public void update(float delta) {
        Vector2 vec=transform.getPosition(Pools.obtain(Vector2.class));
        if(vec.y<=-30)health=0;
        Pools.free(vec);
        
        health=Math.min(health,maxHealth);
        if(health==0)
            gameObject.destroy();
    }
    public final float getMaxHealth() {
        return maxHealth;
    }
    public final void initHealth(float maxHealth) {
        this.maxHealth=maxHealth;
        this.health=maxHealth;
    }

    @Override
    public void messageReceived(GameObject sender,String message,Object... parameters) {
        if(message.equals("damage"))
            health=Math.max(health-((Number)parameters[0]).floatValue(),0f);
    }
}
