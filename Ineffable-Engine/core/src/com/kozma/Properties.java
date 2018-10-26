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
package com.kozma;
import com.badlogic.gdx.utils.Array;
/**
 * The Properties class holds an Array of Pairs.
 * @author HakavoGames
 */
public class Properties
{
    public Array<Pair> elements=new Array<Pair>();
    /**
     * @param key The key for which to search in the elements of the Properties object.
     * @return The index of the Pair with the specified key if it exists. Else (it) will return -1.
     */
    public int getIndex(String key)
    {
        for(int i=0;i<elements.size;i++)
            if(elements.get(i).matchesKey(key))
                return i;
        return -1;
    }
    /**
     * @param key The key for which to search in the elements of the Properties object.
     * @return The Pair with the matching key if it exists. Else (it) will return a null object.
     */
    public Pair get(String key)
    {
        int i=getIndex(key);
        if(i==-1)return null;
        return elements.get(i);
    }
    /**
     * Removes a Pair from the elements Array of the Properties object if it exists.
     * @param key The key or which to search in the elements of the Properties object.
     * @return True if the Pair was successfully removed. False if it wasn't in the elements Array at all.
     */
    public boolean remove(String key)
    {
        int i=getIndex(key);
        if(i==-1)return false;
        elements.removeIndex(i);
        return true;
    }
    /**
     * Sets the Pair from the elements Array with the index of the pair parameter to the pair parameter if it exists. Else (it) will add it at the end of the Array.
     * @param pair The pair whose key to search for in the elements Array and replace the found one with it if exists.
     * @return The new Properties object with the new Pair in it.
     */
    public Properties set(Pair pair)
    {
        int i=getIndex(pair.key);
        if(i==-1)elements.add(pair);
        else elements.set(i,pair);
        return this;
    }
    /**
     * Sets the object of the Pair with the key key to the object value.
     * @param key The key for which to search in the elements of the Array in the Properties object.
     * @param value The object to set the Pair with the key key to if it exists.
     * @return The new Properties object with the new Pair value in it.
     */
    public Properties set(String key,Object value)
    {
        return set(new Pair(key,value));
    }
}
