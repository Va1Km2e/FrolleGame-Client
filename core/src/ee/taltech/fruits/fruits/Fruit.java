package ee.taltech.fruits.fruits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import ee.taltech.fruits.BaseActor;
import ee.taltech.fruits.FruitGame;
import ee.taltech.fruits.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * The Fruit class.
 * Players can collect fruits.
 */
public abstract class Fruit extends BaseActor {
    protected final Integer id;
    protected int spoiledStage;
    protected String[] spoilingStages;

    /**
     * Make new Fruit instance
     *
     * @param x  fruit x coordinate.
     * @param y  fruit y coordinate.
     * @param s  fruit stage.
     * @param id fruit id.
     */
    public Fruit(float x, float y, Stage s, int id) {
        super(x, y, s);
        this.id = id;
    }

    /**
     * Return fruit id.
     *
     * @return int.
     */
    public int getId() {
        return id;
    }

    public void setSpoiledStage(int spoiledStage) {
        this.spoiledStage = spoiledStage;
    }

    public void spoil() {
        if (spoiledStage == 5) {
            if (FruitGame.getActiveScreen() instanceof GameScreen) {
                ((GameScreen) FruitGame.getActiveScreen()).removeFruit(id);
                setOpacity(0);
            }
        }
    }

    @Override
    public void act(float dt) {
        if (spoiledStage > 0 && spoiledStage < 5) {
            setAnimation(loadAnimationFromFiles(new String[] {spoilingStages[spoiledStage - 1]}, 1, true));
        } else if (spoiledStage >= 5) {
            addAction(Actions.removeActor());
        }
    }
}
