package com.hakavo.ineffable.ui;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.hakavo.ineffable.assets.AssetManager;
import com.hakavo.ineffable.core.Sprite2D;

public class Image extends Container {
    public TextureRegion textureRegion;
    
    public Image(Sprite2D sprite) {
        this(sprite.textureRegion);
    }
    public Image(String assetName) { // Textures only!
        this(new TextureRegion(AssetManager.getAsset(assetName,Texture.class)));
    }
    public Image(TextureRegion textureRegion) {
        this.textureRegion=new TextureRegion(textureRegion);
        super.focusable=false;
        super.style.background.set(0,0,0,0);
        super.style.blur=false;
        super.style.foreground.set(1,1,1,1);
        super.bounds.setSize(this.textureRegion.getRegionWidth(),this.textureRegion.getRegionHeight());
    }
    
    @Override
    public void onRender(OrthographicCamera camera,SpriteBatch sb) {
        sb.draw(textureRegion,0,0,super.bounds.width,super.bounds.height);
    }
}
