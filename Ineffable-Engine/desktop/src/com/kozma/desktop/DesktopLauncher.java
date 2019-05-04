package com.kozma.desktop;

import com.badlogic.gdx.backends.lwjgl.*;
import com.hakavo.game.EngineLauncher;
import java.awt.Toolkit;
import java.io.IOException;

public class DesktopLauncher {
    public static void main(String[] arg) throws IOException {
        LwjglApplicationConfiguration config=new LwjglApplicationConfiguration();
        Toolkit tk=Toolkit.getDefaultToolkit();
        config.width=tk.getScreenSize().width;
        config.height=tk.getScreenSize().height;
        config.fullscreen=true;
        config.useGL30=true;
        config.foregroundFPS=10000;
        config.allowSoftwareMode=false;
        config.vSyncEnabled=true;
        config.samples=4;
        new LwjglApplication(new EngineLauncher(),config);
    }
}
