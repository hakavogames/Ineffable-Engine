package com.hakavo.ineffable.ui;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.math.*;
import com.hakavo.ineffable.assets.AssetManager;
import java.util.Comparator;

public class Container {
    public Style style=new Style();
    public String id="default";
    public boolean focusable=true,visible=true;
    private int layer;
    
    private boolean focused=false;
    public Bounds bounds=new Bounds(0,0,0,0);
    private final Bounds transformed=new Bounds();
    
    private final Array<EventListener> eventListeners=new Array<EventListener>();
    public final EventListener containerEventListener=new ContainerEventListener(this);
    
    private Container parent;
    private final Array<Container> children=new Array<Container>();
    private final Array<Container> renderList=new Array<Container>();
    
    public final void add(Container... containers) {
        for(Container container : containers) {
            container.setParent(this);
            container.onAdd(this);
            if(children.size>0)container.layer=children.get(0).layer+1;
            else container.layer=0;
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
    public final void removeByType(Class type) {
        for(int i=children.size-1;i>=0;i--)
            if(children.get(i).getClass().isAssignableFrom(type))
                children.removeIndex(i);
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
        sortContainers();
        Bounds bounds=Pools.obtain(Bounds.class);
        Bounds transformed=Pools.obtain(Bounds.class);
        transformed.set(transform(bounds.set(this.bounds)));
        Matrix4 trans=spriteBatch.getTransformMatrix().idt();
        trans.setToTranslation(bounds.x,bounds.y,0);
        spriteBatch.setTransformMatrix(trans);
        this.getScissorBounds(bounds);
        
        if(!spriteBatch.isDrawing())spriteBatch.begin();
        spriteBatch.setColor(style.background);
        Texture tex;
        if(style.blur)tex=GUI.blur;
        else tex=AssetManager.getAsset("gui-blank");
        Gdx.gl.glScissor(Math.round(bounds.x),Math.round(bounds.y),Math.round(bounds.width),Math.round(bounds.height));
        //System.out.println(bounds+" "+getClass().getSimpleName());
        spriteBatch.draw(tex,0,0,bounds.width,bounds.height,
                         bounds.x/tex.getWidth()/4,bounds.y/tex.getHeight()/4,
                         bounds.getRight()/tex.getWidth()/4,bounds.getTop()/tex.getHeight()/4);
        
        if(style.borderWidth>0) {
            tex=AssetManager.getAsset("gui-blank");
            spriteBatch.setColor(style.borderColor);
            if((style.borderSides&Style.BORDER_BOTTOM)==Style.BORDER_BOTTOM)
                spriteBatch.draw(tex,0,0,transformed.width,style.borderWidth);
            if((style.borderSides&Style.BORDER_LEFT)==Style.BORDER_LEFT)
                spriteBatch.draw(tex,0,0,style.borderWidth,transformed.height);
            if((style.borderSides&Style.BORDER_RIGHT)==Style.BORDER_RIGHT)
                spriteBatch.draw(tex,transformed.width-style.borderWidth,0,style.borderWidth,transformed.height);
            if((style.borderSides&Style.BORDER_TOP)==Style.BORDER_TOP)
                spriteBatch.draw(tex,0,transformed.height-style.borderWidth,transformed.width,style.borderWidth);
        }
        spriteBatch.setColor(style.foreground);
        onRender(camera,spriteBatch);
        spriteBatch.end();
        for(int i=renderList.size-1;i>=0;i--) {
            //for(int i=0;i<getDepth();i++)System.out.print("    ");
            //System.out.println(container.getClass().getSimpleName()+" ("+container.layer+") [");
            renderList.get(i).render(camera,spriteBatch);
            //for(int i=0;i<getDepth();i++)System.out.print("    ");
            //System.out.println("],");
        }
        Pools.free(bounds);
    }
    private void sortContainers() {
        renderList.clear();
        renderList.addAll(children);
        renderList.sort(new Comparator<Container>() {
            @Override
            public int compare(Container a,Container b) {
                if(a.layer<b.layer)
                    return -1;
                return 1;
            }
        });
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
    public final void center() {
        if(parent!=null) {
            bounds.setPosition(parent.bounds.width/2-bounds.width/2,
                               parent.bounds.height/2-bounds.height/2);
        }
    }
    
    public boolean isFocused() {
        return focused;
    }
    public void focus() {
        focused=true;
        if(parent!=null) {
            parent.renderList.get(0).layer=this.layer;
            this.layer=0;
            parent.sortContainers();
            parent.focus();
        }
    }
    
    public int getDepth() {
        return countDepth(0);
    }
    private int countDepth(int i) {
        i++;
        if(parent!=null)
            return parent.countDepth(i);
        return i;
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
    public final Bounds getMaxBounds(Bounds out) {
        out.set(Float.MAX_VALUE,Float.MAX_VALUE,0,0);
        for(Container container : children) {
            out.x=Math.min(out.x,container.bounds.x);
            out.y=Math.min(out.y,container.bounds.y);
            out.width=Math.max(out.getRight(),container.bounds.getRight());
            out.height=Math.max(out.getTop(),container.bounds.getTop());
        }
        return out;
    }
    public final void addEventListener(EventListener listener) {
        listener.parent=this;
        eventListeners.add(listener);
    }
    public final Container findFocusableContainer(float x,float y) {
        Bounds bounds=transform(Pools.obtain(Bounds.class).set(this.bounds));
        Pools.free(bounds);
        if(this.focusable&&this.visible&&bounds.contains(x,y)) {
            Container foo=null;
            for(Container container : renderList)
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
        public void onScroll(int amount) {
            for(EventListener event : parent.eventListeners)event.onScroll(amount);
        }
        @Override
        public void onFocus() {
            parent.focus();
            for(EventListener event : parent.eventListeners)event.onFocus();
        }
        @Override
        public void onFocusLost() {
            parent.focused=false;
            for(EventListener event : parent.eventListeners)event.onFocusLost();
        }
    }
}
