package com.hakavo.textadventure.core;
import com.badlogic.gdx.utils.*;
import com.hakavo.textadventure.tokenizer.*;

public class Player {
    public Array<Item> inventory=new Array<Item>();
    public Room room;
    
    public boolean hasItem(String id) {
        return getItem(id)!=null;
    }
    public Item getItem(String id) {
        for(Item item : inventory)
            if(item.id.equals(id))
                return item;
        return null;
    }
    public ErrorType performAction(WordStream sentence) {
        sentence.reset();
        Word verb=sentence.nextWord(WordType.VERB);
        if(verb.text.equals("look")) {
            System.out.println(room.description);
            if(room.entities.size==0)
                System.out.println("There is nothing here.");
            else if(room.entities.size==1)
                System.out.println("I can see "+room.entities.get(0).description+".");
            else {
                System.out.print("I can see ");
                for(int i=0;i<room.entities.size;i++) {
                    if(i<room.entities.size-2)
                        System.out.print(room.entities.get(i).description+", ");
                    else if(i<room.entities.size-1)
                        System.out.print(room.entities.get(i).description+" ");
                    else System.out.println("and "+room.entities.get(i).description+".");
                }
            }
            return ErrorType.OK;
        }
        else if(verb.text.equals("check"))
        {
            Word noun=sentence.peekWord(WordType.NOUN);
            if(noun!=null&&noun.text.equals("inventory"))
            {
                if(inventory.size==0)
                    System.out.println("My inventory is empty.");
                else if(inventory.size==1)
                    System.out.println("I have "+inventory.get(0).description+".");
                else {
                    System.out.print("I have ");
                    for(int i=0;i<inventory.size;i++) {
                        if(i<inventory.size-2)
                            System.out.print(inventory.get(i).description+", ");
                        else if(i<inventory.size-1)
                            System.out.print(inventory.get(i).description+" ");
                        else System.out.println("and "+inventory.get(i).description+".");
                    }
                }
                return ErrorType.OK;
            }
        }
        else if(verb.text.equals("go"))
        {
            Word adv=sentence.peekWord(WordType.ADVERB);
            if(adv!=null) {
                Passage passage=room.findPassageByName(adv.text);
                if(passage!=null&&!passage.locked) {
                    this.room=passage.destination;
                    System.out.println(passage.destination.description);
                    return ErrorType.OK;
                }
                else return ErrorType.INVALID_ACTION;
            }
            else return ErrorType.INVALID_ACTION;
        }
        return ErrorType.UNKNOWN_ACTION;
    }
}
