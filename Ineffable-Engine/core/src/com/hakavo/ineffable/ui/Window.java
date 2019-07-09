package com.hakavo.ineffable.ui;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.hakavo.ineffable.assets.AssetManager;

public class Window extends Container {
    public boolean movable;
    private final boolean closeable;
    private final boolean alignToCenter;
    
    public Window(String title) {
        this(title,true,true);
    }
    public Window(String title,boolean movable,boolean closeable) {
        this(title,movable,closeable,false);
    }
    public Window(String title,boolean movable,boolean closeable,boolean alignToCenter) {
        this.movable=movable;
        this.closeable=closeable;
        this.alignToCenter=alignToCenter;
        super.style.setBorder(2,0.15f,0.15f,0.15f,1);
        super.style.background.set(0,0,0,0.5f);
        super.style.blur=false;
        TitleBar titleBar=new TitleBar(title,4,closeable);
        super.add(titleBar);
        titleBar.onUpdate(0);
    }
    
    @Override
    public void onAdd(Container parent) {
        if(alignToCenter) {
            Bounds bounds=parent.getRoot().bounds;
            super.bounds.setPosition((bounds.width-bounds.x)/2-super.bounds.width/2,(bounds.height-bounds.y)/2-super.bounds.height/2);
        }
    }
    
    public boolean isCloseable() {
        return closeable;
    }
    
    public void setToFitSize(float width,float height) {
        bounds.setSize(width,height+super.get(TitleBar.class).bounds.height);
    }
    public void setToFitSize(Bounds bounds) {
        setToFitSize(bounds.width,bounds.height);
    }
    
    private static class TitleBar extends Container {
        private Label title;
        private ImageButton closeButton;
        public float padding=0;
        public TitleBar(String text,float padding,boolean closeable) {
            this.padding=padding;
            title=new Label(text);
            title.style.foreground.set(0.9f,0.9f,0.9f,1);
            title.bounds.setPosition(padding,padding);
            super.style.blur=false;
            super.style.background.set(0.15f,0.15f,0.15f,1f);
            
            Image image=new Image("gui-close");
            closeButton=new ImageButton(image,2);
            closeButton.style.borderWidth=0;
            closeButton.onButtonUp.set(1f,0.3f,0.3f,1f);
            closeButton.onButtonDown.set(closeButton.onButtonUp).mul(0.5f);
            closeButton.addEventListener(new CloseButtonListener(this));
            super.add(title);
            if(closeable)super.add(closeButton);
            super.addEventListener(new EventListener() {
                int x,y;
                @Override
                public void onMouseMoved(int x,int y) {
                    this.x=x;
                    this.y=y;
                }
                @Override
                public void onMouseDragged(int x,int y) {
                    int dx=x-this.x,dy=y-this.y;
                    getParent().bounds.x+=dx;
                    if(!checkBounds())getParent().bounds.x-=dx;
                    getParent().bounds.y-=dy;
                    if(!checkBounds())getParent().bounds.y+=dy;
                    this.x=x;
                    this.y=y;
                }
                private boolean checkBounds() {
                    Bounds bounds=getParent().transform(Pools.obtain(Bounds.class).set(getParent().bounds));
                    Bounds parent=getParent().getParent().transform(Pools.obtain(Bounds.class).set(getParent().getParent().bounds));
                    boolean ok=false;
                    if(bounds.x>=parent.x&&bounds.x+bounds.width<=parent.x+parent.width&&
                       bounds.y>=parent.y&&bounds.y+bounds.height<=parent.y+parent.height)
                        ok=true;
                    Pools.free(bounds);Pools.free(parent);
                    return ok;
                }
            });
        }
        @Override
        public void onUpdate(float delta) {
            Bounds parentBounds=super.getParent().bounds;
            super.bounds.set(0,parentBounds.height-title.bounds.height-padding*2-2,parentBounds.width,title.bounds.height+padding*2+2);
            closeButton.getImage().bounds.setSize(bounds.height-closeButton.padding*2-2,bounds.height-closeButton.padding*2-2);
            closeButton.bounds.setPosition(bounds.width-bounds.height,0);
        }
        
        private static class CloseButtonListener extends EventListener {
            private final TitleBar parent;
            public CloseButtonListener(TitleBar parent) {
                this.parent=parent;
            }
            @Override
            public void onButtonUp(int button) {
                if(parent.getParent().getParent()!=null)
                    parent.getParent().getParent().remove(parent.getParent());
            }
        }
    }
}
