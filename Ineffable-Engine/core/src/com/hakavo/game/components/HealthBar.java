package com.hakavo.game.components;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.assets.*;

public class HealthBar extends Renderable {
    public float width=0.2f;
    public float yOffset=0.5f;
    public float health=100;
    public float maxHealth=100;
    protected Transform transform;
    private Vector2 scale=new Vector2();
    
    public HealthBar() {
        
    }

    @Override
    public void start() {
        transform=gameObject.getComponent(Transform.class);
    }
    @Override
    public void update(float delta) {
    }
    @Override
    public void render(OrthographicCamera camera) {
        SpriteBatch spriteBatch=GameServices.getSpriteBatch();
        spriteBatch.end();
        
        Matrix3 mat=transform.calculateMatrix(new Matrix3());
        mat.getScale(scale);
        float ratio=scale.y/scale.x;
        
        ShapeRenderer shape=GameServices.getShapeRenderer();
        shape.setTransformMatrix(new Matrix4().set(mat));
        shape.setProjectionMatrix(GameServices.getCamera().combined);
        shape.setAutoShapeType(true);
        shape.begin();
        shape.set(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.1f,0.1f,0.1f,1f);
        shape.rect(-0.7f,-0.7f,1.4f,1.4f);
        shape.setColor(0.4f,1f,0.3f,1f);
        shape.rect(-0.5f-ratio/2f,-0.5f,health/maxHealth+ratio*2,1f);
        shape.end();
        
        spriteBatch.begin();
    }
}
