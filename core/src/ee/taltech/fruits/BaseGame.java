package ee.taltech.fruits;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import ee.taltech.fruits.screens.BaseScreen;

public abstract class BaseGame extends Game
{
    protected static BaseGame game;
    public static TextButton.TextButtonStyle textButtonStyle;
    public static Label.LabelStyle labelStyle;

    public BaseGame()
    {
        game = this;
        labelStyle = new Label.LabelStyle();
    }
    /**
     * Sets active screen. In the start of the game that should be the MenuScreen.
     * */
    public void create() {
        InputMultiplexer im = new InputMultiplexer();
        Gdx.input.setInputProcessor(im);

        FreeTypeFontGenerator fontGenerator =
                new FreeTypeFontGenerator(Gdx.files.internal("OpenSans.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameters.size = 24;
        fontParameters.color = Color.BLACK;
        fontParameters.minFilter = Texture.TextureFilter.Linear;
        fontParameters.magFilter = Texture.TextureFilter.Linear;
        labelStyle.font = fontGenerator.generateFont(fontParameters);

        textButtonStyle = new TextButton.TextButtonStyle();
        Texture   buttonTex   = new Texture(Gdx.files.internal("button.png"));
        NinePatch buttonPatch = new NinePatch(buttonTex, 24,24,24,24);
        textButtonStyle.up    = new NinePatchDrawable(buttonPatch);
        textButtonStyle.font      = labelStyle.font;
        textButtonStyle.fontColor = Color.GRAY;
    }
    public static void setActiveScreen(BaseScreen s)
    {
        game.setScreen(s);
    }

    public static Screen getActiveScreen() {
        return game.getScreen();
    }

}
