package ee.taltech.fruits;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.compression.lzma.Base;

public class House extends BaseActor {
    /**
     * Create a BaseActor and adds it to a stage (defined in BaseScreen)
     *
     * @param x int, actor position x.
     * @param y int, actor position y.
     * @param s stage.
     */
    public House(float x, float y, Stage s) {
        super(x, y, s);
        setWidth(130);
        setHeight(107);
        setBoundaryPolygon(4);
        rotateBy(45);
    }
}
