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

package com.hakavo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.hakavo.core.Animation;
import com.hakavo.core.AnimationClip;
import com.hakavo.core.AnimationController;
import com.hakavo.core.Joint;
import com.hakavo.core.Sprite2D;
import com.hakavo.core.SpriteRenderer;
import com.hakavo.core.Transform;
import com.hakavo.gameobjects.Map;

/**
 * This is a GameMode Example which you can use to learn to use the Ineffable Engine.
 * @author HakavoGames
 */
public class ExampleGameMode implements GameMode {
    
    Map map;
    OrthographicCamera camera;
    Engine engine;
    
    @Override
    public void init(Engine engine) {
        /*
        Initialize engine
        Neccesary in order to use this engine
        */
        this.engine = engine;
        //Initialize camera
        camera = engine.camera;
        /*
        Tileset are initialized like this
        
        "tileset" is the name for your tileset. It can be whatever you want
        
        The internal file I used ("tileset.xml") is an xml file that defines your tileset
        */
        Tileset tileset = new Tileset(Gdx.files.internal("tileset.xml"));
        /*
        Here is another method to make a new Tileset if you don't have an .xml file
        
        It is almost the same but rather than giving the .xml file as argument I gave the .png file
        that contained the tileset itself and a size for each tile
        
        NOTE THAT TILES MUST BE SQUARES!
        
        Although this method looks much more simple, we recomend using an .xml file as you can
        manipulate the tiles much better such as making them collide
        */
        Tileset posses = new Tileset(Gdx.files.internal("Scavangers_SpriteSheet.png"), 32);
        /*
        You can make a sprite with a texture from your tileset like this
        
        Here I made a sprite with the texture of the wall tile (defined in the .xml file of the tileset)
        using the getTileIndexByName method of the tileset class by putting the string "wall" as argument
        */
        Sprite2D wall = tileset.tiles.get(tileset.getTileIndexByName("wall")).toSprite();
        /*
        In order to have animations, you must make animation clips first and then pass them to the animation itself
        
        Here I will be making an animation for when the character is idling
        
        First we need the animation clip. You can create one like this
        */
        AnimationClip clip1 = new AnimationClip();
        /*
        Right now the AnimationClip is empty so we need to give it some information
        
        First we will tell it which frames to use
        
        You can do it like this
        */
        for(int i = 0; i < 6; i++)clip1.frames.add(posses.tiles.get(i).createTextureRegion());
        /*
        In this case I used a for loop that goes from the first frame index to the last one (in my case the
        first one has the index 0 and the last one has 5. Note that it doesn't get to 6 as I put the condition
        that i, which is the index, to be rigidly less than 6)
        
        Then, for every index I added to the clip's frames Array a TextureRegion from my tileset. I had to
        use the method createTextureRegion() because the tiles Array is an Array of Tile objects so I had to
        convert the Tile to a TextureRegion in order to add it to the clip's frames Array
        
        NOTE THAT THIS METHOD WORKS ONLY IF THE TILES IN THE TILESET ARE CONSECUTIVE. IF THEY ARE NOT, DO SOMETHING
        LIKE THIS:
        
        your_animationclip_name.frames.add(your_tileset_name.tiles.get(tile_you_want_index_1).createTextureRegion());
        your_animationclip_name.frames.add(your_tileset_name.tiles.get(tile_you_want_index_2).createTextureRegion());
        your_animationclip_name.frames.add(your_tileset_name.tiles.get(tile_you_want_index_3).createTextureRegion());
        your_animationclip_name.frames.add(your_tileset_name.tiles.get(tile_you_want_index_4).createTextureRegion());
        
        As many frames as you need in the animation
        
        The AnimationClip also needs a duration so that the Engine will know how much to show every frame
        
        Luckly the AnimationClip class has a duration variable that you can set to whatever duration you want
        
        The default value for this variable is 1 so you don't need to change it if you want
        */
        clip1.duration = 2f;
        /*
        You can also set the AnimationClip to loop
        
        This is set to false by default
        */
        clip1.loop = true;
        /*
        Now that you have the AnimationClip you can finally make the Animation itself
        
        You can define an Animation by doing the following
        The first argument is the name of the Animation. You can change it later by doing:
        
        your_animation_name.name = your_animations_new_name;
        
        The second argument is the AnimationClip you want to assign to the Animation.
        You can change it later by doing:
        
        your_animation_name.clip = your_animations_new_clip;
        
        The Animation class constructor can also havethird argument is the Animation's target - what Sprite2D 
        will have this Animation. You can change it later by doing:
        
        your_animation_name.setTarget(your_new_animation_target);
        
        You can also get the target of your animation by doing:
        
        your_animation_name.getTarget();
        
        This will return you a Sprite2D that is your Animation's target
        
        The Animation class also has more methods about which we will talk later
        */
        Animation idle = new Animation("idle", clip1);
        /*
        Now we will create a player that will have our new Animation asigned to
        
        Before we make the player we will need an AnimationController GameComponent (you will see a little
        bit later what GameComponents are) in order to be able to control it's animations
        
        But the AnimationController needs a Sprite2D as target. You can set this later on but it is recomended
        to do it from the constructor
        */
        Sprite2D sprite = new Sprite2D();
        /*
        Now that we have the Sprite2D, we can create the AnimationController like this
        
        The AnimationController has an optional Sprite2D as parameter and as many Animations as you want. The
        Sprite2D is optional and can be omitted but make sure that you will add one to the AnimationController
        otherwise it won't work properly
        */
        AnimationController animationController = new AnimationController(sprite, idle);
        /*
        Finally we can create the player. You can do it like this
        */
        Joint player = new Joint();
        /*
        The Joint class is a GameObject that can hold multiple GameComponents
        
        I made the player a Joint as it will have a Transform, some Animations and many others!
        
        I know it looks hard at the beginning but I promise you, everything will make semse later on
        
        First things first, we need give a name to our Joint
        
        You can do it as following
        */
        player.name = "player";
        /*
        Now that we have our player we can asign some GameComponents to it
        
        To add a GameComponent to the Joint you can use the addComponent method like this:
        your_joints_name.addComponent(your_game_component);
        
        The your_game_component parameter can be any class that extends the GameComponent
        
        For example, the first GameComponent the player will hold is Transform. It is used to define
        the location, rotation and scale of our player
        */
        player.addComponent(new Transform());
        /*
        Now we can add a SpriteRenderer so that we can render it on the screen
        
        The SpriteRenderer needs a Sprite2D as argument which is the Sprite2D that it will render. We will
        use the sprite we created for the AnimationController as it is the player's Sprite2D
        */
        player.addComponent(new SpriteRenderer(sprite));
        /*
        To make the player have the Animation we created we need to add the 
        AnimationController GameComponent we just created to it
        */
        player.addComponent(animationController);
        /*
        In order to move the player around we need a player controller class
        
        You can use the BasicPlayerController class we included in the Engine
        */
        player.addComponent(new BasicPlayerController());
    }

    @Override
    public void update(float delta) {
        
    }

    @Override
    public void render(OrthographicCamera camera) {
        
    }
    
}
