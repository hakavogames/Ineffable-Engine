package com.hakavo.game.mechanics;

public class Pistol extends Gun {
    public Pistol() {
        super("sound-shoot");
        super.name="Pistol";
        super.bulletLifespan=0.5f;
        super.bulletSpeed=30;
        super.fireRate=1.7f;
        super.spread=6;
        super.density=1;
        super.ammoSize=12;
    }
}
