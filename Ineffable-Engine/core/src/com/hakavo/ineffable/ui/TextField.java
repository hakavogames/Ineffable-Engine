package com.hakavo.ineffable.ui;

import com.badlogic.gdx.graphics.Color;
import com.hakavo.ineffable.GameServices;
import java.awt.event.KeyEvent;

public class TextField extends Container {
    private Label text=new Label();
    private int maxLength;
    private float width;
    public float padding=2;
    public String hint="",input="";
    public TextFieldListener textFieldListener=new TextFieldListener() {
    };
    
    public TextField(int maxLength) {
        this(maxLength,"");
    }
    public TextField(int maxLength,String hint) {
        setMaxLength(maxLength);
        this.hint=hint;
        super.bounds.setSize(super.bounds.height,14);
        super.style.set(new Color(0.95f,0.95f,0.95f,1f),new Color(0.3f,0.3f,0.3f,1f),false);
        super.style.setBorder(2,0f,0f,0f,1f);
        super.addEventListener(new EventListener() {
            @Override
            public void onFocus() {
                parent.style.borderColor.set(0.25f,0.25f,0.25f,1f);
            }
            @Override
            public void onFocusLost() {
                parent.style.borderColor.set(0,0,0,1f);
            }
            @Override
            public void onKeyTyped(char c) {
                if(c=='\b'&&input.length()>0) {
                    input=input.substring(0,input.length()-1);
                }
                else if(isPrintableChar(c)&&input.length()+1<=((TextField)parent).maxLength&&textFieldListener.isValid(c))
                    input+=c;
                textFieldListener.onCharTyped(c);
            }
        });
        super.add(text);
    }
    @Override
    public void onUpdate(float delta) {
        float border=super.style.borderWidth;
        super.bounds.setSize(width+border*2+padding*2,text.bounds.height+border*2+padding*2);
        text.bounds.setPosition(border+padding,border+padding);
        if(input.length()==0&&!super.isFocused()) {
            text.style.foreground.set(0.7f,0.7f,0.7f,1f);
            text.setText(hint);
        }
        else {
            text.style.foreground.set(1,1,1,1);
            String str=input;
            if(super.isFocused()&&GameServices.getElapsedTime()%1f<0.5f)
                str+="|";
            text.setText(str);
        }
    }
    public final void setMaxLength(int length) {
        String str="";
        for(int i=0;i<length;i++)str+="A";
        this.width=text.getTextWidth(str)*1.25f;
        this.maxLength=length;
    }
    public final void setScale(float scale) {
        text.setScale(scale);
        setMaxLength(this.maxLength);
    }
    private static boolean isPrintableChar(char c) {
        Character.UnicodeBlock block=Character.UnicodeBlock.of(c);
        return (!Character.isISOControl(c))&&
                c!=KeyEvent.CHAR_UNDEFINED&&
                block!=null&&
                block!=Character.UnicodeBlock.SPECIALS;
    }
    public String getText() {
        return input;
    }
    
    public static abstract class TextFieldListener {
        public void onCharTyped(char c) {
        }
        public boolean isValid(char c) {
            return true;
        }
    }
}
