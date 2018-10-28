package com.hakavo;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;

public class Game extends ApplicationAdapter {
    Engine engine=new Engine();
    SpriteBatch sb;
    @Override
    public void create()
    {
        sb=new SpriteBatch();
        engine.gameMode=new ExampleGameMode();
        engine.init();
    }
    @Override
    public void render()
    {
        engine.update(Gdx.graphics.getDeltaTime());
        engine.render();
    }
}
