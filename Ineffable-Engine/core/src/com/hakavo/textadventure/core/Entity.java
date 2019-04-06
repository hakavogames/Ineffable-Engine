package com.hakavo.textadventure.core;
import com.badlogic.gdx.utils.Array;
import com.hakavo.textadventure.tokenizer.*;

public abstract class Entity {
    public String description;
    public Array<String> aliases=new Array<String>();
    public Array<String> actions=new Array<String>();
    public EntityType type;
    private boolean remove;
    
    public ErrorType validate(Player player,WordStream sentence) {
        sentence.reset();
        boolean hasVerb=actions.contains(sentence.nextWord(WordType.VERB).text,false);
        boolean hasNoun=aliases.contains(sentence.nextWord(WordType.NOUN).text,false);
        if(additionalValidate(sentence)==false)return ErrorType.UNKNOWN_NOUN;
        if(hasVerb&&hasNoun)
        {
            sentence.reset();
            if(this.onInteract(player,sentence)==false)
                return ErrorType.INVALID_ACTION;
            return ErrorType.OK;
        }
        else if(!hasVerb)
            return ErrorType.UNKNOWN_ACTION;
        else
            return ErrorType.UNKNOWN_NOUN;
    }
    public boolean additionalValidate(WordStream sentence) {
        return true;
    }
    public abstract boolean onInteract(Player who,WordStream sentence);
    protected void drop() {
        this.remove=true;
    }
    public boolean canRemove() {
        return this.remove;
    }
}
