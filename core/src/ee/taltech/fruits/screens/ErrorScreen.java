package ee.taltech.fruits.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import ee.taltech.fruits.BaseGame;
import ee.taltech.fruits.FruitGame;

public class ErrorScreen extends BaseScreen {
    private long startTime;
    private Label serverNotFound;
    @Override
    public void initialize() {
        startTime = System.currentTimeMillis();
        serverNotFound = new Label("Unable to connect to server.", BaseGame.labelStyle);
        uiTable.add(serverNotFound);
        serverNotFound.setVisible(false);


        Button.ButtonStyle buttonStyleExit = new Button.ButtonStyle();
        Texture buttonTextureExit = new Texture(Gdx.files.internal("exit.png"));
        TextureRegion buttonRegionExit = new TextureRegion(buttonTextureExit);
        buttonStyleExit.up = new TextureRegionDrawable(buttonRegionExit);
        Button exitButton = new Button(buttonStyleExit);
        buttonRegionExit.setRegionHeight(420);
        buttonRegionExit.setRegionWidth(540);
        exitButton.addListener(
                (Event e) ->
                {
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
        uiTable.add(exitButton);

    }

    @Override
    public void update(float dt) {
        long elapsedTime = System.currentTimeMillis() - startTime;

        if (elapsedTime > 1000) {
            serverNotFound.setVisible(true);
        }
    }
}
