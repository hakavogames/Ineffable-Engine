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
import com.hakavo.ineffable.core.Renderable;
import com.hakavo.ineffable.core.Joint;
import com.hakavo.ineffable.core.GameObject;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.assets.AssetManager;
import com.hakavo.ineffable.core.collision.Collider;
import com.hakavo.ineffable.core.GameComponent;
import com.hakavo.ineffable.core.collision.PointCollider;
import java.util.Comparator;

public class Engine {
    protected Joint level=new Joint();
    public OrthographicCamera camera;
    protected GameMode gameMode;
    private OrthographicCamera ui;
    public void initServices() {
    }
    public void init() {
        GameServices.init();
        AssetManager.init();
        camera=new OrthographicCamera();
        ui=new OrthographicCamera();
        ui.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        
        Gdx.gl.glDisable(GL30.GL_DEPTH_TEST);
        ShaderProgram.pedantic=false;
    }
    public void loadGameMode(GameMode gameMode) {
        this.gameMode=gameMode;
        level.destroy();
        level=new Joint();
        level.addComponent(new GameManager());
        gameMode.init(this);
        level.start();
        level.update(0);
    }
    public Joint getLevel() {
        return level;
    }
    public void setLevel(Joint level) {
        this.level=level;
    }
    private Array<Renderable> renderList=new Array<Renderable>();
    public void render()
    {
        renderList.clear();
        for(GameObject gameObject : level.getAllGameObjects())
            renderList.addAll(gameObject.getComponents(Renderable.class));
        renderList.sort(new Comparator() {
            @Override
            public int compare(Object o1,Object o2) {
                return ((Renderable)o2).layer-((Renderable)o1).layer;
            }
        });
        
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        GameServices.spriteBatch.begin();
        GameServices.spriteBatch.setShader(GameServices.getDefaultShader());
        
        for(Renderable renderable : renderList)
            if(renderable.visible)
                renderable.render(camera);
        
        GameServices.spriteBatch.setProjectionMatrix(ui.combined);
        Matrix4 mat=Pools.obtain(Matrix4.class).idt();
        GameServices.spriteBatch.setTransformMatrix(mat);
        Pools.free(mat);
        for(Renderable renderable : renderList)
            if(renderable.visible)
                renderable.onGui(ui);
        
        gameMode.renderGui(ui);
        GameServices.spriteBatch.end();
    }
    public void update(float delta)
    {
        level.update(delta);
        gameMode.update(delta);
        camera.update();
        GameServices.getSpriteBatch().setProjectionMatrix(camera.combined);
    }
    private class GameManager extends GameComponent {
        private Joint level;
        private Array<GameObject> gameObjects;
        private PointCollider cursor;
        
        @Override
        public void start() {
            cursor=new PointCollider();
            cursor.active=false;
            cursor.name="mouse-pointer";
            
            level=(Joint)this.getGameObject();
            level.addComponent(cursor);
            gameObjects=new Array<GameObject>();
        }
        @Override
        public void update(float delta) {
            Ray ray=camera.getPickRay(Gdx.input.getX(),Gdx.input.getY());
            cursor.setPosition(ray.origin.x,ray.origin.y);
            cursor.active=Gdx.input.isButtonPressed(Input.Buttons.LEFT);
            
            gameObjects.clear();
            Array<Collider> colliders=new Array<Collider>();
            
            for(GameObject gameObject : level.getAllGameObjects(gameObjects))
                colliders.addAll(gameObject.getComponents(Collider.class));
            
            for(Collider collider : colliders)
                for(int i=0;i<colliders.size;i++)
                    if(!colliders.get(i).equals(collider))
                        collider.testCollision(colliders.get(i));
        }
    }
}
