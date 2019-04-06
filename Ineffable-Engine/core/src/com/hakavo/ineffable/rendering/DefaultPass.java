package com.hakavo.ineffable.rendering;

import com.hakavo.ineffable.core.Transform;
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.graphics.glutils.GLFrameBuffer.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.assets.AssetManager;
import com.hakavo.ineffable.core.*;

public class DefaultPass extends RenderPass {
    public Color clearColor=new Color(0,0,0,1);
    protected Engine engine;
    protected ShaderProgram shader;
    private Matrix4 identity;
    
    @Override
    public void init(Engine engine) {
        this.engine=engine;
        identity=new Matrix4().idt();
        shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/default"),"shader-default");
        
        FrameBufferBuilder fboBuilder=new FrameBufferBuilder(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        fboBuilder.addColorTextureAttachment(GL30.GL_RGBA8,GL30.GL_RGBA,GL30.GL_FLOAT);
        super.frameBuffer=fboBuilder.build();
    }
    @Override
    public void begin() {
        frameBuffer.begin();
        shader.begin();
        Gdx.gl.glClearColor(clearColor.r,clearColor.g,clearColor.b,clearColor.a);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT|GL30.GL_DEPTH_BUFFER_BIT);
        GameServices.getSpriteBatch().begin();
        GameServices.getSpriteBatch().setShader(shader);
        //Gdx.gl.glEnable(GL30.GL_DEPTH_TEST);
    }
    @Override
    public void render(Renderable renderable) {
        OrthographicCamera cam=engine.camera;
        
        if(renderable.visible) {
            renderable.render(cam);
        }
    }
    @Override
    public void end() {
        GameServices.getSpriteBatch().setTransformMatrix(identity);
        GameServices.getSpriteBatch().end();
        shader.end();
        frameBuffer.end();
    }
}
