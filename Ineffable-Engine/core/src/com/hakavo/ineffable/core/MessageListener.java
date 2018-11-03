package com.hakavo.ineffable.core;

public interface MessageListener {
    public void messageReceived(GameObject sender,String message,Object... parameters);
}
