/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-05-28
 * This file the Settings menu screen of Rise of Benum Tower Defense.
 */

package io.github.towerDefense;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;


public class SettingsScreen implements Screen { 
    private final Main game;
    private SpriteBatch batch;
    private Texture backgroundImage;
    private Texture effectImage;
    private Texture musicImage;
    private Texture musicMute;
    private Texture effectMute;

    private ImageButton musicButton;
    private ImageButton effectButton;

    private Stage stage;
    private Skin skin;

    private Sound mainSound;
    private long mainID;
    private Sound buttonClickSound;

    public static boolean effectEnabled = true; 
    public static boolean musicEnabled = true; 

    public SettingsScreen(Main game) {
        this.game = game;
    }

@Override
public void show() {
    batch = new SpriteBatch();
    backgroundImage = new Texture("startBackground.png");
    mainSound = Gdx.audio.newSound(Gdx.files.internal("audio/main.mp3"));
    buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("audio/buttonClick.wav"));

    stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);

    skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

    Table table = new Table();
    table.setFillParent(true);
    table.center(); 

    TextButton startButton = new TextButton("Back", skin);
    startButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            if (effectEnabled){
                buttonClickSound.play(1f);
            }
            mainSound.stop();
            game.setScreen(new StartScreen(game));
        }
    });

    table.add().height(600); 
    table.row();
    table.add(startButton).width(200).height(50).pad(10);
    stage.addActor(table);

    // Effects Button
    effectMute = new Texture("effectsMute.png");
    effectImage = new Texture("effectsButton.png");

    TextureRegionDrawable effectOnDrawable = new TextureRegionDrawable(new TextureRegion(effectImage));
    TextureRegionDrawable effectOffDrawable = new TextureRegionDrawable(new TextureRegion(effectMute));

    effectButton = new ImageButton(effectEnabled ? effectOnDrawable : effectOffDrawable);
    effectButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            effectEnabled = !effectEnabled;
            if (effectEnabled) {
                buttonClickSound.play(1f);
                effectButton.getStyle().imageUp = effectOnDrawable; 
            } else {
                effectButton.getStyle().imageUp = effectOffDrawable; 
            }
        }
    });

    // Music Button
    musicMute = new Texture("musicMute.png");
    musicImage = new Texture("musicButton.png");

    TextureRegionDrawable musicOnDrawable = new TextureRegionDrawable(new TextureRegion(musicImage));
    TextureRegionDrawable musicOffDrawable = new TextureRegionDrawable(new TextureRegion(musicMute));

    musicButton = new ImageButton(musicEnabled ? musicOnDrawable : musicOffDrawable);
    musicButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            musicEnabled = !musicEnabled;
            if (musicEnabled) {
                mainID = mainSound.play(1.0f);
                mainSound.setLooping(mainID, true);
                musicButton.getStyle().imageUp = musicOnDrawable; 
            } else {
                if (mainSound != null) {
                    mainSound.stop(mainID);
                }
                musicButton.getStyle().imageUp = musicOffDrawable; 
            }
            if (effectEnabled){
                buttonClickSound.play(1f);
            }
        }
    });

    Table settingsTable = new Table();
    settingsTable.setFillParent(true);
    settingsTable.center();
    settingsTable.add(musicButton).size(120);
    settingsTable.add(effectButton).size(120).pad(10);
    stage.addActor(settingsTable);
}

    @Override
    public void render(float delta) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        batch.begin();
        batch.draw(backgroundImage, 0, 0, screenWidth, screenHeight);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundImage.dispose();
        musicImage.dispose();
        musicMute.dispose();
        effectImage.dispose();
        effectMute.dispose();
        buttonClickSound.dispose();
        stage.dispose();
        skin.dispose();
        mainSound.dispose();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}

