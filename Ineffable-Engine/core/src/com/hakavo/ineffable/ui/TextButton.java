package com.hakavo.ineffable.ui;

public class TextButton extends Button {
    private Label label;
    public float padding;
    
    public TextButton(String text) {
        this(text,8);
    }
    public TextButton(String text,float padding) {
        super();
        this.padding=padding;
        label=new Label(text);
        super.add(label);
        updateLabel();
        super.addEventListener(new EventListener() {
            public void onButtonDown(int button) {
                updateLabel();
            }
            public void onButtonUp(int button) {
                updateLabel();
            }
        });
    }
    public Label getLabel() {
        return label;
    }
    private void updateLabel() {
        float median=(1-style.background.r+1-style.background.g+1-style.background.b)/3f;
        label.style.foreground.set(median,median,median,1);
    }
    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        label.bounds.setPosition(padding,padding);
        super.bounds.setSize(label.bounds.getRight()+padding,label.bounds.getTop()+padding);
    }
}
