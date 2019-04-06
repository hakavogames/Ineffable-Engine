package com.hakavo.textadventure.core;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.*;

public class ErrorManager {
    public static Array<GameError> errors=new Array<GameError>();
    public static String pickMessage(ErrorType type) {
        for(GameError error : errors)
            if(error.type==type)
                return error.messages.get(MathUtils.random(error.messages.size-1));
        return null;
    }
}
