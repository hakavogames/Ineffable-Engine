package com.hakavo.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.core.collision.*;
import com.hakavo.ineffable.core.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Queue;

public class IntroGameMode implements GameMode {
    Engine engine;
    private GameMode after;
    
    public IntroGameMode(GameMode onFinish) {
        after=onFinish;
    }
    @Override
    public void init(Engine engine) {
        this.engine=engine;
        engine.camera.setToOrtho(false,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        
        GameObject main=new GameObject();
        main.addComponent(new Transform());
        main.addComponent(new TextRenderer());
        main.addComponent(new IntroBehaviour());
        
        GameObject tip=new GameObject();
        tip.addComponent(new Transform(15,25,1,1));
        tip.addComponent(new TextRenderer(GameServices.getFonts().get("pixeltype"),"Press ESC to skip",new Color(0.3f,0.3f,0.3f,1)));
        
        engine.getLevel().addGameObject(main);
        engine.getLevel().addGameObject(tip);
    }
    @Override
    public void update(float delta) {
    }
    @Override
    public void renderGui(OrthographicCamera camera) {
    }
    
    public class IntroBehaviour extends GameComponent {
        GlyphLayout layout=new GlyphLayout();
        TextRenderer textRenderer;
        Transform transform;
        
        Queue<IntroTitle> queue;
        float timeline=0;
        
        @Override
        public void start() {
            queue=new Queue<IntroTitle>();
            queue.addLast(new IntroTitle("Hakavo Games\n     presents",5,0.75f,0.25f,1,1,1));
            queue.addLast(new IntroTitle("A game powered by\n   Ineffable Engine",3.5f,0.6f,0.25f,1f,1f,1f));
            queue.addLast(new IntroTitle("An engine which is too great to be expressed in words",4,0.3f,0.05f,0.4f,0.4f,0.4f));
            
            textRenderer=this.getGameObject().getComponent(TextRenderer.class);
            transform=this.getGameObject().getComponent(Transform.class);
            textRenderer.color.set(1,1,1,1);
            textRenderer.font=GameServices.getFonts().get("adlinnaka");
            textRenderer.font.getData().markupEnabled=true;
        }
        @Override
        public void update(float delta) {
            timeline+=delta;
            float fontScale=1;
            
            if(queue.size>0&&timeline>=queue.first().duration){queue.removeFirst();timeline=0;}
            else if(queue.size>0)
            {
                IntroTitle title=queue.first();
                textRenderer.text=title.text;
                float alpha=0,f=timeline/title.duration;
                fontScale=title.textScale+title.textScale*title.addScale*f;
                
                Interpolation interp=Interpolation.pow2;
                if(f<0.5f)
                    alpha=interp.apply(0,1,f*2);
                else alpha=interp.apply(1,0,(f*2f-1f));
                textRenderer.color.set(title.r,title.g,title.b,alpha);
            }
            else finishIntro();
            
            if(Gdx.input.isKeyJustPressed(Keys.ESCAPE))finishIntro();
            
            transform.matrix.idt();
            transform.matrix.translate(800f-getTextWidth(textRenderer.text)*fontScale/2f,450f+getTextHeight()*fontScale/2f);
            transform.matrix.scale(fontScale,fontScale);
        }
        public void finishIntro() {
            this.getGameObject().getParent().destroy();
            engine.loadGameMode(after);
        }
        public float getTextWidth(String text) {
            layout.setText(textRenderer.font,text);
            return layout.width;
        }
        public float getTextHeight() {
            return textRenderer.font.getLineHeight();
        }
    }
    public static class IntroTitle {
        public String text;
        public float duration;
        public float textScale;
        public float addScale=0.25f;
        public float r,g,b;
        public IntroTitle(String text,float duration,float textScale,float addScale,float red,float green,float blue) {
            this.text=text;
            this.duration=duration;
            this.textScale=textScale;
            this.addScale=addScale;
            this.r=red;
            this.g=green;
            this.b=blue;
        }
    }
}
