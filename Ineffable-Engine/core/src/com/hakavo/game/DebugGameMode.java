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
import com.hakavo.game.mechanics.DialogueSystem.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.GameComponent.Copiable;

public class DebugGameMode implements GameMode {
    Map map;
    OrthographicCamera camera;
    static Engine engine;
    Tileset poses;
    
    public DebugGameMode() {
    }
    @Override
    public void init(Engine engine) {
        Tileset tileset=new Tileset(Gdx.files.internal("tileset.xml"));
        this.engine=engine;
        engine.camera=new OrthographicCamera();
        camera=engine.camera;
        camera.setToOrtho(false,400,225);
        camera.zoom=0.5f;
        camera.position.add(12f,12f,0);
        
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
        animationController.play("idle");
        
        Joint player=new Joint();
        player.name="player";
        player.addComponent(new Transform(camera.viewportWidth/2,camera.viewportHeight/2));
        player.addComponent(new SpriteRenderer(sprite));
        player.addComponent(animationController);
        player.getComponent(SpriteRenderer.class).layer=2;
        
        Dialogue greeting=new Dialogue("greeting","Hello, I'm a trashman.",4);
        Dialogue greeting2=new Dialogue("greeting2","People there call me Gelu.",3);
        Dialogue question=new Dialogue("question","Can you give me some money for cigarettes?",1.5f);
        Dialogue answer1=new Dialogue("answer-yes","Thanx kid",2) {
            @Override
            public void onDialogueComplete() {
                GameObject player=DebugGameMode.engine.getLevel().findGameObjectByName("player");
                player.addComponent(new GameComponent() {
                    Transform transform;
                    public void start() {
                        transform=getGameObject().getComponent(Transform.class);
                        getGameObject().getComponent(AnimationController.class).play("fart");
                    }
                    public void update(float delta) {
                        transform.matrix.translate(delta*50,0);
                    }
                });
            }
        };
        Dialogue answer2=new Dialogue("answer-no","Screw you",2) {
            @Override
            public void onDialogueComplete() {
                GameObject player=DebugGameMode.engine.getLevel().findGameObjectByName("player");
                player.getComponent(SpriteRenderer.class).color.set(1,0.2f,0,1);
            }
        };
        Dialogue threaten=new Dialogue("threaten","Tomorrow I'll get my brothers and hunt you down",4.5f) {
            @Override
            public void onDialogueComplete() {
                GameObject player=DebugGameMode.engine.getLevel().findGameObjectByName("player");
                player.addComponent(new GameComponent() {
                    Transform transform;
                    @Override
                    public void start() {
                        transform=getGameObject().getComponent(Transform.class);
                    }
                    @Override
                    public void update(float delta) {
                        transform.matrix.translate(16,16);
                        transform.matrix.scale(1f+delta*4.3f,1f+delta*4.3f);
                        transform.matrix.translate(-16,-16);
                    }
                });
            }
        };
        
        answer2.choices.add(new Choice("","threaten"));
        greeting.choices.add(new Choice("","greeting2"));
        greeting2.choices.add(new Choice("","question"));
        question.choices.add(new Choice("Here, take this 5$ note.","answer-yes"));
        question.choices.add(new Choice("No.","answer-no"));
        question.choices.add(new Choice("Teleport Gelu away","exit") {
            @Override
            public void onChoose() {
                GameObject player=DebugGameMode.engine.getLevel().findGameObjectByName("player");
                player.addComponent(new GameComponent() {
                    SpriteRenderer spriteRenderer;
                    public void start() {
                        spriteRenderer=getGameObject().getComponent(SpriteRenderer.class);
                    }
                    public void update(float delta) {
                        spriteRenderer.color.a-=delta*0.3f;
                        if(spriteRenderer.color.a<=0)
                            getGameObject().destroy();
                    }
                });
            }
        });
        
        DialogueSystem dialogueSystem=new DialogueSystem(true,clip1.frames.get(0).getRegionWidth()/2,0.3f,0.6f);
        dialogueSystem.dialogues.addAll(greeting,greeting2,question,answer1,answer2,threaten);
        
        GameObject text=new GameObject();
        text.addComponent(new Transform(0,40,1f,1f).setRelative(player.getComponent(Transform.class)));
        text.addComponent(new TextRenderer("",new Color(1,1,1,1)));
        text.addComponent(dialogueSystem);
        player.addGameObject(text);
        
        idle.play();
        engine.getLevel().addGameObject(player);
        dialogueSystem.setDialogue("greeting");
    }
    @Override
    public void update(float delta) {
        if(Gdx.input.isKeyJustPressed(Keys.R))
            engine.loadGameMode(new DebugGameMode());
    }
    @Override
    public void renderGui(OrthographicCamera camera) {
        BitmapFont font=GameServices.getFonts().get("pixeltype");
        font.setColor(1,1,1,1);
        font.draw(GameServices.getSpriteBatch(),"Press R to reset",15,25);
    }
}
