package com.kozma;
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.XmlReader.Element;

public class Tileset {
    protected Array<Tile> tiles=new Array<Tile>();
    public Texture atlas;
    public int tilesize;
    public Tileset() {}
    public Tileset(Texture tex,int tilesize)
    {
        init(tex,tilesize);
    }
    public Tileset(FileHandle fh,int tilesize)
    {
        init(new Texture(fh),tilesize);
    }
    public Tileset(FileHandle fh)
    {
        XmlReader r=new XmlReader();
        try{
            Element root=r.parse(fh);
            Element tiledata=root.getChildByName("tiledata");
            atlas=new Texture(Gdx.files.internal(tiledata.getAttribute("filename")));
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
    public int getTileIndexByName(String name)
    {
        for(int i=0;i<tiles.size;i++)
            if(tiles.get(i).name.equals(name))
                return i;
        return -1;
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