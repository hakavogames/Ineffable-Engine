package com.hakavo.core;

public interface MessageListener {
    public void messageReceived(GameObject sender,String message,Object... parameters);
}
