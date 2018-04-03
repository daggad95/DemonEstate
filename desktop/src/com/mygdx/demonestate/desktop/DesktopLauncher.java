package com.mygdx.demonestate.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.demonestate.DemonEstate;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
		config.height = 960;
		config.title = "Demon Estate";
		config.vSyncEnabled = true;
		config.fullscreen = false;
		config.foregroundFPS = 0; // Setting to 0 disables foreground fps throttling
		config.backgroundFPS = 0;
		new LwjglApplication(new DemonEstate(), config);
	}
}
