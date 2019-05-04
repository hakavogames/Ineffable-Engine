package com.hakavo.ineffable.assets;
import com.hakavo.ineffable.core.*;

public class Prefab {
    protected GameObject gameObject;
    public Prefab(GameObject gameObject) {
        //gameObject.start();
        this.gameObject=gameObject;
    }
    public String getName() {
        return gameObject.name;
    }
    public GameObject newInstance() {
        return gameObject.cpy();
    }
    @Override
    public String toString() {
        return "["+gameObject.toString()+"]";
    }
}
