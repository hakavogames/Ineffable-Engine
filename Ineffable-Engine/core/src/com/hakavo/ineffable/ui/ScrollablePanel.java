package com.hakavo.ineffable.ui;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Pools;
import com.hakavo.ineffable.assets.AssetManager;

public class ScrollablePanel extends Container {
    public float scrollAmount=5;
    
    private final float buttonSize=24;
    private final ScrollableContainer child;
    private final ImageButton up,down,left,right;
    private final Slider horizontal,vertical;
    
    public ScrollablePanel(float width,float height) {
        this(width,height,true,true);
    }
    public ScrollablePanel(float width,float height,boolean scrollVertically,boolean scrollHorizontally) {
        super.bounds.setSize(width,height);
        super.style.background.set(0,0,0,1);
        child=new ScrollableContainer(width-buttonSize,height-buttonSize);
        child.bounds.y=buttonSize;
        super.add(child);
        
        AssetManager.loadAsset("texture",Gdx.files.internal("ui/arrow-up.png"),"gui-arrow-up",true); // enable bilinear interpolation
        AssetManager.loadAsset("texture",Gdx.files.internal("ui/arrow-down.png"),"gui-arrow-down",true);
        AssetManager.loadAsset("texture",Gdx.files.internal("ui/arrow-left.png"),"gui-arrow-left",true);
        AssetManager.loadAsset("texture",Gdx.files.internal("ui/arrow-right.png"),"gui-arrow-right",true);
        
        
        left=new ImageButton(new Image("gui-arrow-left"),0);
        left.style.borderSides=Style.BORDER_NONE;
        left.getImage().style.foreground.set(0.2f,0.2f,0.2f,1f);
        left.getImage().bounds.setSize(buttonSize,buttonSize);
        
        right=new ImageButton(new Image("gui-arrow-right"),0);
        right.style.borderSides=Style.BORDER_NONE;
        right.getImage().bounds.setSize(buttonSize,buttonSize);
        right.getImage().style.foreground.set(0.2f,0.2f,0.2f,1f);
        right.bounds.setPosition(width-buttonSize*2,0);
        
        down=new ImageButton(new Image("gui-arrow-down"),0);
        down.style.borderSides=Style.BORDER_NONE;
        down.getImage().bounds.setSize(buttonSize,buttonSize);
        down.getImage().style.foreground.set(0.2f,0.2f,0.2f,1f);
        down.bounds.setPosition(width-buttonSize,buttonSize);
        
        up=new ImageButton(new Image("gui-arrow-up"),0);
        up.style.borderSides=Style.BORDER_NONE;
        up.getImage().bounds.setSize(buttonSize,buttonSize);
        up.getImage().style.foreground.set(0.2f,0.2f,0.2f,1f);
        up.bounds.setPosition(width-buttonSize,height-buttonSize);
        
        horizontal=new Slider(false,width-buttonSize*3,buttonSize,1,10);
        horizontal.bounds.x=buttonSize;
        
        vertical=new Slider(true,height-buttonSize*3,buttonSize,1,10);
        vertical.bounds.setPosition(width-buttonSize,buttonSize*2);
        
        left.addEventListener(new EventListener() {
            @Override
            public void onButtonUp(int button) {
                horizontal.scrollPercent(-scrollAmount);
            }
        });
        right.addEventListener(new EventListener() {
            @Override
            public void onButtonUp(int button) {
                horizontal.scrollPercent(scrollAmount);
            }
        });
        up.addEventListener(new EventListener() {
            @Override
            public void onButtonUp(int button) {
                vertical.scrollPercent(-scrollAmount);
            }
        });
        down.addEventListener(new EventListener() {
            @Override
            public void onButtonUp(int button) {
                vertical.scrollPercent(scrollAmount);
            }
        });
        
        if(scrollHorizontally)super.add(left,right,horizontal);
        if(scrollVertically)super.add(up,down,vertical);
    }
    
    @Override
    public void onUpdate(float delta) {
        child.bounds.setSize(super.bounds.width-buttonSize,super.bounds.height-buttonSize);
        child.bounds.y=buttonSize;
        Bounds max=getContent().getMaxBounds(Pools.obtain(Bounds.class));
        horizontal.setLimits(Math.max(0,(max.getLeft()-child.bounds.width)/child.bounds.width),
                             Math.max(0,(max.getRight()-child.bounds.width)/child.bounds.width));
        vertical.setLimits(Math.max(0,(max.getBottom()-child.bounds.height)/child.bounds.height),
                           Math.max(0,(max.getTop()-child.bounds.height)/child.bounds.height));
        Pools.free(max);
        
        float x=-horizontal.getValue()*child.bounds.width,y=-vertical.getValue()*child.bounds.height;
        if(Float.isNaN(x))x=0;
        if(Float.isNaN(y))y=0;
        child.setScroll(x,y);
    }
    
    public ScrollableContainer getScrollableContainer() {
        return child;
    }
    public Container getContent() {
        return child.content;
    }
}
