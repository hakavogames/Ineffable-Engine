/*
 * Copyright 2018 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hakavo.ineffable.core;
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
    public <T extends GameObject> T findGameObjectByName(String name)
    {
        for(GameObject gameObject : getAllGameObjects())
            if(gameObject.name.equals(name))
                return (T)gameObject;
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
        return getAllGameObjects(new Array<GameObject>());
    }
    public Array<GameObject> getAllGameObjects(Array<GameObject> gameObjects)
    {
        gameObjects.add(this);
        for(int i=0;i<this.gameObjects.size;i++)
        {
            gameObjects.add(this.gameObjects.get(i));
            if(this.gameObjects.get(i) instanceof Joint)
                ((Joint)this.gameObjects.get(i)).getAllGameObjects(gameObjects);
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
        toAdd.parent=this;
        toAdd.start();
    }
    
    @Override
    public void start()
    {
        super.start();
        for(int i=0;i<gameObjects.size;i++)
            gameObjects.get(i).start();
    }
    @Override
    public void update(float delta)
    {
        super.update(delta);
        for(int i=0;i<gameObjects.size;i++)
        {
            if(gameObjects.get(i).isDestroyed())
                gameObjects.removeIndex(i);
            else gameObjects.get(i).update(delta);
        }
    }
    @Override
    public void render(OrthographicCamera camera)
    {
        super.render(camera);
        for(int i=0;i<gameObjects.size;i++)
            gameObjects.get(i).render(camera);
    }
    @Override
    public GameObject cpy() {
        Joint joint=new Joint();
        joint.name=this.name;
        joint.parent=this.parent;
        for(int i=0;i<this.components.size;i++)
            if(this.components.get(i) instanceof GameComponent.Copiable)
                joint.addComponent(((GameComponent.Copiable)this.components.get(i)).cpy());
        for(int i=0;i<this.gameObjects.size;i++)
            joint.addGameObject(this.gameObjects.get(i).cpy());
        return joint;
    }
    @Override
    public Joint getLevel() {
        if(parent==null)return this;
        return parent.getParent();
    }
    @Override
    public void destroy() {
        if(destroyed)return;
        super.destroy();
        for(int i=0;i<this.gameObjects.size;i++)
            this.gameObjects.get(i).destroy();
        this.gameObjects.clear();
    }
}
