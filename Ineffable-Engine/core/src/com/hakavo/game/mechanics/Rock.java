package com.hakavo.game.mechanics;
import com.badlogic.gdx.graphics.Color;

public class Rock extends Weapon {
    public Rock() {
        super.name="Rock";
        super.bulletLifespan=3;
        super.bulletSpeed=15;
        super.density=50;
        super.fireRate=3f;
        super.ammoSize=-1;
    }
    @Override
    public Color generateColor() {
        return new Color(0.31f,0.31f,0.31f,1f);
    }
}
