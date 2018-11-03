/*
 * Copyright 2018 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hakavo.ineffable.core;

public abstract class GameComponent
{
    protected GameObject gameObject;
    protected MessageListener messageListener;
    public String name="";
    
    public <T extends GameComponent> void copyFrom(T copyFrom) {
        this.gameObject=copyFrom.gameObject;
        this.messageListener=copyFrom.messageListener;
        this.name=copyFrom.name;
    }
    
    public abstract void update(float delta);
    public abstract void start();
    
    public final void setGameObject(GameObject gameObject) {this.gameObject=gameObject;}
    public final void setMessageListener(MessageListener messageListener) {this.messageListener=messageListener;}
    public final GameObject getGameObject() {return gameObject;}
    public final MessageListener getMessageListener() {return messageListener;}
    
    public final void sendMessage(GameObject target,String message,Object... parameters) {
        target.messageReceived(this.getGameObject(),message,parameters);
    }
    
    public void onDestroy() {
    }
    
    public static interface Copiable {
        public <T extends GameComponent> T cpy();
    }
}
