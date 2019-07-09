package com.hakavo.ineffable.ui;

public class Radio extends CheckBox {
    protected RadioGroup radioGroup;
    private final Radio current;
    
    public Radio(String text) {
        super(text);
        current=this;
        super.addEventListener(new EventListener() {
            @Override
            public void onButtonDown(int button) {
                if(radioGroup!=null)
                    radioGroup.select(current);
                checked=true;
            }
        });
    }
    public final void setRadioGroup(RadioGroup group) {
        this.radioGroup=group;
    }
}
