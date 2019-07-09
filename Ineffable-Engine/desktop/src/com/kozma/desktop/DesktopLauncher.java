package com.kozma.desktop;

import com.badlogic.gdx.backends.lwjgl.*;
import java.awt.Toolkit;
import com.hakavo.game.Game;
import java.io.IOException;

public class DesktopLauncher {
    public static void main(String[] arg) throws IOException {
        LwjglApplicationConfiguration config=new LwjglApplicationConfiguration();
        //config.width=1280;config.height=1024;
        Toolkit tk=Toolkit.getDefaultToolkit();
        config.width=1920;config.height=Math.round((9f/16f)*config.width);
        //config.width=800;config.height=600;
        //config.width=tk.getScreenSize().width;config.height=tk.getScreenSize().height;
        config.resizable=false;
        config.fullscreen=true;
        config.useGL30=true;
        config.foregroundFPS=1000;
        config.allowSoftwareMode=false;
        config.vSyncEnabled=true;
        config.samples=4;
        new LwjglApplication(new Game(),config);
    }
}
