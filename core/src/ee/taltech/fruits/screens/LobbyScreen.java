package ee.taltech.fruits.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import ee.taltech.fruits.BaseActor;
import ee.taltech.fruits.BaseGame;
import ee.taltech.fruits.FruitGame;
import ee.taltech.fruits.packets.CreateNewGamePacket;
import ee.taltech.fruits.packets.JoinGamePacket;
import ee.taltech.fruits.packets.NamePacket;
import ee.taltech.fruits.packets.RequestGamesPacket;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class LobbyScreen extends BaseScreen implements Input.TextInputListener {
    private Table buttonsTable;
    private Table instructionsTable;
    private Table spaceTable;
    private Skin uiSkin;

    @Override
    public void initialize() {
        requestGamesFromServer();
        BaseActor background = new BaseActor(0,0, mainStage);
        background.loadTexture("background/menu_background_100.png");
        background.setSize(320, 180);
        //TODO: This should be replaced with async
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        uiSkin = new Skin(Gdx.files.internal("ui_skins/vhs-ui.json"));
        buttonsTable = new Table();
        //uiTable.debug();

        uiTable.add(buttonsTable).left().top().padTop(10f).padRight(150f);

        addNameField();
        buttonsTable.row().padTop(30f);
        addCreateButton();
        buttonsTable.row().padTop(5f);
        addRefreshButton();
        buttonsTable.row().padTop(15f);
        addScrollBar();

        spaceTable = new Table();
        uiTable.add(spaceTable).right().top().padTop(200f).padLeft(100f).padRight(200f);

        instructionsTable = new Table();
        uiTable.add(instructionsTable).right().top().padTop(200f).padLeft(50f);
        addInstructions();
    }

    private void addNameField() {
        TextField.TextFieldStyle tfStyle = new TextField.TextFieldStyle();
        tfStyle.font = FruitGame.labelStyle.font;
        tfStyle.fontColor = Color.BLACK;
        TextField tf = new TextField(FruitGame.playerName, uiSkin);
        tf.setWidth(200f);
        tf.setTextFieldListener((textField, key) -> {
            if (key == '\n'){
                FruitGame.playerName = tf.getText();
                sendNameToServer(tf.getText());
                tf.setDisabled(true);
            }
        });
        tf.addListener(
                (Event e) ->
                {
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent)e).getType().equals(InputEvent.Type.touchDown)) {
                        return false;
                    }
                    tf.setDisabled(false);
                    return false;
                }
        );
        Label nameLabel = new Label("Name: ", uiSkin);
        buttonsTable.add(nameLabel).top().left();
        buttonsTable.add(tf).top().padRight(50f);
    }

    private void addScrollBar() {
        if (!Objects.equals(FruitGame.availableGames, null)) {
            if (!FruitGame.availableGames.isEmpty()) {
                for (Map.Entry<Integer, String> entry : FruitGame.availableGames.entrySet()) {
                    buttonsTable.row().padTop(30f);
                    buttonsTable.add(addButton(entry.getKey(), entry.getValue()));
                }
            }
        }
        ScrollPane scrollPane = new ScrollPane(buttonsTable);
        //System.out.println(scrollPane.se);
        scrollPane.setSize(buttonsTable.getMinWidth(), FruitGame.HEIGHT);
        scrollPane.setScrollbarsVisible(true);
        uiTable.top().left().add(scrollPane).expandY();
    }

    private Button addButton(int gameId, String playerName) {
        TextButton gameButton =
                new TextButton(String.format("Game nr. %d, %s", gameId, playerName), uiSkin);
        gameButton.addListener(
            (Event e) ->
            {
                if (!(e instanceof InputEvent) ||
                        !((InputEvent)e).getType().equals(InputEvent.Type.touchDown)) {
                    return false;
                }
                sendJoinGameRequest(gameId);
                FruitGame.setActiveScreen(new GameScreen());
                FruitGame.gameId = gameId;
                System.out.println(String.format("Joined game %d", gameId));
                return false;
            }
        );
        return gameButton;
    }

    private void addRefreshButton() {
        Button.ButtonStyle buttonStyleRefresh = new Button.ButtonStyle();
        Texture buttonTextureRefresh = new Texture(Gdx.files.internal("buttons/refresh_unpressed_16x16.png"));
        TextureRegion buttonRegionRefresh = new TextureRegion(buttonTextureRefresh);
        buttonStyleRefresh.up = new TextureRegionDrawable(buttonRegionRefresh);
        Button refreshButton = new Button(buttonStyleRefresh);
        refreshButton.addListener(
                (Event e) ->
                {
                    if (refreshButton.isOver()) {
                        Texture buttonTextureRefresh1 = new Texture(Gdx.files.internal("buttons/refresh_pressed_16x16.png"));
                        TextureRegion buttonRegionRefresh1 = new TextureRegion(buttonTextureRefresh1);
                        buttonStyleRefresh.up = new TextureRegionDrawable(buttonRegionRefresh1);
                    } else {
                        buttonStyleRefresh.up = new TextureRegionDrawable(buttonRegionRefresh);
                    }
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent)e).getType().equals(InputEvent.Type.touchDown)) {
                        return false;
                    }
                    FruitGame.setActiveScreen(new LobbyScreen());
                    System.out.println("Refresh");
                    return false;
                }
        );
        buttonsTable.add(refreshButton).expandX().left();
    }

    private void addCreateButton() {
        Button.ButtonStyle buttonStyleCreate = new Button.ButtonStyle();
        Texture buttonTextureCreate = new Texture(Gdx.files.internal("buttons/create_game_unpressed.png"));
        TextureRegion buttonRegionCreate = new TextureRegion(buttonTextureCreate);
        buttonStyleCreate.up = new TextureRegionDrawable(buttonRegionCreate);
        Button createButton = new Button(buttonStyleCreate);
        createButton.addListener(
                (Event e) ->
                {
                    if (createButton.isOver()) {
                        Texture buttonTextureCreate1 = new Texture(Gdx.files.internal("buttons/create_game_pressed.png"));
                        TextureRegion buttonRegionCreate1 = new TextureRegion(buttonTextureCreate1);
                        buttonStyleCreate.up = new TextureRegionDrawable(buttonRegionCreate1);
                    } else {
                        buttonStyleCreate.up = new TextureRegionDrawable(buttonRegionCreate);
                    }
                    if (!(e instanceof InputEvent) ||
                            !((InputEvent)e).getType().equals(InputEvent.Type.touchDown)) {
                        return false;
                    }
                    sendCreateRequestToServer();
                    FruitGame.setActiveScreen(new GameScreen());
                    return false;
                });
        buttonsTable.add(createButton).expandX().left();
    }

    private void addInstructions() {
        Label instructionsLabel = new Label("Instructions", uiSkin);
        instructionsTable.add(instructionsLabel);
        instructionsTable.row().padTop(30f);
        Label instructionsLabel2 = new Label("Your final objective is to fly higher than", uiSkin);
        instructionsTable.add(instructionsLabel2);
        instructionsTable.row().padTop(10f);
        Label instructionsLabel3 = new Label("your rival neighbour and get", uiSkin);
        instructionsTable.add(instructionsLabel3);
        instructionsTable.row().padTop(10f);
        Label instructionsLabel4 = new Label("more exotic fruit than him.", uiSkin);
        instructionsTable.add(instructionsLabel4);
        instructionsTable.row().padTop(10f);
        Label instructionsLabel5 = new Label("That would give you bragging rights", uiSkin);
        instructionsTable.add(instructionsLabel5);
        instructionsTable.row().padTop(10f);
        Label instructionsLabel6 = new Label("and will put him in his place.", uiSkin);
        instructionsTable.add(instructionsLabel6);
        instructionsTable.row().padTop(10f);
        Label instructionsLabel7 = new Label("To succeed you need to gather fallen fruits", uiSkin);
        instructionsTable.add(instructionsLabel7);
        instructionsTable.row().padTop(10f);
        Label instructionsLabel8 = new Label("and deposit them into your bio engine", uiSkin);
        instructionsTable.add(instructionsLabel8);
        instructionsTable.row().padTop(10f);
        Label instructionsLabel9 = new Label("to make fuel for you rocket.", uiSkin);
        instructionsTable.add(instructionsLabel9);
        instructionsTable.row().padTop(10f);
        Label instructionsLabel10 = new Label("Be aware of sheep as he might attack you", uiSkin);
        instructionsTable.add(instructionsLabel10);
        instructionsTable.row().padTop(10f);
        Label instructionsLabel11 = new Label("and make you lose carried fruit.", uiSkin);
        instructionsTable.add(instructionsLabel11);

        instructionsTable.row().padTop(30f);
        Label instructionsLabel12 = new Label("You can move with arrows and attack sheep with \"E\".", uiSkin);
        instructionsTable.add(instructionsLabel12);
        instructionsTable.row().padTop(10f);
        Label instructionsLabel13 = new Label("Some fruits are worth more than others.", uiSkin);
        instructionsTable.add(instructionsLabel13);

    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void input(String s) {
        System.out.println("input");
    }

    @Override
    public void canceled() {
    }

    private void requestGamesFromServer() {
        FruitGame.client.sendUDP(new RequestGamesPacket());
    }

    private void sendNameToServer(String name) {
        System.out.println(name);
        FruitGame.client.sendUDP(new NamePacket(name));
    }

    private void sendJoinGameRequest(int gameId) {
        FruitGame.client.sendUDP(new JoinGamePacket(gameId));
    }

    private void sendCreateRequestToServer() {
        System.out.println("Create");
        FruitGame.client.sendUDP(new CreateNewGamePacket());
    }

    @Override
    public boolean keyDown(int i) {
        if (i == Input.Keys.ESCAPE) {
            FruitGame.setActiveScreen(new MainMenuScreen());
        }
        return false;
    }
}
