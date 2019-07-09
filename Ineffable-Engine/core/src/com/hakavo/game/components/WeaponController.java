package com.hakavo.game.components;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.*;
import com.hakavo.game.mechanics.*;
import com.hakavo.game.objects.*;
import com.hakavo.ineffable.core.*;

public class WeaponController extends GameComponent {
    public final Array<WeaponInstance> weapons=new Array<WeaponInstance>();
    protected WeaponInstance weapon;
    
    public WeaponController() {
    }
    public WeaponController(WeaponInstance... weapons) {
        this.weapons.addAll(weapons);
        if(this.weapons.size>0)equipWeapon(0);
    }
    public WeaponController(Weapon... weapons) {
        addWeapons(weapons);
        if(this.weapons.size>0)equipWeapon(0);
    }
    
    @Override
    public void start() {
    }
    @Override
    public void update(float delta) {
        if(weapons.size>0&&weapon==null)weapon=weapons.get(0);
    }
    
    public void equipWeapon(int id) {
        weapon=weapons.get(MathUtils.clamp(id,0,weapons.size-1));
    }
    public void addWeapons(Weapon... weapons) {
        for(Weapon weapon : weapons) {
            this.weapons.add(new WeaponInstance(weapon));
        }
    }
    public WeaponInstance getCurrentWeapon() {
        return weapon;
    }
}
