package com.hakavo.game.ui;
import com.hakavo.ineffable.ui.*;

public class Link extends Label {
    public LinkListener listener;
    
    public Link(String text) {
        this(text,null);
    }
    public Link(String text,LinkListener linkListener) {
        super(text,"pixeltype");
        super.focusable=true;
        this.listener=linkListener;
        super.addEventListener(new EventListener() {
            @Override
            public void onButtonUp(int button) {
                if(listener!=null)
                    listener.onClick();
            }
        });
    }
}
