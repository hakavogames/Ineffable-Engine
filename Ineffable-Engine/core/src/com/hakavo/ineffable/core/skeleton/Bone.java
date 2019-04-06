package com.hakavo.ineffable.core.skeleton;
import com.badlogic.gdx.utils.Array;
import com.hakavo.ineffable.core.*;

public class Bone {
    private Bone parent;
    public String id;
    public Array<Bone> children=new Array<Bone>();
    private BoneTransform boneTrans;
    private SpriteRenderer spriteRenderer;
    
    public Bone(String id) {
        this(id,new SpriteRenderer(null));
    }
    public Bone(String id,SpriteRenderer spriteRenderer) {
        this(id,new BoneTransform(),spriteRenderer);
    }
    public Bone(String id,BoneTransform transform,SpriteRenderer spriteRenderer) {
        this.id=id;
        this.boneTrans=transform;
        this.spriteRenderer=spriteRenderer;
        spriteRenderer.setTransform(boneTrans.getTransform());
    }
    public Bone(String id,Bone parent,BoneTransform transform,SpriteRenderer spriteRenderer) {
        this(id,transform,spriteRenderer);
        this.parent=parent;
        if(parent!=null)boneTrans.getTransform().setRelative(parent.boneTrans.getTransform());
    }
    public void setBoneTransform(BoneTransform transform) {
        this.boneTrans=transform;
        spriteRenderer.setTransform(boneTrans.getTransform());
    }
    public void setSpriteRenderer(SpriteRenderer spriteRenderer) {
        this.spriteRenderer=spriteRenderer;
        spriteRenderer.setTransform(boneTrans.getTransform());
    }
    public BoneTransform getBoneTransform() {
        return boneTrans;
    }
    public SpriteRenderer getSpriteRenderer() {
        return spriteRenderer;
    }
    public Bone getBone(String id) {
        if(id.equals(this.id))return this;
        for(Bone bone : children) {
            Bone result=bone.getBone(id);
            if(result!=null)return result;
        }
        return null;
    }
    public Bone addBone(Bone bone) {
        children.add(bone);
        bone.setParent(this);
        return this;
    }
    public Bone getParent() {
        if(parent==null)return this;
        return parent.getParent();
    }
    public void setParent(Bone parent) {
        this.parent=parent;
        this.getBoneTransform().getTransform().setRelative(parent.getBoneTransform().getTransform());
    }
    public void getBones(Array<Bone> array) {
        array.add(this);
        for(Bone bone : children)
            bone.getBones(array);
    }
    public Bone cpy() {
        Bone bone=new Bone(id,parent,boneTrans.cpy(),spriteRenderer.cpy());
        for(Bone entry : children) {
            bone.addBone(entry.cpy());
        }
        return bone;
    }
}