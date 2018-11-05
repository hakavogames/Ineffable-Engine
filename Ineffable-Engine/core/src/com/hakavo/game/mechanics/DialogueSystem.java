package com.hakavo.game.mechanics;
import com.badlogic.gdx.*;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.GameServices;
import com.hakavo.ineffable.core.*;
import com.hakavo.ineffable.core.collision.BoxCollider;
import com.hakavo.ineffable.core.collision.Collider;
import com.hakavo.ineffable.core.collision.CollisionAdapter;

public class DialogueSystem extends Renderable {
    public DialogueSystemConfiguration config=new DialogueSystemConfiguration();
    
    public TextRenderer textRenderer;
    public Array<Dialogue> dialogues=new Array<Dialogue>();
    
    private GlyphLayout layout;
    private Transform transform;
    private Matrix3 initialTransform=new Matrix3();
    private Array<BoxCollider> buttons=new Array<BoxCollider>();
    private float timeline;
    
    private Dialogue currentDialogue;
    
    public DialogueSystem(boolean centerText,float xOffset,float textScale,float finalTextScale) {
        config.set(centerText,xOffset,textScale,finalTextScale);
    }
    @Override
    public void start() {
        layout=new GlyphLayout();
        textRenderer=this.getGameObject().getComponent(TextRenderer.class);
        transform=this.getGameObject().getComponent(Transform.class);
    }
    @Override
    public void update(float delta) {
        timeline+=delta;
        float fontScale=config.textScale;
        if(currentDialogue!=null&&timeline>=currentDialogue.duration&&!currentDialogue.hasChoices())
        {
            currentDialogue.onDialogueComplete();
            if(currentDialogue.choices.size==0)setDialogue(null);
            else setDialogue(currentDialogue.choices.get(0).nextDialogue);
            timeline=0;
        }
        else if(currentDialogue!=null)
        {
            textRenderer.text=currentDialogue.text;
            if(currentDialogue.hasChoices())timeline=Math.min(timeline,currentDialogue.duration/2f);
            
            float alpha=0,f=timeline/currentDialogue.duration,decayTime=0.85f;
            
            if(f<decayTime)
                alpha=Interpolation.pow5.apply(0,1,f*(1f/decayTime));
            else alpha=Interpolation.pow2.apply(1,0,(f-decayTime)/(1f-decayTime));
            if(currentDialogue.scaleFont)fontScale+=Math.max(f-decayTime,0)*(config.fadeOutScale-config.textScale);
            textRenderer.color.set(currentDialogue.r,currentDialogue.g,currentDialogue.b,alpha);
            if(f<decayTime)transform.calculateMatrix(initialTransform);
        }
        
        Vector2 position=Pools.obtain(Vector2.class);
        transform.matrix.getTranslation(position);
        
        transform.matrix.idt();
        if(config.centerAlign)
            transform.matrix.translate(-getTextWidth(textRenderer.text)*fontScale/2f+config.xOffset,position.y);
        transform.matrix.scale(fontScale,fontScale);
        
        Pools.free(position);
    }
    private void chooseDialogue(Choice choice) {
        currentDialogue=findDialogue(choice.nextDialogue);
        choice.onChoose();
    }
    public void setDialogue(String name) {
        this.getGameObject().components.removeAll(buttons,false);
        buttons.clear();
        currentDialogue=findDialogue(name);
        if(currentDialogue==null)return;
        
        float maxLineWidth=getTextWidth(currentDialogue.text),lineHeight=textRenderer.font.getLineHeight();
        for(int i=0;i<currentDialogue.choices.size;i++)
            maxLineWidth=Math.max(maxLineWidth,getTextWidth(currentDialogue.choices.get(i).text));
        
        for(int i=0;i<currentDialogue.choices.size;i++)
        {
            Choice choice=currentDialogue.choices.get(i);
            BoxCollider box=new BoxCollider(0,-lineHeight*(i+2)-5,maxLineWidth,lineHeight);
            box.userData=choice;
            box.setCollisionAdapter(new CollisionAdapter() {
                @Override
                public void onCollisionEnter(Collider target) {
                    if(target.name.equals("mouse-pointer"))
                        chooseDialogue((Choice)parent.userData);
                }
            });
            this.getGameObject().addComponent(box);
            buttons.add(box);
        }
    }
    private float getTextWidth(String text) {
        layout.setText(textRenderer.font,text);
        return layout.width;
    }
    private Dialogue findDialogue(String name) {
        for(Dialogue dialogue : dialogues)
            if(dialogue.name.equals(name))
                return dialogue;
        return null;
    }

    @Override
    public void render(OrthographicCamera camera) {
        if(currentDialogue==null||!currentDialogue.hasChoices())return;
        ShapeRenderer sr=GameServices.getShapeRenderer();
        SpriteBatch sb=GameServices.getSpriteBatch();
        sb.end();
        
        float maxLineWidth=0,lineHeight=textRenderer.font.getLineHeight();
        for(int i=0;i<currentDialogue.choices.size;i++)
            maxLineWidth=Math.max(maxLineWidth,getTextWidth(currentDialogue.choices.get(i).nextDialogue));
        
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA,GL20.GL_ONE_MINUS_SRC_ALPHA);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setProjectionMatrix(camera.combined);
        sr.getTransformMatrix().set(initialTransform);
        
        sr.setColor(0.15f,0.15f,0.15f,0.75f);
        sr.rect(0,-lineHeight*(currentDialogue.choices.size+1)-5,
                Math.max(getTextWidth(currentDialogue.text),maxLineWidth),lineHeight*currentDialogue.choices.size);
        
        sr.end();
        sb.begin();
        sb.setTransformMatrix(sr.getTransformMatrix());
        for(int i=0;i<currentDialogue.choices.size;i++)
            textRenderer.font.draw(sb,currentDialogue.choices.get(i).text,2,-lineHeight*(i+1)-5);
    }
    @Override
    public void onGui(OrthographicCamera gui) {
    }
    public static class Dialogue {
        public Array<Choice> choices=new Array<Choice>(10);
        
        public String text;
        public String name; // mandatory
        public float duration;
        public float r,g,b;
        public boolean scaleFont;
        public Dialogue(String name,String text,float duration,float red,float green,float blue,boolean scaleFont) {
            this.name=name;
            this.text=text;
            this.duration=duration;
            this.r=red;
            this.g=green;
            this.b=blue;
            this.scaleFont=scaleFont;
        }
        public Dialogue(String name,String text,float duration,boolean scaleFont) {
            this(name,text,duration,1,1,1,scaleFont);
        }
        public Dialogue(String name,String text,float duration) {
            this(name,text,duration,true);
        }
        public Dialogue(String name,String text) {
            this(name,text,2f);
        }
        
        public void onDialogueComplete() {
        }
        public boolean hasChoices() {
            return choices.size>1;
        }
    }
    public static class Choice {
        public String text="choice";
        public String nextDialogue=""; // mandatory
        
        public Choice(String text,String nextDialogue) {
            this.text=text;
            this.nextDialogue=nextDialogue;
        }
        public void onChoose() {
        }
    }
    public static class DialogueSystemConfiguration {
        public boolean centerAlign;
        public float xOffset=0; // change this value if centerAlign is true
        public float textScale=1;
        public float fadeOutScale=1.5f; // the text scale after the fade out animation is played
        
        public DialogueSystemConfiguration(boolean centerAlign,float xOffset,float textScale,float fadeOutScale) {
            set(centerAlign,xOffset,textScale,fadeOutScale);
        }
        public DialogueSystemConfiguration() {
        }
        public void set(boolean centerAlign,float xOffset,float textScale,float fadeOutScale) {
            this.centerAlign=centerAlign;
            this.xOffset=xOffset;
            this.textScale=textScale;
            this.fadeOutScale=fadeOutScale;
        }
    }
}
