package com.hakavo.textadventure;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.Vector2;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.rendering.postfx.*;

public class TextGameMode implements GameMode {
    @Override
    public void init(Engine engine) {
        engine.camera.setToOrtho(false,400,225);
        //engine.getRenderer().postfx.addFilter(new Blur(Vector2.X,1f/Gdx.graphics.getWidth()*2f,4));
        //engine.getRenderer().postfx.addFilter(new Blur(Vector2.Y,1f/Gdx.graphics.getHeight()*2f,4));
        //engine.getRenderer().postfx.addFilter(new ColorFilter(1.4f,1.23f,1f));
        engine.getRenderer().postfx.addFilter(new Noise(0.175f,6));
        //engine.getRenderer().postfx.addFilter(new CRT());
        GameObject text=new GameObject();
        text.addComponent(new Transform(25,210,0.5f,0.5f));
        text.addComponent(new TextRenderer("[WHITE]Welcome to <insert title here>!\n[#CCCCCC]I'll be your guide throughout the game.",Color.WHITE));
        //engine.getLevel().addGameObject(text);
        engine.getLevel().addGameObject(new Terminal());
    }
    @Override
    public void update(float delta) {
    }
    @Override
    public void renderGui(OrthographicCamera camera) {
    }
}
