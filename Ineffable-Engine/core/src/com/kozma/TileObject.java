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
package com.kozma;

public class TileObject {
    public Tile parent;
    public boolean visible=true;
    public int height=0;
    public Properties properties=new Properties();
    public TileObject() {}
    public TileObject(Tile parent) {this.parent=parent;}
    public TileObject(Tile parent,Properties properties) {this.parent=parent;this.properties=properties;}
}
