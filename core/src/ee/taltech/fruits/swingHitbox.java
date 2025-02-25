package ee.taltech.fruits;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class swingHitbox extends BaseActor {
    private TextureRegion textureRegion;
    /**
     * Create a BaseActor and adds it to a stage (defined in BaseScreen)
     *
     * @param x int, actor position x.
     * @param y int, actor position y.
     * @param s stage.
     */
    public swingHitbox(float x, float y, float rotation, Stage s) {
        super(x, y, s);
        setWidth(30);
        setHeight(2);
        setRotation(rotation);
        setBoundaryPolygon(4);
    }
}
