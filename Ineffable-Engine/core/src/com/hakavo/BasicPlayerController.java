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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.core.AnimationController;
import com.hakavo.core.GameComponent;
import com.hakavo.core.GameObject;
import com.hakavo.core.Joint;
import com.hakavo.core.ParticleSystem;
import com.hakavo.core.SpriteRenderer;
import com.hakavo.core.TextRenderer;
import com.hakavo.core.Transform;

/**
 *
 * @author HakavoGames
 */
public class BasicPlayerController extends GameComponent {
    public float speed=50;
        private Transform transform;
        private SpriteRenderer spriteRenderer;
        private GameObject tag;
        private ParticleSystem particleSystem;
        private AnimationController animationController;
        @Override
        public void start() {
            this.transform=super.getGameObject().getComponent(Transform.class);
            this.spriteRenderer=super.getGameObject().getComponent(SpriteRenderer.class);
            this.particleSystem=super.getGameObject().getComponent(ParticleSystem.class);
            this.animationController=super.getGameObject().getComponent(AnimationController.class);
            
            animationController.play("idle");
            tag=new GameObject();
            tag.addComponent(new Transform(0,40,0.25f,0.25f).setRelative(this.transform));
            tag.addComponent(new TextRenderer("Press F to fart"));
            particleSystem.isTransformDependent=false;
            ((Joint)this.gameObject).addGameObject(tag);
        }
        @Override
        public void update(float delta) {
            tag.getComponent(Transform.class).matrix.rotate(50*delta);
            
            if(Gdx.input.isKeyPressed(Input.Keys.A))
            {
                spriteRenderer.flipX=true;
                transform.matrix.translate(-speed*delta,0);
            }
            else if(Gdx.input.isKeyPressed(Input.Keys.D))
            {
                spriteRenderer.flipX=false;
                transform.matrix.translate(speed*delta,0);
            }
        }
}