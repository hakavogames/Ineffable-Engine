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
import com.hakavo.ineffable.core.Sprite2D;
import javax.script.*;
import java.util.Random;

public class GameServices {
    protected static SpriteBatch spriteBatch;
    protected static ShapeRenderer shapeRenderer;
    protected static ShaderProgram defaultShader;
    protected static ArrayMap<String,BitmapFont> fonts;
    protected static OrthographicCamera camera;
    private static long startTime;
    
    public static final FileHandle scriptUtilsPath=Gdx.files.internal("scripts/utils/on_script_load.js");
    
    public static void init()
    {
        MathUtils.random=new Random();
        spriteBatch=new SpriteBatch();
        shapeRenderer=new ShapeRenderer();
        startTime=TimeUtils.millis();
        
        fonts=new ArrayMap<String,BitmapFont>();
        fonts.put("pixeltype",createFont(Gdx.files.internal("fonts/pixeltype.ttf"),32));
        fonts.put("opensans",createFont(Gdx.files.internal("fonts/opensans-regular.ttf"),18));
        fonts.put("opensans-bold",createFont(Gdx.files.internal("fonts/opensans-bold.ttf"),18));
        fonts.getValueAt(1).getData().markupEnabled=true;
        fonts.getValueAt(2).getData().markupEnabled=true;
        
        initPools();
    }
    public static void loadScript(FileHandle fileHandle) {
        try {
            ScriptEngine engine=new ScriptEngineManager().getEngineByName("nashorn");
            engine.eval(scriptUtilsPath.readString());
            engine.eval(fileHandle.readString());
        } catch(Exception ex) {
            System.err.println("Could not load script located at "+fileHandle.path());
            ex.printStackTrace(System.err);
        }
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
    public static OrthographicCamera getCamera() {
        return camera;
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
        fnt.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear,Texture.TextureFilter.Linear);
        generator.dispose();
        return fnt;
    }
    public static ArrayMap<String,BitmapFont> getFonts() {
        return fonts;
    }
    public static class TextureSplitter {
        public static Array<TextureRegion> split(Texture input,int size) {
            return split(new TextureRegion(input),size);
        }
        public static Array<TextureRegion> split(TextureRegion input,int size) {
            Array<TextureRegion> out=new Array<TextureRegion>();
            for(int i=0;i<input.getRegionHeight()/size;i++)
                for(int j=0;j<input.getRegionWidth()/size;j++)
                    out.add(new TextureRegion(input,j*size,i*size,size,size));
            return out;
        }
    }
}
