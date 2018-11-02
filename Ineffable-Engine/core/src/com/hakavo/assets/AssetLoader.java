package com.hakavo.assets;
import com.badlogic.gdx.files.*;

public abstract class AssetLoader<T> {
    public abstract T load(FileHandle fh);
}
