package ee.taltech.fruits.fruits;
import com.badlogic.gdx.scenes.scene2d.Stage;
import java.util.List;

public class BlueFruit extends Fruit {
    public BlueFruit(float x, float y , Stage s, int id) {
        super(x, y ,s, id);
        spoilingStages = new String[] {"SpoiledFruits/b_s_1.png", "SpoiledFruits/b_s_2.png",
                "SpoiledFruits/b_s_3.png", "SpoiledFruits/b_s_4.png"};
        loadTexture("EBlueFruit.png");
        setBoundaryRectangle();
    }
}
