package ee.taltech.fruits.fruits;

import com.badlogic.gdx.scenes.scene2d.Stage;

public class GoldenFruit extends Fruit {
    public GoldenFruit(float x, float y , Stage s, int id) {
        super(x, y ,s, id);
        spoilingStages = new String[] {"SpoiledFruits/Gold_s_1.png", "SpoiledFruits/Gold_s_2.png",
                "SpoiledFruits/Gold_s_3.png", "SpoiledFruits/Gold_s_4.png" };
        loadTexture("EGoldenFruit.png");
        setBoundaryRectangle();
    }
}
