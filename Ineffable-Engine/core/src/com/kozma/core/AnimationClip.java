package com.kozma.core;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class AnimationClip
{
    public Array<TextureRegion> frames=new Array<TextureRegion>();
    public float duration=1; // seconds
    public boolean loop=false;
}
