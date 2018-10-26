package com.kozma.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kozma.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                //config.width=1600;
                //config.height=900;
                config.fullscreen=true;
                config.useGL30=true;
                config.allowSoftwareMode=false;
                config.vSyncEnabled=true;
		new LwjglApplication(new Game(), config);
	}
}
