package com.hakavo.textadventure.core;
import com.badlogic.gdx.utils.Array;

public class GameError {
    public ErrorType type;
    public Array<String> messages=new Array<String>();
    public GameError(ErrorType type,String... messages) {
        this.type=type;
        this.messages.addAll(messages);
    }
}
