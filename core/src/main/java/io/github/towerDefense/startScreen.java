/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-05-28
 * This file the start menu screen of Rise of Benum Tower Defense.
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

import io.github.towerDefense.map.jungleMap;


public class startScreen implements Screen { 
    private final Main game;
    private SpriteBatch batch;
    private Texture backgroundImage;
    private Texture logoImage;
    private Texture settingsImage;

    private Stage stage;
    private Skin skin;

    private Sound mainSound;
    private long mainID;

    public startScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        backgroundImage = new Texture("startBackground.png");
        logoImage = new Texture("textBasedLogo.png");
        settingsImage = new Texture("settings.png");

        // Start music
        mainSound = Gdx.audio.newSound(Gdx.files.internal("audio/main.mp3"));
        if (settingsScreen.musicEnabled) {
            mainID = mainSound.play(1.0f);
            mainSound.setLooping(mainID, true);
        }

        // Stage and input
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Skin
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        // Layout table for buttons
        Table table = new Table();
        table.setFillParent(true);
        table.center(); // Center everything

        TextButton startButton = new TextButton("Play", skin);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainSound.stop();
                game.setScreen(new jungleMap(game));
            }
        });

        TextButton tutorialButton = new TextButton("Tutorial", skin);
        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainSound.stop();
                game.setScreen(new Tutorial(game));
            }
        });

        table.add().height(300); // Spacer
        table.row();
        table.add(startButton).width(200).height(50).pad(10);
        table.row();
        table.add(tutorialButton).width(200).height(50).pad(10);

        stage.addActor(table);

        // Settings button (top right)
        ImageButton settingsButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(settingsImage)));
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new settingsScreen(game));
            }
        });

        Table settingsTable = new Table();
        settingsTable.setFillParent(true);
        settingsTable.top().right().pad(20);
        settingsTable.add(settingsButton).size(70); // Adjust size as needed
        stage.addActor(settingsTable);
    }

    
    /** 
     * @param delta
     */
    @Override
    public void render(float delta) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        float logoX = (screenWidth - logoImage.getWidth()) / 2f;
        float logoY = screenHeight - logoImage.getHeight() - 30;

        batch.begin();
        batch.draw(backgroundImage, 0, 0, screenWidth, screenHeight);
        batch.draw(logoImage, logoX, logoY);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundImage.dispose();
        logoImage.dispose();
        settingsImage.dispose();
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