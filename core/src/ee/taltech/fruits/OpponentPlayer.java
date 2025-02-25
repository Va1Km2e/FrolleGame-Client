package ee.taltech.fruits;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Class for your opponent.
 */
public class OpponentPlayer extends BaseActor {
    private String standingPositionToTake = "standing/r_stand_front.png";
    private boolean wasItMoving;

    /**
     * Make new Opponent class instance.
     *
     * @param x coordinate.
     * @param y coordinate.
     * @param s stage.
     */
    public OpponentPlayer(float x, float y, Stage s) {
        super(x, y, s);
        loadTexture("R_single.png");

        setAcceleration(400);
        setMaxSpeed(140);
        setDeceleration(600);
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        setAnimationPaused(false);
        float deltaX = (getX() * -1) + FruitGame.FULL_WIDTH_X - FruitGame.enemyX;
        float deltaY = getY() - FruitGame.enemyY;

        wasItMoving = false;
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            if (FruitGame.enemyX < (getX() * -1) + FruitGame.FULL_WIDTH_X) {
                setAnimation(loadAnimationFromSheet("R_right.png", 3, 1, 0.2f, true));
                standingPositionToTake = "standing/r_stand_right.png";
                wasItMoving = true;
            } else if (FruitGame.enemyX > (getX() * -1) + FruitGame.FULL_WIDTH_X) {
                setAnimation(loadAnimationFromSheet("R_left.png", 3, 1, 0.2f, true));
                standingPositionToTake = "standing/r_stand_left.png";
                wasItMoving = true;
            }
        } else {
            if (FruitGame.enemyY > getY()) {
                setAnimation(loadAnimationFromSheet("R_back.png", 3, 1, 0.2f, true));
                standingPositionToTake = "standing/r_stand_back.png";
                wasItMoving = true;
            } else if (FruitGame.enemyY < getY()) {
                setAnimation(loadAnimationFromSheet("R_front.png", 3, 1, 0.2f, true));
                standingPositionToTake = "standing/r_stand_front.png";
                wasItMoving = true;
            }
        }
        if (!wasItMoving) {
            setAnimation(loadAnimationFromSheet(standingPositionToTake, 1, 1, 0.2f, false));
        }

        setPosition(FruitGame.FULL_WIDTH_X - FruitGame.enemyX, FruitGame.enemyY);
    }
}
