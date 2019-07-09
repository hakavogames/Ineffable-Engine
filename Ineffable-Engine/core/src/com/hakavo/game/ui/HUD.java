package com.hakavo.game.ui;
import com.badlogic.gdx.graphics.Color;
import com.hakavo.game.components.*;
import com.hakavo.ineffable.core.GameObject;
import com.hakavo.ineffable.ui.*;

public class HUD extends Container {
    public static GameObject target;
    
    public HUD() {
        super.style.blur=false;
        super.style.background.set(0,0,0,0);
        super.bounds.set(20,20,360,120);
        super.focusable=false;
        
        HealthBar bar=new HealthBar();
        bar.bounds.set(0,0,340,10);
        AmmoBar ammo=new AmmoBar();
        ammo.bounds.set(0,40,340,10);
        
        super.add(bar,ammo);
        
        Title title=new Title();
        title.bounds.setPosition(450,450);
        GUI.mainContainer.add(title);
    }
    
    @Override
    public void onUpdate(float delta) {
        if(target.isDestroyed())
            super.visible=false;
    }
}
