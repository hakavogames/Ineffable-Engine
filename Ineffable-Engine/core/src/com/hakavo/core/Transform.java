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

package com.hakavo.core;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Pools;

public class Transform extends GameComponent {
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
    @Override
    public void start() {
    }
    @Override
    public void update(float delta) {
    }
}
