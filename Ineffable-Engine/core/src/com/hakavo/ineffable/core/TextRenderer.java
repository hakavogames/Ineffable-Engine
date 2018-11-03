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

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.ineffable.GameServices;

public class TextRenderer extends Renderable implements GameComponent.Copiable {
    public BitmapFont font;
    public Color color;
    public String text;
    private Transform transform;
    
    public TextRenderer(BitmapFont font,String text,Color color) {
        this.font=font;
        this.text=text;
        this.color=color;
    }
    public TextRenderer(String text,Color color) {
        this(GameServices.getFonts().getValueAt(0),text,color);
    }
    public TextRenderer(String text) {
        this(text,new Color(0,0,0,1));
    }
    public TextRenderer() {
        this("");
    }
    
    @Override
    public void start() {
        transform=this.getGameObject().getComponent(Transform.class);
    }
    @Override
    public void render(OrthographicCamera camera) {
        Matrix4 foo=Pools.obtain(Matrix4.class);
        Matrix3 bar=transform.calculateMatrix(Pools.obtain(Matrix3.class));
        foo.set(bar);
        
        GameServices.getSpriteBatch().setTransformMatrix(foo);
        font.setColor(color);
        font.draw(GameServices.getSpriteBatch(),text,0,0);
        
        Pools.free(foo);
        Pools.free(bar);
    }
    @Override
    public void onGui(OrthographicCamera gui) {
    }
    @Override
    public void update(float delta) {
    }

    @Override
    public TextRenderer cpy() {
        TextRenderer textRenderer=new TextRenderer(this.text);
        textRenderer.copyFrom(this);
        textRenderer.font=this.font;
        textRenderer.color.set(this.color);
        return textRenderer;
    }
}
