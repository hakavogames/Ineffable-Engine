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

package com.hakavo.ineffable;
import com.badlogic.gdx.utils.Array;

public class Properties
{
    public Array<Pair> elements=new Array<Pair>();
    public int getIndex(String key)
    {
        for(int i=0;i<elements.size;i++)
            if(elements.get(i).matchesKey(key))
                return i;
        return -1;
    }
    public Pair get(String key)
    {
        int i=getIndex(key);
        if(i==-1)return null;
        return elements.get(i);
    }
    public boolean remove(String key)
    {
        int i=getIndex(key);
        if(i==-1)return false;
        elements.removeIndex(i);
        return true;
    }
    public Properties set(Pair pair)
    {
        int i=getIndex(pair.key);
        if(i==-1)elements.add(pair);
        else elements.set(i,pair);
        return this;
    }
    public Properties set(String key,Object value)
    {
        return set(new Pair(key,value));
    }
}
