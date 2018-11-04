package com.kozma.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.hakavo.game.Game;
import java.io.IOException;

public class DesktopLauncher {
	public static void main (String[] arg) throws IOException {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                config.width=1600;
                config.height=900;
                config.fullscreen=false;
                config.useGL30=true;
                config.foregroundFPS=10000;
                config.allowSoftwareMode=false;
                config.samples=16;
                config.vSyncEnabled=true;
		new LwjglApplication(new Game(), config);
	}
}
