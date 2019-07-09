package com.hakavo.ineffable.utils;

public class Timer {
    public TimedTask task;
    public float delay,initialDelay;
    private float accumulator;
    private boolean running,loop,dead;
    private TimerManager parent;
    private int count;
    
    public Timer(TimedTask task) {
        this(task,1);
    }
    public Timer(TimedTask task,float delay) {
        this(task,delay,delay,false);
    }
    public Timer(TimedTask task,float initialDelay,float delay,boolean loop) {
        this.task=task;
        this.delay=delay;
        this.initialDelay=initialDelay;
        this.loop=loop;
    }
    
    public Timer setDelay(float delay) {
        setInterval(delay);
        this.loop=false;
        return this;
    }
    public Timer setInterval(float delay) {
        return setInterval(delay,delay);
    }
    public Timer setInterval(float initialDelay,float delay) {
        this.delay=delay;
        this.initialDelay=initialDelay;
        this.loop=true;
        return this;
    }
    
    public void reset() {
        running=false;
        dead=false;
        count=0;
        accumulator=0;
    }
    public void start() {
        running=true;
    }
    public void pause() {
        running=false;
    }
    public boolean isRunning() {
        return running;
    }
    public boolean isDead() {
        return dead;
    }
    public void stop() {
        dead=true;
    }
    
    public void setParent(TimerManager parent) {
        this.parent=parent;
    }
    
    public void update(float delta) {
        if(dead)return;
        if(running)accumulator+=delta;
        if((count==0&&accumulator>=initialDelay)||(count>0&&accumulator>=delay)) {
            accumulator=0f;
            task.performTask(parent.getEngine());
            count++;
            if(!loop)stop();
        }
    }
}
