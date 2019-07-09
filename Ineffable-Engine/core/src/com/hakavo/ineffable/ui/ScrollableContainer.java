package com.hakavo.ineffable.ui;

public class ScrollableContainer extends Container {
    public final Container content;
    
    public ScrollableContainer(float width,float height) {
        super.style.borderSides=Style.BORDER_NONE;
        super.style.blur=false;
        super.style.background.set(0,0,0,0);
        super.bounds.setSize(width,height);
        
        content=new Container();
        content.style.background.set(0,0,0,0);
        content.style.blur=false;
        super.add(content);
    }
    @Override
    public void onUpdate(float delta) {
        content.bounds.setSize(Float.MAX_VALUE,Float.MAX_VALUE);
    }
    public void scroll(float x,float y) {
        content.bounds.x+=x;
        content.bounds.y+=y;
    }
    public void setScroll(float x,float y) {
        content.bounds.setPosition(x,y);
    }
}
