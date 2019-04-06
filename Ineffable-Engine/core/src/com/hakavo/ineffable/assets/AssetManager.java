package com.hakavo.ineffable.assets;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;

public final class AssetManager {
    public static ObjectMap<String,Object> assets;
    public static ObjectMap<String,AssetLoader> loaders;
    public static void init()
    {
        assets=new ObjectMap();
        loaders=new ObjectMap();
        
        loaders.put("texture",new TextureLoader());
        loaders.put("sound",new SoundLoader());
        loaders.put("music",new MusicLoader());
        loaders.put("shader",new ShaderLoader());
    }
    public static <T> T loadAsset(String format,FileHandle file,String name,Object... params)
    {
        if(assets.containsKey(name))return (T)assets.get(name);
        AssetLoader loader=loaders.get(format);
        T asset=(T)loader.load(file,params);
        assets.put(name,asset);
        return asset;
    }
    public static <T> T getAsset(String name)
    {
        T asset=(T)assets.get(name);
        return asset;
    }
}
