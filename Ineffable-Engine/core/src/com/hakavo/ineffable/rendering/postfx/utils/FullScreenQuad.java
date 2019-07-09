package com.hakavo.ineffable.rendering.postfx.utils;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;

public class FullScreenQuad {
    public static Mesh mesh;
    
    public static Mesh generate() {
        mesh=new Mesh(true,4,6,new VertexAttribute(Usage.Position,2,"a_position"),
                               new VertexAttribute(Usage.TextureCoordinates,2,"a_texCoord0"));
        mesh.setVertices(new float[] {
            -1,-1,0,0,
            1,-1,1,0,
            1,1,1,1,
            -1,1,0,1
        });
        mesh.setIndices(new short[] {
            0,1,2,
            0,2,3
        });
        
        return mesh;
    }
    public static Mesh getQuad() {
        if(mesh==null)return generate();
        else return mesh;
    }
}
