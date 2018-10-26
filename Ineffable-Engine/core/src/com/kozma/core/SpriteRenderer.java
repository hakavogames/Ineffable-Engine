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
    }
}
