package com.hakavo.ineffable.ui;
import com.badlogic.gdx.utils.Array;

public class RadioGroup {
    private final Array<Radio> radios=new Array<Radio>();
    
    public int getSelectedRadio() {
        for(int i=0;i<radios.size;i++)
            if(radios.get(i).checked)
                return i;
        return -1;
    }
    public boolean isSelected(int index) {
        return radios.get(index).checked;
    }
    public void deselect() {
        for(Radio radio : radios)
            radio.checked=false;
    }
    public void select(int index) {
        deselect();
        radios.get(index).checked=true;
    }
    public void select(Radio radio) {
        deselect();
        radios.get(radios.indexOf(radio,true)).checked=true;
    }
    public void add(Radio... radios) {
        for(Radio radio : radios) {
            this.radios.add(radio);
            radio.setRadioGroup(this);
        }
    }
}
