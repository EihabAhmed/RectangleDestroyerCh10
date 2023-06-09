package com.mygdx.rectangledestroyerch10;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LevelScreen extends BaseScreen {
    Paddle paddle;
    Ball ball;

    int score;
    int balls;
    Label scoreLabel;
    Label ballsLabel;
    Label messageLabel;

    Sound bounceSound;
    Sound brickBumpSound;
    Sound wallBumpSound;
    Sound itemAppearSound;
    Sound itemCollectSound;
    Music backgroundMusic;

    @Override
    public void initialize() {
        camera.setToOrtho(false, 832, 640);

        TilemapActor tma = new TilemapActor("map.tmx", mainStage);

        for (MapObject obj : tma.getTileList("Wall")) {
            MapProperties props = obj.getProperties();
            new Wall((float) props.get("x"), (float) props.get("y"), (float) props.get("width"),
                    (float) props.get("height"), mainStage);
        }

        for (MapObject obj : tma.getTileList("Brick")) {
            MapProperties props = obj.getProperties();
            Brick b = new Brick((float) props.get("x"), (float) props.get("y"), mainStage);
            b.setSize((float) props.get("width"), (float) props.get("height"));
            b.setBoundaryRectangle();

            String colorName = (String) props.get("Color");
            if (colorName.equals("Red"))
                b.setColor(Color.RED);
            else if (colorName.equals("Orange"))
                b.setColor(Color.ORANGE);
            else if (colorName.equals("Yellow"))
                b.setColor(Color.YELLOW);
            else if (colorName.equals("Green"))
                b.setColor(Color.GREEN);
            else if (colorName.equals("Blue"))
                b.setColor(Color.BLUE);
            else if (colorName.equals("Purple"))
                b.setColor(Color.PURPLE);
            else if (colorName.equals("White"))
                b.setColor(Color.WHITE);
            else if (colorName.equals("Gray"))
                b.setColor(Color.GRAY);
        }

        MapObject startPoint = tma.getRectangleList("Start").get(0);
        MapProperties props = startPoint.getProperties();
        paddle = new Paddle((float) props.get("x"), (float) props.get("y"), mainStage);

        ball = new Ball(0, 0, mainStage);

        score = 0;
        balls = 3;
        scoreLabel = new Label("Score: " + score, BaseGame.labelStyle);
        ballsLabel = new Label("Balls: " + balls, BaseGame.labelStyle);
        messageLabel = new Label("Click to start", BaseGame.labelStyle);
        messageLabel.setColor(Color.CYAN);

        uiTable.pad(5);
        uiTable.add(scoreLabel);
        uiTable.add().expandX();
        uiTable.add(ballsLabel);
        uiTable.row();
        uiTable.add(messageLabel).colspan(3).expandY();

        bounceSound = Gdx.audio.newSound(Gdx.files.internal("boing.wav"));
        brickBumpSound = Gdx.audio.newSound(Gdx.files.internal("bump.wav"));
        wallBumpSound = Gdx.audio.newSound(Gdx.files.internal("bump-low.wav"));
        itemAppearSound = Gdx.audio.newSound(Gdx.files.internal("swoosh.wav"));
        itemCollectSound = Gdx.audio.newSound(Gdx.files.internal("pop.wav"));

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("Rollin-at-5.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.50f);
        backgroundMusic.play();
    }

    @Override
    public void update(float dt) {
        Vector3 touchPos = new Vector3();
        touchPos.set(Gdx.input.getX(), 0, 0);
        camera.unproject(touchPos);
        paddle.setX(touchPos.x - paddle.getWidth() / 2);
        paddle.boundToWorld();

        if (ball.isPaused()) {
            ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
            ball.setY(paddle.getY() + paddle.getHeight() / 2 + ball.getHeight() / 2);
        }

        for (BaseActor wall : BaseActor.getList(mainStage, "com.mygdx.rectangledestroyerch10.Wall")) {
            if (ball.overlaps(wall)) {
                ball.bounceOff(wall);
                wallBumpSound.play();
            }
        }

        for (BaseActor brick : BaseActor.getList(mainStage, "com.mygdx.rectangledestroyerch10.Brick")) {
            if (ball.overlaps(brick)) {
                ball.bounceOff(brick);
                brickBumpSound.play();
                brick.remove();
                score += 100;
                scoreLabel.setText("Score: " + score);

                float spawnProbability = 20;
                if (MathUtils.random(0, 100) < spawnProbability) {
                    Item i = new Item(0, 0, mainStage);
                    i.centerAtActor(brick);
                    itemAppearSound.play();
                }

                if (BaseActor.getList(mainStage, "com.mygdx.rectangledestroyerch10.Brick").size() == 0) {
                    messageLabel.setText("You win!");
                    messageLabel.setColor(Color.LIME);
                    messageLabel.setVisible(true);
                }
            }
        }

        if (ball.overlaps(paddle)) {
            float ballCenterX = ball.getX() + ball.getWidth() / 2;
            float paddlePercentHit = (ballCenterX - paddle.getX()) / paddle.getWidth();
            float bounceAngle = MathUtils.lerp(150, 30, paddlePercentHit);
            ball.setMotionAngle(bounceAngle);
            bounceSound.play();
        }

        if (ball.getY() < -50 && BaseActor.getList(mainStage, "com.mygdx.rectangledestroyerch10.Brick").size() > 0) {
            ball.remove();

            if (balls > 0) {
                balls -= 1;
                ballsLabel.setText("Balls: " + balls);
                ball = new Ball(0, 0, mainStage);

                messageLabel.setText("Click to start");
                messageLabel.setColor(Color.CYAN);
                messageLabel.setVisible(true);
            } else {
                messageLabel.setText("Game Over");
                messageLabel.setColor(Color.RED);
                messageLabel.setVisible(true);
            }
        }

        for (BaseActor item : BaseActor.getList(mainStage, "com.mygdx.rectangledestroyerch10.Item")) {
            if (paddle.overlaps(item)) {
                Item realItem = (Item)item;

                if (realItem.getType() == Item.Type.PADDLE_EXPAND)
                    paddle.setWidth(paddle.getWidth() * 1.25f);
                else if (realItem.getType() == Item.Type.PADDLE_SHRINK)
                    paddle.setWidth(paddle.getWidth() * 0.80f);
                else if (realItem.getType() == Item.Type.BALL_SPEED_UP)
                    ball.setSpeed(ball.getSpeed() * 1.50f);
                else if (realItem.getType() == Item.Type.BALL_SPEED_DOWN)
                    ball.setSpeed(ball.getSpeed() * 0.90f);

                paddle.setBoundaryRectangle();
                item.remove();
                itemCollectSound.play();
            }
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (ball.isPaused()) {
            ball.setPaused(false);
            messageLabel.setVisible(false);
        }

        return false;
    }
}