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
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hakavo.ineffable.assets.*;

public class Sprite2D extends GameComponent
{
    public TextureRegion textureRegion;
    public Sprite2D() {textureRegion=new TextureRegion();}
    public Sprite2D(String assetName) {
        this((Texture)AssetManager.getAsset(assetName));
    }
    public Sprite2D(Texture texture) {
        textureRegion=new TextureRegion(texture);
    }
    public Sprite2D(TextureRegion textureRegion) {
        this.textureRegion=textureRegion;
    }
    public Sprite2D(Sprite2D copyFrom) {
        this.textureRegion=new TextureRegion(copyFrom.textureRegion);
    }
    @Override
    public void update(float delta) {
    }
    @Override
    public void start() {
    }
}
