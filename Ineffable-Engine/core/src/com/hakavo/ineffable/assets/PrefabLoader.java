package com.hakavo.ineffable.assets;
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import com.hakavo.ineffable.GameServices;
import javax.script.*;

public class PrefabLoader extends AssetLoader<Prefab> {
    private ScriptEngine scriptEngine;
    
    @Override
    public Prefab load(FileHandle fh,Object... params) {
        try {
            scriptEngine=new ScriptEngineManager().getEngineByName("nashorn");
            scriptEngine.eval(GameServices.scriptUtilsPath.readString());
            scriptEngine.eval(fh.readString());
            System.gc();
            return (Prefab)(((Invocable)scriptEngine).invokeFunction("getPrefab"));
        } catch(Exception exception) {
            exception.printStackTrace(System.err);
        }
        return null;
    }
}
