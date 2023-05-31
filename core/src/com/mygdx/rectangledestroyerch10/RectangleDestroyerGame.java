package com.mygdx.rectangledestroyerch10;

public class RectangleDestroyerGame extends BaseGame {
	public void create() {
		super.create();
		setActiveScreen(new LevelScreen());
	}
}
