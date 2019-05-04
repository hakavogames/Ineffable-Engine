package com.hakavo.ineffable.core;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.Array;
import com.hakavo.ineffable.core.skeleton.*;

public class SkeletalSpriteRenderer extends Renderable implements GameComponent.Copiable {
    private Bone skeleton;
    private final Array<Bone> bones=new Array<Bone>();
    private Transform transform;
    
    public SkeletalSpriteRenderer() {
        skeleton=new Bone("default");
    }
    public SkeletalSpriteRenderer(Bone skeleton) {
        this.skeleton=skeleton;
    }
    @Override
    public void start() {
        transform=getGameObject().getComponent(Transform.class);
    }
    @Override
    public void update(float delta) {
        if(skeleton!=null)skeleton.getBoneTransform().getTransform().setRelative(transform);
    }
    @Override
    public void render(OrthographicCamera camera) {
        if(skeleton==null)return;
        bones.clear();
        skeleton.getBones(bones);
        for(Bone bone : bones) {
            bone.getBoneTransform().update();
            if(bone.getSpriteRenderer()!=null)
                bone.getSpriteRenderer().render(camera);
        }
    }
    public void setSkeleton(Bone root) {
        this.skeleton=root.cpy();
    }
    public Bone getSkeleton() {
        return this.skeleton;
    }
    @Override
    public SkeletalSpriteRenderer cpy() {
        return new SkeletalSpriteRenderer(this.skeleton.cpy());
    }
}
