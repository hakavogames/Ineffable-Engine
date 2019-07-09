package com.hakavo.ineffable.utils;
import com.badlogic.gdx.utils.ArrayMap;
import com.hakavo.ineffable.Engine;

public class TimerManager {
    private final ArrayMap<String,Timer> timers=new ArrayMap<String,Timer>();
    private final Engine engine;
    
    public TimerManager(Engine parent) {
        this.engine=parent;
    }
    
    public void addTimer(String name,Timer timer) {
        timer.setParent(this);
        timers.put(name,timer);
    }
    public void scheduleTimer(String name,Timer timer) {
        addTimer(name,timer);
        timer.start();
    }
    public boolean hasTimer(String name) {
        return timers.get(name)==null;
    }
    public Timer getTimer(String name) {
        return timers.get(name);
    }
    public void clearTimers() {
        timers.clear();
    }
    public Engine getEngine() {
        return engine;
    }
    
    public void update(float delta) {
        for(int i=0;i<timers.size;i++) {
            Timer timer=timers.getValueAt(i);
            if(timer.isDead())timers.removeIndex(i);
            else timer.update(delta);
        }
    }
}
