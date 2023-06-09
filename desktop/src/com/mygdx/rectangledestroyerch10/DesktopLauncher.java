package com.mygdx.rectangledestroyerch10;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mygdx.rectangledestroyerch10.RectangleDestroyerGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("Rectangle Destroyer Ch10");
		config.setWindowedMode(832, 640);
		new Lwjgl3Application(new RectangleDestroyerGame(), config);
	}
}
