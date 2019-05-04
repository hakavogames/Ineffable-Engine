package com.hakavo.ineffable.ui;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.math.*;
import com.hakavo.ineffable.assets.AssetManager;

public class Container {
    public Style style=new Style();
    public String id="default";
    public boolean focusable=true,visible=true;
    
    private boolean focused=false;
    public Bounds bounds=new Bounds(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    private final Bounds transformed=new Bounds();
    
    private final Array<EventListener> eventListeners=new Array<EventListener>();
    public final EventListener containerEventListener=new ContainerEventListener(this);
    
    private Container parent;
    private final Array<Container> children=new Array<Container>();
    
    public final void add(Container... containers) {
        for(Container container : containers) {
            container.setParent(this);
            container.onAdd(this);
            children.add(container);
        }
    }
    public final boolean exists(String id) {
        int hash=id.hashCode();
        for(Container container : children) {
            if(container.id.hashCode()==hash) {
                return true;
            }
        }
        return false;
    }
    public final boolean remove(String id) {
        int hash=id.hashCode();
        for(Container container : children) {
            if(container.id.hashCode()==hash) {
                children.removeValue(container,true);
                return true;
            }
        }
        return false;
    }
    public final void remove(Container reference) {
        children.removeValue(reference,true);
    }
    public final Container get(String id) {
        int hash=id.hashCode();
        for(Container container : children) {
            if(container.id.hashCode()==hash) {
                return container;
            }
        }
        return null;
    }
    public final <T extends Container> T get(Class<T> containerType) {
        for(Container container : children)
            if(containerType.isAssignableFrom(container.getClass()))
                return (T)container;
        return null;
    }
    public final <T extends Container> T getParentByType(Class<T> type) {
        if(parent==null)return null;
        if(parent.getClass().isAssignableFrom(type))
            return (T)parent;
        return parent.getParentByType(type);
    }
    public final void clear() {
        children.clear();
    }
    public final int getChildrenCount() {
        return children.size;
    }
    public final int countVisibleChildren() {
        int s=0;
        for(Container container : children)
            if(container.visible)
                s++;
        return s;
    }
    
    public final void update(float delta) {
        for(Container container : children)
            container.update(delta);
        onUpdate(delta);
    }
    public final void render(OrthographicCamera camera,SpriteBatch spriteBatch) {
        if(!visible)return;
        Bounds bounds=Pools.obtain(Bounds.class);
        transform(bounds.set(this.bounds));
        this.getScissorBounds(bounds);
        Matrix4 trans=spriteBatch.getTransformMatrix().idt();
        trans.setToTranslation(bounds.x,bounds.y,0);
        spriteBatch.setTransformMatrix(trans);
        
        if(!spriteBatch.isDrawing())spriteBatch.begin();
        spriteBatch.setColor(style.background);
        Texture tex;
        if(style.blur)tex=GUI.blur;
        else tex=AssetManager.getAsset("gui-blank");
        Gdx.gl.glScissor(Math.round(bounds.x),Math.round(bounds.y),Math.round(bounds.width),Math.round(bounds.height));
        //System.out.println(bounds+" "+getClass().getSimpleName());
        spriteBatch.draw(tex,0,0,bounds.width,bounds.height,
                         (int)(bounds.x),(int)(bounds.y),(int)bounds.width,(int)bounds.height,false,true);
        
        if(style.borderWidth>0) {
            tex=AssetManager.getAsset("gui-blank");
            spriteBatch.setColor(style.borderColor);
            spriteBatch.draw(tex,0,0,bounds.width,style.borderWidth);
            spriteBatch.draw(tex,0,0,style.borderWidth,bounds.height);
            spriteBatch.draw(tex,bounds.width-style.borderWidth,0,style.borderWidth,bounds.height);
            spriteBatch.draw(tex,0,bounds.height-style.borderWidth,bounds.width,style.borderWidth);
        }
        spriteBatch.setColor(style.foreground);
        onRender(camera,spriteBatch);
        spriteBatch.end();
        for(Container container : children)
            container.render(camera,spriteBatch);
        Pools.free(bounds);
    }
    
    public void onUpdate(float delta) {
    }
    public void onRender(OrthographicCamera camera,SpriteBatch spriteBatch) {
    }
    public void onAdd(Container parent) {
    }
    
    public final void setParent(Container parent) {
        this.parent=parent;
    }
    public final Container getParent() {
        return parent;
    }
    public final Container getRoot() {
        if(parent==null)return this;
        return parent.getRoot();
    }
    
    public void setFocused(boolean focus) {
        this.focused=focus;
    }
    public boolean isFocused() {
        return focused;
    }
    
    public final Bounds transform(Bounds out) {
        if(parent!=null) {
            out.x+=parent.bounds.x;
            out.y+=parent.bounds.y;
            return parent.transform(out);
        }
        return out;
    }
    public final Bounds getScissorBounds() {
        Bounds out=bounds.cpy();
        getScissorBounds(out);
        return out;
    }
    public final void getScissorBounds(Bounds out) {
        Bounds bounds=transform(new Bounds(this.bounds)).positive();
        out.setPosition(Math.max(bounds.x,out.x),Math.max(bounds.y,out.y));
        out.setSize(Math.min(bounds.x+bounds.width,out.x+out.width)-out.x,Math.min(bounds.y+bounds.height,out.y+out.height)-out.y);
        if(parent!=null)parent.getScissorBounds(out);
    }
    public final Bounds getTransformedBounds() {
        return transform(transformed.set(this.bounds));
    }
    public final void addEventListener(EventListener listener) {
        listener.parent=this;
        eventListeners.add(listener);
    }
    
    public final Container findFocusableContainer(float x,float y) {
        Bounds bounds=transform(Pools.obtain(Bounds.class).set(this.bounds));
        Pools.free(bounds);
        if(this.focusable&&bounds.contains(x,y)) {
            Container foo=null;
            for(Container container : children)
                if(container.findFocusableContainer(x,y)!=null) {
                    foo=container;
                    break;
                }
            if(foo==null)return this;
            return foo.findFocusableContainer(x,y);
        }
        return null;
    }

    private static class ContainerEventListener extends EventListener {
        private Container parent;
        public ContainerEventListener(Container parent) {
            this.parent=parent;
        }
        @Override
        public void onButtonDown(int button) {
            for(EventListener event : parent.eventListeners)event.onButtonDown(button);
        }
        @Override
        public void onButtonUp(int button) {
            for(EventListener event : parent.eventListeners)event.onButtonUp(button);
        }
        @Override
        public void onKeyUp(int key) {
            for(EventListener event : parent.eventListeners)event.onKeyUp(key);
        }
        @Override
        public void onKeyDown(int key) {
            for(EventListener event : parent.eventListeners)event.onKeyDown(key);
        }
        @Override
        public void onKeyTyped(char c) {
            for(EventListener event : parent.eventListeners)event.onKeyTyped(c);
        }
        @Override
        public void onHoverEnter() {
            for(EventListener event : parent.eventListeners)event.onHoverEnter();
        }
        @Override
        public void onHover() {
            for(EventListener event : parent.eventListeners)event.onHover();
        }
        @Override
        public void onHoverExit() {
            for(EventListener event : parent.eventListeners)event.onHoverExit();
        }
        @Override
        public void onMouseMoved(int screenX,int screenY) {
            for(EventListener event : parent.eventListeners)event.onMouseMoved(screenX,screenY);
        }
        @Override
        public void onMouseDragged(int screenX,int screenY) {
            for(EventListener event : parent.eventListeners)event.onMouseDragged(screenX,screenY);
        }
        @Override
        public void onFocus() {
            for(EventListener event : parent.eventListeners)event.onFocus();
        }
        @Override
        public void onFocusLost() {
            for(EventListener event : parent.eventListeners)event.onFocusLost();
        }
    }
}
