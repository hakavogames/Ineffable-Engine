package com.hakavo.textadventure.tokenizer;

public class WordStream {
    protected String sentence;
    private int pos,saved;
    
    public WordStream(String sentence) {
        this.sentence=sentence;
    }
    
    public Word nextWord() {
        String str="";
        while(peek()!=0&&Character.isAlphabetic(peek()))
            str+=next();
        next();
        return Dictionary.search(str);
    }
    public Word nextWord(WordType type) {
        while(pos<sentence.length()) {
            Word word=nextWord();
            if(word.type==type)
                return word;
        }
        return new Word("",WordType.UNKNOWN);
    }
    public Word peekWord() {
        return peekWord(1);
    }
    public Word peekWord(int n) {
        int position=pos;
        Word word=null;
        while(n-->0)
            word=nextWord();
        pos=position;
        return word;
    }
    public Word peekWord(WordType type) {
        int position=pos;
        Word word=null;
        while((word==null||word.type!=type)&&pos<sentence.length())
            word=nextWord();
        pos=position;
        return word;
    }
    public char peek() {
        if(pos>=sentence.length())return 0;
        return sentence.charAt(pos);
    }
    public String peek(int n) {
        return sentence.substring(pos,Math.min(sentence.length(),pos+n));
    }
    public char next() {
        if(pos>=sentence.length())return 0;
        return sentence.charAt(pos++);
    }
    public String next(int n) {
        int end=Math.min(sentence.length(),pos+n);
        String str=sentence.substring(pos,end);
        pos=end;
        return str;
    }
    public boolean hasMoreWords() {
        return this.pos<sentence.length();
    }
    public int getPos() {
        return this.pos;
    }
    public void mark() {
        this.saved=pos;
    }
    public void rewind() {
        this.pos=saved;
    }
    public void reset() {
        this.pos=0;
        this.saved=0;
    }
}
