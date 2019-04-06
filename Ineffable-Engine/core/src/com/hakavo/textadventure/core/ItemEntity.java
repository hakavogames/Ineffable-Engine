package com.hakavo.textadventure.core;
import com.hakavo.textadventure.tokenizer.*;

public class ItemEntity extends Entity {
    private final Item item;
    public ItemEntity(Item item) {
        this.item=item;
        super.description=item.description;
        super.actions.add("take","pick","check");
        super.aliases.add(item.id);
        Dictionary.insertWords(new Word(item.id,WordType.NOUN));
    }
    @Override
    public boolean onInteract(Player who,WordStream sentence) {
        sentence.reset();
        String verb=sentence.nextWord(WordType.VERB).text;
        if(verb.equals("check")) {
            System.out.println(Character.toUpperCase(super.description.charAt(0))+super.description.substring(1)+".");
        }
        else
        {
            who.inventory.add(item);
            System.out.println("I took the "+item.id+".");
            super.drop();
        }
        return true;
    }
}
