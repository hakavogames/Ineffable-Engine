package com.kozma.core;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.kozma.GameServices;

public abstract class Renderable extends GameComponent
{
    public int layer=0;
    public boolean visible=true;
    public void prepareRendering() {
    }
    public abstract void render(OrthographicCamera camera);
}
