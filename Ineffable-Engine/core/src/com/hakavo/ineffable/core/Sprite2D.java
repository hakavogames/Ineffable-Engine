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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hakavo.ineffable.assets.*;

public class Sprite2D extends GameComponent implements GameComponent.Copiable
{
    public TextureRegion textureRegion;
    public Sprite2D() {
        AssetManager.loadAsset("texture",Gdx.files.internal("sprites/pixel.png"),"tex_blank");
        textureRegion=new TextureRegion(AssetManager.getAsset("tex_blank",Texture.class));
    }
    public Sprite2D(String assetName) {
        this(AssetManager.getAsset(assetName,Texture.class));
    }
    public Sprite2D(Texture texture) {
        textureRegion=new TextureRegion(texture);
    }
    public Sprite2D(TextureRegion textureRegion) {
        this.textureRegion=textureRegion;
        this.textureRegion.setTexture(textureRegion.getTexture());
    }
    public Sprite2D(Sprite2D copyFrom) {
        this.textureRegion=new TextureRegion(copyFrom.textureRegion);
        this.textureRegion.setTexture(copyFrom.textureRegion.getTexture());
    }
    @Override
    public void update(float delta) {
    }
    @Override
    public void start() {
    }
    @Override
    public Sprite2D cpy() {
        return new Sprite2D(textureRegion);
    }
}
