package ee.taltech.fruits.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import ee.taltech.fruits.BaseActor;
import ee.taltech.fruits.FruitGame;

public class CreditsScreen extends BaseScreen {
    private Label heading;
    private TextButton backButton;
    @Override
    public void initialize() {
        BaseActor background = new BaseActor(0,0, mainStage);
        background.loadTexture("menu_background_200.png");
        background.setSize(320, 180);
        Skin uiSkin = new Skin(Gdx.files.internal("ui_skins/vhs-ui.json"));


        BitmapFont headingFont = new BitmapFont();
        headingFont.getData().setScale(1f);
        Label.LabelStyle headingStyle = new Label.LabelStyle(headingFont, Color.BLACK);

        heading = new Label("Credits:", uiSkin);
        uiTable.add(heading);
        uiTable.row().padTop(50f);
        Label backgroundSong = new Label("Peach by  Sakura Girl | https://soundcloud.com/sakuragirl_official", headingStyle);
        uiTable.add(backgroundSong);
        uiTable.row();
        Label backgroundSong2 = new Label("Music promoted by https://www.chosic.com/free-music/all/", headingStyle);
        uiTable.add(backgroundSong2);
        uiTable.row();
        Label backgroundSong3 = new Label("Creative Commons CC BY 3.0", headingStyle);
        uiTable.add(backgroundSong3);
        uiTable.row();
        Label backgroundSong4 = new Label("https://creativecommons.org/licenses/by/3.0/", headingStyle);
        uiTable.add(backgroundSong4);
        uiTable.row().padTop(20f);
        Label vhs0 = new Label("VHS UI Ver. 1", headingStyle);
        uiTable.add(vhs0);
        uiTable.row();
        Label vhs = new Label("Created by Raymond \"Raeleus\" Buckley", headingStyle);
        uiTable.add(vhs);
        uiTable.row();
        Label vhs2 = new Label("Visit ray3k.wordpress.com for games, tutorials, and much more!", headingStyle);
        uiTable.add(vhs2);
        uiTable.row();
        Label vhs3 = new Label("https://github.com/czyzby/gdx-skins/tree/master/vhs", headingStyle);
        uiTable.add(vhs3);
        uiTable.row().padTop(50f);

        backButton = new TextButton("Back", uiSkin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                FruitGame.setActiveScreen(new MainMenuScreen());
            }
        });
        uiTable.add(backButton).padTop(40f);
    }

    @Override
    public void update(float dt) {
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.ESCAPE) {
            FruitGame.setActiveScreen(new MainMenuScreen());
        }
        return false;
    }
}
