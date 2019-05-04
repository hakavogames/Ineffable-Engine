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
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.assets.AssetManager;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.*;
import java.io.*;

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
        
        this.addComponent(new MapCollider());
        this.addComponent(new MapRenderer());
        this.addComponent(new Transform());
    }
    public Map(Tileset tileset,FileHandle data) {
        this(0,0,tileset);
        this.read(data);
        this.width=layers.get(0).getWidth();
        this.height=layers.get(0).getHeight();
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
        MapLayer layer=new MapLayer(getWidth(),getHeight(),name);
        layers.add(layer);
    }
    public void removeLayer(int index) {layers.removeIndex(index);}
    public void removeLayer(String name) {layers.removeIndex(getLayerIndexByName(name));}
    
    public void write(FileHandle fh) { // fh must be a directory!
        try {
            fh.emptyDirectory();
            File file=fh.file();
            file.mkdirs();
            
            File xmlFile=new File(file.getPath().concat("/map.xml"));
            XmlWriter xml=new XmlWriter(new FileWriter(xmlFile)).element("map");
            for(MapLayer layer : layers) {
                xml.element("layer").attribute("name",layer.name).attribute("path",fh.path()+"/"+layer.name).pop();
                layer.write(Gdx.files.internal(file.getPath()+"/"+layer.name));
            }
            xml.flush();
            xml.close();
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void read(FileHandle fh) { // fh must be a xml file!
        try {
            this.layers.clear();
            XmlReader xml=new XmlReader();
            Element e=xml.parse(fh);
            for(Element layer : e.getChildrenByName("layer")) {
                MapLayer ml=new MapLayer(0,0);
                ml.name=layer.getAttribute("name").intern();
                ml.read(new FileHandle((layer.getAttribute("path").intern())),this.tileset);
                this.layers.add(ml);
            }
            this.getComponent(MapCollider.class).initWalls();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public class MapCollider extends Collider {
        private Map map;
        private boolean[][] walls;
        @Override
        public void start() {
            map=(Map)this.getGameObject();
            initWalls();
        }
        public void initWalls() {
            map=(Map)this.getGameObject();
            walls=new boolean[map.getWidth()][map.getHeight()];
            for(int i=0;i<map.getLayerCount();i++)
                for(int x=0;x<map.getWidth();x++)
                    for(int y=0;y<map.getHeight();y++)
                        if(walls[x][y]==false&&map.getLayer(i).data[x][y].parent!=null&&
                                map.getLayer(i).data[x][y].parent.collide==true&&map.getLayer(i).data[x][y].visible)
                            walls[x][y]=true;
        }
        public boolean[][] getWalls() {
            return walls;
        }
        @Override
        public void update(float delta) {
        }

        @Override
        public boolean collides(Collider collider) {
            if(collider instanceof BoxCollider) {
                Vector2 scale=super.getGameObject().getComponent(Transform.class).getScale(Pools.obtain(Vector2.class));
                
                BoxCollider coll=(BoxCollider)collider;
                int size=map.tileset.tilesize;
                /*BoxCollider test=new BoxCollider((coll.getTransformedX()/size/scale.x),(coll.getTransformedY()/size/scale.y),
                                                 size,size);
                test.setGameObject(map);
                test.tags.addAll(this.tags);
                test.start();
                test.update(0);
                
                float tx=coll.getTransformedX()/scale.x/size,ty=coll.getTransformedY()/scale.y/size;
                float tw=coll.getTransformedWidth()/scale.x/size,th=coll.getTransformedHeight()/scale.y/size;
                if(checkWall((int)tx,(int)ty)||
                   checkWall((int)(tx+tw),(int)ty)||
                   checkWall((int)(tx+tw),(int)(ty+th))||
                   checkWall((int)tx,(int)(ty+th)))
                    return true;
                */
                
                int left=(int)(coll.getTransformedX()/scale.x/size);
                int right=(int)((coll.getTransformedX()+coll.getTransformedWidth())/scale.y/size);
                int bottom=(int)(coll.getTransformedY()/scale.y/size);
                int top=(int)((coll.getTransformedY()+coll.getTransformedHeight())/scale.y/size);
                
                for(int x=left;x<=right;x++)
                    for(int y=bottom;y<=top;y++)
                        if(checkWall(x,y))
                            return true;
                
                Pools.free(scale);
            }
            else if(collider instanceof PointCollider) {
                Vector2 scale=super.getGameObject().getComponent(Transform.class).getScale(Pools.obtain(Vector2.class));
                
                PointCollider coll=(PointCollider)collider;
                int size=map.tileset.tilesize;
                PointCollider test=new PointCollider((coll.getTransformedX()/size/scale.x),(coll.getTransformedY()/size/scale.y));
                test.setGameObject(map);
                test.tags.addAll(this.tags);
                test.start();
                test.update(0);
                
                float tx=test.x,ty=test.y;
                if(checkWall((int)tx,(int)ty))
                    return true;
                
                Pools.free(scale);
            }
            else if(collider instanceof CircleCollider) {
                Vector2 scale=super.getGameObject().getComponent(Transform.class).getScale(Pools.obtain(Vector2.class));
                
                CircleCollider coll=(CircleCollider)collider;
                int size=map.tileset.tilesize;
                CircleCollider test=new CircleCollider((coll.getTransformedX()/size/scale.x),(coll.getTransformedY()/size/scale.y)+0.2f);
                test.setGameObject(map);
                test.tags.addAll(this.tags);
                test.start();
                test.update(0);
                
                float tx=test.x,ty=test.y;
                if(checkWall((int)tx,(int)ty))
                    return true;
                
                Pools.free(scale);
            }
            return false;
        }
        private boolean checkWall(int x,int y) {
            return x<0||y<0||x>=getWidth()||y>=getWidth() ? false : walls[x][y];
        }

        @Override
        public MapCollider cpy() {
            return new MapCollider();
        }

        @Override
        public Vector2 getNormal(Collider collider,Vector2 out) {
            return out.set(0,0);
        }

        @Override
        public float getVolume() {
            return 1;
        }
    }
    public class MapRenderer extends Renderable
    {
        private Transform transform;
        @Override
        public void render(OrthographicCamera cam) {
            Map map=(Map)this.gameObject;
            Tileset tileset=map.tileset;
            SpriteBatch sb=GameServices.getSpriteBatch();
            super.prepareRendering();
            
            Matrix3 matrix=transform.calculateMatrix(Pools.obtain(Matrix3.class));
            Matrix4 matrix4=Pools.obtain(Matrix4.class).set(matrix);
            sb.setTransformMatrix(matrix4);
            sb.setColor(1,1,1,1);
            Vector2 scale=matrix.getScale(Pools.obtain(Vector2.class));

            float viewportWidth=cam.viewportWidth*cam.zoom;
            float viewportHeight=cam.viewportHeight*cam.zoom;
            int minx=(int)(cam.position.x/tileset.tilesize/scale.x-viewportWidth/2/tileset.tilesize/scale.x);
            int maxx=(int)(cam.position.x/tileset.tilesize/scale.x+viewportWidth/2/tileset.tilesize/scale.x);
            int miny=(int)(cam.position.y/tileset.tilesize/scale.y-viewportHeight/2/tileset.tilesize/scale.y-1);
            int maxy=(int)(cam.position.y/tileset.tilesize/scale.y+viewportHeight/2/tileset.tilesize/scale.y);

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
                            /*if(data[x][y].parent.collide)sb.setColor(1,0,0,1);
                            else sb.setColor(1,1,1,1);
                            sb.draw((Texture)AssetManager.getAsset("pixel"),x*tileset.tilesize,y*tileset.tilesize,
                                    data[x][y].parent.tileX*tileset.tilesize,data[x][y].parent.tileY*tileset.tilesize,
                                    tileset.tilesize,tileset.tilesize);*/
                        }
            }
            Pools.free(scale);
            Pools.free(matrix);
            Pools.free(matrix4);
        }
        public void onGui(OrthographicCamera gui) {
        }
        @Override
        public void update(float delta) {
        }
        @Override
        public void start() {
            transform=this.getGameObject().getComponent(Transform.class);
        }
    }
    public class MapLayer {
        public String name="default";
        public TileObject[][] data;
        public MapLayer(int width,int height)
        {
            this(width,height,"default");
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
        public void write(FileHandle fh) {
            try {
                DataOutputStream out=new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fh.file())));
                out.writeInt(getWidth());out.writeInt(getHeight());
                for(int x=0;x<getWidth();x++)
                    for(int y=0;y<getHeight();y++)
                    {
                        if(data[x][y].parent!=null)
                        {
                            out.writeInt(data[x][y].parent.tileX);
                            out.writeInt(data[x][y].parent.tileY);
                            out.writeBoolean(data[x][y].visible);
                        }
                        else {
                            out.writeInt(0);
                            out.writeInt(0);
                            out.writeBoolean(false);
                        }
                    }
                
                out.flush();
                out.close();
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
        public void read(FileHandle fh,Tileset tileset) {
            try {
                DataInputStream in=new DataInputStream(new BufferedInputStream(new FileInputStream(fh.file())));
                data=new TileObject[in.readInt()][in.readInt()];
                for(int x=0;x<getWidth();x++)
                    for(int y=0;y<getHeight();y++)
                    {
                        TileObject tileObject=new TileObject();
                        tileObject.parent=tileset.getTileByCoords(in.readInt(),in.readInt());
                        tileObject.visible=in.readBoolean();
                        data[x][y]=tileObject;
                    }
                
                in.close();
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}