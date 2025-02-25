package ee.taltech.fruits.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ee.taltech.fruits.FruitGame;

public abstract class BaseScreen implements Screen, InputProcessor {
    protected Stage mainStage;
    protected ExtendViewport extendViewport;
    protected ScreenViewport screenViewport;
    protected Stage uiStage;
    protected Table uiTable;
    public static boolean isThemePlaying = false;
    public static Music themeSong = Gdx.audio.newMusic(Gdx.files.internal("audio/themeSong.mp3"));
    protected SpriteBatch spriteBatch;
    protected TextureRegion backgroundTextureRegion;
    private int loopsPassed = 0; // made to slow down changing between windowed and fullscreen.
    private static boolean firstTimeFullscreen = false;


    /**
     * mainStage is where all the objects (actors) are rendered (before the ui stage).
     * uiStage is where the UI elements are rendered.
     * */
    public BaseScreen()
    {
        extendViewport = new ExtendViewport(320, 180);
        screenViewport = new ScreenViewport();
        spriteBatch = new SpriteBatch();
        mainStage = new Stage(extendViewport, spriteBatch);
        uiStage = new Stage(screenViewport);
        uiTable = new Table();
        uiTable.setFillParent(true);
        uiStage.addActor(uiTable);
        Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
        Gdx.graphics.setFullscreenMode(currentMode);
        initialize();
    }

    /**
     * Actor specific create method.
     * */
    public abstract void initialize();

    /**
     * Actor specific render method.
     * */
    public abstract void update(float dt);

    /**Render method that's called every frame.*/
    public void render(float dt)
    {
        extendViewport.apply();
        update(dt);
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (backgroundTextureRegion != null) {
            spriteBatch.setProjectionMatrix(extendViewport.getCamera().combined);
            spriteBatch.begin();
            spriteBatch.draw(backgroundTextureRegion, 0, 0);
            spriteBatch.end();
        }

        mainStage.act(dt);
        uiStage.act(dt);
        screenViewport.apply();
        mainStage.draw();
        uiStage.draw();

        changeScreenMode();
    }
    // methods required by Screen interface
    public void resize(int width, int height)
    {
        extendViewport.update(width, height);
        screenViewport.update(width, height);
    }
    public void pause()   {  }
    public void resume()  {  }
    public void dispose() {  }
    public void show() {
        InputMultiplexer im = (InputMultiplexer)Gdx.input.getInputProcessor();
        im.addProcessor(this);
        im.addProcessor(uiStage);
        im.addProcessor(mainStage);
    }
    public void hide() {
        InputMultiplexer im = (InputMultiplexer)Gdx.input.getInputProcessor();
        im.removeProcessor(this);
        im.removeProcessor(uiStage);
        im.removeProcessor(mainStage);
    }

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

    /**
     * Start theme song
     */
    public void startThemeSong() {
        if (!themeSong.isPlaying()) {
            themeSong.setVolume(0.15f); // 0.15f
            themeSong.play();
            System.out.println("play");
            themeSong.setLooping(true);
        }
    }

    public void endThemeSong() {
        System.out.println("tried stopping theme");
        themeSong.dispose();
    }

    /**
     * Let players change between fullscreen and windowed screen
     */
    public void changeScreenMode() {
        if (!firstTimeFullscreen) {
            FruitGame.setActiveScreen(new MainMenuScreen());
            firstTimeFullscreen = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_RIGHT) && loopsPassed > 50) {
            boolean fullScreen = Gdx.graphics.isFullscreen();
            Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
            if (fullScreen) {
                Gdx.graphics.setWindowedMode(960, 580);
                System.out.println("go windowed");
                loopsPassed = 0;
            } else {
                Gdx.graphics.setFullscreenMode(currentMode);
                System.out.println("fullscreen");
                loopsPassed = 0;
            }
        } else {
            if (loopsPassed < 100) {
                loopsPassed++;
            }
        }
    }
}

