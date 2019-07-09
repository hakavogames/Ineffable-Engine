package com.hakavo.ineffable.rendering;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.rendering.postfx.*;
import com.hakavo.ineffable.rendering.postfx.utils.FullScreenQuad;

public class Renderer implements Disposable {
    public Array<RenderPass> renderPasses=new Array<RenderPass>();
    public PostProcessor postfx;
    protected Engine engine;
    
    private DefaultPass defaultPass;
    
    public Renderer(Engine engine) {
        this.engine=engine;
    }
    public void init() {
        postfx=new PostProcessor(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),Pixmap.Format.RGB888);
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
        postfx.render(defaultPass.getFrameBuffer(),null).toRenderBuffer();
        //Gdx.gl.glActiveTexture(GL30.GL_TEXTURE0);
    }
    public void addRenderPasses(RenderPass... passes) {
        for (RenderPass pass : passes)
            renderPasses.add(pass);
    }
    public final Engine getEngine() {
        return engine;
    }
    
    @Override
    public final void dispose() {
        renderPasses.clear();
        postfx.dispose();
        for(RenderPass pass : renderPasses)pass.dispose();
    }
}
