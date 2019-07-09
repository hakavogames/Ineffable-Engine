package com.hakavo.ineffable.ui;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.hakavo.ineffable.assets.*;

public class CheckBox extends Container {
    public Label label;
    public boolean checked;
    
    public CheckBox(String text) {
        this.label=new Label(text);
        this.label.bounds.setPosition(20,0);
        super.focusable=true;
        super.style.foreground.set(0.4f,0.4f,0.4f,1f);
        super.style.blur=false;
        super.style.background.set(0,0,0,0);
        super.addEventListener(new EventListener() {
            @Override
            public void onButtonDown(int button) {
                checked=(checked==false);
            }
        });
        super.add(label);
    }
    
    @Override
    public void onRender(OrthographicCamera camera,SpriteBatch sb) {
        Texture tex=AssetManager.getAsset("gui-blank");
        sb.draw(tex,0,0,bounds.height,bounds.height);
        if(checked) {
            sb.setColor(1,1,1,1);
            sb.draw(tex,4,4,bounds.height-8,bounds.height-8);
        }
    }
    @Override
    public void onUpdate(float delta) {
        super.bounds.setSize(label.bounds.width+label.bounds.height+5,label.bounds.height);
        label.bounds.setPosition(label.bounds.height+5,0);
    }
}
