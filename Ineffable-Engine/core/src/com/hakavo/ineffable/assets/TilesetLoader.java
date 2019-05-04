package com.hakavo.ineffable.assets;
import com.badlogic.gdx.files.FileHandle;
import com.hakavo.ineffable.*;

public class TilesetLoader extends AssetLoader<Tileset> {
    @Override
    public Tileset load(FileHandle fh,Object... params) {
        if(params.length==1&&params[0] instanceof Integer)
            return new Tileset(fh,(Integer)params[0]);
        return new Tileset(fh);
    }
}
