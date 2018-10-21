package com.kozma.core;

public abstract class GameComponent
{
    protected GameObject gameObject;
    public String name;
    
    public abstract void update(float delta);
    public abstract void start();
    public final void setGameObject(GameObject gameObject) {this.gameObject=gameObject;}
    public final GameObject getGameObject() {return gameObject;}
}
