package com.hakavo.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.assets.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.ui.*;

public class BlankGameMode implements GameMode {
    
    @Override
    public void init(Engine engine) {
        Joint level=engine.getLevel();
        AssetManager.loadAsset("texture",Gdx.files.internal("sprites/pattern.png"),"pattern",true);
        GameServices.getCamera().setToOrtho(false,1f,((float)Gdx.graphics.getHeight())/Gdx.graphics.getWidth());
        GameObject background=new GameObject("background",new Transform(0,0,0.00025f,0.00025f));
        background.addComponent(new TiledBackground(new Sprite2D("pattern")));
        level.addGameObject(background);
        
        Container main=GUI.mainContainer;
        Window window=new Window("I am just a window and I'm doing my job",true,false,true);
        window.bounds.setSize(640,360);
        Label label=new Label("Welcome to Ineffable Engine. Enjoy!\n"+
                              "Here are some of the latest changes:\n\n"+
                              "+ An alpha-state GUI system which will face some major updates\n"+
                              "+ Prefab System using JavsScript\n"+
                              "+ Asset Bundle support\n"+
                              "[YELLOW]- Hopefully the present font glitch will be fixed in the next update.");
        label.bounds.setPosition(320-label.bounds.width/2,180-label.bounds.height/2);
        TextButton button=new TextButton("Close",8);
        button.bounds.setPosition(25,20);
        button.addEventListener(new EventListener() {
            @Override
            public void onButtonUp(int button) {
                Gdx.app.exit();
            }
        });
        window.add(label,button);
        main.add(window);
    }
    @Override
    public void update(float delta) {
        GameServices.getCamera().position.add(0.0425f*delta,0.025f*delta,0);
    }
    @Override
    public void renderGui(OrthographicCamera camera) {
    }
}
