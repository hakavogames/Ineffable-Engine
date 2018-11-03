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

package com.hakavo.ineffable.core;

import com.hakavo.ineffable.GameServices;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;

public class TiledBackground extends Renderable implements GameComponent.Copiable
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
            Vector2 pos=Pools.obtain(Vector2.class),scale=Pools.obtain(Vector2.class).set(1,1);
            Matrix3 matrix=Pools.obtain(Matrix3.class).idt();
            if(transform!=null)
            {
                transform.calculateMatrix(matrix);
                Vector2 foo=Pools.obtain(Vector2.class);
                pos.set(matrix.getTranslation(foo));
                scale.set(matrix.getScale(foo));
            }
            
            float viewportWidth=cam.viewportWidth*cam.zoom;
            float viewportHeight=cam.viewportHeight*cam.zoom;
            
            int minx=(int)(pos.x/width/scale.x-viewportWidth/2/width/scale.x);
            int maxx=(int)(pos.x/width/scale.x+viewportWidth/2/width/scale.x);
            int miny=(int)(pos.y/height/scale.y-viewportHeight/2/height/scale.y-1);
            int maxy=(int)(pos.y/height/scale.y+viewportHeight/2/height/scale.y);
            
            for(int x=minx-1;x<=maxx;x++)
                for(int y=miny-1;y<=maxy;y++)
                    sb.draw(sprite.textureRegion,x*width*scale.x,y*height*scale.y,0,0,width,height,scale.x,scale.y,0);
            Pools.free(matrix);
        }
    }
    public void onGui(OrthographicCamera gui) {
    }

    @Override
    public TiledBackground cpy() {
        TiledBackground tiledBackground=new TiledBackground(this.sprite);
        tiledBackground.copyFrom(this);
        tiledBackground.color.set(this.color);
        return tiledBackground;
    }
}
