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

package com.hakavo.ineffable;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hakavo.ineffable.core.Sprite2D;

public class Tile
{
    public final Tileset tileset;
    
    public String name="default";
    public int tileX,tileY;
    public boolean collide;
    public Tile(Tileset parent){this(0,0,parent);}
    public Tile(int tx,int ty,Tileset parent)
    {
        tileX=tx;
        tileY=ty;
        this.tileset=parent;
    }
    
    public Sprite2D toSprite()
    {
        Sprite2D sprite=new Sprite2D();
        int size=tileset.tilesize;
        sprite.textureRegion=new TextureRegion(tileset.atlas,tileX*size,tileY*size,size,size);
        return sprite;
    }
    public TextureRegion createTextureRegion()
    {
        int size=tileset.tilesize;
        return new TextureRegion(tileset.atlas,tileX*size,tileY*size,size,size);
    }
}
