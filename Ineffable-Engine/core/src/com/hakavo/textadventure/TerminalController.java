package com.hakavo.textadventure;
import com.hakavo.textadventure.core.GameError;
import com.hakavo.textadventure.core.ErrorManager;
import com.hakavo.textadventure.core.ErrorType;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.math.MathUtils;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.core.*;

import com.hakavo.textadventure.Terminal.*;
import com.hakavo.textadventure.core.*;
import com.hakavo.textadventure.core.entities.*;
import com.hakavo.textadventure.tokenizer.*;

public class TerminalController extends GameComponent implements TerminalInputController.InputListener {
    protected TerminalInputController in;
    protected TerminalOutput out;
    
    private Player player;
    
    public TerminalController() {
        
        ErrorManager.errors.add(new GameError(ErrorType.EMPTY_SENTENCE,new String[] {
            "Don't be shy.",
            "Use your words.",
            "Speak up.",
            "Project your voice.",
            "Tell me what should I do."
        }));
        String[] unknownWord=new String[]{
            "What?",
            "I can't understand you.",
            "That makes no sense to me.",
            "Say again?",
            "Speak clearly."
        };
        ErrorManager.errors.add(new GameError(ErrorType.UNKNOWN_ACTION,unknownWord),
                                new GameError(ErrorType.UNKNOWN_NOUN,unknownWord));
        ErrorManager.errors.add(new GameError(ErrorType.INVALID_ACTION,new String[] {
            "I can't do that.",
            "Sorry, I can't do that."
        }));
        Dictionary.init();
        
        Room outside=new Room();
        Room room=new Room();
        outside.description="I am outside, at the edge of a small village. The sun is shining on the old house next to me.";
        outside.passages.add(new Passage("inside",room,false));
        
        room.description="I am in an old, small house. It smells like mold here.";
        room.passages.add(new Passage("outside",outside,true));
        room.entities.add(new Door(true,room.findPassageByName("outside"),"key",false));
        room.entities.add(new ItemEntity(new Item("key","a silver key")));
        player=new Player();
        player.room=room;
    }
    @Override
    public void start() {
        in=((Joint)getGameObject()).getGameObject(TerminalInput.class).getComponent(TerminalInputController.class);
        out=getGameObject().getComponent(TerminalOutput.class);
        in.readLine(this);
    }
    @Override
    public void update(float delta) {
    }
    @Override
    public void onTextRead(String input) {
        //out.reset();
        //System.out.print(" ");
        parse(input);
        in.readLine(this);
    }
    private void parse(String sentence) {
        String[] words=sentence.split(" ");
        if(words.length==0||sentence.length()==0)System.out.println(ErrorManager.pickMessage(ErrorType.EMPTY_SENTENCE));
        else
        {
            sentence=sentence.toLowerCase();
            WordStream stream=new WordStream(sentence);
            ErrorType error=player.performAction(stream);
            if(error==ErrorType.UNKNOWN_ACTION||error==ErrorType.UNKNOWN_NOUN) {
                ErrorType objectError=player.room.performAction(player,stream);
                if(objectError!=ErrorType.OK)
                    System.out.println(ErrorManager.pickMessage(objectError));
            }
            else if(error!=ErrorType.OK)
                System.out.println(ErrorManager.pickMessage(error));
            player.room.update();
        }
    }
}