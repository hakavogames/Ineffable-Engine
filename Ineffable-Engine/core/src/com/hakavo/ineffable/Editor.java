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
import com.hakavo.ineffable.gameobjects.Map;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.Random;

public class Editor implements GameMode,InputProcessor {
    Tileset tileset;
    Map map;
    Vector2 velocity=new Vector2(),camAdd=new Vector2(),selStart=new Vector2(),selEnd=new Vector2();
    ShapeRenderer sr;
    Skin skin;
    Stage stage;
    Window window;
    InputMultiplexer inputMultiplexer;
    TextField tsname;
    TextButton loadts;
    Dialog tiles;
    Tile tile;
    ButtonGroup<Button> bt=new ButtonGroup<Button>();
    Engine engine;
    float targetZoom=1;
    @Override
    public void init(Engine engine) {
        this.engine=engine;
        inputMultiplexer=new InputMultiplexer();
        sr=new ShapeRenderer();
        tileset=new Tileset(Gdx.files.internal("tileset.xml"));
        map=new Map(32,32,tileset);
        map.addLayer();
        tile=tileset.tiles.get(0);
        
        for(int i=0;i<32;i++)
            for(int j=0;j<32;j++)
                map.getLayer("layer0").data[i][j].parent=tileset.tiles.get(0);
        
        engine.level.gameObjects.add(map);
        initUI();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }
    public void initUI()
    {
        skin=new Skin(Gdx.files.internal("ui/uiskin.json"));
        stage=new Stage(new ScreenViewport());
        stage.getRoot().addCaptureListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                if (!(event.getTarget() instanceof TextField)) stage.setKeyboardFocus(null);
                return false;
            }
        });
        initTilesDialog();
        window=new Window("Level Editor",skin);
        window.setSize(360,Gdx.graphics.getHeight());
        window.setMovable(false);
        window.setResizable(false);
        window.setX(Gdx.graphics.getWidth()-window.getWidth());
        window.setLayoutEnabled(true);
        window.align(Align.topLeft);
        
        tsname=new TextField("tileset.xml",skin);
        loadts=new TextButton("Load",skin);
        loadts.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event,float x,float y)
            {
                map.tileset=new Tileset(Gdx.files.internal(tsname.getText()));
            }
        });
        Table tileset=new Table(skin);
        tileset.add("Load tileset").left().row();
        tileset.add(tsname);
        tileset.add(loadts).row();
        window.add(tileset).row();
        Table brush=new Table(skin);
        TextButton pickBrush=new TextButton("Pick tile",skin);
        pickBrush.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event,float x,float y)
            {
                tiles.setVisible(true);
            }
        });
        brush.add("Brush").left().row();
        brush.add(pickBrush);
        window.add(brush).left().row();
        
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(this);
        stage.addActor(window);
        stage.addActor(tiles);
    }
    int i;
    Tile t;
    public void initTilesDialog()
    {
        tiles=new Dialog("Tiles",skin);
        tiles.setVisible(false);
        tiles.setSize(950,800);
        tiles.align(Align.topLeft);
        Table table=new Table(skin);
        TextButton close=new TextButton("Close",skin);
        close.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event,float x,float y)
            {
                tiles.setVisible(false);
            }
        });
        table.add(close).left().row();
        
        int r=0;
        for(i=0;i<map.tileset.tiles.size;i++)
        {
            int tilesize=map.tileset.tilesize;
            t=map.tileset.tiles.get(i);
            Image img=new Image(new TextureRegion(map.tileset.atlas,t.tileX*tilesize,t.tileY*tilesize,tilesize,tilesize));
            Button b=new Button(skin);
            b.align(Align.topLeft);
            b.add(img).left().top().size(tilesize*2).padTop(5).row();
            b.add(new Label(t.name,skin)).left().padLeft(-5);
            b.setName(t.name);
            b.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event,float x,float y)
                {
                    tile=map.tileset.tiles.get(bt.getCheckedIndex());
                }
            });
            r++;
            if(r<10)table.add(b).size(tilesize*2+30,tilesize*2+30).left();
            else {table.add(b).size(tilesize*2+30,tilesize*2+30).left().row();r=0;}
            bt.add(b);
        }
        
        tiles.add(table).align(Align.topLeft).pad(0).row();
    }
    @Override
    public void update(float delta) {
        
        float speed=Gdx.graphics.getDeltaTime()*100;
        
        velocity.set(0,0);
        if(Gdx.input.isKeyPressed(Input.Keys.W))velocity.y=speed;
        if(Gdx.input.isKeyPressed(Input.Keys.S))velocity.y=-speed;
        if(Gdx.input.isKeyPressed(Input.Keys.A))velocity.x=-speed;
        if(Gdx.input.isKeyPressed(Input.Keys.D))velocity.x=speed;
        
        camAdd.x=Interpolation.linear.apply(camAdd.x,velocity.x,0.1f);
        camAdd.y=Interpolation.linear.apply(camAdd.y,velocity.y,0.1f);
        
        if(Math.abs(camAdd.x)<=0.5&&velocity.x==0)camAdd.x=0;
        if(Math.abs(camAdd.y)<=0.5&&velocity.y==0)camAdd.y=0;
        
        engine.camera.position.x+=camAdd.x;
        engine.camera.position.y+=camAdd.y;
        engine.camera.position.x=(float)Math.floor((double)engine.camera.position.x*10)/10f;
        engine.camera.position.y=(float)Math.floor((double)engine.camera.position.y*10)/10f;
        engine.camera.update();
        
        boolean ok=true;
        int x=Gdx.input.getX();
        int y=Gdx.input.getY();
        for(int i=0;i<stage.getActors().size;i++)
        {
            Actor actor=stage.getActors().get(i);
            if(x>=actor.getX()&&y>=actor.getY()&&x<=actor.getX()+actor.getWidth()&&y<=actor.getY()+actor.getHeight())
            {
                ok=false;
                break;
            }
        }
        if(Gdx.input.isButtonPressed(Buttons.BACK)||Gdx.input.isButtonPressed(Buttons.FORWARD)||
           Gdx.input.isButtonPressed(Buttons.LEFT)||Gdx.input.isButtonPressed(Buttons.MIDDLE)||Gdx.input.isButtonPressed(Buttons.RIGHT)&&ok==true)
            if(ok==true)onButtonDown();
        
        int tilesize=map.tileset.tilesize;
        int w=map.getWidth();
        int h=map.getHeight();
        Vector3 v=engine.camera.getPickRay(Gdx.input.getX()+tilesize/2,Gdx.input.getY()+tilesize/2).origin;
        selStart.x=MathUtils.clamp((int)(v.x/tilesize),0,w-1)*tilesize;
        selStart.y=MathUtils.clamp((int)(v.y/tilesize),0,h-1)*tilesize;
        
        stage.act(delta);
        engine.camera.zoom=Interpolation.linear.apply(engine.camera.zoom,targetZoom,0.25f);
    }
    @Override
    public void renderGui(OrthographicCamera cam) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setProjectionMatrix(engine.camera.combined);
        
        sr.setColor(1,1,1,0.25f);
        sr.rect(selStart.x,selStart.y,24,24);
        sr.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
        
        stage.draw();
    }
    private void onButtonDown()
    {
        int tilesize=map.tileset.tilesize;
        int w=map.getWidth();
        int h=map.getHeight();
        
        if(Gdx.input.isButtonPressed(Buttons.LEFT))
            map.getLayer(0).data[(int)selStart.x/tilesize][(int)selStart.y/tilesize].parent=tile;
        else if(Gdx.input.isButtonPressed(Buttons.RIGHT))
            engine.camera.position.sub(Gdx.input.getDeltaX()/5*engine.camera.zoom,-Gdx.input.getDeltaY()/5*engine.camera.zoom,0);
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }
    @Override
    public boolean keyUp(int i) {
        return false;
    }
    @Override
    public boolean keyTyped(char c) {
        return false;
    }
    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }
    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }
    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }
    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }
    @Override
    public boolean scrolled(int i) {
        if(i==-1)targetZoom=Math.max(1,engine.camera.zoom-0.5f);
        else targetZoom=engine.camera.zoom+0.5f;
        return true;
    }
}
