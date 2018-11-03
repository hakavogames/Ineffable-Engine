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

package com.hakavo.game;
import com.hakavo.ineffable.core.collision.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.gameobjects.Map;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.ineffable.Engine;
import com.hakavo.ineffable.GameMode;
import com.hakavo.ineffable.GameServices;
import com.hakavo.ineffable.Tileset;
import com.hakavo.ineffable.core.GameComponent.Copiable;
import com.hakavo.ineffable.core.ParticleSystem.Particle;
import java.io.IOException;
import java.util.logging.*;

public class DebugGameMode implements GameMode {
    Map map;
    OrthographicCamera camera;
    Engine engine;
    Tileset poses;
    
    public DebugGameMode() {
    }
    @Override
    public void init(Engine engine) {
        Tileset tileset=new Tileset(Gdx.files.internal("tileset.xml"));
        this.engine=engine;
        camera=engine.camera;
        camera.setToOrtho(false,400,225);
        
        Sprite2D sprite=new Sprite2D();
        
        poses=new Tileset(Gdx.files.internal("Scavengers_SpriteSheet.png"),32);
        AnimationClip clip1=new AnimationClip();
        for(int i=0;i<6;i++)clip1.frames.add(poses.tiles.get(i).createTextureRegion());
        clip1.duration=1f;
        clip1.loop=true;
        
        AnimationClip clip2=new AnimationClip();
        for(int i=46;i<48;i++)clip2.frames.add(poses.tiles.get(i).createTextureRegion());
        clip2.duration=0.6f;
        clip2.loop=true;
        
        Animation idle=new Animation("idle",clip1,sprite);
        Animation fart=new Animation("fart",clip2,sprite);
        AnimationController animationController=new AnimationController(sprite,idle,fart);
        
        GameObject player=new GameObject();
        player.name="player";
        player.addComponent(new Transform(camera.viewportWidth/2,camera.viewportHeight/2));
        player.addComponent(new SpriteRenderer(sprite));
        player.addComponent(idle);
        player.getComponent(SpriteRenderer.class).layer=2;
        
        idle.play();
        engine.getLevel().addGameObject(player);
    }
    @Override
    public void update(float delta) {
        GameObject player=engine.getLevel().getGameObjectByName("player");
        player.getComponent(Animation.class).update(delta);
    }
    @Override
    public void renderGui(OrthographicCamera camera) {
    }
}
