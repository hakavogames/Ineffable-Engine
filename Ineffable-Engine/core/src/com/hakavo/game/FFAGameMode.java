package com.hakavo.game;
import com.hakavo.ineffable.core.CameraFollower;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.hakavo.game.components.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.assets.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.physics.*;
import com.hakavo.ineffable.core.collision.*;
import com.hakavo.ineffable.core.skeleton.*;
import com.hakavo.ineffable.ui.*;

public class FFAGameMode implements GameMode {
    Joint level;
    
    @Override
    public void init(Engine engine) {
        level=engine.getLevel();
        AssetManager.loadAsset("texture",Gdx.files.internal("sprites/circle.png"),"circle",true);
        AssetManager.loadAsset("texture",Gdx.files.internal("sprites/pattern.png"),"pattern",true);
        GameServices.getCamera().setToOrtho(false,1f,((float)Gdx.graphics.getHeight())/Gdx.graphics.getWidth());
        
        GameObject background=new GameObject("background",new Transform(0,0,0.00025f,0.00025f));
        background.addComponent(new TiledBackground(new Sprite2D("pattern")));
        
        GameObject player=new GameObject("player",new Transform());
        player.addComponent(new SpriteRenderer(new Sprite2D("circle"),0.1f));
        player.addComponent(new BacteriaPlayerController(0.01f,Math.min(Gdx.graphics.getWidth(),Gdx.graphics.getHeight())));
        player.addComponent(new CircleCollider(0.05f));
        player.addComponent(new RigidBody(1));
        player.addComponent(new CameraFollower());
        
        GameObject snack=new GameObject("snack",new Transform(),
                                                new SpriteRenderer(new Sprite2D("circle")),
                                                new CircleCollider(0.1f),
                                                new SnackController());
        AssetManager.assets.put("snack",new Prefab(snack));
        
        
        /*float size=900;
        GameObject background=new GameObject("background",new Transform(16f/9f*size/2,size/2));
        background.addComponent(new SpriteRenderer(new Sprite2D("pattern"),size));*/
        level.addGameObject(background);
        level.addGameObject(player);
        //level.addGameObject(snack);
        level.addComponent(new GameComponent() {
            @Override
            public void start() {
                for(int i=1;i<=240;i++)insert();
            }
            @Override
            public void update(float delta) {
            }
            private void insert() {
                GameObject snack=AssetManager.getAsset("snack",Prefab.class).newInstance();
                ((Joint)gameObject).addGameObject(snack);
            }
        });
        initGUI();
    }
    public void initGUI() {
        Label label=new Label("Score: 0");
        label.bounds.setPosition(20,20);
        label.id="score";
        GUI.mainContainer.add(label);
    }
    
    @Override
    public void update(float delta) {
        ((Label)GUI.mainContainer.get("score")).setText(
            "Score: "+level.getGameObjectByName("player").getComponent(BacteriaPlayerController.class).getScore()
        );
    }
    @Override
    public void renderGui(OrthographicCamera camera) {
    }
}
