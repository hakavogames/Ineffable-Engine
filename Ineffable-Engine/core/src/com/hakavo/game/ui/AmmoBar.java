package com.hakavo.game.ui;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.hakavo.game.components.WeaponController;
import com.hakavo.game.mechanics.WeaponInstance;
import com.hakavo.ineffable.assets.AssetManager;
import com.hakavo.ineffable.ui.*;

public class AmmoBar extends Container {
    
    public AmmoBar() {
        super.style.blur=false;
        super.style.background.set(0.1f,0.1f,0.1f,1f);
        super.style.foreground.set(0,0.3f,1f,1f);
    }
    
    @Override
    public final void onRender(OrthographicCamera camera,SpriteBatch sb) {
        Texture pixel=AssetManager.getAsset("gui-blank");
        WeaponInstance weapon=HUD.target.getComponent(WeaponController.class).getCurrentWeapon();
        float f=(float)weapon.getAmmo()/weapon.parent.ammoSize;
        sb.draw(pixel,0,0,f*super.bounds.width,super.bounds.height);
    }
}
