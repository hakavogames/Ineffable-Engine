package com.hakavo.ineffable.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class ShaderLoader extends AssetLoader<ShaderProgram> {

    @Override
    public ShaderProgram load(FileHandle fh,Object... params) {
        ShaderProgram shader=new ShaderProgram(Gdx.files.internal(fh.pathWithoutExtension()+".vert"),
                                               Gdx.files.internal(fh.pathWithoutExtension()+".frag"));
        if(!shader.isCompiled())
            throw new GdxRuntimeException(shader.getLog());
        return shader;
    }
}
