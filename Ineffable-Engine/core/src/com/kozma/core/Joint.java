package com.kozma.core;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.*;

public class Joint extends GameObject
{
    public Array<GameObject> gameObjects=new Array<GameObject>();
    public <T extends GameObject> T getGameObject(Class type)
    {
        for(int i=0;i<gameObjects.size;i++)
            if(gameObjects.get(i).getClass().isAssignableFrom(type))
                return (T)gameObjects.get(i);
        return null;
    }
    public <T extends GameObject> T getGameObjectByName(String name)
    {
        for(int i=0;i<gameObjects.size;i++)
            if(gameObjects.get(i).name.equals(name))
                return (T)gameObjects.get(i);
        return null;
    }
    public <T extends GameObject> Array<T> getGameObjects(Class<T> type)
    {
        Array<T> array=new Array<T>();
        for(int i=0;i<gameObjects.size;i++)
            if(gameObjects.get(i).getClass().isAssignableFrom(type))
                array.add((T)gameObjects.get(i));
        return array;
    }
    public Array<GameObject> getGameObjects()
    {
        Array<GameObject> array=new Array<GameObject>();
        for(int i=0;i<gameObjects.size;i++)
            array.addAll(gameObjects.get(i));
        return array;
    }
    public Array<GameObject> getAllGameObjects()
    {
        Array<GameObject> gameObjects=new Array<GameObject>();
        for(int i=0;i<this.gameObjects.size;i++)
        {
            gameObjects.add(this.gameObjects.get(i));
            if(gameObjects.get(i) instanceof Joint)
                gameObjects.addAll(((Joint)this.gameObjects.get(i)).getAllGameObjects());
        }
        return gameObjects;
    }
    public void removeGameObject(Class type,boolean allInstances)
    {
        for(int i=0;i<gameObjects.size;i++)
            if(gameObjects.get(i).getClass().isAssignableFrom(type))
            {
                gameObjects.removeIndex(i);
                if(!allInstances)return;
            }
    }
    public void removeGameObjectByName(String name,boolean allInstances)
    {
        for(int i=0;i<gameObjects.size;i++)
            if(gameObjects.get(i).name.equals(name))
            {
                gameObjects.removeIndex(i);
                if(!allInstances)return;
            }
    }
    public final void setGameObject(Class type,GameObject newGameObject)
    {
        for(int i=0;i<gameObjects.size;i++)
            if(gameObjects.get(i).getClass().isAssignableFrom(type))
                gameObjects.set(i,newGameObject);
    }
    public final void addGameObject(GameObject toAdd)
    {
        gameObjects.add(toAdd);
        toAdd.start();
    }
    
    @Override
    public void start()
    {
        for(int i=0;i<gameObjects.size;i++)
            gameObjects.get(i).start();
    }
    @Override
    public void update(float delta)
    {
        for(int i=0;i<gameObjects.size;i++)
            gameObjects.get(i).update(delta);
    }
    @Override
    public void render(OrthographicCamera camera)
    {
        for(int i=0;i<gameObjects.size;i++)
            gameObjects.get(i).render(camera);
    }
}
