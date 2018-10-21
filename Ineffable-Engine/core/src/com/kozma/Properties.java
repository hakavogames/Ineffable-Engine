package com.kozma;
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
