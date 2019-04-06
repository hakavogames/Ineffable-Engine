package com.hakavo.textadventure.core.entities;
import com.hakavo.textadventure.core.*;
import com.hakavo.textadventure.tokenizer.*;

public class Door extends Entity {
    protected Passage passage;
    private boolean locked;
    private String key;
    private boolean canLock;
    private boolean adv;
    
    public Door(boolean locked,Passage passage,String keyItemId,boolean passageAdverbMustExist) {
        this.locked=locked;
        this.passage=passage;
        this.key=keyItemId; // null=no key required
        this.adv=passageAdverbMustExist;
        if(keyItemId==null)canLock=false;
        super.description="a door";
        super.actions.addAll("lock","unlock","check");
        super.aliases.add("door");
        Dictionary.insertWords(new Word("door",WordType.NOUN));
    }
    private boolean hasKey(Player player) {
        return key==null||player.hasItem(key);
    }
    @Override
    public boolean additionalValidate(WordStream sentence) {
        sentence.reset();
        Word word=sentence.peekWord(WordType.ADVERB);
        return adv==false||(word!=null&&word.text.equals(passage.alias));
    }
    @Override
    public boolean onInteract(Player who,WordStream sentence) {
        sentence.reset();
        Word action=sentence.peekWord(WordType.VERB);
        if(action.text.equals("lock")) {
            if(locked==true||!canLock||!hasKey(who)) {
                if(!hasKey(who)&&locked==true)
                {
                    System.out.println("I need a key to lock the door.");
                    return true;
                }
                return false;
            }
            System.out.println("I've locked the door.");
            locked=true;
            passage.locked=true;
        }
        else if(action.text.equals("unlock")) {
            if(locked==false||!hasKey(who)) {
                if(!hasKey(who))
                    System.out.println("I need a key to unlock the door.");
                return true;
            }
            System.out.println("I've unlocked the door.");
            locked=false;
            passage.locked=false;
        }
        else if(action.text.equals("check")) {
            if(locked==false)System.out.println("The door is unlocked.");
            else System.out.println("The door is locked. It requires a key to open.");
        }
        return true;
    }
}
