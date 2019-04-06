package com.hakavo.game.ai;

import com.hakavo.ineffable.GameServices;

public abstract class Task {
    protected AgentController parent;
    protected boolean complete;
    protected float beginTime;
    public float beginDelay=0; // in seconds
    
    private float delay;
    private float delayTime;
    
    public abstract void onTaskAssigned();
    public abstract void onTaskPerform(float delta);
    public abstract void onTaskCompleted();
    public abstract void onTaskReset();
    
    public final void init(AgentController agent) {
        this.parent=agent;
        beginTime=GameServices.getElapsedTime();
        complete=false;
        onTaskAssigned();
    }
    public final void update(float delta) {
        if(isActive())
            onTaskPerform(delta);
    }
    public final void complete() {
        complete=true;
        onTaskCompleted();
    }
    public final void reset() {
        complete=false;
        onTaskReset();
    }
    public final void setDelay(float seconds) {
        delay=seconds;
        delayTime=GameServices.getElapsedTime();
    }
    public final boolean isComplete() {
        return complete;
    }
    public final boolean isActive() {
        return beginTime+beginDelay<GameServices.getElapsedTime()&&delayTime+delay<GameServices.getElapsedTime()&&!complete;
    }
}
