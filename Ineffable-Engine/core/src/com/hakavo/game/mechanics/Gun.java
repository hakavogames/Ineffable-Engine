package com.hakavo.game.mechanics;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.hakavo.ineffable.assets.AssetManager;

public abstract class Gun extends Weapon {
    public Gun(String soundAsset) {
        super.fireSound=AssetManager.getAsset(soundAsset,Sound.class);
    }
    @Override
    public Color generateColor() {
        Color color=new Color().fromHsv(MathUtils.random(0f,360f),0.75f,1);
        color.a=1;
        return color;
    }
}
