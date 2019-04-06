package com.kozma.desktop;

import com.badlogic.gdx.backends.lwjgl.*;
import com.hakavo.game.Game;
import com.hakavo.textadventure.tokenizer.*;
import java.io.IOException;

public class DesktopLauncher {
    public static void main(String[] arg) throws IOException {
        LwjglApplicationConfiguration config=new LwjglApplicationConfiguration();
        //config.width=1066;config.height=600;
        //config.width=1280;config.height=1024;
        config.width=1200;config.height=850;
        config.fullscreen=false;
        config.useGL30=true;
        config.foregroundFPS=10000;
        config.allowSoftwareMode=false;
        config.vSyncEnabled=true;
        config.samples=4;
        LwjglApplication app=new LwjglApplication(new Game(),config);
    }
}
