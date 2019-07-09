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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.ineffable.core.AnimationController;
import com.hakavo.ineffable.core.GameComponent;
import com.hakavo.ineffable.core.GameObject;
import com.hakavo.ineffable.core.Joint;
import com.hakavo.ineffable.core.ParticleSystem;
import com.hakavo.ineffable.core.SpriteRenderer;
import com.hakavo.ineffable.core.TextRenderer;
import com.hakavo.ineffable.core.Transform;

/**
 *
 * @author HakavoGames
 */
public class BasicPlayerController extends GameComponent {
        public float speed = 50f;
        private Transform transform;
        private SpriteRenderer spriteRenderer;
        private GameObject tag;
        private ParticleSystem particleSystem;
        private AnimationController animationController;
        private int UP = Keys.W, DOWN = Keys.S, LEFT = Keys.A, RIGHT = Keys.D;
        public BasicPlayerController() {
        }
        public BasicPlayerController(int UP, int DOWN, int LEFT, int RIGHT, float speed) {
            this.UP = UP;
            this.DOWN = DOWN;
            this.LEFT = LEFT;
            this.RIGHT = RIGHT;
            this.speed = speed;
        }
        public BasicPlayerController(int UP, int DOWN, int LEFT, int RIGHT) {
            this(UP, DOWN, LEFT, RIGHT, 50f);
        }
        @Override
        public void start() {
            this.transform=super.getGameObject().getComponent(Transform.class);
            this.spriteRenderer=super.getGameObject().getComponent(SpriteRenderer.class);
            //this.particleSystem=super.getGameObject().getComponent(ParticleSystem.class);
            this.animationController=super.getGameObject().getComponent(AnimationController.class);
            
            animationController.play("idle");
            /*
            tag=new GameObject();
            tag.addComponent(new Transform(0,40,0.25f,0.25f).setRelative(this.transform));
            tag.addComponent(new TextRenderer("Press F to fart"));
            particleSystem.isTransformDependent=false;
            ((Joint)this.gameObject).addGameObject(tag);
            */
        }
        @Override
        public void update(float delta) {
            //tag.getComponent(Transform.class).matrix.rotate(50*delta);
            
            if(Gdx.input.isKeyPressed(LEFT))
            {
                spriteRenderer.flipX=true;
                transform.matrix.translate(-speed*delta,0);
            }
            else if(Gdx.input.isKeyPressed(RIGHT))
            {
                spriteRenderer.flipX=false;
                transform.matrix.translate(speed*delta,0);
            }
            if(Gdx.input.isKeyPressed(UP))
            {
                transform.matrix.translate(0, speed*delta);
            }
            else if(Gdx.input.isKeyPressed(DOWN))
            {
                transform.matrix.translate(0, -speed*delta);
            }
        }
}
