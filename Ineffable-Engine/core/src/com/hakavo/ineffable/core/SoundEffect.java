/*
 * Copyright 2018 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hakavo.ineffable.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

/**
 *
 * @author HakavoGames
 */
public class SoundEffect extends Hearable {
    public Sound sound;
    private long[] ids = new long[50];
    private byte lastEmptyID = 0;
    
    public SoundEffect(Sound sound, float volume, int layer) {
        super(volume, layer);
        this.sound = sound;
    }
    public SoundEffect(Sound sound, float volume) {
        super(volume);
        this.sound = sound;
    }
    public SoundEffect(Sound sound, int layer) {
        super(layer);
        this.sound = sound;
    }
    public SoundEffect(Sound sound) {
        this.sound = sound;
    }
    
    public SoundEffect(FileHandle handle, float volume, int layer) {
        super(volume, layer);
        this.sound = Gdx.audio.newSound(handle);
    }
    public SoundEffect(FileHandle handle, float volume) {
        super(volume);
        this.sound = Gdx.audio.newSound(handle);
    }
    public SoundEffect(FileHandle handle, int layer) {
        super(layer);
        this.sound = Gdx.audio.newSound(handle);
    }
    public SoundEffect(FileHandle handle) {
        this.sound = Gdx.audio.newSound(handle);
    }
    
    public SoundEffect(String path, float volume, int layer) {
        super(volume, layer);
        this.sound = Gdx.audio.newSound(Gdx.files.internal(path));
    }
    public SoundEffect(String path, float volume) {
        super(volume);
        this.sound = Gdx.audio.newSound(Gdx.files.internal(path));
    }
    public SoundEffect(String path, int layer) {
        super(layer);
        this.sound = Gdx.audio.newSound(Gdx.files.internal(path));
    }
    public SoundEffect(String path) {
        this.sound = Gdx.audio.newSound(Gdx.files.internal(path));
    }
    
    public void play() {
        ids[lastEmptyID] = sound.play(this.volume);
        lastEmptyID++;
    }
    public void pause() {
        if(lastEmptyID > 0)sound.pause(ids[lastEmptyID - 1]);
    }
    public void pause(int IDIndex) {
        if(IDIndex >= 0)sound.pause(ids[IDIndex]);
    }
    public void resume() {
        if(lastEmptyID > 0)sound.resume(ids[lastEmptyID - 1]);
    }
    public void resume(int IDIndex) {
        if(IDIndex >= 0)sound.resume(ids[IDIndex]);
    }
    public void stop() {
        if(lastEmptyID > 0) {
            sound.stop(ids[lastEmptyID - 1]);
            ids[lastEmptyID - 1] = 0;
            lastEmptyID--;
        }
    }
    public void stop(int IDIndex) {
        if(IDIndex >= 0 && IDIndex < lastEmptyID) {
            sound.stop(ids[IDIndex]);
            for(int i = IDIndex; i < lastEmptyID - 1; i++) {
                ids[i] = ids[i + 1];
            }
            ids[lastEmptyID - 1] = 0;
            lastEmptyID--;
        }
        
    }
    public long getID() {
        if(lastEmptyID > 0)return ids[lastEmptyID - 1];
        return 0;
    }
    public int getIndex() {
        return (lastEmptyID - 1);
    }
    public int getIndexByID(long ID) {
        for(int i = 0; i < lastEmptyID; i++) {
            if(ids[i] == ID)return i;
        }
        return -1;
    }
    public long getIDByIndex(int IDIndex) {
        if(IDIndex >= 0)return ids[IDIndex];
        return 0;
    }
    public void setPanLeft() {
        if(lastEmptyID > 0)sound.setPan(ids[lastEmptyID - 1], -1, this.volume);
    }
    public void setPanLeft(int IDIndex) {
        if(IDIndex >= 0)sound.setPan(ids[IDIndex], -1, this.volume);
    }
    public void setPanRight() {
        if(lastEmptyID > 0)sound.setPan(ids[lastEmptyID - 1], 1, this.volume);
    }
    public void setPanRight(int IDIndex) {
        if(IDIndex >= 0)sound.setPan(ids[IDIndex], 1, this.volume);
    }
    public void setPan(float position, float volume) {
        if(lastEmptyID > 0)sound.setPan(ids[lastEmptyID - 1], position, volume);
    }
    public void setPan(float position, float volume, int IDIndex) {
        if(IDIndex >= 0)sound.setPan(ids[IDIndex], position, volume);
    }
    public void setPitch(float factor) {
        if(lastEmptyID > 0)sound.setPitch(ids[lastEmptyID - 1], factor);
    }
    public void setPitch(float factor, int IDIndex) {
        if(IDIndex >= 0)sound.setPitch(ids[IDIndex], factor);
    }
    public void setLooping(boolean loop) {
        if(lastEmptyID > 0)sound.setLooping(ids[lastEmptyID - 1], loop);
    }
    public void setLooping(boolean loop, int IDIndex) {
        if(IDIndex >= 0)sound.setLooping(ids[IDIndex], loop);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void start() {
    }
}
