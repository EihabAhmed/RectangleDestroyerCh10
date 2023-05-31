package com.mygdx.rectangledestroyerch10;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Wall extends BaseActor {
    public Wall(float x, float y, float width, float height, Stage s) {
        super(x, y, s);
        loadTexture("white-square.png");
        setSize(width, height);
        setColor(Color.GRAY);
        setBoundaryRectangle();
    }
}
