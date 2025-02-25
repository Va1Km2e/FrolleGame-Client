package ee.taltech.fruits;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class EndingAnimation extends BaseActor {
    /**
     * Create a BaseActor and adds it to a stage (defined in BaseScreen)
     *
     * @param x int, actor position x.
     * @param y int, actor position y.
     * @param s stage.
     */
    public EndingAnimation(float x, float y, Stage s, String[] animation) {
        super(x, y, s);
        setAnimation(loadAnimationFromFiles(animation, 0.05f, false));
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        System.out.println("Animation: " + animation.getKeyFrameIndex(elapsedTime) + "Time: " + dt);
    }

}
