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
import com.badlogic.gdx.utils.*;
import com.hakavo.game.mechanics.*;
import com.hakavo.game.mechanics.DialogueSystem.Dialogue;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.GameComponent.Copiable;

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
        
        Joint player=new Joint();
        player.name="player";
        player.addComponent(new Transform(camera.viewportWidth/2,camera.viewportHeight/2));
        player.addComponent(new SpriteRenderer(sprite));
        player.addComponent(idle);
        player.getComponent(SpriteRenderer.class).layer=2;
        
        DialogueSystem dialogueSystem=new DialogueSystem(true,clip1.frames.get(0).getRegionWidth()/2,1);
        dialogueSystem.dialogues.addLast(new Dialogue("Hi, I'm Gelu.",2,false));
        dialogueSystem.dialogues.addLast(new Dialogue("I am a trashman.",2,false));
        dialogueSystem.dialogues.addLast(new Dialogue("I like trains",4,false));
        
        GameObject text=new GameObject();
        text.addComponent(new Transform(0,50,0.3f,0.3f).setRelative(player.getComponent(Transform.class)));
        text.addComponent(new TextRenderer("",new Color(1,1,1,1)));
        text.addComponent(dialogueSystem);
        player.addGameObject(text);
        
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
