package com.kozma;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kozma.core.Sprite2D;

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
