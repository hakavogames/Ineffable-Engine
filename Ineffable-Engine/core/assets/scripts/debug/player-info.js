var container=new Window("Player info",true,true);
container.bounds.setSize(320,100);
container.bounds.setPosition(GUI.mainContainer.bounds.width-container.bounds.width,240);

var info=new Label("","pixeltype");
info.autoSize=false;
info.bounds.set(5,5,300,60);
container.add(info);

function round(number,decimals) {
    if(decimals===undefined)decimals=2;
    var p=Math.pow(10,decimals);
    return Math.round(number*p)/p;
}

GameServices.timerManager.scheduleTimer("player-debug",new Timer(new TimedTask() {
    performTask: function(engine) {
        var player=engine.getLevel().getGameObjectByName("player");
        if(player!=null) {
            var square=player.getComponent(SquareController.class);
            var weapon=player.getComponent(WeaponController.class);
            var ammo;
            if(weapon.getCurrentWeapon().parent.ammoSize==-1)ammo="infinite";
            else ammo=weapon.getCurrentWeapon().ammo+"/"+weapon.getCurrentWeapon().parent.ammoSize;
            info.setText("HP: "+round(square.health)+"/"+round(square.getMaxHealth())+"\n"+
                         "Weapon: "+weapon.getCurrentWeapon().parent.getName()+"\n"+
                         "Ammo: "+ammo);
        }
        else info.setText("[RED]Invalid target");
    }
}).setInterval(0.1));

GUI.mainContainer.add(container);
