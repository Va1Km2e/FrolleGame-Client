package ee.taltech.fruits;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class Generator extends BaseActor {
    /**
     * Create a BaseActor and adds it to a stage (defined in BaseScreen)
     *
     * @param x int, actor position x.
     * @param y int, actor position y.
     * @param s stage.
     */
    public Generator(float x, float y, Stage s) {
        super(x, y, s);
        setWidth(50);
        setHeight(45);
        setBoundaryPolygon(4);
    }
}
