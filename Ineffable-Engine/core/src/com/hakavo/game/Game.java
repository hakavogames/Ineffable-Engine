package com.hakavo.game;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.hakavo.ineffable.Engine;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends ApplicationAdapter {
    Engine engine=new Engine();
    SpriteBatch sb;
    @Override
    public void create()
    {
        sb=new SpriteBatch();
        engine.init();
        engine.loadGameMode(new MenuGameMode());
    }
    @Override
    public void render()
    {
        engine.update(Gdx.graphics.getDeltaTime());
        engine.render();
    }
}
