/*
 * Copyright 2018 .
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hakavo.ineffable.core;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;

public class Transform extends GameComponent implements GameComponent.Copiable {
    private Transform relative;
    public Matrix3 matrix=new Matrix3();
    
    public Transform() {}
    public Transform(Matrix3 copyFrom) {
        this.matrix.set(copyFrom);
    }
    public Transform(float x,float y,float sclX,float sclY,float rotation) {
        matrix.translate(x,y);
        matrix.scale(sclX,sclY);
        matrix.rotate(rotation);
    }
    public Transform(float x,float y,float sclX,float sclY) {
        this(x,y,sclX,sclY,0);
    }
    public Transform(float x,float y) {
        this(x,y,1,1);
    }
    
    public Transform setRelative(Transform relative) {
        this.relative=relative;
        return this;
    }
    public Transform getRelative() {
        return relative;
    }
    
    public Matrix3 calculateMatrix(Matrix3 out) {
        return calculateMatrix(out,true);
    }
    public Vector2 getPosition(Vector2 out) {
        Matrix3 mat=calculateMatrix(Pools.obtain(Matrix3.class));
        mat.getTranslation(out);
        Pools.free(mat);
        return out;
    }
    public Vector2 getScale(Vector2 out) {
        Matrix3 mat=calculateMatrix(Pools.obtain(Matrix3.class));
        mat.getScale(out);
        Pools.free(mat);
        return out;
    }
    private Matrix3 calculateMatrix(Matrix3 out,boolean identity)
    {
        if(identity)out.idt();
        out.mulLeft(matrix);
        if(relative!=null)relative.calculateMatrix(out,false);
        return out;
    }
    @Override
    public void start() {
    }
    @Override
    public void update(float delta) {
    }
    @Override
    public Transform cpy() {
        Transform transform=new Transform(this.matrix).setRelative(this.relative);
        transform.name=super.name;
        transform.setMessageListener(this.messageListener);
        return transform;
    }
}
