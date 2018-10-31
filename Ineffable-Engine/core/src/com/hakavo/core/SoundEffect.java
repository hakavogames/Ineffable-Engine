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
package com.hakavo.core;

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
        sound.pause(ids[lastEmptyID - 1]);
    }
    public void pause(int IDIndex) {
        sound.pause(ids[IDIndex]);
    }
    public void resume() {
        sound.resume(ids[lastEmptyID - 1]);
    }
    public void resume(int IDIndex) {
        sound.resume(ids[IDIndex]);
    }
    public void stop() {
        sound.stop(ids[lastEmptyID - 1]);
    }
    public void stop(int IDIndex) {
        sound.stop(ids[IDIndex]);
    }
    public long getID() {
        return ids[lastEmptyID - 1];
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
        return ids[IDIndex];
    }
    public void setPanLeft() {
        sound.setPan(ids[lastEmptyID - 1], -1, this.volume);
    }
    public void setPanLeft(int IDIndex) {
        sound.setPan(ids[IDIndex], -1, this.volume);
    }
    public void setPanRight() {
        sound.setPan(ids[lastEmptyID - 1], 1, this.volume);
    }
    public void setPanRight(int IDIndex) {
        sound.setPan(ids[IDIndex], 1, this.volume);
    }
    public void setPan(float position, float volume) {
        sound.setPan(ids[lastEmptyID - 1], position, volume);
    }
    public void setPan(float position, float volume, int IDIndex) {
        sound.setPan(ids[IDIndex], position, volume);
    }
    public void setPitch(float factor) {
        sound.setPitch(ids[lastEmptyID - 1], factor);
    }
    public void setPitch(float factor, int IDIndex) {
        sound.setPitch(ids[IDIndex], factor);
    }
    public void setLooping(boolean loop) {
        sound.setLooping(ids[lastEmptyID - 1], loop);
    }
    public void setLooping(boolean loop, int IDIndex) {
        sound.setLooping(ids[IDIndex], loop);
    }

    @Override
    public void update(float delta) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void start() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
