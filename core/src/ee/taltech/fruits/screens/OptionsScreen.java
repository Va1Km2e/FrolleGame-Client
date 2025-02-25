package ee.taltech.fruits.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import ee.taltech.fruits.BaseActor;
import ee.taltech.fruits.FruitGame;
import ee.taltech.fruits.Player;

public class OptionsScreen extends BaseScreen {

    private Slider volumeSlider;
    private Label heading;
    private TextButton moveLeftButton;
    private TextButton moveRightButton;
    private TextButton moveUpButton;
    private TextButton moveDownButton;
    private Label buttonCommentMoveLeft;
    private Label buttonCommentMoveRight;
    private Label buttonCommentMoveUp;
    private Label buttonCommentMoveDown;
    private TextButton backButton;

    @Override
    public void initialize() {
        BaseActor background = new BaseActor(0,0, mainStage);
        background.loadTexture("menu_background_200.png");
        background.setSize(320, 180);
        Skin uiSkin = new Skin(Gdx.files.internal("ui_skins/vhs-ui.json"));


        BitmapFont headingFont = new BitmapFont();
        headingFont.getData().setScale(2f);
        Label.LabelStyle headingStyle = new Label.LabelStyle(headingFont, Color.BLACK);

        heading = new Label("Options", headingStyle);
        uiTable.add(heading).width(-100f);
        uiTable.row();

        volumeSlider = new Slider(0f, 0.8f, 0.05f, false, uiSkin);
        volumeSlider.setValue(0.15f); // Set initial value

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                float volume = volumeSlider.getValue();
                BaseScreen.themeSong.setVolume(volume);
                // You can use the volume value here to adjust the game volume
                // For example, you can use it to set the volume of your game's audio
            }
        });

        buttonCommentMoveLeft = new Label("Change volume", headingStyle);
        uiTable.add(buttonCommentMoveLeft).width(210f).padTop(20f);
        uiTable.add(volumeSlider).width(190f).padTop(20f); // Add slider to the UI table with specified width and padding

        moveLeftButton = new TextButton(Input.Keys.toString(Player.moveLeft), uiSkin);
        final InputProcessor previousInputProcessor = Gdx.input.getInputProcessor();
        moveLeftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Set an input processor to listen for the next key press
                Gdx.input.setInputProcessor(new InputAdapter() {
                    @Override
                    public boolean keyDown(int keycode) {
                        // Set player.moveLeft to the keycode of the pressed key
                        Player.moveLeft = keycode;

                        String keyName = Input.Keys.toString(keycode);

                        // Print the keycode for demonstration purposes
                        System.out.println("Keycode set to: " + keyName);

                        moveLeftButton.setText(String.valueOf(keyName));

                        // Remove this input processor after capturing the key press
                        Gdx.input.setInputProcessor(previousInputProcessor);


                        // Return true to indicate that the key press event was handled
                        return true;
                    }
                });
            }
        });
        uiTable.row();
        buttonCommentMoveLeft = new Label("Move left", headingStyle);
        uiTable.add(buttonCommentMoveLeft).width(200f).padTop(20f);;
        uiTable.add(moveLeftButton).width(200f).padTop(20f);

        moveRightButton = new TextButton(Input.Keys.toString(Player.moveRight), uiSkin);
        moveRightButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Set an input processor to listen for the next key press
                Gdx.input.setInputProcessor(new InputAdapter() {
                    @Override
                    public boolean keyDown(int keycode) {
                        // Set player.moveLeft to the keycode of the pressed key
                        Player.moveRight = keycode;

                        String keyName = Input.Keys.toString(keycode);

                        // Print the keycode for demonstration purposes
                        System.out.println("Keycode set to: " + keyName);

                        moveRightButton.setText(String.valueOf(keyName));

                        // Remove this input processor after capturing the key press
                        Gdx.input.setInputProcessor(previousInputProcessor);


                        // Return true to indicate that the key press event was handled
                        return true;
                    }
                });
            }
        });
        uiTable.row();
        buttonCommentMoveRight = new Label("Move right", headingStyle);
        uiTable.add(buttonCommentMoveRight).width(200f).padTop(20f);;
        uiTable.add(moveRightButton).width(200f).padTop(20f);

        moveUpButton = new TextButton(Input.Keys.toString(Player.moveUp), uiSkin);
        moveUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Set an input processor to listen for the next key press
                Gdx.input.setInputProcessor(new InputAdapter() {
                    @Override
                    public boolean keyDown(int keycode) {
                        // Set player.moveLeft to the keycode of the pressed key
                        Player.moveUp = keycode;

                        String keyName = Input.Keys.toString(keycode);

                        // Print the keycode for demonstration purposes
                        System.out.println("Keycode set to: " + keyName);

                        moveUpButton.setText(String.valueOf(keyName));

                        // Remove this input processor after capturing the key press
                        Gdx.input.setInputProcessor(previousInputProcessor);


                        // Return true to indicate that the key press event was handled
                        return true;
                    }
                });
            }
        });
        uiTable.row();
        buttonCommentMoveUp = new Label("Move up", headingStyle);
        uiTable.add(buttonCommentMoveUp).width(200f).padTop(20f);;
        uiTable.add(moveUpButton).width(200f).padTop(20f);

        moveDownButton = new TextButton(Input.Keys.toString(Player.moveDown), uiSkin);
        moveDownButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Set an input processor to listen for the next key press
                Gdx.input.setInputProcessor(new InputAdapter() {
                    @Override
                    public boolean keyDown(int keycode) {
                        // Set player.moveLeft to the keycode of the pressed key
                        Player.moveDown = keycode;

                        String keyName = Input.Keys.toString(keycode);

                        // Print the keycode for demonstration purposes
                        System.out.println("Keycode set to: " + keyName);

                        moveDownButton.setText(String.valueOf(keyName));

                        // Remove this input processor after capturing the key press
                        Gdx.input.setInputProcessor(previousInputProcessor);


                        // Return true to indicate that the key press event was handled
                        return true;
                    }
                });
            }
        });
        uiTable.row();
        buttonCommentMoveDown = new Label("Move down", headingStyle);
        uiTable.add(buttonCommentMoveDown).width(200f).padTop(20f);;
        uiTable.add(moveDownButton).width(200f).padTop(20f);

        backButton = new TextButton("Back", uiSkin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FruitGame.setActiveScreen(new MainMenuScreen());
            }
        });
        uiTable.row();
        uiTable.add(backButton).padTop(20f);
    }
    @Override
    public void update(float dt) {
        // Update logic if needed
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.ESCAPE) {
            FruitGame.setActiveScreen(new MainMenuScreen());
        }
        return false;
    }
}
