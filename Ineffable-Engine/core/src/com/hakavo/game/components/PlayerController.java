package com.hakavo.game.components;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.physics.*;
import com.hakavo.ineffable.core.collision.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.hakavo.game.mechanics.*;
import com.hakavo.game.objects.Projectile;
import com.hakavo.ineffable.GameServices;
import com.hakavo.ineffable.assets.AssetManager;

public class PlayerController extends GameComponent {
    protected RigidBody rigidBody;
    protected BoxCollider collider;
    protected WeaponController weaponController;
    private float lastFire;
    private Array<Projectile> array=new Array<Projectile>();
    
    @Override
    public void start() {
        rigidBody=gameObject.getComponent(RigidBody.class);
        collider=gameObject.getComponent(BoxCollider.class);
        weaponController=gameObject.getComponent(WeaponController.class);
        collider.ignoreTags.add(2);
        weaponController.addWeapons(new Rock(),new Pistol(),new Shotgun(),new SMG(),new SniperRifle());
        weaponController.equipWeapon(3);
    }
    @Override
    public void update(float delta) {
        WeaponInstance weapon=weaponController.getCurrentWeapon();
        if(Gdx.input.isKeyPressed(Keys.A)) {
            sendMessage(gameObject,"go_left");
        }
        if(Gdx.input.isKeyPressed(Keys.D)) {
            sendMessage(gameObject,"go_right");
        }
        if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
            sendMessage(gameObject,"jump");
        }
        if((Gdx.input.isButtonPressed(Buttons.LEFT)||Gdx.input.isKeyPressed(Keys.F))&&
            (GameServices.getElapsedTime()-lastFire>=weapon.parent.getFireDelay()&&weapon.hasAmmo())) {
            weapon.fire();
            if(weapon.parent.getSound()!=null)
                weapon.parent.getSound().play(0.2f,MathUtils.random(0.9f,2f),-0.2f);
            lastFire=GameServices.getElapsedTime();
            OrthographicCamera camera=GameServices.getCamera();
            Vector3 mouse=camera.getPickRay(Gdx.input.getX(),Gdx.input.getY()).origin;
            Vector2 proj=new Vector2(rigidBody.position.x,rigidBody.position.y);
            Vector2 mouseDir=new Vector2(mouse.x,mouse.y).sub(proj.x,proj.y).nor();
            /*mouseDir.scl(30);
            Color color=new Color().fromHsv(MathUtils.random(0f,360f),0.75f,1);
            color.a=1;
            gameObject.getLevel().addGameObject(new Projectile(new Vector2(rigidBody.position),mouseDir.add(vel),color));*/
            weapon.parent.generateProjectiles(rigidBody.position,mouseDir,array);
            for(Projectile projectile : array)
                gameObject.getLevel().addGameObject(projectile);
        }
    }
}
