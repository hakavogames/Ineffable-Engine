package com.hakavo.ineffable.ui;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.*;

public class Label extends Container {
    public static String defaultFont="opensans";
    private static GlyphLayout glyphLayout;
    private BitmapFont font;
    private String text="";
    private float scale=1f;
    
    public Label() {
        if(glyphLayout==null)glyphLayout=new GlyphLayout();
        super.focusable=false;
        super.style=new Style(new Color(1,1,1,1),new Color(0,0,0,0),false);
    }
    public Label(String text,String fontName) {
        this();
        this.font=GameServices.getFonts().get(fontName);
        this.setText(text);
    }
    public Label(String text,BitmapFont font) {
        this(text);
        this.font=font;
    }
    public Label(String text) {
        this();
        this.font=GameServices.getFonts().get(defaultFont);
        setText(text);
    }
    
    @Override
    public void onUpdate(float delta) {
    }
    @Override
    public void onRender(OrthographicCamera camera,SpriteBatch sb) {
        float s=font.getScaleY();
        font.getData().setScale(this.scale);
        Color color=Pools.obtain(Color.class).set(font.getColor());
        sb.setColor(1,1,1,1);
        font.setColor(style.foreground);
        font.draw(sb,this.text,0,super.bounds.height);
        font.getData().setScale(s);
        font.setColor(color);
        Pools.free(color);
    }
    
    public Label setText(String text) {
        this.text=text;
        glyphLayout.setText(font,text);
        float lineHeight=font.getAscent()+font.getXHeight();
        float height=lineHeight;
        for(int i=0;i<text.length();i++)
            if(text.charAt(i)=='\n')
                height+=font.getLineHeight()+lineHeight;
        super.bounds.setSize(glyphLayout.width,height);
        return this;
    }
    public Label setScale(float scale) {
        bounds.setSize(bounds.width/this.scale*scale,bounds.height/this.scale*scale);
        this.scale=scale;
        return this;
    }
    public String getText() {
        return text;
    }
}
