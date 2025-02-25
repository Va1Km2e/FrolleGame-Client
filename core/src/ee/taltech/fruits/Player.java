package ee.taltech.fruits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ee.taltech.fruits.packets.PlayerPositionXPacket;
import ee.taltech.fruits.packets.PlayerPositionYPacket;
import ee.taltech.fruits.packets.UnloadFruitsPacket;
import ee.taltech.fruits.screens.GameScreen;

/**
 * This class for main player.
 */
public class Player extends BaseActor {

    public static int moveLeft = Input.Keys.LEFT;
    public static int moveRight = Input.Keys.RIGHT;
    public static int moveUp = Input.Keys.UP;
    public static int moveDown = Input.Keys.DOWN;
    public Sound walkingSound = Gdx.audio.newSound(Gdx.files.internal("audio/player_walking.mp3"));
    public Sound depositingSound = Gdx.audio.newSound(Gdx.files.internal("audio/depositing_sound.mp3"));
    public boolean isMoving = false;
    public boolean isWalkingSoundPlaying = false;
    public static int hit = Input.Keys.E;
    private boolean isUnloadingFruits = false;
    private float unloadingTimeCounter = 0;
    private GameScreen screen;
    private SpriteBatch spriteBatch;
    private String standingPositionToTake = "standing/g_stand_front.png";

    /**
     * Make new Player class instance.
     *
     * @param x coordinate.
     * @param y coordinate.
     * @param s stage.
     */
    public Player(int x, int y, Stage s, GameScreen screen, SpriteBatch batch) {
        super(x, y, s);
        this.screen = screen;
        this.spriteBatch = batch;
        loadTexture("G_single.png");

        setAcceleration(Integer.MAX_VALUE);
        setMaxSpeed(140);
        setDeceleration(Integer.MAX_VALUE);
        setBoundaryRectangle();
    }

    /**
     * Act. Called every frame.
     *
     * @param dt float delta time.
     */
    @Override
    public void act(float dt) {
        if (FruitGame.gameOver) {
            return;
        }
        super.act(dt);
        if (isUnloadingFruits) {
            if (unloadingTimeCounter == 0) {
                setAnimation(loadAnimationFromSheet("g_deposit.png", 7, 1, 0.2f, true));
                depositingSound.play(0.5f);
            }
            System.out.println(dt);

            //setAnimationPaused(false);
            unloadingTimeCounter += dt;
            System.out.println("UNLOADING " + unloadingTimeCounter);
            if (unloadingTimeCounter > 1.4f) {
                isUnloadingFruits = false;
            } else {
                System.out.println("returned");
                return;
            }
        }

        //super.act(dt);

        checkMovementInput();

        if (isMoving) {
            if (!isWalkingSoundPlaying) {
                walkingSound.play(0.7f);
                isWalkingSoundPlaying = true;
            }
            isMoving = false;
        } else {
            walkingSound.stop();
            isWalkingSoundPlaying = false;
        }

        checkHitInput();
        calculateMovement(dt);

        if (isMoving()) {
            sendPositionXToServer();
            sendPositionYToServer();
        }

        setAnimationPaused(!isMoving());

        if (getSpeed() > 0) {
            setRotation(getMotionAngle());
        }

        boundToWorld();
    }

    private void checkMovementInput() {
        if (Gdx.input.isKeyPressed(moveLeft)) {
            accelerateAtAngle(180);
            setAnimation(loadAnimationFromSheet("g_walking/G_left_walking.png", 3, 1, 0.2f, true));
            standingPositionToTake = "standing/g_stand_left.png";
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(moveRight)) {
            accelerateAtAngle(0);
            setAnimation(loadAnimationFromSheet("g_walking/G_right_walking.png", 3, 1, 0.2f, true));
            standingPositionToTake = "standing/g_stand_right.png";
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(moveUp)) {
            accelerateAtAngle(90);
            setAnimation(loadAnimationFromSheet("g_walking/G_back_walking.png", 3, 1, 0.2f, true));
            standingPositionToTake = "standing/g_stand_back.png";
            isMoving = true;
        }
        if (Gdx.input.isKeyPressed(moveDown)) {
            accelerateAtAngle(270);
            setAnimation(loadAnimationFromSheet("g_walking/G_front_walking.png", 3, 1, 0.2f, true));
            standingPositionToTake = "standing/g_stand_front.png";
            isMoving = true;
        }
        if (!isMoving) {
            setAnimation(loadAnimationFromSheet(standingPositionToTake, 1, 1, 0.2f, false));
        }
    }

    private void checkHitInput() {
        if (Gdx.input.isKeyPressed(hit)) {
            System.out.println("hit");
            screen.swing();
        }
    }

    /**
     * Send player x coordinate to server.
     */
    private void sendPositionXToServer() {
        FruitGame.client.sendUDP(new PlayerPositionXPacket(getX(), FruitGame.gameId));
    }

    /**
     * Send player y coordinate to server.
     */
    private void sendPositionYToServer() {
        FruitGame.client.sendUDP(new PlayerPositionYPacket(getY(), FruitGame.gameId));
    }

    /**Aligns camera to player*/
    public void alignCamera(Camera cam)
    {
        // center camera on actor
        cam.position.set( this.getX() + this.getOriginX(), this.getY() + this.getOriginY(), 0 );
        // bound camera to layout
        cam.position.x = MathUtils.clamp(cam.position.x,
                cam.viewportWidth/2,  worldBounds.width - cam.viewportWidth/2 + 200);
        cam.position.y = MathUtils.clamp(cam.position.y,
                cam.viewportHeight/2, worldBounds.height - cam.viewportHeight/2 + 100);
        cam.update();
    }

    /**
     * Used at the end of the game for stopping walking sound effect.
     */
    public void stopWalkingSound() {
        walkingSound.stop();
    }

    public void unloadFruits() {
        if (!isUnloadingFruits) {
            unloadingTimeCounter = 0;
            isUnloadingFruits = true;
            sendUnloadToServer();
            FruitGame.inventory.clear();
        }
    }

    private void sendUnloadToServer() {
        FruitGame.client.sendUDP(new UnloadFruitsPacket());
    }
}
