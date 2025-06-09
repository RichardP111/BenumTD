/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-06-04
 * This file is part of Rise of Benum Tower Defense.
 * Classroom map class for the game.
 * COPY
 */
package io.github.towerDefense.map;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20; 
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;

import io.github.towerDefense.Enemy;
import io.github.towerDefense.Main;
import io.github.towerDefense.TowerPlacementManager;
import io.github.towerDefense.Towers;

public class classMap implements Screen {
    private final Main game;

    private Texture backgroundImage;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private ArrayList<Enemy> enemies;
    private JunglePath enemyPath;

    private float waveTimer;
    private final float TIME_BETWEEN_WAVES = 5f;
    private int waveNumber;
    private final int MAX_WAVES = 40;
    private int enemiesPerWave;
    private int enemiesSpawnedInWave;
    private float enemySpawnIntervalInWave;
    private float individualEnemySpawnTimer;

    private BitmapFont font;
    private GlyphLayout glyphLayout;

    private ArrayList<Towers> towers;
    private OrthographicCamera camera; 
    private TowerPlacementManager placementManager;

    private int benumCoin;

    private Stage stage; 
    private DragAndDrop dragAndDrop;
    private Texture towerIconTexture1, towerIconTexture2, towerIconTexture3;
    private Image towerDraggableImage1, towerDraggableImage2, towerDraggableImage3;
    private Actor mapDropTargetActor; 
    private DragAndDrop.Payload currentDragPayload = null; 

    private Texture enemyTexture;

    //tower properties
    private static final int COST_TOWER_1 = 50;
    private static final Color COLOR_TOWER_1 = Color.BLUE;
    private static final float RANGE_TOWER_1 = 150f;
    private static final float DAMAGE_TOWER_1 = 1f;
    private static final float COOLDOWN_TOWER_1 = 0.3f;

    private static final int COST_TOWER_2 = 75;
    private static final Color COLOR_TOWER_2 = Color.GREEN;
    private static final float RANGE_TOWER_2 = 170f;
    private static final float DAMAGE_TOWER_2 = 1.2f;
    private static final float COOLDOWN_TOWER_2 = 0.26f;

    private static final int COST_TOWER_3 = 100;
    private static final Color COLOR_TOWER_3 = Color.RED;
    private static final float RANGE_TOWER_3 = 200f;
    private static final float DAMAGE_TOWER_3 = 1.5f;
    private static final float COOLDOWN_TOWER_3 = 0.2f;

    private static final float PATH_CLEARANCE_FROM_TOWER_EDGE = 10f; 

    public classMap(Main game) {
        this.game = game;
    }

    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height); 
        stage.getViewport().update(width, height, true); 

        if (mapDropTargetActor != null) {
            mapDropTargetActor.setBounds(0, 0, width, height);
        }

        enemyPath = new JunglePath(); 
        enemyPath.addWaypoint(width * 1.00f, height * 0.87f);
        enemyPath.addWaypoint(width * 0.82f, height * 0.87f);
        enemyPath.addWaypoint(width * 0.82f, height * 0.66f);
        enemyPath.addWaypoint(width * 0.84f, height * 0.65f);
        enemyPath.addWaypoint(width * 0.86f, height * 0.64f);
        enemyPath.addWaypoint(width * 0.88f, height * 0.61f);
        enemyPath.addWaypoint(width * 0.91f, height * 0.59f);
        enemyPath.addWaypoint(width * 0.92f, height * 0.57f);
        enemyPath.addWaypoint(width * 0.95f, height * 0.50f);
        enemyPath.addWaypoint(width * 0.95f, height * 0.40f);
        enemyPath.addWaypoint(width * 0.95f, height * 0.38f);
        enemyPath.addWaypoint(width * 0.94f, height * 0.35f);
        enemyPath.addWaypoint(width * 0.92f, height * 0.30f);
        enemyPath.addWaypoint(width * 0.87f, height * 0.25f);
        enemyPath.addWaypoint(width * 0.84f, height * 0.23f);
        enemyPath.addWaypoint(width * 0.78f, height * 0.20f);
        enemyPath.addWaypoint(width * 0.70f, height * 0.20f);
        enemyPath.addWaypoint(width * 0.65f, height * 0.22f);
        enemyPath.addWaypoint(width * 0.62f, height * 0.27f);
        enemyPath.addWaypoint(width * 0.58f, height * 0.34f);
        enemyPath.addWaypoint(width * 0.58f, height * 0.53f);
        enemyPath.addWaypoint(width * 0.61f, height * 0.58f);
        enemyPath.addWaypoint(width * 0.66f, height * 0.63f);
        enemyPath.addWaypoint(width * 0.70f, height * 0.67f);
        enemyPath.addWaypoint(width * 0.70f, height * 0.87f);
        enemyPath.addWaypoint(width * 0.65f, height * 0.87f);
        enemyPath.addWaypoint(width * 0.57f, height * 0.70f);
        enemyPath.addWaypoint(width * 0.50f, height * 0.50f);
        enemyPath.addWaypoint(width * 0.42f, height * 0.30f);
        enemyPath.addWaypoint(width * 0.35f, height * 0.20f);
        enemyPath.addWaypoint(width * 0.31f, height * 0.20f);
        enemyPath.addWaypoint(width * 0.31f, height * 0.40f);
        enemyPath.addWaypoint(width * 0.38f, height * 0.45f);
        enemyPath.addWaypoint(width * 0.41f, height * 0.49f);
        enemyPath.addWaypoint(width * 0.43f, height * 0.52f);
        enemyPath.addWaypoint(width * 0.45f, height * 0.65f);
        enemyPath.addWaypoint(width * 0.42f, height * 0.75f);
        enemyPath.addWaypoint(width * 0.39f, height * 0.83f);
        enemyPath.addWaypoint(width * 0.36f, height * 0.85f);
        enemyPath.addWaypoint(width * 0.30f, height * 0.87f);
        enemyPath.addWaypoint(width * 0.22f, height * 0.87f);
        enemyPath.addWaypoint(width * 0.18f, height * 0.84f);
        enemyPath.addWaypoint(width * 0.12f, height * 0.79f);
        enemyPath.addWaypoint(width * 0.10f, height * 0.75f);
        enemyPath.addWaypoint(width * 0.07f, height * 0.70f);
        enemyPath.addWaypoint(width * 0.07f, height * 0.57f);
        enemyPath.addWaypoint(width * 0.10f, height * 0.50f);
        enemyPath.addWaypoint(width * 0.15f, height * 0.45f);
        enemyPath.addWaypoint(width * 0.20f, height * 0.40f);
        enemyPath.addWaypoint(width * 0.20f, height * 0.20f);
        enemyPath.addWaypoint(width * 0.00f, height * 0.20f); 
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (backgroundImage != null) backgroundImage.dispose();
        if(enemyTexture != null) enemyTexture.dispose();
        if (font != null) font.dispose(); 
        if (stage != null) stage.dispose();
        if (towerIconTexture1 != null) towerIconTexture1.dispose();
        if (towerIconTexture2 != null) towerIconTexture2.dispose();
        if (towerIconTexture3 != null) towerIconTexture3.dispose();

    }
}
