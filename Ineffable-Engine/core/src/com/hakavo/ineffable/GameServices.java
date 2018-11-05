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
import com.hakavo.ineffable.core.Transform;
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import java.util.Random;

public class GameServices {
    protected static SpriteBatch spriteBatch;
    protected static ShapeRenderer shapeRenderer;
    protected static ShaderProgram defaultShader;
    protected static ArrayMap<String,BitmapFont> fonts;
    private static long startTime;
    
    public static void init()
    {
        MathUtils.random=new Random();
        spriteBatch=new SpriteBatch();
        shapeRenderer=new ShapeRenderer();
        startTime=TimeUtils.millis();
        
        fonts=new ArrayMap<String,BitmapFont>();
        fonts.put("pixeltype",createFont(Gdx.files.internal("fonts/pixeltype.ttf"),32));
        
        initPools();
        
        defaultShader=new ShaderProgram(Gdx.files.internal("shaders/default.vert"),Gdx.files.internal("shaders/default.frag"));
        if(!defaultShader.isCompiled())
            throw new GdxRuntimeException(defaultShader.getLog());
    }
    private static void initPools()
    {
        Pool<Color> colorPool=new Pool<Color>() {
            @Override
            protected Color newObject() {
                return new Color();
            }
        };
        Pool<Matrix3> mat3Pool=new Pool<Matrix3>() {
            @Override
            protected Matrix3 newObject() {
                return new Matrix3();
            }
        };
        Pool<Matrix4> mat4Pool=new Pool<Matrix4>() {
            @Override
            protected Matrix4 newObject() {
                return new Matrix4().idt();
            }
        };
        Pool<Transform> transformPool=new Pool<Transform>() {
            @Override
            protected Transform newObject() {
                return new Transform();
            }
        };
        Pool<Vector2> vector2Pool=new Pool<Vector2>() {
            @Override
            protected Vector2 newObject() {
                return new Vector2();
            }
        };
        Pools.set(Color.class,colorPool);
        Pools.set(Matrix3.class,mat3Pool);
        Pools.set(Matrix4.class,mat4Pool);
        Pools.set(Transform.class,transformPool);
        Pools.set(Vector2.class,vector2Pool);
    }
    public static SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }
    public static ShaderProgram getDefaultShader() {
        return defaultShader;
    }
    public static ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }
    public static float getElapsedTime()
    {
        return TimeUtils.timeSinceMillis(startTime)/1000f;
    }
    public static float getStartTime()
    {
        return startTime/1000f;
    }
    public static void resetTime()
    {
        startTime=TimeUtils.millis();
    }
    public static BitmapFont createFont(FileHandle fh,int size)
    {
        FreeTypeFontGenerator generator=new FreeTypeFontGenerator(fh);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter=new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size=size;
        BitmapFont fnt=generator.generateFont(parameter);
        generator.dispose();
        return fnt;
    }
    public static ArrayMap<String,BitmapFont> getFonts() {
        return fonts;
    }
}
