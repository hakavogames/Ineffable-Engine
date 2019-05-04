package com.hakavo.ineffable.ui;
import com.badlogic.gdx.graphics.Color;

public class Style {
    public Color foreground=new Color(1,1,1,1);
    public Color background=new Color(0.85f,0.85f,0.85f,1);
    public boolean blur=true;
    
    public float borderWidth=0;
    public Color borderColor=new Color(1,1,1,1);
    
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
