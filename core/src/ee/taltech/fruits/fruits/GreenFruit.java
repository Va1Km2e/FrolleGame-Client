package ee.taltech.fruits.fruits;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

public class GreenFruit extends Fruit {
    public GreenFruit(float x, float y , Stage s, int id) {
        super(x, y ,s, id);
        spoilingStages = new String[] {"SpoiledFruits/g_s_1.png", "SpoiledFruits/g_s_2.png",
                "SpoiledFruits/g_s_3.png", "SpoiledFruits/g_s_4.png" };
        loadTexture("EGreenFruit.png");
        setBoundaryRectangle();
    }
}
