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

package com.hakavo.ineffable.core;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.hakavo.ineffable.GameServices;

public abstract class Renderable extends GameComponent
{
    public int layer=0;
    public boolean visible=true;
    public void prepareRendering() {
    }
    public abstract void render(OrthographicCamera camera);
    public abstract void onGui(OrthographicCamera gui);
    @Override
    public <T extends GameComponent> void copyFrom(T copyFrom) {
        super.copyFrom(copyFrom);
        if(copyFrom instanceof Renderable)
        {
            Renderable renderable=(Renderable)copyFrom;
            this.layer=renderable.layer;
            this.visible=renderable.visible;
        }
    }
}
