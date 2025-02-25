package ee.taltech.fruits.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import ee.taltech.fruits.BaseActor;
import ee.taltech.fruits.FruitGame;

public class MainMenuScreen extends BaseScreen {
    public static boolean isThemePlaying = false;
    private TextButton creditsButton;

    @Override
    public void initialize() {
        super.startThemeSong();
        BaseActor background = new BaseActor(0,0, mainStage);
        background.loadTexture("background/starting_menu_final.png");
        resize(540, 960);
        Button.ButtonStyle buttonStyleStart = new Button.ButtonStyle();
        Skin uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
        Texture buttonTextureStart = new Texture(Gdx.files.internal("buttons/play_unpressed.png"));
        TextureRegion buttonRegionStart = new TextureRegion(buttonTextureStart);
        buttonStyleStart.up = new TextureRegionDrawable(buttonRegionStart);
        Button startButton = new Button(buttonStyleStart);
        startButton.addListener(
                (Event e) ->
                {
                    if (startButton.isOver()) {
                        Texture buttonTextureStart1 = new Texture(Gdx.files.internal("buttons/play_pressed.png"));
                        TextureRegion buttonRegionStart1 = new TextureRegion(buttonTextureStart1);
                        buttonStyleStart.up = new TextureRegionDrawable(buttonRegionStart1);
                    } else {
                        buttonStyleStart.up = new TextureRegionDrawable(buttonRegionStart);
                    }
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent)e).getType().equals(InputEvent.Type.touchDown)) {
                        return false;
                    }
                    FruitGame.setActiveScreen(new LobbyScreen());
                    return false;
                }
        );
        uiTable.add(startButton).padTop(250f);

        Button.ButtonStyle buttonStyleOptions = new Button.ButtonStyle();
        Texture buttonTextureOptions = new Texture(Gdx.files.internal("buttons/options_unpressed.png"));
        TextureRegion buttonRegionOptions = new TextureRegion(buttonTextureOptions);
        buttonStyleOptions.up = new TextureRegionDrawable(buttonRegionOptions);
        Button optionsButton = new Button(buttonStyleOptions);
        optionsButton.addListener(
                (Event e) ->
                {
                    if (optionsButton.isOver()) {
                        Texture buttonTextureOptions1 = new Texture(Gdx.files.internal("buttons/options_pressed.png"));
                        TextureRegion buttonRegionOptions1 = new TextureRegion(buttonTextureOptions1);
                        buttonStyleOptions.up = new TextureRegionDrawable(buttonRegionOptions1);
                    } else {
                        buttonStyleOptions.up = new TextureRegionDrawable(buttonRegionOptions);
                    }
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent)e).getType().equals(InputEvent.Type.touchDown)) {
                        return false;
                    }
                    FruitGame.setActiveScreen(new OptionsScreen());
                    return false;
                }
        );
        uiTable.row();
        uiTable.add(optionsButton).padTop(30f);

        Button.ButtonStyle buttonStyleCredits = new Button.ButtonStyle();
        Texture buttonTextureCredits = new Texture(Gdx.files.internal("buttons/credits_unpressed.png"));
        TextureRegion buttonRegionCredits = new TextureRegion(buttonTextureCredits);
        buttonStyleCredits.up = new TextureRegionDrawable(buttonRegionCredits);
        Button creditsButton = new Button(buttonStyleCredits);
        creditsButton.addListener(
                (Event e) ->
                {
                    if (creditsButton.isOver()) {
                        Texture buttonTextureCredits1 = new Texture(Gdx.files.internal("buttons/credits_pressed.png"));
                        TextureRegion buttonRegionCredits1 = new TextureRegion(buttonTextureCredits1);
                        buttonStyleCredits.up = new TextureRegionDrawable(buttonRegionCredits1);
                    } else {
                        buttonStyleCredits.up = new TextureRegionDrawable(buttonRegionCredits);
                    }
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent)e).getType().equals(InputEvent.Type.touchDown)) {
                        return false;
                    }
                    FruitGame.setActiveScreen(new CreditsScreen());
                    return false;
                }
        );
        uiTable.row();
        uiTable.add(creditsButton).padTop(30f);

        Button.ButtonStyle buttonStyleExit = new Button.ButtonStyle();
        Texture buttonTextureExit = new Texture(Gdx.files.internal("buttons/exit_unpressed.png"));
        TextureRegion buttonRegionExit = new TextureRegion(buttonTextureExit);
        buttonStyleExit.up = new TextureRegionDrawable(buttonRegionExit);
        Button exitButton = new Button(buttonStyleExit);
        exitButton.addListener(
                (Event e) ->
                {
                    if (exitButton.isOver()) {
                        Texture buttonTextureExit1 = new Texture(Gdx.files.internal("buttons/exit_pressed.png"));
                        TextureRegion buttonRegionExit1 = new TextureRegion(buttonTextureExit1);
                        buttonStyleExit.up = new TextureRegionDrawable(buttonRegionExit1);
                    } else {
                        buttonStyleExit.up = new TextureRegionDrawable(buttonRegionExit);
                    }
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent)e).getType().equals(InputEvent.Type.touchDown)) {
                        return false;
                    }
                    Gdx.app.exit();
                    System.exit(-1);
                    FruitGame.client.close();
                    return false;
                }
        );
        uiTable.row();
        uiTable.add(exitButton).padTop(30f);
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height);
        uiStage.getViewport().update(width, height);
        System.out.println("Resize main");
    }
}
