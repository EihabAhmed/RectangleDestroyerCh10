package com.mygdx.rectangledestroyerch10;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

public abstract class BaseGame extends Game {
    private static BaseGame game;

    public static LabelStyle labelStyle;

    public static TextButtonStyle textButtonStyle;

    public BaseGame() {
        game = this;
    }

    public static void setActiveScreen(BaseScreen s) {
        game.setScreen(s);
    }

    @Override
    public void create() {
        // prepare for multiple classes/stages to receive discrete input
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);


        FreeTypeFontGenerator fontGenerator =
                new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));

        FreeTypeFontParameter fontParameters = new FreeTypeFontParameter();
        fontParameters.size = 32;
        fontParameters.color = Color.WHITE;
        fontParameters.borderWidth = 2;
        fontParameters.borderColor = Color.BLACK;
        fontParameters.borderStraight = true;
        fontParameters.minFilter = Texture.TextureFilter.Linear;
        fontParameters.magFilter = Texture.TextureFilter.Linear;

        labelStyle = new LabelStyle();

        BitmapFont customFont = fontGenerator.generateFont(fontParameters);
        labelStyle.font = customFont;

        textButtonStyle = new TextButtonStyle();
        Texture buttonTex = new Texture(Gdx.files.internal("button.png"));
        NinePatch buttonPatch = new NinePatch(buttonTex, 24, 24, 24, 24);
        textButtonStyle.up = new NinePatchDrawable(buttonPatch);
        textButtonStyle.font = customFont;
        textButtonStyle.fontColor = Color.GRAY;
    }
}
