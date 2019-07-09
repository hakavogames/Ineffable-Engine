package com.hakavo.ineffable.ui;
import com.badlogic.gdx.math.*;

public class Bounds {
    public float x,y,width,height;
    
    public Bounds() {
    }
    public Bounds(Bounds bounds) {
        set(bounds);
    }
    public Bounds(float x,float y,float width,float height) {
        set(x,y,width,height);
    }
    
    public Bounds set(float x,float y,float width,float height) {
        setPosition(x,y);
        setSize(width,height);
        return this;
    }
    public Bounds set(Bounds bounds) {
        set(bounds.x,bounds.y,bounds.width,bounds.height);
        return this;
    }
    public Bounds setPosition(float x,float y) {
        this.x=x;
        this.y=y;
        return this;
    }
    public Bounds setSize(float width,float height) {
        this.width=width;
        this.height=height;
        return this;
    }
    public Bounds setTopRightCorner(float x2,float y2) {
        width=x2-x;
        height=y2-y;
        return this;
    }
    public Bounds positive() {
        //width+=Math.min(0,x);
        //height+=Math.min(0,y);
        x=Math.max(x,0);
        y=Math.max(y,0);
        //width=Math.max(width,0);
        //height=Math.max(height,0);
        return this;
    }
    
    public float getLeft() {
        return x;
    }
    public float getRight() {
        return x+width;
    }
    public float getBottom() {
        return y;
    }
    public float getTop() {
        return y+height;
    }
    
    public Vector2 getCenter(Vector2 out) {
        return out.set(x+width/2,y+height/2);
    }
    public Vector2 getTopLeftCorner(Vector2 out) {
        return out.set(x,y);
    }
    public Vector2 getBottomRightCorner(Vector2 out) {
        return out.set(x+width,y+width);
    }
    
    public boolean contains(float x,float y) {
        return x>=this.x&&x<=this.x+this.width&&y>=this.y&&y<=this.y+this.height;
    }
    
    public Bounds cpy() {
        return new Bounds(x,y,width,height);
    }
    @Override
    public String toString() {
        return "["+x+","+y+","+width+","+height+"]";
    }
}
