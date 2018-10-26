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
/**
 * The Pair class holds an Object that has a key to identify it.
 * @author HakavoGames
 */
public class Pair
{
    public String key;
    public Object value;
    /**
     * Creates an empty Pair.
     */
    public Pair() {}
    /**
     * Creates a Pair.
     * @param key The key of the created Pair in form of a String.
     * @param value The Object in the Pair.
     */
    public Pair(String key,Object value) {this.key=key;this.value=value;}
    /**
     * @param c The class to compare the value's class with.
     * @return Whether or not the value's class and the c class match.
     */
    public boolean isTypeOf(Class c) {
        return value.getClass().equals(c);
    }
    /**
     * @param key The key with which to compare the Pair's key.
     * @return Whether or not the keys match.
     */
    public boolean matchesKey(String key) {
        return this.key.equals(key);
    }
}
