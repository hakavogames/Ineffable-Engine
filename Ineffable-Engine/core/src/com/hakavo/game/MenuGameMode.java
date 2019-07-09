package com.hakavo.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.ui.*;
import com.hakavo.game.ui.*;

public class MenuGameMode implements GameMode {
    private Engine engine;
    private TextField file;
    
    @Override
    public void init(Engine gameEngine) {
        this.engine=gameEngine;
        Label.defaultFont="pixeltype";
        /*Link link=new Link("Survival GameMode",new LinkListener() {
            @Override
            public void onClick() {
                engine.loadGameMode(new SurvivalGameMode());
            }
        });
        link.setScale(4f);
        link.style.foreground.set(1,0.7f,0.5f,1f);
        GUI.mainContainer.add(link);
        link.center();*/
        
        Window window=new Window("JavaScript",true,false);
        window.bounds.setSize(580,70);
        file=new TextField(32,"Script path");
        file.input="server.js";
        file.bounds.setPosition(5,5);
        
        Link load=new Link("Load",new LinkListener() {
            @Override
            public void onClick() {
                GameServices.loadScript(Gdx.files.internal(file.getText()));
            }
        });
        load.bounds.setPosition(500,5);
        
        window.add(file,load);
        GUI.mainContainer.add(window);
        
        RadioGroup radio=new RadioGroup();
        Radio a=new Radio("Easy");
        Radio b=new Radio("Medium");
        Radio c=new Radio("Hard");
        a.bounds.setPosition(500,450);
        b.bounds.setPosition(500,430);
        c.bounds.setPosition(500,410);
        radio.add(a,b,c);
        radio.select(0);
        GUI.mainContainer.add(a,b,c);
    }
    @Override
    public void update(float delta) {
    }
    @Override
    public void renderGui(OrthographicCamera camera) {
    }
}
