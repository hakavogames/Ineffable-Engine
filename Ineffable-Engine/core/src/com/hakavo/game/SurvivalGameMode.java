package com.hakavo.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.*;
import com.hakavo.game.mechanics.*;
import com.hakavo.game.components.*;
import com.hakavo.game.objects.*;
import com.hakavo.game.ui.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.assets.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.*;
import com.hakavo.ineffable.core.physics.*;
import com.hakavo.ineffable.rendering.*;
import com.hakavo.ineffable.rendering.postfx.*;
import com.hakavo.ineffable.rendering.postfx.effects.*;
import com.hakavo.ineffable.rendering.postfx.filters.Noise;
import com.hakavo.ineffable.ui.*;
import com.hakavo.ineffable.utils.*;

public class SurvivalGameMode implements GameMode, TimedTask {
    private Label label;
    protected Engine engine;
    
    @Override
    public void init(Engine engine) {
        this.engine=engine;
        Label.defaultFont="pixeltype";
        GameServices.loadScript(Gdx.files.internal("scripts/debug/physics.js"));
        GameServices.loadScript(Gdx.files.internal("scripts/debug/player-info.js"));
        HUD hud=new HUD();
        GUI.mainContainer.add(hud);
        AssetManager.loadAsset("texture",Gdx.files.internal("sprites/pixel.png"),"pixel");
        AssetManager.loadAsset("sound",Gdx.files.internal("sounds/shoot.wav"),"sound-shoot");
        AssetManager.loadAsset("sound",Gdx.files.internal("sounds/hit.wav"),"sound-hit");
        ((DefaultPass)engine.getRenderer().renderPasses.get(0)).clearColor.set(0.2f,0.22f,0.22f,1f);
        engine.getRenderer().postfx.effects.add(new MotionBlur());
        //engine.getRenderer().postfx.addFilter(new ChromaticAberration(0.025f));
        engine.getRenderer().postfx.effects.add(new Bloom(30f,0.7f));
        engine.getRenderer().postfx.effects.add(new SimpleEffect(new Noise(0.02f,1f)));
        engine.getRenderer().postfx.effects.add(new Vignette());
        //engine.getRenderer().postfx.effects.add(new CRT());
        float size=34f;
        engine.camera.setToOrtho(false,size,((float)Gdx.graphics.getHeight()/Gdx.graphics.getWidth())*size);
        engine.camera.position.set(0,0,engine.camera.position.z);
        engine.getLevel().addComponent(new CameraEffects());
        
        Square player=new Square(1.5f,1.3f);
        player.name="player";
        //player.addComponent(new CameraFollower());
        player.addComponent(new PlayerController());
        player.getComponent(SquareController.class).initHealth(200);
        player.getComponent(RigidBody.class).restitution=0.3f;
        HUD.target=player;
        
        /*TextRenderer text=new TextRenderer("Training Bot");
        text.color.set(1,1,1,1);
        text.layer=-5;
        GameObject label=new GameObject("label",new Transform(0,1.25f,0.02f,0.02f),text);
        bot.addGameObject(label);*/
        
        GameObject floor=new GameObject("floor");
        floor.addComponent(new Transform(0,0,22,1));
        floor.addComponent(new SpriteRenderer(new Sprite2D("pixel"),1f));
        floor.addComponent(new BoxCollider(-0.5f,-0.5f,1f,1f));
        floor.addComponent(new RigidBody(new Vector2(0,-7f),0,0f));
        
        GameObject floor2=new GameObject("floor2");
        floor2.addComponent(new Transform(0,0,8,1));
        floor2.addComponent(new SpriteRenderer(new Sprite2D("pixel"),1f));
        floor2.addComponent(new BoxCollider(-0.5f,-0.5f,1f,1f));
        floor2.addComponent(new RigidBody(new Vector2(8,-3.5f),0,0));
        
        GameObject floor3=new GameObject("floor3");
        floor3.addComponent(new Transform(0,0,8,1));
        floor3.addComponent(new SpriteRenderer(new Sprite2D("pixel"),1f));
        floor3.addComponent(new BoxCollider(-0.5f,-0.5f,1f,1f));
        floor3.addComponent(new RigidBody(new Vector2(-7,-1.5f),0,0));
        
        GameObject floor4=new GameObject("floor4");
        floor4.addComponent(new Transform(0,0,1,8));
        floor4.addComponent(new SpriteRenderer(new Sprite2D("pixel"),1f));
        floor4.addComponent(new BoxCollider(-0.5f,-0.5f,1f,1f));
        floor4.addComponent(new RigidBody(new Vector2(-14,-3.5f),0,0f));
        
        engine.getLevel().addGameObject(player);
        engine.getLevel().addGameObject(floor);
        engine.getLevel().addGameObject(floor2);
        engine.getLevel().addGameObject(floor3);
        engine.getLevel().addGameObject(floor4);
        
        initGUI();
        GameServices.timerManager.scheduleTimer("test",new Timer(this).setInterval(5,1));
        GUI.dispatchEvent("title","Tutorial",4f,5.5f,new Color(0.75f,1f,0.55f,1f),Interpolation.exp10Out);
    }
    public void initGUI() {
        Container container=new Container();
        container.bounds.set(0,30,100,30);
        container.style.blur=false;
        container.style.background.set(0,0,0,0f);
        Label weapon=new Label("","pixeltype") {
            @Override
            public void onUpdate(float delta) {
                GameObject player=GUI.getGameLevel().getGameObjectByName("player");
                if(player!=null)
                    setText(player.getComponent(WeaponController.class).getCurrentWeapon().parent.getName());
            }
        };
        weapon.autoSize=false;
        weapon.bounds.width=1000;
        weapon.bounds.setPosition(5,5);
        container.add(weapon);
        GUI.mainContainer.add(container);
        GUI.mainContainer.addEventListener(new EventListener() {
            private int current;
            
            @Override
            public void onScroll(int amount) {
                GameObject player=GUI.getGameLevel().getGameObjectByName("player");
                if(player!=null) {
                    WeaponController controller=player.getComponent(WeaponController.class);
                    current=MathUtils.clamp(current+amount,0,controller.weapons.size);
                    equip(current);
                }
            }
            @Override
            public void onKeyTyped(char c) {
                if(c>='0'&&c<='9') {
                    if(c-'0'-1<getWeaponCount()) {
                        current=c-'0'-1;
                        equip(current);
                    }
                }
            }
            private void equip(int id) {
                GameObject player=GUI.getGameLevel().getGameObjectByName("player");
                if(player!=null) {
                    WeaponController controller=player.getComponent(WeaponController.class);
                    controller.equipWeapon(id);
                }
            }
            private int getWeaponCount() {
                GameObject player=GUI.getGameLevel().getGameObjectByName("player");
                if(player!=null) {
                    WeaponController controller=player.getComponent(WeaponController.class);
                    return controller.weapons.size;
                }
                return 1;
            }
        });
    }
    public void addServerWindow() {
        Window window=new Window("Server status",true,false,true);
        window.bounds.setSize(640,360);
        label=new Label();
        label.autoSize=false;
        label.bounds.setPosition(10,10);
        label.bounds.setSize(620,320);
        window.add(label);
        GUI.mainContainer.add(window);
        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    /*GameServer server=GUI.getGameLevel().getComponent(GameServerManager.class).server;
                    String text="";
                    for(PlayerData data : server.getPlayers())
                        text+=data.username+"\n";
                    label.getFont().getData().markupEnabled=true;
                    if(server.getPlayers().size()==0)text="[GRAY]No one";
                    label.setText("Players:[YELLOW]\n"+text);
                    try{Thread.sleep(100);}catch(Exception ex){}*/
                }
            }
        }).start();
    }
    @Override
    public void update(float delta) {
    }
    @Override
    public void renderGui(OrthographicCamera camera) {
    }
    
    int count=1;
    @Override
    public void performTask(Engine engine) {
        if(engine.getLevel().getGameObjectByName("player")==null) {
            GUI.dispatchEvent("title","You DIED!",4f,5.5f,new Color(1,0.85f,0.75f,1f),Interpolation.exp10Out);
            GameServices.timerManager.getTimer("test").stop();
            GUI.mainContainer.style.blur=true;
            GUI.mainContainer.style.background.set(0.6f,0.6f,0.6f,1f);
            
            Link respawn=new Link("Respawn",new LinkListener() {
                @Override
                public void onClick() {
                    GameServices.timerManager.getEngine().loadGameMode(new SurvivalGameMode());
                }
            });
            respawn.setScale(2);
            GUI.mainContainer.add(respawn);
            respawn.center();
            respawn.bounds.y-=70;respawn.bounds.x-=150;
            
            Link exit=new Link("Quit game",new LinkListener() {
                @Override
                public void onClick() {
                    quit();
                }
            });
            exit.setScale(2);
            GUI.mainContainer.add(exit);
            exit.center();
            exit.bounds.y-=70;exit.bounds.x+=150;
            return;
        }
        if(engine.getLevel().getGameObjects(Square.class).size>1)return;
        GUI.dispatchEvent("title","Round "+count,3f,4.5f);
        for(int i=0;i<=count/3;i++) {
            insertEnemy();
        }
        count++;
    }
    public void insertEnemy() {
        Square bot=new Square(1,0.1f,new SniperRifle());
        bot.name="bot";
        bot.addComponent(new EnemyController(true));
        bot.getComponent(RigidBody.class).position.set(MathUtils.random(-8,8),2);
        bot.getComponent(SpriteRenderer.class).color.set(1,0.8f,0.8f,1f);
        bot.getComponent(SquareController.class).initHealth(10);
        engine.getLevel().addGameObject(bot);
    }
    public void quit() {
        GUI.mainContainer.removeByType(Link.class);
        GUI.dispatchEvent("title","Thanks for playing!",6f,5.5f,new Color(0.75f,1f,0.85f,1f),Interpolation.exp10Out,new TitleListener() {
            @Override
            public void onTitleFinished() {
                Gdx.app.exit();
            }
        });
    }
}
