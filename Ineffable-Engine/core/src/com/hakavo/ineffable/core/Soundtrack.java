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
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

/**
 *
 * @author HakavoGames
 */
public class Soundtrack extends Hearable {
    public Music sound;
    
    public Soundtrack(Music sound, float volume, int layer) {
        super(volume, layer);
        this.sound = sound;
    }
    public Soundtrack(Music sound, float volume) {
        super(volume);
        this.sound = sound;
    }
    public Soundtrack(Music sound, int layer) {
        super(layer);
        this.sound = sound;
    }
    public Soundtrack(Music sound) {
        this.sound = sound;
    }
    
    public Soundtrack(FileHandle handle, float volume, int layer) {
        super(volume, layer);
        this.sound = Gdx.audio.newMusic(handle);
    }
    public Soundtrack(FileHandle handle, float volume) {
        super(volume);
        this.sound = Gdx.audio.newMusic(handle);
    }
    public Soundtrack(FileHandle handle, int layer) {
        super(layer);
        this.sound = Gdx.audio.newMusic(handle);
    }
    public Soundtrack(FileHandle handle) {
        this.sound = Gdx.audio.newMusic(handle);
    }
    
    public Soundtrack(String path, float volume, int layer) {
        super(volume, layer);
        this.sound = Gdx.audio.newMusic(Gdx.files.internal(path));
    }
    public Soundtrack(String path, float volume) {
        super(volume);
        this.sound = Gdx.audio.newMusic(Gdx.files.internal(path));
    }
    public Soundtrack(String path, int layer) {
        super(layer);
        this.sound = Gdx.audio.newMusic(Gdx.files.internal(path));
    }
    public Soundtrack(String path) {
        this.sound = Gdx.audio.newMusic(Gdx.files.internal(path));
    }
    
    public void play() {
        sound.play();
    }
    public void pause() {
        sound.pause();
    }
    public void stop() {
        sound.stop();
    }
    public void setPanLeft() {
        sound.setPan(-1, 1);
    }
    public void setPanRight() {
        sound.setPan(1, 1);
    }
    public void setPan(float location, float volume) {
        sound.setPan(location, volume);
    }
    public void setPosition(float position) {
        sound.setPosition(position);
    }
    public float getPosition() {
        return sound.getPosition();
    }
    public void setLooping(boolean loop) {
        sound.setLooping(loop);
    }
    public void setVolume(float volume) {
        this.volume = volume;
        sound.setVolume(volume);
    }
    public float getVolume() {
        return sound.getVolume();
    }
    public boolean isPlaying() {
        return sound.isPlaying();
    }
    public boolean isLooping() {
        return sound.isLooping();
    }
    public void setOnCompletitionListener(OnCompletionListener ol) {
        sound.setOnCompletionListener(ol);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void start() {
    }
}
