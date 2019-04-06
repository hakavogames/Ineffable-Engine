package com.hakavo.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.hakavo.game.components.CharacterController;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.assets.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.BoxCollider;
import com.hakavo.ineffable.core.skeleton.*;
import com.hakavo.ineffable.rendering.DefaultPass;

public class StickmanPrototype implements GameMode {
    Engine engine;
    Bone pivot;
    
    @Override
    public void init(Engine engine) {
        this.engine=engine;
        ((DefaultPass)engine.getRenderer().renderPasses.get(0)).clearColor.set(1f,1f,1f,1f);
        engine.camera.setToOrtho(false,1600,900);
        engine.camera.position.sub(engine.camera.viewportWidth/2,engine.camera.viewportHeight/2,0);
        
        AssetManager.loadAsset("texture",Gdx.files.internal("sprites/stickman.png"),"stick");
        Sprite2D head=new Sprite2D(new TextureRegion((Texture)AssetManager.getAsset("stick"),0,0,32,32));
        Sprite2D bone=new Sprite2D(new TextureRegion((Texture)AssetManager.getAsset("stick"),0,32,32,64));
        
        pivot=new Bone("pivot",new SpriteRenderer(head));
        Bone torso=new Bone("torso",new BoneTransform().setPosition(new Vector2(0,-48)).setPivot(new Vector2(0,32)),new SpriteRenderer(bone));
        torso.getBoneTransform().scale.scl(0.5f);
        Bone larm=torso.cpy();
        larm.id="larm";
        larm.getBoneTransform().position.set(0,0);
        larm.getBoneTransform().scale.scl(0.5f,0.5f);
        Bone rarm=torso.cpy();
        rarm.id="rarm";
        rarm.getBoneTransform().position.set(0,0);
        rarm.getBoneTransform().scale.scl(0.5f,0.5f);
        
        Bone lhand=larm.cpy();
        lhand.id="lhand";
        lhand.getBoneTransform().position.set(0,-60);
        lhand.getBoneTransform().rotation=-90;
        
        Bone rhand=larm.cpy();
        rhand.id="rhand";
        rhand.getBoneTransform().position.set(0,-60);
        rhand.getBoneTransform().rotation=90;
        
        larm.addBone(lhand);
        rarm.addBone(rhand);
        torso.addBone(larm);
        torso.addBone(rarm);
        pivot.addBone(torso);
        
        pivot.getBone("torso").getBoneTransform().rotation=20;
        pivot.getBone("larm").getBoneTransform().rotation=-30;
        pivot.getBone("rarm").getBoneTransform().rotation=45;
        
        SkeletalSpriteRenderer renderer=new SkeletalSpriteRenderer(pivot);
        GameObject player=new GameObject("player",new Transform(),renderer);
        engine.getLevel().addGameObject(player);
    }
    @Override
    public void update(float delta) {
    }
    @Override
    public void renderGui(OrthographicCamera camera) {
    }
}
