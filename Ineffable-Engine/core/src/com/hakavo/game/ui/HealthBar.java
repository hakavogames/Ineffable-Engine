package com.hakavo.game.ui;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.hakavo.game.components.SquareController;
import com.hakavo.ineffable.assets.AssetManager;
import com.hakavo.ineffable.ui.*;

public class HealthBar extends Container {
    private Color low,high,foo=new Color();
    
    public HealthBar() {
        super.style.blur=false;
        super.style.background.set(0.1f,0.1f,0.1f,1f);
        
        high=new Color(0.3f,0.6f,0.2f,1f);
        low=new Color(0.9f,0.2f,0.1f,1f);
    }
    
    @Override
    public final void onRender(OrthographicCamera camera,SpriteBatch sb) {
        Texture pixel=AssetManager.getAsset("gui-blank");
        SquareController square=HUD.target.getComponent(SquareController.class);
        float f=(square.health/square.getMaxHealth());
        sb.setColor(foo.set(low).lerp(high,f));
        sb.draw(pixel,0,0,f*super.bounds.width,super.bounds.height);
    }
}
