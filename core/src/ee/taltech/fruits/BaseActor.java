package ee.taltech.fruits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

public class BaseActor extends Actor {
    protected static Rectangle worldBounds;
    protected Animation<TextureRegion> animation;
    protected float elapsedTime;
    private boolean animationPaused;
    private Vector2 velocityVec;
    private Vector2 accelerationVec;
    private float acceleration;
    private float maxSpeed;
    private float deceleration;
    private Polygon boundaryPolygon;

    /**
     * Set the bounds a BaseActor can't leave.
     *
     * @param originX down left corner x.
     * @param originY down left corner y.
     * @param width width.
     * @param height height.
     * */
    public static void setWorldBounds(float originX, float originY, float width, float height) {
        worldBounds = new Rectangle(originX, originY, width, height);
    }

    /**
     * Set world bounds based on a background BaseActor.
     * @param ba BaseActor background.
     * */
    public static void setWorldBounds(BaseActor ba)
    {
        setWorldBounds(ba.getOriginX(), ba.getOriginY(), ba.getWidth(), ba.getHeight());
    }

    /**Returns all actors of class c in a stage.
     * @param stage mainStage for game objects, uiStage for ui objects
     * @param c class.
     * @return list of baseActor.*/
    public static ArrayList<BaseActor> getList(Stage stage, Class c)
    {
        ArrayList<BaseActor> list = new ArrayList<>();
        for (Actor a : stage.getActors())
        {
            if (c.isInstance(a)) {
                list.add((BaseActor) a);
            }
        }
        return list;
    }

    /**
     * Create a BaseActor and adds it to a stage (defined in BaseScreen)
     * @param x int, actor position x.
     * @param y int, actor position y.
     * @param s stage.
     * */
    public BaseActor(float x, float y, Stage s) {
        // call constructor from Actor class
        super();
        // perform additional initialization tasks
        setPosition(x, y);
        s.addActor(this);

        animation = null;
        elapsedTime = 0;
        animationPaused = false;
        velocityVec = new Vector2(0, 0);
        accelerationVec = new Vector2(0, 0);
        acceleration = 0;
        maxSpeed = 1000;
        deceleration = 0;
    }

    /**
     * Set an animation.
     * @param animation Animation<TextureRegion>. TextureRegion can use uv coordinates.
     * */
    public void setAnimation(Animation<TextureRegion> animation) {
        this.animation = animation;
        TextureRegion tr = this.animation.getKeyFrame(0);
        float w = tr.getRegionWidth();
        float h = tr.getRegionHeight();
        setSize(w, h);
        setOrigin(w / 2, h / 2);

        if (boundaryPolygon == null)
            setBoundaryRectangle();
    }

    /**
     * Pause animation.
     * @param pause, pause animation.
     * */
    public void setAnimationPaused(boolean pause) {
        animationPaused = pause;
    }

    /**
     * Act, called every frame. Calculates elapsedTime to see if animation is done.
     * @param dt deltaTime.
     * */
    @Override
    public void act(float dt) {
        super.act(dt);
        if (!animationPaused)
            elapsedTime += dt;
    }

    /**
     * Draw the actor the BaseActor to the screen.
     * @param batch batch, defined and set in the base method.
     * @param parentAlpha parent alpha, set to 1 in base method.
     * */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (animation != null && isVisible()) {
            batch.setProjectionMatrix(getStage().getCamera().combined);
            batch.draw(animation.getKeyFrame(elapsedTime),
                    getX(), getY(), getWidth(), getHeight());
        }
    }

    /**
     * Load an animation from an array of files.
     * @param fileNames array of filenames.
     * @param frameDuration time between frames in seconds.
     * @param loop boolean loop or not.
     * @return animation.
     * */
    public Animation<TextureRegion> loadAnimationFromFiles(String[] fileNames,
                                                           float frameDuration, boolean loop) {
        int fileCount = fileNames.length;
        Array<TextureRegion> textureArray = new Array<>();
        for (int n = 0; n < fileCount; n++) {
            String fileName = fileNames[n];
            Texture texture = new Texture(Gdx.files.internal(fileName));
            texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            textureArray.add(new TextureRegion(texture));
        }
        Animation<TextureRegion> anim = new Animation<>(frameDuration, textureArray);
        if (loop)
            anim.setPlayMode(Animation.PlayMode.LOOP);
        else
            anim.setPlayMode(Animation.PlayMode.NORMAL);
        if (animation == null)
            setAnimation(anim);
        return anim;
    }

    /**
     * Load animation from a single file divided up into cells.
     * @param fileName filename.
     * @param rows int rows.
     * @param cols int columns.
     * @param frameDuration time between frames in seconds.
     * @param loop boolean loop animation.
     * @return animation.
     * */
    public Animation<TextureRegion> loadAnimationFromSheet(String fileName, int rows, int cols,
                                                           float frameDuration, boolean loop) {
        Texture texture = new Texture(Gdx.files.internal(fileName), true);
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureArray = new Array<>();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                textureArray.add(temp[r][c]);
        Animation<TextureRegion> anim = new Animation<>(frameDuration, textureArray);
        if (loop)
            anim.setPlayMode(Animation.PlayMode.LOOP);
        else
            anim.setPlayMode(Animation.PlayMode.NORMAL);
        if (animation == null)
            setAnimation(anim);
        return anim;
    }

    /**
     * Load a texture. Uses the loadAnimationFromFiles method with a single file animation looping every frame.
     * @return animation.
     * */
    public Animation<TextureRegion> loadTexture(String fileName) {
        String[] fileNames = new String[1];
        fileNames[0] = fileName;
        return loadAnimationFromFiles(fileNames, 1, true);
    }

    /**
     * Is animation finished.
     * @return boolean animation is finished.
     * */
    public boolean isAnimationFinished() {
        return animation.isAnimationFinished(elapsedTime);
    }

    /**
     * Set a rectangle around the actor for collisions.
     * */
    public void setBoundaryRectangle() {
        float w = getWidth();
        float h = getHeight();
        float[] vertices = {0, 0, w, 0, w, h, 0, h};
        boundaryPolygon = new Polygon(vertices);
    }

    /**
     * Set a polygon around the actor with n sides to more closely approximate the shape.
     * @param numSides int number of sides for polygon.
     * */
    public void setBoundaryPolygon(int numSides) {
        float w = getWidth();
        float h = getHeight();
        float[] vertices = new float[2 * numSides];
        for (int i = 0; i < numSides; i++) {
            float angle = i * 6.28f / numSides;
            // x-coordinate
            vertices[2 * i] = w / 2 * MathUtils.cos(angle) + w / 2;
            // y-coordinate
            vertices[2 * i + 1] = h / 2 * MathUtils.sin(angle) + h / 2;
        }
        boundaryPolygon = new Polygon(vertices);
    }

    /**
     * Get boundary polygon.
     * @return boundary polygon.
     * */
    public Polygon getBoundaryPolygon() {
        boundaryPolygon.setPosition(getX(), getY());
        boundaryPolygon.setOrigin(getOriginX(), getOriginY());
        boundaryPolygon.setRotation(getRotation());
        boundaryPolygon.setScale(getScaleX(), getScaleY());
        return boundaryPolygon;
    }

    /**
     * See if BaseActor overlaps with another BaseActor.
     * @param other BaseActor.
     * @return boolean overlaps.
     * */
    public boolean overlaps(BaseActor other) {
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();
        // initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))
            return false;
        return Intersector.overlapConvexPolygons(poly1, poly2);
    }

    /**
     * Center at position.
     * @param x x.
     * @param y y.
     * */
    public void centerAtPosition(float x, float y) {
        setPosition(x - getWidth() / 2, y - getHeight() / 2);
    }

    /**
     * Center at actor.
     * @param other other BaseActor to center on.
     * */
    public void centerAtActor(BaseActor other) {
        centerAtPosition(other.getX() + other.getWidth() / 2, other.getY() + other.getHeight() / 2);
    }

    /**
     * Set opacity.
     * @param opacity float.
     * */
    public void setOpacity(float opacity) {
        this.getColor().a = opacity;
    }

    /**
     * Prevent overlap.
     * If this BaseActor overlaps with other BaseActor,
     * move this BaseActor the minimum distance to stop the overlapping.
     * @param other BaseActor.
     * @return MinimumTranslationVector so that the actors no longer overlap.
     * */
    public Vector2 preventOverlap(BaseActor other) {
        Polygon poly1 = this.getBoundaryPolygon();
        Polygon poly2 = other.getBoundaryPolygon();
        // initial test to improve performance
        if (!poly1.getBoundingRectangle().overlaps(poly2.getBoundingRectangle()))
            return null;
        Intersector.MinimumTranslationVector mtv = new Intersector.MinimumTranslationVector();
        boolean polygonOverlap = Intersector.overlapConvexPolygons(poly1, poly2, mtv);
        if (!polygonOverlap)
            return null;
        this.moveBy(mtv.normal.x * mtv.depth, mtv.normal.y * mtv.depth);
        return mtv.normal;
    }

    /**
     * Make sure actor doesn't leave world bounds.
     * */
    public void boundToWorld() {
        // check left edge
        if (getX() < worldBounds.x)
            setX(worldBounds.x);
        // check right edge
        if (getX() + getWidth() > worldBounds.width)
            setX(worldBounds.width - getWidth());
        // check bottom edge
        if (getY() < worldBounds.y)
            setY(worldBounds.y);
        // check top edge
        if (getY() + getHeight() > worldBounds.height)
            setY(worldBounds.height - getHeight());
    }

    /**
     * Set speed.
     * velocityVec is Vector2(speed, angle).
     * @param speed float.
     * */
    public void setSpeed(float speed)
    {
        // if length is zero, then assume motion angle is zero degrees
        if (velocityVec.len() == 0)
            velocityVec.set(speed, 0);
        else
            velocityVec.setLength(speed);
    }

    /**
     * Get speed.
     * @return float speed.
     * */
    public float getSpeed()
    {
        return velocityVec.len();
    }

    /**
     * Set motion angle.
     * @param angle float.
     * */
    public void setMotionAngle(float angle)
    {
        velocityVec.setAngleDeg(angle);
    }

    /**
     * Get motion angle.
     * @return float angle.
     * */
    public float getMotionAngle()
    {
        return velocityVec.angleDeg();
    }

    /**
     * Is actor moving.
     * @return boolean is moving.
     * */
    public boolean isMoving()
    {
        return (getSpeed() > 0);
    }

    /**
     * Set acceleration
     * @return boolean is moving.
     * */
    public void setAcceleration(float acc)
    {
        acceleration = acc;
    }

    /**
     * Accelerate at an angle.
     * Default method for player movement.
     * @param angle float.
     * */
    public void accelerateAtAngle(float angle)
    {
        accelerationVec.add(new Vector2(acceleration, 0).setAngleDeg(angle));
    }

    /**
     * Accelerate at the current angle.
     * Calls accelerateAtAngle.
     * */
    public void accelerateForward()
    {
        accelerateAtAngle(getRotation());
    }

    /**
     * Set max speed.
     * @param ms float max speed.
     * */
    public void setMaxSpeed(float ms)
    {
        maxSpeed = ms;
    }

    /**
     * Set deceleration - how fast character stops.
     * @param dec float deceleration.
     * */
    public void setDeceleration(float dec)
    {
        deceleration = dec;
    }

    /**
     * Calculate movement.
     * @param dt delta time.
     * */
    public void calculateMovement(float dt)
    {
        // apply acceleration
        velocityVec.add(accelerationVec.x * dt, accelerationVec.y * dt);
        float speed = getSpeed();
        // decrease speed (decelerate) when not accelerating
        if (accelerationVec.len() == 0)
            speed -= deceleration * dt;
        // keep speed within set bounds
        speed = MathUtils.clamp(speed, 0, maxSpeed);
        // update velocity
        setSpeed(speed);
        // apply velocity
        moveBy(velocityVec.x * dt, velocityVec.y * dt);
        // reset acceleration
        accelerationVec.set(0,0);
    }
}
