package com.hakavo.game.mechanics;

public class SniperRifle extends Gun {
    public SniperRifle() {
        super("sound-shoot");
        super.name="Sniper Rifle";
        super.bulletLifespan=2f;
        super.bulletSpeed=35;
        super.fireRate=1.5f;
        super.spread=0;
        super.density=20f;
        super.enableGravity=true;
        super.ammoSize=7;
    }
}
