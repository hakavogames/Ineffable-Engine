package com.hakavo.game.tasks;

import com.hakavo.ineffable.core.*;
import com.badlogic.gdx.utils.Queue;
import com.hakavo.ineffable.core.GameComponent.Copiable;

public class AgentController extends GameComponent implements Copiable {
    protected Queue<Task> tasks=new Queue<Task>();
    protected Task lastTask;
    public boolean running=true;
    
    @Override
    public void update(float delta) {
        if(running&&tasks.size>0) {
            Task task=tasks.first();
            
            if(task!=lastTask)task.onTaskAssigned();
            if(task.isActive())
                task.onTaskPerform(delta);
            else if(task.isComplete())
                tasks.removeFirst();
            
            lastTask=task;
        }
    }
    @Override
    public void start() {
    }
    
    public void assignTask(Task task) {
        task.parent=this;
        tasks.addFirst(task);
    }

    @Override
    public AgentController cpy() {
        AgentController ac=new AgentController();
        for(int i=tasks.size-1;i>=0;i--)
            ac.assignTask(tasks.get(i));
        return ac;
    }
}
