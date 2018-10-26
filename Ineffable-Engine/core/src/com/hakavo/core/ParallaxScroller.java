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

package com.hakavo.core;

import com.hakavo.GameServices;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

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
            //float scaleX=1,scaleY=1;
            //float posx=cam.position.x,posy=cam.position.y;
            Vector2 pos=Pools.obtain(Vector2.class).set(cam.position.x,cam.position.y);
            Vector2 scale=Pools.obtain(Vector2.class).set(1,1);
            if(transform!=null)
            {
                Vector2 foo=Pools.obtain(Vector2.class);
                scale.scl(transform.matrix.getScale(foo));
                pos.add(transform.matrix.getTranslation(foo));
                width*=scale.x;
                height*=scale.y;
                Pools.free(foo);
            }
            
            float viewportWidth=cam.viewportWidth/(1f/cam.zoom);
            float viewportHeight=cam.viewportHeight/(1f/cam.zoom);
            
            
            float minx=pos.x*(1f-speed)-viewportWidth/2;
            float maxx=pos.x*(1f-speed)+viewportWidth/2;
            float miny=pos.y-viewportHeight/2;
            float maxy=pos.y+viewportHeight/2;
            
            minx-=(int)((minx-(pos.x-viewportWidth/2))/width+1)*width;
            maxx=minx+viewportWidth+width*2;
            
            if(!scrollX){maxx=minx+width-1;}
            if(!scrollY){maxy=miny+height-1;}
            
            Matrix4 foo=Pools.obtain(Matrix4.class).idt();
            sb.setTransformMatrix(foo);
            for(float x=minx;x<=maxx;x+=width)
                for(float y=miny;y<=maxy;y+=height)
                    sb.draw(sprite.textureRegion,x,y,0,0,width,height,1,1,0);
            
            Pools.free(pos);
            Pools.free(scale);
            Pools.free(foo);
        }
    }
}
