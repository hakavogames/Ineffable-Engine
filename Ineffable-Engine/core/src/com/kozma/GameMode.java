package com.kozma;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameMode
{
    public void init(Engine engine);
    public void update(float delta);
    public void render(OrthographicCamera camera);
}
