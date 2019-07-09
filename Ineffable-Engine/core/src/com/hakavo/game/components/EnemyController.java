/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hakavo.game.components;

import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.hakavo.game.mechanics.*;
import com.hakavo.game.objects.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.*;
import com.hakavo.ineffable.ui.GUI;

public class EnemyController extends GameComponent {
    public boolean canMove;
    
    protected Transform A,B;
    protected WeaponController weaponController;
    protected BoxCollider box;
    private PointCollider coll;
    private float accumulator;
    private final Array<Projectile> array=new Array<Projectile>();
    
    public EnemyController(boolean canMove) {
        this.canMove=canMove;
    }
    
    @Override
    public void start() {
        weaponController=gameObject.getComponent(WeaponController.class);
        A=gameObject.getComponent(Transform.class);
        box=gameObject.getComponent(BoxCollider.class);
        box.tags.add(3);
        B=gameObject.getLevel().getGameObjectByName("player").getComponent(Transform.class);
        gameObject.addComponent(new PointCollider());
        coll=gameObject.getComponent(PointCollider.class);
        sendMessage(gameObject,"set_speed",0.15f);
    }
    @Override
    public void update(float delta) {
        WeaponInstance weapon=weaponController.getCurrentWeapon();
        Vector2 start=A.getPosition(new Vector2());
        Vector2 dir=B.getPosition(new Vector2());
        dir.sub(start);
    
        boolean ok=false;
        Array<Collider> cast=coll.cast(dir.cpy().nor(),1f,Math.round(weapon.parent.getSpeed()*weapon.parent.getMaxTravelTime()));
        for(int i=0;i<cast.size;i++) {
            if(cast.get(i)!=box) {
            if(cast.get(i).getGameObject().name.equals("player")) {
                ok=true;
                break;
            }
            else break;
            }
        }
        if(ok)accumulator+=delta;
        else accumulator=0;
        
        if(ok) {
            if(canMove) {
                if(dir.x<-1f)sendMessage(gameObject,"go_left");
                else if(dir.x>1f)sendMessage(gameObject,"go_right");
            }
            if(accumulator>=weapon.parent.getFireDelay()&&weapon.hasAmmo()) {
                if(weapon.parent.getSound()!=null)
                    weapon.parent.getSound().play(0.2f,MathUtils.random(0.9f,2f),0);
                accumulator=0;
                float len=dir.len();
                float angle=len/4f;
                dir.nor();
                if(dir.x>=0)dir.rotate(angle);
                else dir.rotate(-angle);
                
                weapon.parent.generateProjectiles(A.getPosition(new Vector2()),dir,array);
                for(Projectile p : array) {
                    p.getComponent(Collider.class).tags.clear();
                    p.getComponent(Collider.class).tags.add(0,5);
                    p.getComponent(Collider.class).ignoreTags.add(3,5);
                    gameObject.getLevel().addGameObject(p);
                }
            }
        }
    }
    public void die() {
        GUI.dispatchEvent("enemy_dead",super.gameObject);
    }
}
