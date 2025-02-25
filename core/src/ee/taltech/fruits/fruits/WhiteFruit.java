package ee.taltech.fruits.fruits;

import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.List;

public class WhiteFruit extends Fruit {
    public WhiteFruit(float x, float y , Stage s, int id) {
        super(x, y ,s, id);
        spoilingStages = new String[] {"SpoiledFruits/w_s_1.png", "SpoiledFruits/w_s_2.png",
                "SpoiledFruits/w_s_3.png", "SpoiledFruits/w_s_4.png"};
        loadTexture("EWhiteFruit.png");
        setBoundaryRectangle();
    }
}
