package com.hakavo.ineffable.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public class MusicLoader extends AssetLoader<Music> {
    @Override
    public Music load(FileHandle fh) {
        Music music=Gdx.audio.newMusic(fh);
        return music;
    }
}
