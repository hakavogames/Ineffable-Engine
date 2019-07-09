package com.hakavo.ineffable.core;
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.*;
import com.hakavo.ineffable.*;
import com.hakavo.ineffable.assets.*;

public class Trail extends Renderable {
    public float width;
    
    public Color start,end; // blending parameters
    public float blendExponent;
    public Color primary;
    public boolean useTexture=true;
    
    private ShaderProgram shader;
    private Texture texture;
    private Queue<Vector2> history;
    private Mesh mesh;
    private float[] vertices;
    private short[] indices;
    private final int length;
    
    protected Transform transform;
    
    public Trail(float width) {
        this(width,new Color(1,1,1,1));
    }
    public Trail(float width,Color primary) {
        this(width,15,primary);
    }
    public Trail(float width,int length,Color primary) {
        this(width,length,new Color(1,1,1,1),new Color(1,1,1,0),primary);
    }
    public Trail(float width,int length,Color start,Color end) {
        this(width,length,start,end,new Color(1,1,1,1));
    }
    public Trail(float width,int length,Color start,Color end,Color primary) {
        this(width,length,start,end,1.5f,primary);
    }
    public Trail(float width,int length,Color start,Color end,float blendExponent,Color primary) {
        this.width=width;
        this.length=length+2;
        history=new Queue<Vector2>(this.length);
        vertices=new float[this.length*16];
        indices=new short[this.length*16];
        this.start=start;
        this.end=end;
        this.primary=primary;
        this.blendExponent=blendExponent;
        for(short i=0;i<indices.length;i++)
            indices[i]=i;
    }
    
    @Override
    public void start() {
        shader=AssetManager.loadAsset("shader",Gdx.files.internal("shaders/trail"),"shader-trail");
        texture=AssetManager.loadAsset("texture",Gdx.files.internal("sprites/gradient.png"),"texture-gradient");
        transform=gameObject.getComponent(Transform.class);
        mesh=new Mesh(false,1024,1024*8,
            new VertexAttribute(Usage.Position,2,"a_position"),
            new VertexAttribute(Usage.ColorUnpacked,4,"a_color"),
            new VertexAttribute(Usage.TextureCoordinates,2,"a_texCoords")
        );
    }
    @Override
    public void render(OrthographicCamera camera) {
        Matrix4 identity=Pools.obtain(Matrix4.class).idt();
        GameServices.getSpriteBatch().end();
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA,GL30.GL_ONE_MINUS_SRC_ALPHA);
        shader.begin();
        shader.setUniformMatrix("u_projTrans",camera.combined);
        shader.setUniformf("u_color",primary);
        if(useTexture==true) {
            shader.setUniformi("u_texture",texture.getTextureObjectHandle());
            shader.setUniformf("u_noTexture",0,0,0,0);
            Gdx.gl.glActiveTexture(GL30.GL_TEXTURE0+texture.getTextureObjectHandle());
            Gdx.gl.glBindTexture(GL30.GL_TEXTURE_2D,texture.getTextureObjectHandle());
            Gdx.gl.glActiveTexture(GL30.GL_TEXTURE0);
        }
        else shader.setUniformf("u_noTexture",1,1,1,1);
        mesh.render(shader,GL30.GL_TRIANGLE_STRIP);
        shader.end();
        Gdx.gl.glDisable(GL30.GL_BLEND);
        GameServices.getSpriteBatch().begin();
        Pools.free(identity);
    }
    @Override
    public void update(float delta) {
        if(history.size==length) {
            Pools.free(history.get(history.size-1));
            history.removeLast();
        }
        history.addFirst(transform.getPosition(Pools.obtain(Vector2.class)));
        Vector2 tmp=Pools.obtain(Vector2.class);
        Color foo=Pools.obtain(Color.class);
        for(int i=0;i<history.size-2;i++) {
            float f=(float)(1f/(history.size-2)*i);
            perpendicular(tmp.set(history.get(i+1)).sub(history.get(i)).nor()).scl(width*(1-f));
            foo.set(start).lerp(end,(float)Math.pow(f,blendExponent));
            vertices[i*16]=history.get(i).x+tmp.x;
            vertices[i*16+1]=history.get(i).y+tmp.y;
            vertices[i*16+2]=foo.r;vertices[i*16+3]=foo.g;vertices[i*16+4]=foo.b;vertices[i*16+5]=foo.a;
            vertices[i*16+6]=0;
            vertices[i*16+7]=1;
            
            vertices[i*16+8]=history.get(i).x-tmp.x;
            vertices[i*16+9]=history.get(i).y-tmp.y;
            vertices[i*16+10]=foo.r;vertices[i*16+11]=foo.g;vertices[i*16+12]=foo.b;vertices[i*16+13]=foo.a;
            vertices[i*16+14]=0;
            vertices[i*16+15]=0;
        }
        Pools.free(tmp);
        Pools.free(foo);
        for(short i=0;i<indices.length;i++)indices[i]=i;
        mesh.setVertices(vertices,0,Math.max(history.size*16-32,0));
        mesh.setIndices(indices,0,Math.max(history.size*2-4,0));
    }
    @Override
    public void onDestroy() {
        mesh.dispose();
    }
    private Vector2 perpendicular(Vector2 vec) {
        return vec.set(vec.y,-vec.x);
    }
}
