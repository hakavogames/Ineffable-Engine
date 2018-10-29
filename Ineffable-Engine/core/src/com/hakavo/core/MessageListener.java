package com.hakavo.core;

public interface MessageListener {
    public void messageReceived(GameComponent sender,String message,Object... parameters);
}
