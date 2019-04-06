package com.hakavo.ineffable.rendering;

import com.hakavo.ineffable.core.Transform;
import com.badlogic.gdx.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.rendering.postfx.*;

public class Renderer {
    public Array<RenderPass> renderPasses=new Array<RenderPass>();
    public PostProcessor postfx=new PostProcessor(this);
    protected Engine engine;
    
    private DefaultPass defaultPass;
    
    public Renderer(Engine engine) {
        this.engine=engine;
    }
    public void init() {
        defaultPass=new DefaultPass();
        renderPasses.add(defaultPass);
        //postfx.addFilter(new ChromaticAberration());
        
        for(RenderPass renderPass : renderPasses)
            renderPass.init(engine);
    }
    public void render(Array<Renderable> renderables) {
        for(RenderPass renderPass : renderPasses) {
            renderPass.begin();
            for(Renderable renderable : renderables)
                renderPass.render(renderable);
            renderPass.end();
        }
        postfx.render(true,defaultPass.getFrameBuffer().getTextureAttachments().get(0));
        Gdx.gl.glActiveTexture(GL30.GL_TEXTURE0);
    }
    public void addRenderPasses(RenderPass... passes) {
        for (RenderPass pass : passes)
            renderPasses.add(pass);
    }
    public final Engine getEngine() {
        return engine;
    }
}
