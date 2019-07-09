var container=new Window("Physics Debug",true,true);
container.bounds.setSize(320,180);
container.bounds.setPosition(GUI.mainContainer.bounds.width-container.bounds.width,0);

var info=new Label("","pixeltype");
info.autoSize=false;
info.bounds.set(5,5,300,140);
container.add(info);

function round(number,decimals) {
    var p=Math.pow(10,decimals);
    return Math.round(number*p)/p;
}

GameServices.timerManager.scheduleTimer("physics-debug",new Timer(new TimedTask() {
    performTask: function(engine) {
        var player=engine.getLevel().getGameObjectByName("player");
        if(player!=null) {
            var body=player.getComponent(RigidBody.class);
            var delta=body.getDeltaTime();
            info.setText("[GRAY]Target: \""+player.name+"\"[WHITE]\n\n"+
                         "Velocity: "+round(body.getRawVelocity().len()/delta,2)+" u/s\n"+
                         "Position: "+round(body.position.x,2)+" x "+round(body.position.y,2)+"\n"+
                         "Mass: "+round(body.mass,2)+" units\n"+
                         "Density: "+round(body.density,2)+"\n"+
                         "Delta time: "+round(delta,3)+" seconds");
        }
        else info.setText("[RED]Invalid target");
    }
}).setInterval(0.1));

GUI.mainContainer.add(container);
