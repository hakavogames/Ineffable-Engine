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

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author HakavoGames
 */
public class SoundEffects extends GameComponent {
    
    public Array<SoundEffect> effects = new Array<SoundEffect>();
    
    public SoundEffects(SoundEffect... sounds) {
        for(SoundEffect sound : sounds) {
            effects.add(sound);
        }
    }
    public SoundEffect getSoundByName(String name) {
        int index = getSoundIndexByName(name);
        if(index != -1)return effects.get(index);
        return null;
    }
    public int getSoundIndexByName(String name) {
        for(int i = 0; i < effects.size; i++) {
            if(effects.get(i).name.equals(name))return i;
        }
        return -1;
    }
    
    @Override
    public void update(float delta) {
    }

    @Override
    public void start() {
    }
    
}
