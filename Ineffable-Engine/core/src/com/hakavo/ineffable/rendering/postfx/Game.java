package com.hakavo.ineffable.rendering.postfx;

import com.hakavo.ineffable.rendering.postfx.effects.*;
import com.hakavo.ineffable.rendering.postfx.utils.FullScreenQuad;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.hakavo.ineffable.assets.AssetManager;
import com.hakavo.ineffable.rendering.postfx.utils.*;

public class Game extends ApplicationAdapter {
    Mesh mesh;
    SpriteBatch sb;
    PostProcessor post;
    FrameBuffer fbo;
    
    @Override
    public void create() {
        AssetManager.init();
        mesh=FullScreenQuad.generate();
        sb=new SpriteBatch();
        post=new PostProcessor(1600,900,Format.RGBA8888);
        post.effects.add(new Bloom(20,0.7f));
        post.effects.add(new CRT());
        fbo=new FrameBuffer(Format.RGB888,1600,900,false);
        AssetManager.loadAsset("texture",Gdx.files.internal("badlogic.jpg"),"texture-test");
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        
        fbo.begin();
        sb.begin();
        sb.draw(AssetManager.getAsset("texture-test",Texture.class),600,300,500,500);
        sb.end();
        fbo.end();
        
        post.render(fbo,null);
        sb.begin();
        Texture tex=post.buffer.getLast().getColorBufferTexture();
        sb.draw(tex,0,0,tex.getWidth(),tex.getHeight(),0,0,tex.getWidth(),tex.getHeight(),false,true);
        sb.end();
    }
    @Override
    public void dispose() {
    }
}
