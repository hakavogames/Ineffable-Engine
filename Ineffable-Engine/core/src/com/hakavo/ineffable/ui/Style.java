package com.hakavo.ineffable.ui;
import com.badlogic.gdx.graphics.Color;

public class Style {
    public static final int BORDER_NONE=0;
    public static final int BORDER_TOP=1;
    public static final int BORDER_BOTTOM=2;
    public static final int BORDER_LEFT=4;
    public static final int BORDER_RIGHT=8;
    public static final int BORDER_ALL=15;
    
    public Color foreground=new Color(1,1,1,1);
    public Color background=new Color(0.15f,0.15f,0.15f,1);
    public boolean blur;
    
    public float borderWidth=0;
    public Color borderColor=new Color(1,1,1,1);
    public int borderSides=BORDER_ALL; // use bitwise OR here
    
    public Style() {
    }
    public Style(Color foreground,Color background,boolean useBlurredBackground) {
        set(foreground,background,useBlurredBackground);
    }
    public void set(Color foreground,Color background,boolean useBlurredBackground) {
        this.foreground=foreground;
        this.background=background;
        this.blur=useBlurredBackground;
    }
    
    public void setBorder(float width,float r,float g,float b,float a) {
        this.borderWidth=width;
        this.borderColor.set(r,g,b,a);
    }
}
