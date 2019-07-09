package com.hakavo.game.objects;
import com.hakavo.game.components.*;
import com.hakavo.game.mechanics.Weapon;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.BoxCollider;
import com.hakavo.ineffable.core.physics.RigidBody;

public class Square extends Joint {
    public Square() {
        this(1f);
    }
    public Square(float size,Weapon... weapons) {
        this(size,1,weapons);
    }
    public Square(float size,float density,Weapon... weapons) {
        super.name="dummy";
        addComponent(new Transform());
        addComponent(new SpriteRenderer(new Sprite2D("pixel"),size));
        addComponent(new BoxCollider(-size/2,-size/2,size,size));
        addComponent(new RigidBody(density));
        addComponent(new MovementController());
        addComponent(new SquareController());
        addComponent(new WeaponController(weapons));
        
        getComponent(BoxCollider.class).tags.add(1);
    }
}
