package com.hakavo.ineffable.ui;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.core.Joint;
import com.hakavo.ineffable.rendering.postfx.*;
import com.hakavo.ineffable.assets.AssetManager;
import com.hakavo.ineffable.rendering.postfx.effects.*;
import com.hakavo.ineffable.rendering.postfx.filters.*;

public class GUI {
    protected static Texture blur;
    protected static Engine engine;
    private static final InputProcessor inputProcessor=new GUIListener();
    private static PostProcessor postfx;
    
    private static ArrayMap<String,EventHandler> eventHandler;
    
    public static Container mainContainer;
    private static Container focused;
    
    public static void init(Engine engine) { // Internally called by the GameServices class
        GUI.engine=engine;
        Gdx.input.setInputProcessor(inputProcessor);
        eventHandler=new ArrayMap<String,EventHandler>();
        Pools.set(Bounds.class,new Pool<Bounds>() {
            @Override
            protected Bounds newObject() {
                return new Bounds();
            }
        });
        
        postfx=new PostProcessor(Gdx.graphics.getWidth()/4,Gdx.graphics.getHeight()/4,Format.RGB888);
        
        Pixmap pix=new Pixmap(1,1,Pixmap.Format.RGBA8888);
        pix.setColor(1,1,1,1);
        pix.drawPixel(0,0);
        AssetManager.assets.put("gui-blank",new Texture(pix));
        AssetManager.loadAsset("texture",Gdx.files.internal("ui/close.png"),"gui-close",true);
        pix.dispose();
        
        reset();
    }
    public static Joint getGameLevel() {
        return engine.getLevel();
    }
    
    public static void dispatchEvent(String name,Object... parameters) {
        eventHandler.get(name).handleMessage(parameters);
    }
    public static boolean hasEventHandler(String name) {
        return eventHandler.containsKey(name);
    }
    public static void setEventHandler(String eventName,EventHandler handler) {
        if(!hasEventHandler(eventName))
            eventHandler.put(eventName,handler);
        else eventHandler.setValue(eventHandler.indexOfKey(eventName),handler);
    }
    public static void destroyEventHandlers() {
        eventHandler.clear();
    }
    public static void reset() {
        destroyEventHandlers();
        
        postfx.effects.clear();
        postfx.effects.add(new SimpleEffect(new Blur(new Vector2(1,0),1f/postfx.getWidth()*8,16)));
        postfx.effects.add(new SimpleEffect(new Blur(new Vector2(0,1),1f/postfx.getHeight()*8,16)));
        postfx.effects.add(new SimpleEffect(new Noise(0.02f,1,true)));
        
        mainContainer=new Container();
        mainContainer.focus();
        mainContainer.style.blur=false;
        mainContainer.style.background.set(0,0,0,0);
        mainContainer.bounds.set(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        focused=mainContainer;
    }
    
    public static void update(float delta) {
        mainContainer.update(delta);
    }
    public static void render(OrthographicCamera camera) {
        if(GameServices.getSpriteBatch().isDrawing())GameServices.getSpriteBatch().end();
        if(mainContainer.countVisibleChildren()==0)return;
        
        postfx.render(engine.getRenderer().postfx.getBuffer().getCurrent(),null);
        GUI.blur=postfx.getBuffer().getCurrent().getColorBufferTexture();
        
        Gdx.gl.glEnable(GL30.GL_SCISSOR_TEST);
        
        GameServices.getSpriteBatch().setShader(null);
        GameServices.getSpriteBatch().setTransformMatrix(GameServices.getSpriteBatch().getTransformMatrix().idt());
        GameServices.getSpriteBatch().begin();
        //System.out.println("[");
        mainContainer.render(camera,GameServices.getSpriteBatch());
        //System.out.println("]");
        Gdx.gl.glDisable(GL30.GL_SCISSOR_TEST);
        
        Matrix4 trans=GameServices.getSpriteBatch().getTransformMatrix().idt();
        GameServices.getSpriteBatch().setTransformMatrix(trans);
    }

    public static void dispose() {
        postfx.dispose();
    }
    
    private static class GUIListener implements InputProcessor {
        private Container current,last;
        
        @Override
        public boolean keyDown(int keycode) {
            if(focused!=null) {
                focused.containerEventListener.onKeyDown(keycode);
            }
            return false;
        }
        @Override
        public boolean keyUp(int keycode) {
            if(focused!=null)
                focused.containerEventListener.onKeyUp(keycode);
            return false;
        }
        @Override
        public boolean keyTyped(char character) {
            if(focused!=null)
                focused.containerEventListener.onKeyTyped(character);
            return false;
        }
        @Override
        public boolean touchDown(int screenX,int screenY,int pointer,int button) {
            if(current!=null) {
                if(focused!=current&&focused!=null)focused.containerEventListener.onFocusLost();
                focused=current;
                current.containerEventListener.onFocus();
                current.containerEventListener.onButtonDown(button);
            }
            return false;
        }
        @Override
        public boolean touchUp(int screenX,int screenY,int pointer,int button) {
            if(current!=null) {
                current.containerEventListener.onButtonUp(button);
            }
            return false;
        }
        @Override
        public boolean touchDragged(int screenX,int screenY,int pointer) {
            if(current!=null)
                current.containerEventListener.onMouseDragged(screenX,screenY);
            return false;
        }
        @Override
        public boolean mouseMoved(int screenX,int screenY) {
            findCoords(screenX,screenY);
            if(current!=null)
                current.containerEventListener.onMouseMoved(screenX,screenY);
            return false;
        }
        @Override
        public boolean scrolled(int amount) {
            if(current!=null)
                current.containerEventListener.onScroll(amount);
            return false;
        }
        
        private void findCoords(int x,int y) {
            current=mainContainer.findFocusableContainer(x,Gdx.graphics.getHeight()-y);
            if(last!=current) {
                if(last!=null)last.containerEventListener.onHoverExit();
                if(current!=null)current.containerEventListener.onHoverEnter();
            }
            else if(current!=null)current.containerEventListener.onHover();
            last=current;
        }
    }
}
