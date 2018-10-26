/*
 * Copyright 2018 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kozma.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.kozma.*;

public class ParallaxScroller extends Renderable
{
    public Sprite2D sprite;
    public Color color=new Color(1,1,1,1);
    
    public boolean scrollX,scrollY;
    public float speed=1f; // 0-1. 0=far 1=near
    protected Transform transform;
    
    public ParallaxScroller(Sprite2D sprite,float speed,boolean scrollX,boolean scrollY) {
        this.sprite=sprite;
        this.speed=speed;
        this.scrollX=scrollX;
        this.scrollY=scrollY;
    }
    public ParallaxScroller(Sprite2D sprite,float speed) {
        this(sprite,speed,true,false);
    }
    public ParallaxScroller(Sprite2D sprite) {
        this(sprite,1f);
    }
    public ParallaxScroller() {
        this(null);
    }
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
            float posx=cam.position.x,posy=cam.position.y;
            if(transform!=null)
            {
                scaleX*=transform.scale.x;
                scaleY*=transform.scale.y;
                posx+=transform.position.x;
                posy+=transform.position.y;
                posx*=scaleX;
                posy*=scaleY;
            }
            
            float viewportWidth=cam.viewportWidth/(1f/cam.zoom);
            float viewportHeight=cam.viewportHeight/(1f/cam.zoom);
            
            
            float minx=posx*(1f-speed)-viewportWidth/2;
            float maxx=posx*(1f-speed)+viewportWidth/2;
            float miny=posy-viewportHeight/2;
            float maxy=posy+viewportHeight/2;
            
            minx-=(int)((minx-(posx-viewportWidth/2))/width+1)*width;
            maxx=minx+viewportWidth+width*2;
            
            if(!scrollX){maxx=minx+width-1;}
            if(!scrollY){maxy=miny+height-1;}
            
            for(float x=minx;x<=maxx;x+=width)
                for(float y=miny;y<=maxy;y+=height)
                    sb.draw(sprite.textureRegion,x,y,0,0,width,height,scaleX,scaleY,0);
        }
    }
}
