package com.hakavo.ineffable.ui;

import com.badlogic.gdx.graphics.Color;

public abstract class Button extends Container {
    public static final int STATE_UP=0;
    public static final int STATE_DOWN=1;
    public static final int STATE_HOVERED=2;
    public static final int STATE_NO_LONGER_HOVERED=3;
    private int state=0;
    
    public Color onButtonDown=new Color(0.3f,0.3f,0.3f,1f);
    public Color onButtonUp=new Color(0.9f,0.9f,0.9f,1f);
    public Button() {
        super.style.setBorder(2,0.35f,0.35f,0.35f,1f);
        super.style.blur=false;
        super.style.background.set(onButtonUp);
        super.addEventListener(new EventListener() {
            @Override
            public void onButtonDown(int button) {
                style.background.set(onButtonDown);
                state=STATE_DOWN;
            }
            @Override
            public void onButtonUp(int button) {
                style.background.set(onButtonUp);
                state=STATE_UP;
            }
            @Override
            public void onHoverEnter() {
                style.background.set(onButtonDown).lerp(onButtonUp,0.75f);
                state=STATE_HOVERED;
            }
            @Override
            public void onHover() {
                style.background.set(onButtonDown).lerp(onButtonUp,0.75f);
                state=STATE_HOVERED;
            }
            @Override
            public void onHoverExit() {
                style.background.set(onButtonUp);
                state=STATE_NO_LONGER_HOVERED;
            }
        });
    }
    public int getState() {
        return state;
    }
    @Override
    public void onUpdate(float delta) {
        if(state==STATE_UP)style.background.set(onButtonUp);
    }
}
