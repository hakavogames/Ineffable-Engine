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
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;

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
            Matrix3 mat=Pools.obtain(Matrix3.class).idt();
            if(transform!=null)mat.set(transform.matrix);
            Matrix4 foo=Pools.obtain(Matrix4.class).set(mat);
            foo=new Matrix4().set(mat);
            
            TextureRegion tr=sprite.textureRegion;
            sb.setTransformMatrix(foo);
            sb.setColor(color);
            sb.draw(tr.getTexture(),0,0,tr.getRegionWidth(),tr.getRegionHeight(),
                    tr.getRegionX(),tr.getRegionY(),tr.getRegionWidth(),tr.getRegionHeight(),flipX,flipY);
            
            Pools.free(mat);
            //Pools.free(foo);
        }
    }
}