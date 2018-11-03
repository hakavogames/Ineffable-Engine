package com.hakavo.ineffable.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class SoundLoader extends AssetLoader<Sound> {
    @Override
    public Sound load(FileHandle fh) {
        Sound sound=Gdx.audio.newSound(fh);
        return sound;
    }
}
