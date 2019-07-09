package com.hakavo.game.components;
import com.hakavo.ineffable.core.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.assets.AssetManager;
import com.hakavo.ineffable.assets.Prefab;
import com.hakavo.ineffable.core.collision.*;
import com.hakavo.ineffable.core.physics.*;

public class SnackController extends GameComponent implements GameComponent.Copiable {
    protected CircleCollider collider;
    protected SpriteRenderer spriteRenderer;
    protected Transform transform;

    @Override
    public void start() {
        collider=gameObject.getComponent(CircleCollider.class);
        spriteRenderer=gameObject.getComponent(SpriteRenderer.class);
        transform=gameObject.getComponent(Transform.class);
        transform.matrix.translate(MathUtils.random(-5f,5f),MathUtils.random(-5f,5f));
        spriteRenderer.setScaleToUnit(true);
        collider.radius=MathUtils.random(0.008f,0.013f);
        collider.tags.add(1);
        collider.ignoreTags.add(1);
        spriteRenderer.size=collider.radius*2;
        
        collider.setCollisionAdapter(new CollisionAdapter() {
            @Override
            public void onCollisionEnter(Collider target) {
                sendMessage(target.getGameObject(),"score",collider.radius*100);
                GameObject snack=AssetManager.getAsset("snack",Prefab.class).newInstance();
                gameObject.getLevel().addGameObject(snack);
                gameObject.destroy();
            }
        });
    }
    @Override
    public void update(float delta) {
    }
    @Override
    public SnackController cpy() {
        return new SnackController();
    }
}
