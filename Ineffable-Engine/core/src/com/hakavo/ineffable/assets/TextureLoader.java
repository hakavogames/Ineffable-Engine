package com.hakavo.ineffable.assets;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class TextureLoader extends AssetLoader<Texture> {
    @Override
    public Texture load(FileHandle fh,Object... params) {
        Texture texture=new Texture(fh,true);
        if(params.length==0||params[0].equals(Boolean.TRUE))
            texture.setFilter(Texture.TextureFilter.MipMapLinearLinear,Texture.TextureFilter.Linear);
        else texture.setFilter(Texture.TextureFilter.Nearest,Texture.TextureFilter.Nearest);
        return texture;
    }
}
