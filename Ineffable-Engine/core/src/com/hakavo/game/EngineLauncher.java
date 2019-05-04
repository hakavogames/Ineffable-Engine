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
import com.hakavo.ineffable.assets.*;

public class EngineLauncher extends ApplicationAdapter {
    public Engine engine=new Engine();
	
    @Override
    public void create()
    {
        engine.init();
        engine.getRenderer().postfx.addFilter(new Copy());
        engine.getRenderer().postfx.addFilter(new Copy());
        // TODO make GUI blur work without adding two Copy filters to the post processor
	engine.loadGameMode(new BlankGameMode());
    }
    @Override
    public void render()
    {
        engine.update(Gdx.graphics.getDeltaTime());
        engine.render();
    }
}
