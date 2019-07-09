package com.hakavo.game.mechanics;

public class SMG extends Gun {
    public SMG() {
        super("sound-shoot");
        super.name="SMG";
        super.bulletLifespan=0.55f;
        super.bulletSpeed=30;
        super.fireRate=10f;
        super.spread=10;
        super.density=6f;
        super.enableGravity=false;
        super.ammoSize=-1; // 30
    }
}
