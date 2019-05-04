package com.hakavo.ineffable.ui;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

public class ImageButton extends Button {
    private Image image;
    public float padding;
    
    public ImageButton(Image image) {
        this(image,8);
    }
    public ImageButton(Image image,float padding) {
        super();
        this.padding=padding;
        this.image=image;
        super.add(image);
    }
    public Image getImage() {
        return image;
    }
    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        image.bounds.setPosition(padding,padding);
        super.bounds.setSize(image.bounds.getRight()+padding,image.bounds.getTop()+padding);
    }
}
