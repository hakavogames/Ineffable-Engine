package com.kozma.core;
import com.kozma.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.Affine2;

public class SpriteRenderer extends Renderable
{
    public Sprite2D sprite;
    public boolean flipX,flipY;
    public Color color=new Color(1,1,1,1);
    private Transform transform;
    
    public SpriteRenderer(Sprite2D sprite)
    {
        this.sprite=sprite;
    }
    @Override
    public void start() {
        transform=this.gameObject.getComponent(Transform.class);
    }
    @Override
    public void update(float delta) {
    }
    @Override
    public void render(OrthographicCamera camera) {
        super.prepareRendering();
        SpriteBatch sb=GameServices.getSpriteBatch();
        if(sprite!=null)
        {
            TextureRegion tr=sprite.textureRegion;
            sb.setColor(color);
            if(transform==null)
                sb.draw(tr.getTexture(),0,0,tr.getRegionWidth(),tr.getRegionHeight(),
                        tr.getRegionX(),tr.getRegionY(),tr.getRegionWidth(),tr.getRegionHeight(),flipX,flipY);
            else
                sb.draw(tr.getTexture(),transform.position.x,transform.position.y,
                        tr.getRegionWidth()*transform.scale.x,tr.getRegionHeight()*transform.scale.y,
                        tr.getRegionX(),tr.getRegionY(),tr.getRegionWidth(),tr.getRegionHeight(),flipX,flipY);
        }
        sb.flush();
    }
}
