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

package com.hakavo.ineffable.gameobjects;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.hakavo.ineffable.GameServices;
import com.hakavo.ineffable.TileObject;
import com.hakavo.ineffable.Tileset;
import com.hakavo.ineffable.core.GameObject;
import com.hakavo.ineffable.core.Renderable;

public class Map extends GameObject {
    public final Array<MapLayer> layers;
    public Tileset tileset;
    protected int width,height;
    public Map(int width,int height)
    {
        this(width,height,null);
    }
    public Map(int width,int height,Tileset tileset)
    {
        this.tileset=tileset;
        this.width=width;this.height=height;
        layers=new Array<MapLayer>();
        layers.add(new MapLayer(width,height,"layer0"));
        
        this.addComponent(new MapRenderer());
    }
    public void setTileset(Tileset tileset) {this.tileset=tileset;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public void addLayer(MapLayer layer)
    {
        if(layer.getWidth()!=getWidth()||layer.getHeight()!=getHeight())
            throw new GdxRuntimeException("All layers must have the same sizes.");
        layers.add(layer);
    }
    public int getLayerCount() {return layers.size;}
    public int getLayerIndexByName(String name)
    {
        for(int i=0;i<layers.size;i++)
            if(layers.get(i).name.equals(name))
                return i;
        return -1;
    }
    public MapLayer getLayer(int index) {return layers.get(index);}
    public MapLayer getLayer(String name) {return layers.get(getLayerIndexByName(name));}
    public void addLayer()
    {
        addLayer("layer"+layers.size);
    }
    public void addLayer(String name)
    {
        layers.add(new MapLayer(getWidth(),getHeight(),name));
    }
    public void removeLayer(int index) {layers.removeIndex(index);}
    public void removeLayer(String name) {layers.removeIndex(getLayerIndexByName(name));}
    
    public class MapRenderer extends Renderable
    {
        @Override
        public void render(OrthographicCamera cam) {
            Map map=(Map)this.gameObject;
            Tileset tileset=map.tileset;
            SpriteBatch sb=GameServices.getSpriteBatch();
            super.prepareRendering();

            float viewportWidth=cam.viewportWidth*cam.zoom;
            float viewportHeight=cam.viewportHeight*cam.zoom;
            int minx=(int)(cam.position.x/tileset.tilesize-viewportWidth/2/tileset.tilesize);
            int maxx=(int)(cam.position.x/tileset.tilesize+viewportWidth/2/tileset.tilesize);
            int miny=(int)(cam.position.y/tileset.tilesize-viewportHeight/2/tileset.tilesize-1);
            int maxy=(int)(cam.position.y/tileset.tilesize+viewportHeight/2/tileset.tilesize);

            minx=Math.max(0,minx-1);
            maxx=Math.min(maxx+1,map.getWidth());
            miny=Math.max(0,miny-1);
            maxy=Math.min(maxy+1,map.getHeight());
            for(int i=0;i<map.layers.size;i++)
            {
                TileObject[][] data=map.layers.get(i).data;
                for(int x=minx;x<maxx;x++)
                    for(int y=miny;y<maxy;y++)
                        if(data[x][y]!=null&&data[x][y].visible&&data[x][y].parent!=null)
                        {
                            sb.draw(tileset.atlas,x*tileset.tilesize,y*tileset.tilesize,
                                    data[x][y].parent.tileX*tileset.tilesize,data[x][y].parent.tileY*tileset.tilesize,
                                    tileset.tilesize,tileset.tilesize);
                        }
            }
        }
        public void onGui(OrthographicCamera gui) {
        }
        @Override
        public void update(float delta) {
        }
        @Override
        public void start() {
        }
    }
    public class MapLayer {
        public String name="default";
        public TileObject[][] data;
        public MapLayer(int width,int height)
        {
            data=new TileObject[width][height];
            for(int i=0;i<width;i++)
                for(int j=0;j<height;j++)
                    data[i][j]=new TileObject();
        }
        public MapLayer(int width,int height,String name)
        {
            data=new TileObject[width][height];
            this.name=name;
            for(int i=0;i<width;i++)
                for(int j=0;j<height;j++)
                    data[i][j]=new TileObject();
        }
        public int getWidth() {return data.length;}
        public int getHeight() {return data[0].length;}
    }

}