package com.hakavo.game.mechanics;

public class Shotgun extends Gun {
    public Shotgun() {
        super("sound-shoot");
        super.name="Shotgun";
        super.enableGravity=false;
        super.bulletLifespan=1f;
        super.bulletSpeed=20f;
        super.bullets=5;
        super.spread=40;
        super.density=100f;
        super.fireRate=1;
        super.ammoSize=-1; // 9
    }
}
