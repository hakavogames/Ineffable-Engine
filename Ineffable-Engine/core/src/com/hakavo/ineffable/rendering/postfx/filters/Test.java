package com.hakavo.ineffable.rendering.postfx.filters;
import com.badlogic.gdx.Gdx;
import com.hakavo.ineffable.assets.AssetManager;

public class Test extends Filter<Test> {
    private long start=System.currentTimeMillis();
    
    public Test() {
        super.shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/filters/test"),"shader-test");
    }

    @Override
    public void beforeRender() {
        super.shader.setUniformf("u_time",(System.currentTimeMillis()-start)/1000f);
    }
}
