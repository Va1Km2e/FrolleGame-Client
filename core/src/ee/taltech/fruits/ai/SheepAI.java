package ee.taltech.fruits.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import ee.taltech.fruits.BaseActor;
import ee.taltech.fruits.FruitGame;
import ee.taltech.fruits.Player;
import ee.taltech.fruits.fruits.Fruit;
import ee.taltech.fruits.packets.SheepAteFruitPacket;
import ee.taltech.fruits.packets.SheepAttackedPlayerPacket;
import ee.taltech.fruits.screens.GameScreen;

import java.util.List;
import java.util.Random;


public class SheepAI extends BaseActor {
    public static final int PLAYER_OUT_OF_RANGE = 70;
    public static final int PLAYER_IN_RANGE = 70;
    public static final int FRUIT_IN_RANGE = 200;
    private float[] targetedLocation = new float[] {0, 0};
    private int eatingFruitTimer = 0;
    private Player player;
    private GameScreen screen;
    private final Sound sheepSound = Gdx.audio.newSound(Gdx.files.internal("audio/sheep_sound.mp3"));
    private final Sound sheepMovingSound = Gdx.audio.newSound(Gdx.files.internal("audio/sheep_moving.mp3"));
    private final Sound playerHit = Gdx.audio.newSound(Gdx.files.internal("audio/male_hurt.mp3"));
    public boolean movingSoundPlaying = false;
    private long sheepMovingSoundId;
    private enum State {
        WANDERING,
        MOVING_TO_FRUIT,
        EATING_FRUIT,
        ATTACKING
    }

    private enum LastMovement {
        FRONT,
        BACK,
        LEFT,
        RIGHT
    }

    private State state;
    private LastMovement lastMovement = LastMovement.FRONT;
    private Fruit targetedFruit = null;
    private Fruit closestFruit = null;
    private boolean playerAttacked = false;

    /**
     * Create a BaseActor and adds it to a stage (defined in BaseScreen)
     *
     * @param x int, actor position x.
     * @param y int, actor position y.
     * @param s stage.
     */
    public SheepAI(float x, float y, Stage s, Player player, GameScreen screen) {
        super(x, y, s);
        this.player = player;
        state = State.WANDERING;

        loadTexture("Sheep/Sheep_front.png");

        setAcceleration(Integer.MAX_VALUE);
        setMaxSpeed(20);
        setDeceleration(Integer.MAX_VALUE);
        setBoundaryRectangle();
        this.screen = screen;
        pickNewTargetedLocation();

        System.out.println("SHEEP CREATED");
    }

    @Override
    public void act(float dt) {
        System.out.println("sheep act" );
        super.act(dt);
        sense();
        plan();
        calculateMovement(dt);
        applyAnimation();
        setAnimationPaused(!isMoving() && !state.equals(State.EATING_FRUIT));
        boundToWorld();
        System.out.println(state);

        if (playerLeftRange()) {
            playerAttacked = false;
        }

        if (Math.random() < 0.0005) {
            sheepSound.play(0.2f);
        }

        if (state.equals(State.ATTACKING) || state.equals(State.WANDERING) || state.equals(State.MOVING_TO_FRUIT)) {
            playSheepMovingSound(true);
        } else {
            playSheepMovingSound(false);
        }
    }

    private void applyAnimation() {
        switch (state) {
            case WANDERING:
            case MOVING_TO_FRUIT:
                chooseAnimation(new String[] {"Sheep/Sheep_Right_Walking.png", "Sheep/Sheep_Back_Walking.png",
                        "Sheep/Sheep_Left_Walking.png", "Sheep/Sheep_Front_Walking.png"}, 2);
                break;
            case EATING_FRUIT:
                chooseAnimationFromLast(new String[] {"Sheep/Right_Eating.png", "Sheep/Back_eating.png",
                        "Sheep/Left_eating.png", "Sheep/Front_Eating.png"}, 9);
                break;
        }
    }

    private void chooseAnimation(String[] fileNames, int rows) {
        if (getMotionAngle() > 315 && getMotionAngle() < 360 || getMotionAngle() > 0 && getMotionAngle() < 45) {
            setAnimation(loadAnimationFromSheet(fileNames[0], rows, 1, 0.2f, true));
            lastMovement = LastMovement.RIGHT;
        } else if (getMotionAngle() >= 45 && getMotionAngle() < 135) {
            setAnimation(loadAnimationFromSheet(fileNames[1], rows, 1, 0.2f, true));
            lastMovement = LastMovement.BACK;
        } else if (getMotionAngle() >= 135 && getMotionAngle() < 225) {
            setAnimation(loadAnimationFromSheet(fileNames[2], rows, 1, 0.2f, true));
            lastMovement = LastMovement.LEFT;
        } else {
            setAnimation(loadAnimationFromSheet(fileNames[3], rows, 1, 0.2f, true));
            lastMovement = LastMovement.FRONT;
        }
        System.out.println(getMotionAngle());
    }

    private void chooseAnimationFromLast(String[] fileNames, int rows) {
        if (lastMovement == LastMovement.RIGHT) {
            setAnimation(loadAnimationFromSheet(fileNames[0], rows, 1, 0.2f, true));
        } else if (lastMovement == LastMovement.BACK) {
            setAnimation(loadAnimationFromSheet(fileNames[1], rows, 1, 0.2f, true));
        } else if (lastMovement == LastMovement.LEFT) {
            setAnimation(loadAnimationFromSheet(fileNames[2], rows, 1, 0.2f, true));
        } else {
            setAnimation(loadAnimationFromSheet(fileNames[3], rows, 1, 0.2f, true));
        }
    }

    private void sense() {
        findTheClosestFruit();
    }

    private void plan() {
        switch (state) {
            case WANDERING:
                if (isPlayerNearby() && !playerAttacked) {
                    state = State.ATTACKING;
                } else if (isFruitNearby()) {
                    state = State.MOVING_TO_FRUIT;
                    setMaxSpeed(40);
                    targetedFruit = closestFruit;
                } else if (isAtFruit()) {
                    state = State.EATING_FRUIT;
                } else {
                    wander();
                }
                break;
            case MOVING_TO_FRUIT:
                if (isPlayerNearby() && !playerAttacked) {
                    state = State.ATTACKING;
                } else if (didFruitDisappear()) {
                    state = State.WANDERING;
                    setMaxSpeed(20);
                } else if (isAtFruit()) {
                    state = State.EATING_FRUIT;
                } else {
                    moveToFruit();
                }
                break;
            case EATING_FRUIT:
                if (isPlayerNearby() && !playerAttacked) {
                    attackPlayer();
                } else if (didFruitDisappear()) {
                    state = State.WANDERING;
                    setMaxSpeed(20);
                } else {
                    eatFruit();
                }
                break;
            case ATTACKING:
                if (playerAttacked || playerLeftRange()) {
                    state = State.WANDERING;
                    setMaxSpeed(20);
                } else {
                    setMaxSpeed(100);
                    attackPlayer();
                }
                break;
        }
    }

    private void wander() {
        if (isWithinRange(getX(), getY(), targetedLocation[0], targetedLocation[1], 30)) {
            pickNewTargetedLocation();
        }
        moveTo(targetedLocation[0], targetedLocation[1]);
    }

    private void pickNewTargetedLocation() {
        Random r = new Random();
        targetedLocation[0] = r.nextFloat(170, 640);
        targetedLocation[1] = r.nextFloat(90, 580);
    }

    private void moveToFruit() {
        if (targetedFruit == null) {
            return;
        }
        moveTo(targetedFruit.getX(), targetedFruit.getY());
    }

    private void moveTo(float targetX, float targetY) {
        float dy = Math.abs(targetY - getY());
        float dx = Math.abs(targetX - getX());
        float hypotenuse = (float) Math.sqrt(dy * dy + dx * dx);
        float angle = (float) Math.toDegrees(Math.asin(dy / hypotenuse));
        if (targetY < getY() && targetX > getX()) {
            angle = -angle;
        } else if (targetY > getY() && targetX < getX()) {
            angle = 180 - angle;
        } else if (targetY < getY() && targetX < getX()) {
            angle = 180 + angle;
        }
        accelerateAtAngle(angle);
    }

    private void eatFruit() {
        if (eatingFruitTimer < 108) {
            eatingFruitTimer++;
            return;
        }
        eatingFruitTimer = 0;
        sendSheepAteFruitToServer(targetedFruit.getId());
        screen.removeFruit(targetedFruit.getId());
        //screen.addSheep(targetedFruit.getX(), targetedFruit.getY());
        targetedFruit = null;
    }

    private void attackPlayer() {
        moveTo(player.getX(), player.getY());
        if (this.overlaps(player)) {
            playerAttacked = true;
            sendSheepAttackedPlayerToServer();
            System.out.println("Player attacked!!!");
        }
    }

    private boolean isFruitNearby() {
        return closestFruit != null && isWithinRange(closestFruit.getX(), closestFruit.getY(), getX(), getY(), FRUIT_IN_RANGE);
    }
    private boolean isPlayerNearby() {
        return isWithinRange(getX(), getY(), player.getX(), player.getY(), PLAYER_IN_RANGE);
    }
    private boolean isAtFruit() {
        return targetedFruit != null && this.overlaps(targetedFruit);
    }

    private boolean didFruitDisappear() {
        return !stageContainsFruit(targetedFruit);
    }

    private boolean stageContainsFruit(Fruit targetedFruit) {
        return BaseActor.getList(player.getStage(), Fruit.class).contains(targetedFruit);
    }

    private boolean playerLeftRange() {
        return !isWithinRange(getX(), getY(), player.getX(), player.getY(), PLAYER_OUT_OF_RANGE);
    }

    private boolean isWithinRange(float x1, float y1, float x2, float y2, float range) {
        return Math.abs(x1 - x2) < range && Math.abs(y1 - y2) < range;
    }

    private double calculateDistance(float x1, float y1, float x2, float y2) {
            return Math.sqrt(Math.abs(x1 - x2) + Math.abs(y1 - y2));
    }

    private void findTheClosestFruit() {
        double smallestDistance = Integer.MAX_VALUE;
        Fruit closestFruit = null;

        List<BaseActor> fruits = BaseActor.getList(player.getStage(), Fruit.class);
        if (fruits.isEmpty()) {
            this.closestFruit = null;
            return;
        }
        for (BaseActor fruit : fruits) {
            if (fruit instanceof Fruit) {
                if (fruit.getX() > 630) {
                    continue;
                }

                double distance = calculateDistance(getX(), getY(), fruit.getX(), fruit.getY());
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                    closestFruit = (Fruit) fruit;
                }
            }
        }
        this.closestFruit = closestFruit;
    }

    private void sendSheepAteFruitToServer(int fruitId) {
        FruitGame.client.sendUDP(new SheepAteFruitPacket(fruitId,FruitGame.gameId));
    }

    public void playSheepMovingSound(boolean play) {
        if (play && !movingSoundPlaying) {
            sheepMovingSoundId = sheepMovingSound.play(1.5f);
            sheepSound.setLooping(sheepMovingSoundId, true);
            movingSoundPlaying = true;
        }
        if (!play) {
            sheepSound.stop(sheepMovingSoundId);
            movingSoundPlaying = false;
        }
    }

    public void stopSounds() {
        sheepSound.stop(sheepMovingSoundId);
    }

    public void sendSheepAttackedPlayerToServer() {
        playerHit.play(0.5f);
        FruitGame.client.sendUDP(new SheepAttackedPlayerPacket());
    }
}
