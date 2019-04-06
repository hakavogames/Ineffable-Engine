package com.hakavo.game;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.maps.*;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.math.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.rendering.postfx.*;
import com.hakavo.textadventure.TextGameMode;

public class Game extends ApplicationAdapter {
    Engine engine=new Engine();
    @Override
    public void create()
    {
        engine.init();
        engine.getRenderer().postfx.addFilter(new Copy());
        engine.loadGameMode(new PrisonGameMode());
    }
    @Override
    public void render()
    {
        engine.update(Gdx.graphics.getDeltaTime());
        engine.render();
    }
}
