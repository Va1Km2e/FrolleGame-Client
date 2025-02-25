package ee.taltech.fruits;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class eButton extends BaseActor{
    private final TextureRegion textureRegion;
    /**
     * Create a BaseActor and adds it to a stage (defined in BaseScreen)
     *
     * @param x int, actor position x.
     * @param y int, actor position y.
     * @param s stage.
     */
    public eButton(float x, float y, Stage s) {
        super(x, y, s);
        textureRegion = new TextureRegion(new Texture("return.png"));
        setWidth(50);
        setHeight(50);
        setRotation(0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Set the color and transparency of the hitbox
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
        // Draw the hitbox texture at the hitbox's position and size
        batch.draw(textureRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
    }
}
