package com.hakavo.game.survival;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.assets.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.*;
import com.hakavo.ineffable.rendering.postfx.effects.*;
import com.hakavo.ineffable.rendering.postfx.filters.*;
import com.hakavo.ineffable.ui.*;
import com.hakavo.ineffable.utils.*;

public class SurvivalMenuGameMode implements GameMode {
    protected Engine engine;
    private float target,current;
    Curves curves;
    
    @Override
    public void init(Engine engine) {
        this.engine=engine;
        //GameServices.getFonts().getValueAt(0).getData().setScale(0.5f);
        Label.defaultFont="pixeltype";
        GameServices.loadScript(Gdx.files.internal("scripts/load_assets.js"));
        AssetManager.loadAsset("prefab",Gdx.files.internal("scripts/prefabs/mountains_background.js"),"background");
        float height=160;
        float width=(float)Gdx.graphics.getWidth()/Gdx.graphics.getHeight()*height;
        engine.camera.setToOrtho(false,width,height);
        engine.camera.position.x=4000;
        
        engine.getRenderer().postfx.effects.add(new SimpleEffect(curves=new Curves(1.1f,1f,1.3f,1)));
        engine.getRenderer().postfx.effects.add(new Bloom(40f,0.7f));
        
        engine.getLevel().addGameObject(AssetManager.getAsset("background",Prefab.class).newInstance());
        
        Window window=new Window("Color curves",true,false);
        window.style.background.set(0,0,0,0.5f);
        window.bounds.setSize(320,240);
        Slider red=new Slider(false,300,15,0.2f,2f);
        Slider green=new Slider(false,300,15,0.2f,2f);
        Slider blue=new Slider(false,300,15,0.2f,2f);
        Slider saturation=new Slider(false,300,15,0.2f,2f);
        red.scrollPercent(50);
        green.scrollPercent(50);
        blue.scrollPercent(50);
        blue.bounds.setPosition(5,5);
        green.bounds.setPosition(5,30);
        red.bounds.setPosition(5,55);
        saturation.bounds.setPosition(5,80);
        
        red.listener=new SliderListener() {
            @Override
            public void onChange(float newValue) {
                curves.powRed=newValue;
            }
        };
        green.listener=new SliderListener() {
            @Override
            public void onChange(float newValue) {
                curves.powGreen=newValue;
            }
        };
        blue.listener=new SliderListener() {
            @Override
            public void onChange(float newValue) {
                curves.powBlue=newValue;
            }
        };
        saturation.listener=new SliderListener() {
            @Override
            public void onChange(float newValue) {
                curves.saturation=newValue;
            }
        };
        
        Label label=new Label();
        label.bounds.set(5,120,320,80);
        label.autoSize=false;
        window.add(red,green,blue,saturation,label);
        GUI.mainContainer.add(window);
        //GUI.mainContainer.style.set(new Color(1,1,1,1),new Color(0.5f,0.5f,0.5f,1f),true);
        
        GameServices.timerManager.scheduleTimer("curves-update",new Timer(new TimedTask() {
            @Override
            public void performTask(Engine engine) {
                GUI.mainContainer.get(Window.class).get(Label.class).setText(
                    "Red: "+Math.round(curves.powRed*100)/100f+
                    "\nGreen: "+Math.round(curves.powGreen*100)/100f+
                    "\nBlue: "+Math.round(curves.powBlue*100)/100f+
                    "\nSaturation: "+Math.round(curves.saturation*100)/100f
                );
            }
        }).setInterval(0f));
    }
    @Override
    public void update(float delta) {
        float f=(float)Gdx.graphics.getWidth()/20000f;
        target-=Gdx.input.getDeltaX()*f;
        current+=(target-current)*0.1f;
        target=MathUtils.clamp(target,-100,100);
        current=MathUtils.clamp(current,-100,100);
        engine.getLevel().getGameObjectByName("background").getComponent(Transform.class).matrix.setToTranslation(current,0);
        //engine.camera.position.x+=Gdx.input.getDeltaX()*f;
    }
    @Override
    public void renderGui(OrthographicCamera camera) {
        BitmapFont font=GameServices.getFonts().getValueAt(0);
        SpriteBatch sb=GameServices.getSpriteBatch();
        
        font.setColor(1,1,1,1);
        font.getData().setScale(0.5f);
        font.draw(sb,"Powered by Ineffable Engine v1.0b",5,10);
        font.getData().setScale(1f);
    }
    public static class MenuEntry extends GameObject {
        public MenuEntry(Transform transform,String text,CollisionAdapter collAdapter) {
            TextRenderer tr=new TextRenderer(text,Color.WHITE);
            GlyphLayout glyphLayout=new GlyphLayout();
            glyphLayout.setText(tr.font,text);
            BoxCollider collider=new BoxCollider(0,-glyphLayout.height,glyphLayout.width,glyphLayout.height);
            collider.tags.set(0,-1);
            collider.tags.add(0);
            collider.ignoreTags.add(0);
            collider.setCollisionAdapter(collAdapter);
            
            addComponent(transform);
            addComponent(tr);
            addComponent(collider);
            
            addComponent(new GameComponent() {
                protected Transform trans;
                protected BoxCollider coll;
                private Vector2 scale=new Vector2(),pos=new Vector2();
                @Override
                public void start() {
                    trans=gameObject.getComponent(Transform.class);
                    coll=gameObject.getComponent(BoxCollider.class);
                    trans.getScale(scale);
                    trans.getPosition(pos);
                }
                @Override
                public void update(float delta) {
                    Vector3 camPos=GameServices.getCamera().position;
                    Vector2 foo=trans.matrix.getTranslation(Pools.obtain(Vector2.class)).scl(-1);
                    trans.matrix.setToTranslation(pos.x+camPos.x,pos.y+camPos.y);
                    trans.matrix.scale(scale);
                    Pools.free(foo);
                }
            });
        }
    }
}
