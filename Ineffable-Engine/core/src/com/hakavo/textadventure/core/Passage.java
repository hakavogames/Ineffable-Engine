package com.hakavo.textadventure.core;
import com.hakavo.textadventure.tokenizer.*;

public class Passage {
    public String alias;
    public Room destination;
    public boolean locked;
    public Passage(String alias,Room destination,boolean locked) {
        this.alias=alias;
        this.destination=destination;
        this.locked=locked;
        Dictionary.insertWords(new Word(alias,WordType.ADVERB));
    }
}
