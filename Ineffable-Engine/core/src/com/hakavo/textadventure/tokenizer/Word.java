package com.hakavo.textadventure.tokenizer;

public class Word {
    public String text;
    public WordType type;
    
    public Word() {
    }
    public Word(String text,WordType type) {
        this.text=text;
        this.type=type;
    }
    
    @Override
    public String toString() {
        return "[\""+text+"\","+type+"]";
    }
}
