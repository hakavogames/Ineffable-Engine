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

package com.hakavo.core;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;

public class GameObject
{
    private boolean initialized=false;
    protected boolean dead=false;
    public String name="default";
    public final Array<GameComponent> components=new Array<GameComponent>();
    
    public final <T extends GameComponent> T getComponent(Class<T> type)
    {
        for(int i=0;i<components.size;i++)
            if(type.isAssignableFrom(components.get(i).getClass()))
                return (T)components.get(i);
        return null;
    }
    public final <T extends GameComponent> T getComponentByName(String name)
    {
        for(int i=0;i<components.size;i++)
            if(components.get(i).name.equals(name))
                return (T)components.get(i);
        return null;
    }
    public final <T extends GameComponent> Array<T> getComponents(Class<T> type)
    {
        Array<T> array=new Array<T>();
        for(int i=0;i<components.size;i++)
        {
            if(type.isAssignableFrom(components.get(i).getClass()))
                array.add((T)components.get(i));
        }
        return array;
    }
    public final void removeComponent(Class type,boolean allInstances)
    {
        for(int i=0;i<components.size;i++)
            if(type.isAssignableFrom(components.get(i).getClass()))
            {
                components.removeIndex(i);
                if(!allInstances)return;
            }
    }
    public final void removeComponentByName(String name,boolean allInstances)
    {
        for(int i=0;i<components.size;i++)
            if(components.get(i).name.equals(name))
            {
                components.removeIndex(i);
                if(!allInstances)return;
            }
    }
    public final void setComponent(Class type,GameComponent newComponent)
    {
        for(int i=0;i<components.size;i++)
            if(type.isAssignableFrom(components.get(i).getClass()))
                components.set(i,newComponent);
    }
    public final void addComponent(GameComponent component)
    {
        this.components.add(component);
        component.setGameObject(this);
    }
    public final void addComponents(GameComponent... components)
    {
        for(GameComponent gameComponent : components)
            addComponent(gameComponent);
    }
    
    public void start()
    {
        if(!initialized)
        {
            initialized=true;
            for(int i=0;i<components.size;i++)
                components.get(i).start();
        }
    }
    public void update(float delta)
    {
        for(int i=0;i<components.size;i++)
            components.get(i).update(delta);
    }
    public void render(OrthographicCamera camera)
    {
        Array<Renderable> array=getComponents(Renderable.class);
        for(int i=0;i<array.size;i++)
        {
            if(array.get(i).visible==true)
                array.get(i).render(camera);
        }
    }
    public final boolean isInitialized() {
        return initialized;
    }
    
    public final boolean isDead() {
        return dead;
    }
    public final void kill() {
        dead=true;
    }
}
