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
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.XmlReader.Element;

public class Tileset {
    public final Array<Tile> tiles=new Array<Tile>();
    public Texture atlas;
    private String fname;
    public int tilesize;
    public Tileset() {}
    public Tileset(Texture tex,int tilesize)
    {
        init(tex,tilesize);
    }
    public Tileset(FileHandle fh,int tilesize)
    {
        fname=fh.path();
        init(new Texture(fh),tilesize);
    }
    public Tileset(FileHandle fh)
    {
        XmlReader r=new XmlReader();
        try{
            Element root=r.parse(fh);
            Element tiledata=root.getChildByName("tiledata");
            atlas=new Texture(Gdx.files.internal(tiledata.getAttribute("filename")));
            fname=tiledata.getAttribute("filename");
            tilesize=Integer.valueOf(tiledata.getAttribute("tilesize"));
            if(atlas.getWidth()%tilesize>0||atlas.getHeight()%tilesize>0)
                throw new GdxRuntimeException("Tileset texture can't be split in "+tilesize+"x"+tilesize+" tiles.");
            Array<Element> t=root.getChildrenByName("tile");
            for(int i=0;i<t.size;i++)
            {
                Tile tile=new Tile(this);
                tile.tileX=Integer.valueOf(t.get(i).getAttribute("x"));
                tile.tileY=Integer.valueOf(t.get(i).getAttribute("y"));
                tile.name=t.get(i).getChildByName("name").getText();
                tile.collide=Boolean.valueOf(t.get(i).getChildByName("collide").getText());
                tiles.add(tile);
            }
        }catch(Exception ex){ex.printStackTrace(System.err);Gdx.app.exit();}
    }
    public void save(FileHandle fh) {
        XmlWriter w=new XmlWriter(fh.writer(false));
        try {
            XmlWriter root=w.element("tileset");
            root.element("tiledata").attribute("filename",fname).attribute("tilesize",tilesize);
            root.pop();
            for(Tile tile : tiles) {
                root.element("tile").attribute("x",tile.tileX).attribute("y",tile.tileY);
                root.element("name").text(tile.name);
                root.pop();
                root.element("collide").text(tile.collide);
                root.pop();
                root.pop();
            }
            w.flush();
            w.close();
        }
        catch(Exception ex) {
            ex.printStackTrace(System.err);
            Gdx.app.exit();
        }
    }
    public int getTileIndexByName(String name)
    {
        for(int i=0;i<tiles.size;i++)
            if(tiles.get(i).name.equals(name))
                return i;
        return -1;
    }
    public Tile getTileByCoords(int tileX,int tileY)
    {
        for(int i=0;i<tiles.size;i++)
            if(tiles.get(i).tileX==tileX&&tiles.get(i).tileY==tileY)
                return tiles.get(i);
        return null;
    }
    public void init(Texture tex,int tilesize)
    {
        if(tex.getWidth()%tilesize>0||tex.getHeight()%tilesize>0)
            throw new GdxRuntimeException("Tileset texture can't be split in "+tilesize+"x"+tilesize+" tiles.");
        atlas=tex;
        this.tilesize=tilesize;
        if(tiles.size>0)tiles.clear();
        for(int j=0;j<tex.getHeight()/tilesize;j++)
            for(int i=0;i<tex.getWidth()/tilesize;i++)
            {
                Tile tile=new Tile(i,j,this);
                tiles.add(tile);
            }
    }
}