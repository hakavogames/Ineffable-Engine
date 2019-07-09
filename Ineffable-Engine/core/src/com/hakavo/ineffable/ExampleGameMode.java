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

package com.hakavo.ineffable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.ineffable.core.Animation;
import com.hakavo.ineffable.core.AnimationClip;
import com.hakavo.ineffable.core.AnimationController;
import com.hakavo.ineffable.core.GameObject;
import com.hakavo.ineffable.core.Joint;
import com.hakavo.ineffable.core.ParallaxScroller;
import com.hakavo.ineffable.core.ParticleSystem;
import com.hakavo.ineffable.core.Sprite2D;
import com.hakavo.ineffable.core.SpriteRenderer;
import com.hakavo.ineffable.core.Transform;
import com.hakavo.ineffable.gameobjects.Map;

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
        Tileset posses = new Tileset(Gdx.files.internal("Scavengers_SpriteSheet.png"), 32);
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
        The Joint class is a GameObject that can hold multiple GameObjects
        
        A GameObject can hold multiple GameComponents
        
        The Joint class extends the GameObject so it is a GameObject with an Array of multiple
        diffrent GameObjects. Because of this, we can also have a Joint that holds a Joint if we want
        
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
        
        You can use the BasicPlayerController class we included in the Engine or create your own class
        */
        player.addComponent(new BasicPlayerController(Keys.W, Keys.S, Keys.A, Keys.D, 50f));
        /*
        Now we cam set the player to a layer. Layers are rendered in the order of their value. First are
        GameObjects with biggest layer value, then the next one and so on. The layer value is hold by the Renderable class.
        The classes that extend Renderable and have a layer value are: SpriteRenderer, ParallaxScroller,
        TextRenderer, ParticleSystem and TiledBackground.
        
        In our case we added a SpriteRenderer to the player so we can use the layer value of the SpriteRenderer
        we just added.
        
        You can do it like this
        */
        player.getComponent(SpriteRenderer.class).layer = 3;
        /*
        I called the getComponent method of the Joint player (because Joint class extends GameObject) and I
        put as argument SpriteRenderer.class because we want the SpriteRenderer class from the GameComponents
        array of the Joint.
        
        Then I added .layer to get the layer value of the SpriteRenderer class and I passed to it the value 3
        as I want it to be on the third layer.
        
        The Renderable classes also have a visible boolean value. This means that if you set it to true the
        Engine will know to render the GameObject having the class as a GameComponent. If it is false it won't render it.
        
        If we run it as it is until this point we can see that it won't render the player and that is because
        we need to add the player to the Engine's level Joint.
        
        We can do it like this
        */
        engine.level.addGameObject(player);
        /*
        To add it we just use the method addGameObject from the Joint class, in this case Engine's level Joint
        
        The parameter is the GameObject we want to add to the Engine, in this case the player Joint
        
        Now if we run it we can see our player on the screen and we can move it with the keys we set, in this
        case W, A, S and D
        
        But the camera won't follow the player as we didn't set it to
        
        To set the camera to follow the player, you have to do something in update so that every time
        the player moves the camera will go to it's position
        
        Now go to the update method down the class and look what is inside
        
        
        
        Now that the camera follows the player it's time for some background
        
        There are 2 types of background:
            -tiled background
            -texture background
        
        In this tutorial we will learn hou to make the texture background because it allows for some more
        powerful things such as ParallaxScrolling
        
        ParallaxScrolling is how the background that looks to be further moves slower as you move than the one closer.
        It is usually used at WebPages but it also allows for some cool effects in games
        
        First we need all the Sprite2Ds we will have in the background. I will
        make exactly the same background of the TestGameMode GameMode because I'm lazy
        */
        Sprite2D l1 = new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/trees1.png"))));
        Sprite2D l2 = new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/trees2.png"))));
        Sprite2D l3 = new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/trees3.png"))));
        Sprite2D l4 = new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/mountains.png"))));
        Sprite2D l5 = new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/clouds.png"))));
        Sprite2D l6 = new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("parallax/mountain/background.png"))));
        /*
        Here comes the fun part. We will create a ParallaxScroller object for every Sprite2D we made
        
        You can do one like this
        */
        ParallaxScroller p1 = new ParallaxScroller(l1, 1f);
        /*
        The first argument is the Sprite2D we are asigning to the ParallaxScroller. The second one is the speed of the
        scrolling which should be between 0f and 1f where 1 will be for the near background and 0 for the further one.
        
        You can also give 2 more arguments for which axis to do the scrolling on: x or y which are boolean values
        NOTE THAT SCROLLING ON THE Y AXIS IS NOT SUPPORTED YET.
        
        Now I will also make the other ParallaxScrollers
        */
        ParallaxScroller p2 = new ParallaxScroller(l2, 0.75f);
        ParallaxScroller p3 = new ParallaxScroller(l3, 0.4f);
        ParallaxScroller p4 = new ParallaxScroller(l4, 0.15f);
        ParallaxScroller p5 = new ParallaxScroller(l5, 0.05f);
        ParallaxScroller p6 = new ParallaxScroller(l6, 0f);
        /*
        As you remember the ParallaxScroller extends Renderable so it has a layer value which we have to set
        
        I will set them all here
        */
        p1.layer = 5;
        p2.layer = 6;
        p3.layer = 7;
        p4.layer = 8;
        p5.layer = 9;
        p6.layer = 10;
        /*
        Now we can create a new background GameObject that will hold all those ParallaxScrollers
        */
        GameObject bg = new GameObject();
        /*
        We will give it a name
        */
        bg.name = "background";
        /*
        And now we will pass all the scrollers to the background object and also a Transform GameComponent
        */
        bg.addComponents(new Transform(0, 0, 0.4f, 0.4f), p1, p2, p3, p4, p5, p6);
        /*
        And finally we will pass the background to the Engine
        */
        engine.level.addGameObject(bg);
        /*
        If we run it we can see that the background is there and if we move it gives the ParallaxScrolling effect!
        
        We've looked on how to make a background with ParallaxScrolling, a player and how to make the camera follow it so
        now it's time to look at ParticleSystems
        
        A ParticleSystem contains multiple Particles that are rendered and always face the camera. When we are doing
        2D we don't need to worry about facing the camera because any sprite will always face the camera
        
        To create a ParticleSystem we first need a Sprite that the ParticleSystem will have
        */
        Sprite2D fire_sprite = new Sprite2D(new TextureRegion(new Texture(Gdx.files.internal("fire.png"))));
        /*
        Now we can define the ParticleSystem
        */
        ParticleSystem fire = new ParticleSystem(fire_sprite);
        /*
        After that we have to pass the ParticleSystem to the Engine
        */
        engine.level.addComponent(fire);
        /*
        Now that we've added it to the level Joint we can go to upgrade to make the behaviour of the particles
        */
        
    }

    @Override
    public void update(float delta) {
        /*
        First we need a temporary Vector2. By temporary I meant we need to use Pools. Pools are used to
        hold temporary data such as Vector2s Vector3s Matrix4s and many others. You can get a temporary
        Vector2 from the Pools class you can do the following
        */
        Vector2 pos = Pools.obtain(Vector2.class);
        /*
        pos is just the name I gave to the Vector2. I used the obtain() method from the Pools class to get
        a new class from it. Then I put as it's parameter Vector2.class as I wanted a Vector2 class.
        
        Now we need a refrance to the player GameObject we created. We can't reffer directly to it as we
        declared it in the init() method so it will be deleted after initialization
        
        Remember that we passed the player to the Engine? We will take the player from the Engine's level Joint
        
        You can do it like this
        */
        GameObject player = engine.level.getGameObjectByName("player");
        /*
        I used the getGameObjectByName method of the Joint because I gave the player the name "player" so
        that I can reffer to it from the level Joint as I did here
        
        Now we need to get the translation of the player but also check if it found any because
        if we didn't pass to the level Joint any GameObject with the name "player", then it gave a null object
        
        I will show you this step without many explanation as there would be a lot to go through
        */
        if(player != null)
            player.getComponent(Transform.class).matrix.getTranslation(pos);
        /*
        I got the Transform class of the player and used the getTranslation() method of it's matrix with the
        parameter pos so that it will return the translation of the player to the pos Vector2
        
        Now that we have the position of the player we can set the camera to the same position
        
        You can do it like this
        */
        camera.position.set(pos.x, pos.y, 0);
        /*
        I set the position of the camera to the x and y values of the pos Vector2
        
        Altough it looks done, we need to free the Vector2 from the Pools class for later use
        
        You can do it like this
        */
        Pools.free(pos);
        /*
        I used the free() method and I passed the pos Vector2 as it's parameter
        
        Now, if we run the code we can see that the camera follows the player wherever it goes
        altough it's hard to see because we have a black background. Go back in init() to where
        you left to see how to add a background!
        
        
        
        RIGHT NOW I HAVEN'T FINISHED THE PARTICLESYSTEM TUTORIAL. PLEASE WAIT UNTIL IT IS FINISHED.
        */
    }

    @Override
    public void renderGui(OrthographicCamera camera) {
        
    }
    
}

