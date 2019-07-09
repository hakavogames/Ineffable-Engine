package com.hakavo.game.mechanics;

public class WeaponInstance {
    public final Weapon parent;
    private int ammo;
    
    public WeaponInstance(Weapon parent) {
        this.parent=parent;
        this.ammo=parent.ammoSize;
    }
    
    public void fire() {
        if(hasAmmo()&&parent.ammoSize!=-1)ammo--;
    }
    public boolean hasAmmo() {
        return (parent.ammoSize==-1||ammo>0);
    }
    public int getAmmo() {
        return ammo;
    }
}
