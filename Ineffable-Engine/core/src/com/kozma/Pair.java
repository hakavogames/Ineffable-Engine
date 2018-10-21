package com.kozma;

public class Pair
{
    public String key;
    public Object value;
    public Pair() {}
    public Pair(String key,Object value) {this.key=key;this.value=value;}
    public boolean isTypeOf(Class c) {
        return value.getClass().equals(c);
    }
    public boolean matchesKey(String key) {
        return this.key.equals(key);
    }
}
