package com.hakavo.game.mechanics;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Queue;
import com.hakavo.game.*;
import com.hakavo.ineffable.core.*;

public class DialogueSystem extends GameComponent {
    public boolean centerText=true;
    public float centerOffset=0;
    public float textScale=1;
    
    public TextRenderer textRenderer;
    public Queue<Dialogue> dialogues=new Queue<Dialogue>();
    
    private GlyphLayout layout;
    private Transform transform;
    private float timeline;
    
    public DialogueSystem(boolean centerText,float centerOffset,float textScale) {
        this.centerText=centerText;
        this.centerOffset=centerOffset;
        this.textScale=textScale;
    }
    public void start() {
        layout=new GlyphLayout();
        textRenderer=this.getGameObject().getComponent(TextRenderer.class);
        transform=this.getGameObject().getComponent(Transform.class);
    }
    public void update(float delta) {
        timeline+=delta;
        float fontScale=textScale;
        if(dialogues.size>0&&timeline>=dialogues.first().duration){dialogues.removeFirst();timeline=0;}
        else if(dialogues.size>0)
        {
            Dialogue dialogue=dialogues.first();
            textRenderer.text=dialogue.text;
            float alpha=0,f=timeline/dialogue.duration;
            
            float decayTime=0.85f;
            
            if(f<decayTime)
                alpha=Interpolation.pow5.apply(0,1,f*(1f/decayTime));
            else alpha=Interpolation.pow2.apply(1,0,(f-decayTime)/(1f-decayTime));
            if(dialogue.scaleFont)fontScale+=Math.max(f-decayTime,0)*2;
            
            textRenderer.color.set(dialogue.r,dialogue.g,dialogue.b,alpha);
        }
        else textRenderer.text="";
        
        Vector2 position=Pools.obtain(Vector2.class);
        transform.matrix.getTranslation(position);
        
        transform.matrix.idt();
        if(centerText)transform.matrix.translate(-getTextWidth(textRenderer.text)*fontScale/2f,position.y);
        transform.matrix.scale(fontScale,fontScale);
        transform.matrix.translate(centerOffset,0);
        
        Pools.free(position);
    }
    private float getTextWidth(String text) {
        layout.setText(textRenderer.font,text);
        return layout.width;
    }
    public static class Dialogue {
        public String text;
        public float duration;
        public float r,g,b;
        public boolean scaleFont;
        public Dialogue(String text,float duration,float red,float green,float blue,boolean scaleFont) {
            this.text=text;
            this.duration=duration;
            this.r=red;
            this.g=green;
            this.b=blue;
        }
        public Dialogue(String text,float duration,boolean scaleFont) {
            this(text,duration,1,1,1,scaleFont);
        }
    }
}
