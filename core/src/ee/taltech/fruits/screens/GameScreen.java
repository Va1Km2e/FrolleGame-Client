package ee.taltech.fruits.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.compression.lzma.Base;
import com.badlogic.gdx.utils.viewport.Viewport;
import ee.taltech.fruits.ai.RedSheepAI;
import ee.taltech.fruits.packets.PlayerLeftGamePacket;
import ee.taltech.fruits.swingHitbox;
import ee.taltech.fruits.*;
import ee.taltech.fruits.ai.SheepAI;
import ee.taltech.fruits.fruits.*;
import ee.taltech.fruits.packets.FruitCollisionPacket;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static ee.taltech.fruits.screens.MainMenuScreen.isThemePlaying;

public class GameScreen extends BaseScreen {

	private OpponentPlayer enemyPlayer;
	private Player player;
	private Map<String, Float> fruitToBeAddedPos = new HashMap<>();
	private Button returnButton;
	private Label scoreLabel;
	private Label fruitInventoryLabel;
	private Label timerLabel;
	private Label endScores;
	private Integer fruitToBeAddedId = null;
	private Integer fruitToBeRemovedById = null;
	private Viewport viewport;
	private Camera camera;
	private Generator generator;
	private House house;
	private swingHitbox swingHitbox;
	private SheepAI sheep;
	private int fruitToBeAddedPoints;
	private int oppositeFruitToBeAddedId;
    private final Sound endMusic = Gdx.audio.newSound(Gdx.files.internal("audio/game_ending.mp3"));
	private final Sound sheepHit = Gdx.audio.newSound(Gdx.files.internal("audio/sheep_hit.mp3"));
	private final Sound missedHit = Gdx.audio.newSound(Gdx.files.internal("audio/missed_hit.mp3"));
	private long lastTime = System.currentTimeMillis();
    private long endId;
    private eButton eButton;
    private RedSheepAI redSheep;
	private Image[] uiFruits;
	private Table upperTable;
	private Table fruitsTable;
	private Window menuWindow;
	private Sound inventoryFullSound;
	private EndingAnimation endingAnimation;
	private BaseActor background;
	private Skin uiSkin;
	private boolean endScreenCreated;
	private boolean returnTableCreated;
	private Skin font;

	/**
     * Return player object.
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

	/**
	 * This is called when the screen is initialized. Called from inside the create method of the base class.
	 * */
	@Override
	public void initialize() {
		uiSkin = new Skin(Gdx.files.internal("uiskin.json"));
		font = new Skin(Gdx.files.internal("ui_skins/vhs-ui.json"));

		uiFruits = new Image[] {null, null, null, null, null};
		Texture texture = new Texture(Gdx.files.internal("Worldmap_final.png"));
		background = new BaseActor(0, 0, mainStage);
		background.loadTexture("Worldmap_final.png");

		inventoryFullSound = Gdx.audio.newSound(Gdx.files.internal("audio/Inventory_full.mp3"));

		BaseActor.setWorldBounds(175, 108, 647, 580); //whole width 1120
		viewport = mainStage.getViewport();
		camera = mainStage.getViewport().getCamera();
		player = new Player(200, 200, mainStage, this, spriteBatch); //Player is automatically added to mainStage and saved there

		generator = new Generator(530, 505, mainStage);
		house = new House(220, 273, mainStage);

		enemyPlayer = new OpponentPlayer(200, 200, mainStage);
		scoreLabel = new Label("Score: 0", BaseGame.labelStyle);
		timerLabel = new Label("", BaseGame.labelStyle);
		endScores = new Label("", BaseGame.labelStyle);
		upperTable = new Table();
		uiTable.top().left();
		uiTable.add(upperTable);
		upperTable.add(timerLabel).padLeft(150).padTop(50);
		upperTable.row();
		upperTable.add(scoreLabel).padLeft(150);


		fruitsTable = new Table();
		uiTable.row().expandY();
		uiTable.add(fruitsTable).top().left().padLeft(100);



		FruitGame.score = 0;

		sheep = new SheepAI(500, 500, mainStage, player, this);
		//redSheep = new RedSheepAI(500, 500, mainStage, player, this);

		Button.ButtonStyle buttonStyleReturn = new Button.ButtonStyle();
		Texture buttonTextureReturn = new Texture(Gdx.files.internal("buttons/refresh_unpressed_16x16.png"));
		TextureRegion buttonRegionReturn = new TextureRegion(buttonTextureReturn);
		buttonStyleReturn.up = new TextureRegionDrawable(buttonRegionReturn);
		returnButton = new Button(buttonStyleReturn);
		returnButton.addListener(
				(Event e) ->
				{
					if (returnButton.isOver()) {
						Texture buttonTextureReturn1 = new Texture(Gdx.files.internal("buttons/refresh_pressed_16x16.png"));
						TextureRegion buttonRegionReturn1 = new TextureRegion(buttonTextureReturn1);
						buttonStyleReturn.up = new TextureRegionDrawable(buttonRegionReturn1);
					} else {
						buttonStyleReturn.up = new TextureRegionDrawable(buttonRegionReturn);
					}
					if (!(e instanceof InputEvent) ||
							!((InputEvent)e).getType().equals(InputEvent.Type.touchDown)) {
						return false;
					}
					FruitGame.setActiveScreen(new MainMenuScreen());
					FruitGame.gameOver = false;
					return false;
				}
		);
		returnButton.setDisabled(true);
		returnButton.setVisible(false);

		createMenu();
	}

	/**
	 * This is called every frame.
	 * */
	@Override
	public void update(float dt) {
		if (FruitGame.gameOver) {
			if (!endScreenCreated) {
				scoreLabel.setVisible(false);
				timerLabel.setVisible(false);
				uiTable.clear();
				sheep.addAction(Actions.removeActor());
				endingAnimation = new EndingAnimation(0, 0, uiStage, loadEndingAnimation());
				endingAnimation.setSize(1920, 1080);
				uiStage.addActor(endingAnimation);
				uiTable.addAction(Actions.removeActor());
				System.out.println("New ending");
				endScreenCreated = true;
			}


			if (endingAnimation != null && endingAnimation.isAnimationFinished() && !returnTableCreated) {
				uiTable.setFillParent(true);
				uiStage.addActor(uiTable);
				endScores.setText(formatScores(FruitGame.scores));
				Table scoresTable = new Table();
				uiTable.center().add(scoresTable).center();
				scoresTable.center();
				scoresTable.add(endScores);
				scoresTable.row();
				scoresTable.add(returnButton);
				returnButton.setVisible(true);
				returnButton.setDisabled(false);
				returnTableCreated = true;
			}
			return;
		}

		//updateEnemyPosition(); // maybe do this in OpponentPlayer class
		for (BaseActor fruit : BaseActor.getList(mainStage, Fruit.class)) {
			if (fruit instanceof Fruit) {
				if (player.overlaps(fruit)) {
					sendCollectionPacket(((Fruit) fruit).getId(), player.getX(), player.getY());
					if (FruitGame.inventory.size() == 5) {
						inventoryFullSound.play();
					}
				}
			}
		}

		if (!fruitToBeAddedPos.isEmpty()) {
			generateFruit(false);
			generateFruit(true);
			clearFruit();
		}

		if (fruitToBeRemovedById != null) {
			for (BaseActor fruit : BaseActor.getList(mainStage, Fruit.class)) {
				if (fruit instanceof Fruit) {
					if (((Fruit) fruit).getId() == fruitToBeRemovedById) {
						fruit.addAction(Actions.removeActor());
						fruit.remove();
						System.out.printf("Removed fruit with id %d%n", fruitToBeRemovedById);
					}
				}
			}
			fruitToBeRemovedById = null;
		}

		player.alignCamera(camera);

		scoreLabel.setText(String.format("Score: %d", FruitGame.score));
		timerLabel.setText(String.format("%02d : %02d", FruitGame.timeInSeconds / 60, FruitGame.timeInSeconds % 60));

		if (player.overlaps(generator)) {
			if (!FruitGame.inventory.isEmpty()) {
				player.unloadFruits();
			}
		}

		if (player.overlaps(house)) {
			player.preventOverlap(house);
			eButton = new eButton(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, mainStage);
			for (BaseActor actor : BaseActor.getList(mainStage, eButton.class)) {
				actor.remove();
			}
		}
		uiFruits[0] = null;
		uiFruits[1] = null;
		uiFruits[2] = null;
		uiFruits[3] = null;
		uiFruits[4] = null;

		fruitsTable.clear();
		for (int i = 0; i < FruitGame.inventory.size(); i++) {
			uiFruits[i] = chooseUIFruit(FruitGame.inventory.get(i));
		}

		fruitsTable.add(uiFruits[0]);
		fruitsTable.add(uiFruits[1]);
		fruitsTable.add(uiFruits[2]);
		fruitsTable.add(uiFruits[3]);
		fruitsTable.add(uiFruits[4]);

	}

	private void createMenu() {

		menuWindow = new Window("Menu", uiSkin);
		menuWindow.setSize(480, 270);
		menuWindow.setPosition(810, 440); // Center the menu
		menuWindow.setMovable(true); // Allow the window to be moved

		// Add buttons
//		TextButton btnExit = new TextButton("Exit Game", font);
		Button.ButtonStyle buttonStyleExit = new Button.ButtonStyle();
		Texture buttonTextureExit = new Texture(Gdx.files.internal("buttons/exit_unpressed_300.png"));
		TextureRegion buttonRegionExit = new TextureRegion(buttonTextureExit);
		buttonStyleExit.up = new TextureRegionDrawable(buttonRegionExit);
		Button btnExit = new Button(buttonStyleExit);

		// Add listeners to button
		btnExit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				sheep.stopSounds();
				FruitGame.setActiveScreen(new MainMenuScreen());
				sendPlayerLeftGamePacketToServer();
			}
		});
		btnExit.addListener(
				(Event e) ->
				{
					if (btnExit.isOver()) {
						Texture buttonTextureExit1 = new Texture(Gdx.files.internal("buttons/exit_pressed.png"));
						TextureRegion buttonRegionExit1 = new TextureRegion(buttonTextureExit1);
						buttonStyleExit.up = new TextureRegionDrawable(buttonRegionExit1);
					} else {
						buttonStyleExit.up = new TextureRegionDrawable(buttonRegionExit);
					}
					return false;
				});

		Slider volumeSlider = new Slider(0f, 0.8f, 0.05f, false, font);
		Label sliderComment = new Label("Change volume", font);
		volumeSlider.setValue(volumeSlider.getValue()); // Set initial value

		volumeSlider.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
				float volume = volumeSlider.getValue();
				BaseScreen.themeSong.setVolume(volume);
				// You can use the volume value here to adjust the game volume
				// For example, you can use it to set the volume of your game's audio
			}
		});


		menuWindow.add(sliderComment);
		menuWindow.row().padTop(20f);
		menuWindow.add(volumeSlider);
		menuWindow.row().pad(40, 0, 10, 0); // Add padding
		// Add buttons
		menuWindow.add(btnExit);


		menuWindow.setVisible(false); // Initially not visible

		uiStage.addActor(menuWindow); // Add window to stage
	}

	private void sendPlayerLeftGamePacketToServer() {
		FruitGame.client.sendUDP(new PlayerLeftGamePacket(FruitGame.gameId));
	}

	private Image chooseUIFruit(int fruitPoints) {
		switch (fruitPoints) {
			case 100:
				return new Image(new Texture("UIFruits/EGreenFruit300.png"));
			case 200:
				return new Image(new Texture("UIFruits/EBlueFruit300.png"));
			case 300:
				return new Image(new Texture("UIFruits/EWhiteFruit300.png"));
			case 500:
				return new Image(new Texture("UIFruits/EGoldenFruit300.png"));
			default:
				throw new IllegalArgumentException();
		}
	}

	private void generateFruit(boolean opponentFruit) {
		float x = fruitToBeAddedPos.get("x");
		float y = fruitToBeAddedPos.get("y");
		if (opponentFruit) {
			fruitToBeAddedId = oppositeFruitToBeAddedId;
			x = FruitGame.FULL_WIDTH_X - x;
		}

		if (fruitToBeAddedPoints == 100) {
			new GreenFruit(x, y, mainStage, fruitToBeAddedId);
		} else if (fruitToBeAddedPoints == 200) {
			new BlueFruit(x, y, mainStage, fruitToBeAddedId);
		} else if (fruitToBeAddedPoints == 300) {
			new WhiteFruit(x, y, mainStage, fruitToBeAddedId);
		} else if (fruitToBeAddedPoints == 500) {
			new GoldenFruit(x, y, mainStage, fruitToBeAddedId);
		}
		System.out.println(String.format("Created fruit with id %d", fruitToBeAddedId));
	}

	private void clearFruit() {
		fruitToBeAddedPos.remove("x");
		fruitToBeAddedPos.remove("y");
		fruitToBeAddedId = null;
	}

	private String formatScores(Map<String, Integer> map) {
		List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(map.entrySet());
		sortedEntries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

		StringBuilder formattedOutput = new StringBuilder();
		for (Map.Entry<String, Integer> entry : sortedEntries) {
			formattedOutput.append(String.format("%s: %d%n", entry.getKey(), entry.getValue()));
		}
		return formattedOutput.toString();
	}

	public void updateEnemyPosition () {
		enemyPlayer.setPosition(FruitGame.enemyX, FruitGame.enemyY);
	}

	@Override
	public void dispose() {
		FruitGame.client.close();
        try {
            FruitGame.client.dispose();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	public void addFruit(float x, float y, int id, int points, int oppositeFruitId) {
        fruitToBeAddedPos.put("x", x);
		fruitToBeAddedPos.put("y", y);
		fruitToBeAddedId = id;
		fruitToBeAddedPoints = points;
		oppositeFruitToBeAddedId = oppositeFruitId;
	}

	public void addSheep(float x, float y) {
		sheep = new SheepAI(x, y, mainStage, player, this);
	}

	public void removeFruit(Integer id) {
		fruitToBeRemovedById = id;
		System.out.println("RemoveFruit id" + id);
	}

	public void spoilFruit(int id, int spoiledStage) {
		for (BaseActor fruit : BaseActor.getList(mainStage, Fruit.class)) {
			if (fruit instanceof Fruit) {
				if (((Fruit) fruit).getId() == id) {
					((Fruit) fruit).setSpoiledStage(spoiledStage);
					((Fruit) fruit).spoil();
				}
			}
		}
	}

	public void swing() {
		boolean hitSomething = false;
		swingHitbox = new swingHitbox(player.getX() + player.getWidth() / 2, player.getY() + player.getHeight() / 2, player.getRotation(), mainStage);
		for (BaseActor sheep : BaseActor.getList(mainStage, RedSheepAI.class)) {
			if (swingHitbox.overlaps(sheep)) {
				sheep.remove();
				sheepHit.play();
				hitSomething = true;
			}
		}
		for (BaseActor sheep : BaseActor.getList(mainStage, SheepAI.class)) {
			if (swingHitbox.overlaps(sheep)) {
				sheep.remove();
				sheepHit.play();
				((SheepAI) sheep).stopSounds();
				hitSomething = true;
			}
		}
		for (BaseActor actor : BaseActor.getList(mainStage, swingHitbox.class)) {
			actor.remove();
		}
		if (!hitSomething) {
			if (System.currentTimeMillis() - lastTime > 500) {
				missedHit.play();
				lastTime = System.currentTimeMillis();
			}

		}
	}

	private void sendCollectionPacket(int fruitId, float playerX, float playerY) {
		FruitGame.client.sendUDP(new FruitCollisionPacket(fruitId,
				playerX, playerY, FruitGame.gameId));
	}

	@Override
	public void resize(int width, int height) {
		mainStage.getViewport().update(width, height, true);
		System.out.println("Resize game screen");
	}

	@Override
	public boolean keyDown(int i) {
		if (i == Input.Keys.ESCAPE) {
			menuWindow.setVisible(!menuWindow.isVisible());
		}
		return false;
	}

	public void playEndSound(boolean shouldItPlay) {
		if (shouldItPlay) {
			endId = endMusic.play(0.5f);
		} else {
			endMusic.stop(endId);
		}
	}

	private String[] loadEndingAnimation() {
		sheep.stopSounds();
		LinkedList<String> filenames = new LinkedList<>();
		for (int i = 1; i < 212; i++) {
			filenames.add("EndingAnimation/Base_ending/Green_end" + i + ".png");
		}
		Map<String, Integer> map = FruitGame.scores;
		List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(map.entrySet());
		sortedEntries.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));
		for (int i = 213; i < 280; i++) {
			if (sortedEntries.get(0).getKey().equals(FruitGame.playerName)) {
				filenames.add("EndingAnimation/Green_end/Green_end" + i + ".png");
			} else {
				filenames.add("EndingAnimation/Red_end/Red_end" + i + ".png");
			}
		}
		return filenames.toArray(String[]::new);
	}

}
