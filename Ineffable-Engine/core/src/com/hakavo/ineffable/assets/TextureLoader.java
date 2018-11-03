package com.hakavo.ineffable.assets;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;

public class TextureLoader extends AssetLoader<Texture> {
    @Override
    public Texture load(FileHandle fh) {
        Texture texture=new Texture(fh,true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear,Texture.TextureFilter.Linear);
        return texture;
    }
}
