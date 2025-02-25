package ee.taltech.fruits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.fruits.fruits.Fruit;
import ee.taltech.fruits.packets.*;
import ee.taltech.fruits.screens.BaseScreen;
import ee.taltech.fruits.screens.ErrorScreen;
import ee.taltech.fruits.screens.GameScreen;
import ee.taltech.fruits.screens.MainMenuScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FruitGame extends BaseGame
{
    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static Client client = new Client();
    public static float enemyX;
    public static float enemyY;
    public static int gameId;
    public static Map<Integer, String> availableGames;
    public static boolean gamesLobbyUpdate = false;
    public static String playerName = "Name";
    public static int score = 0;
    public static List<Integer> inventory = new ArrayList<>();
    public static int timeInSeconds = 0;
    public static boolean gameOver = false;
    public static Map<String, Integer> scores;
    public static int FULL_WIDTH_X = 1280;
    //public static


    public void create()
    {
        super.create();
        availableGames = new HashMap<>();
        setActiveScreen(new MainMenuScreen());
        startListener();
    }

    public void startListener() {
        Sound fruitCollectSound = Gdx.audio.newSound(Gdx.files.internal("audio/fruit_collect.wav"));
        client.start();
        registerPackets();
        try {
            client.connect(5000, "193.40.255.26", 8080, 8081);
            client.sendUDP(new JoinServerPacket());
        } catch (IOException e) {
            e.fillInStackTrace();
            setActiveScreen(new ErrorScreen());
        }

        client.addListener(new Listener.ThreadedListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof JoinServerPacket) {
                    System.out.println("JoinPacket");
                    playerName = "Player" + connection.getID();
                }

                if (object instanceof PlayerPositionXPacket) {
                    if (((PlayerPositionXPacket) object).getConnectionId() != client.getID()) {
                        enemyX = ((PlayerPositionXPacket) object).getX();
                    }
                }

                if (object instanceof PlayerPositionYPacket) {
                    if (((PlayerPositionYPacket) object).getConnectionId() != client.getID()) {
                        enemyY = ((PlayerPositionYPacket) object).getY();
                    }
                }

                if (object instanceof RequestGamesPacket) {
                    availableGames = ((RequestGamesPacket) object).getGames();
                    gamesLobbyUpdate = true;
                    System.out.println("Recieved request games packet");
                }

                if (object instanceof CreateNewGamePacket) {
                    gameId = ((CreateNewGamePacket) object).getGameId();
                }

                if (object instanceof SendFruitPacket) {
                    if (game.getScreen() instanceof GameScreen) {
                        float x = ((SendFruitPacket) object).getX();
                        float y = ((SendFruitPacket) object).getY();
                        int id = ((SendFruitPacket) object).getId();
                        int points = ((SendFruitPacket) object).getPoints();
                        int oppositeFruitId = ((SendFruitPacket) object).getOpponentFruitId();

                        ((GameScreen) game.getScreen()).addFruit(x, y, id, points, oppositeFruitId);
                        System.out.println("fruit recieved " + id);
                    }
                }

                if (object instanceof FruitCollisionPacket) {
                    if (game.getScreen() instanceof GameScreen) {
                        ((GameScreen) game.getScreen()).removeFruit(((FruitCollisionPacket) object).getFruitId());
                        fruitCollectSound.play();
                        System.out.println("Collection packet received " + ((FruitCollisionPacket) object).getFruitId());
                    }
                }

                if (object instanceof UpdatedScorePacket) {
                    score = ((UpdatedScorePacket) object).getScore();
                }

                if (object instanceof UpdatedInventoryPacket) {

                    inventory = ((UpdatedInventoryPacket) object).getInventory();
                    System.out.println("inventory packet recieved. Player inventory: " + inventory);
                }

                if (object instanceof UpdateTimerPacket) {
                    timeInSeconds = ((UpdateTimerPacket) object).getTimeInSeconds();
                    if (timeInSeconds == 15) {
                        if (game.getScreen() instanceof GameScreen) {
                            ((GameScreen) game.getScreen()).endThemeSong();
                            ((GameScreen) game.getScreen()).playEndSound(true);
                        } else if (timeInSeconds == 0) {
                            ((GameScreen) game.getScreen()).playEndSound(false);
                        }
                    }
                    System.out.println(timeInSeconds);
                }

                if (object instanceof GameOverPacket) {
                    scores = ((GameOverPacket) object).getScores();
                    gameOver = true;
                    ((GameScreen) game.getScreen()).getPlayer().stopWalkingSound();
                }

                if (object instanceof FruitSpoliedPacket) {
                    if (game.getScreen() instanceof GameScreen) {
                        ((GameScreen) game.getScreen()).spoilFruit(((FruitSpoliedPacket) object).getFruitId(),
                                ((FruitSpoliedPacket) object).getSpoiledStage());
                    }
                }
            }

            @Override
            public void disconnected(Connection connection) {

            }
        }));
    }

    private void registerPackets() {
        Kryo kryo = client.getKryo();
        kryo.register(ArrayList.class);
        kryo.register(HashMap.class);
        kryo.register(JoinServerPacket.class);
        kryo.register(PlayerPositionXPacket.class);
        kryo.register(PlayerPositionYPacket.class);
        kryo.register(RequestGamesPacket.class);
        kryo.register(NamePacket.class);
        kryo.register(CreateNewGamePacket.class);
        kryo.register(JoinGamePacket.class);
        kryo.register(SendFruitPacket.class);
        kryo.register(FruitCollisionPacket.class);
        kryo.register(UpdatedScorePacket.class);
        kryo.register(UpdatedInventoryPacket.class);
        kryo.register(GameOverPacket.class);
        kryo.register(UpdateTimerPacket.class);
        kryo.register(UnloadFruitsPacket.class);
        kryo.register(FruitSpoliedPacket.class);
        kryo.register(SheepAteFruitPacket.class);
        kryo.register(SheepAttackedPlayerPacket.class);
        kryo.register(PlayerLeftGamePacket.class);
    }

    private void timeRunningOut() {

    }
}
