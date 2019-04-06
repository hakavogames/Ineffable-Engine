package com.hakavo.textadventure.core;
import com.badlogic.gdx.utils.Array;
import com.hakavo.textadventure.tokenizer.WordStream;

public class Room {
    public String description;
    public Array<Entity> entities=new Array<Entity>();
    public Array<Passage> passages=new Array<Passage>();
    
    public ErrorType performAction(Player player,WordStream sentence) {
        ErrorType error=ErrorType.UNKNOWN_ACTION;
        for(Entity entity : entities) {
            ErrorType t=entity.validate(player,sentence);
            if(t==ErrorType.OK)return t;
            else if(t==ErrorType.INVALID_ACTION)return t;
            else if(t==ErrorType.UNKNOWN_NOUN&&error==ErrorType.UNKNOWN_ACTION)
                error=ErrorType.UNKNOWN_NOUN;
        }
        return error;
    }
    public void update() {
        for(int i=0;i<entities.size;i++)
            if(entities.get(i).canRemove())
                entities.removeIndex(i);
    }
    public Passage findPassageByName(String alias) {
        for(Passage passage : passages)
            if(passage.alias.equals(alias))
                return passage;
        return null;
    }
}
