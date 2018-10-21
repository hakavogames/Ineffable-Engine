package com.kozma.core;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.kozma.*;

public class TiledBackground extends Renderable
{
    public Sprite2D sprite;
    public Color color=new Color(1,1,1,1);
    protected Transform transform;
    
    public TiledBackground() {}
    public TiledBackground(Sprite2D sprite) {this.sprite=sprite;}
    @Override
    public void start() {
        transform=super.getGameObject().getComponent(Transform.class);
    }
    @Override
    public void update(float delta) {
    }
    @Override
    public void render(OrthographicCamera cam) {
        super.prepareRendering();
        SpriteBatch sb=GameServices.getSpriteBatch();
        if(sprite!=null)
        {
            sb.setColor(color);
            
            float width=sprite.textureRegion.getRegionWidth();
            float height=sprite.textureRegion.getRegionHeight();
            float scaleX=1,scaleY=1;
            if(transform!=null)
            {
                scaleX*=transform.scale.x;
                scaleY*=transform.scale.y;
            }
            
            float viewportWidth=cam.viewportWidth*cam.zoom;
            float viewportHeight=cam.viewportHeight*cam.zoom;
            
            float posx=cam.position.x;
            float posy=cam.position.y;
            
            int minx=(int)(posx/width/scaleX-viewportWidth/2/width/scaleX);
            int maxx=(int)(posx/width/scaleX+viewportWidth/2/width/scaleX);
            int miny=(int)(posy/height/scaleY-viewportHeight/2/height/scaleY-1);
            int maxy=(int)(posy/height/scaleY+viewportHeight/2/height/scaleY);
            
            for(int x=minx-1;x<=maxx;x++)
                for(int y=miny-1;y<=maxy;y++)
                    sb.draw(sprite.textureRegion,x*width*scaleX,y*height*scaleY,0,0,width,height,scaleX,scaleY,0);
        }
        sb.flush();
    }
}
