package com.mygdx.demonestate.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.demonestate.DemonEstate;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1820;
		config.height = 980;
		config.title = "Demon Estate";
		config.vSyncEnabled = true;
		new LwjglApplication(new DemonEstate(), config);
	}
}
