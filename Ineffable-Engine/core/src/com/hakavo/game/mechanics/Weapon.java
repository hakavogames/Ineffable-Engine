package com.hakavo.game.mechanics;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.hakavo.game.objects.Projectile;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.hakavo.ineffable.core.physics.RigidBody;

public abstract class Weapon {
    protected Sound fireSound;
    
    protected String name;
    protected float bulletSpeed=1;
    protected float bulletLifespan=1;
    protected float fireRate=1.2f; // projectiles per second
    protected float damage;
    protected float density=1;
    protected boolean enableGravity=true;
    
    public int ammoSize=-1;
    
    protected int bullets=1;
    protected float spread=0; // Max. bullet spread angle in degrees
    
    protected abstract Color generateColor();
    
    public final void generateProjectiles(Vector2 origin,Vector2 direction,Array<Projectile> out) {
        out.clear();
        for(int i=1;i<=bullets;i++) {
            Color color=generateColor();
            float angle=MathUtils.random(-spread/2f,spread/2f);
            Projectile projectile=new Projectile(origin.cpy(),direction.cpy().rotate(angle).scl(bulletSpeed),color,bulletLifespan,density);
            projectile.getComponent(RigidBody.class).enableGravity=enableGravity;
            out.add(projectile);
        }
    }
    public final float getFireDelay() {
        return 1f/fireRate;
    }
    public final float getSpeed() {
        return bulletSpeed;
    }
    public final float getMaxTravelTime() {
        return bulletLifespan;
    }
    public final float getDamage() {
        return damage;
    }
    public final Sound getSound() {
        return fireSound;
    }
    public final String getName() {
        return name;
    }
}
