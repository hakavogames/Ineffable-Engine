package com.kozma;

public class TileObject {
    public Tile parent;
    public boolean visible=true;
    public int height=0;
    public Properties properties=new Properties();
    public TileObject() {}
    public TileObject(Tile parent) {this.parent=parent;}
    public TileObject(Tile parent,Properties properties) {this.parent=parent;this.properties=properties;}
}