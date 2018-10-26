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
package com.kozma;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.*;
import java.util.Random;

public class GameServices {
    protected static SpriteBatch spriteBatch;
    protected static ShaderProgram defaultShader;
    private static long startTime;
    
    public static void init()
    {
        MathUtils.random=new Random();
        spriteBatch=new SpriteBatch();
        startTime=TimeUtils.millis();
        defaultShader=new ShaderProgram(Gdx.files.internal("shaders/default.vert"),Gdx.files.internal("shaders/default.frag"));
        if(!defaultShader.isCompiled())
            throw new GdxRuntimeException(defaultShader.getLog());
    }
    public static SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
    public static ShaderProgram getDefaultShader() {
        return defaultShader;
    }
    public static float getElapsedTime()
    {
        return TimeUtils.timeSinceMillis(startTime)/1000f;
    }
}
