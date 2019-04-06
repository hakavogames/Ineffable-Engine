package com.hakavo.textadventure.tokenizer;
import com.badlogic.gdx.utils.*;

public class Dictionary {
    private static final Array<Word> entries=new Array<Word>();
    
    public static void init() {
        Dictionary.insertWords(new Word("take",WordType.VERB),
                               new Word("pick",WordType.VERB),
                               new Word("go",WordType.VERB),
                               new Word("look",WordType.VERB),
                               new Word("check",WordType.VERB),
                               new Word("inventory",WordType.NOUN),
                               new Word("lock",WordType.VERB),
                               new Word("unlock",WordType.VERB),
                               new Word("check",WordType.VERB));
    }
    public static Word search(String str) {
        for(Word word : entries)
            if(word.text.equals(str))
                return word;
        return new Word("",WordType.UNKNOWN);
    }
    public static void insertWords(Word... words) {
        for(Word word : words)
            if(!hasWord(word))
                entries.add(word);
    }
    public static boolean hasWord(Word word) {
        for(Word entry : entries)
            if(entry.type==word.type&&entry.text.equals(word.text))
                return true;
        return false;
    }
}
