/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-06-02
 * This file is part of Rise of Benum Tower Defense.
 * Jungle map class for the game.
 * This class will handle the jungle map layout and logic,
 * including enemy waves, tower placement, and tower-enemy interactions.
 */
package io.github.towerDefense.map;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20; // Added GL20 for glClear

import io.github.towerDefense.Enemy;
import io.github.towerDefense.Main;
import io.github.towerDefense.Towers;
import io.github.towerDefense.TowerPlacementManager;

public class jungleMap implements Screen {
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

    // Tower-related fields
    private ArrayList<Towers> towers;
    private OrthographicCamera camera;
    private TowerPlacementManager placementManager;

    public jungleMap(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        backgroundImage = new Texture("C:\\Users\\sahil\\Documents\\Highschool\\Grade 11\\Computer Science\\Benum'sTD part2\\BenumTD\\assets\\maps\\jungleMap.jpg"); // Ensure this path is correct
        enemies = new ArrayList<>();
        towers = new ArrayList<>();

        // Initialize camera for tower placement
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        placementManager = new TowerPlacementManager(camera, towers);

        initializePath();

        waveTimer = TIME_BETWEEN_WAVES; // Start with a full timer to immediately start first wave or wait
        waveNumber = 1;
        enemiesPerWave = 3; // Starting number of enemies per wave
        enemiesSpawnedInWave = 0;
        enemySpawnIntervalInWave = 1.0f; // Time between individual enemy spawns
        individualEnemySpawnTimer = 0f;

        font = new BitmapFont(); // Initialize BitmapFont
        glyphLayout = new GlyphLayout(); // Initialize GlyphLayout
    }

    @Override
    public void render(float delta) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        // Clear the screen
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update camera
        camera.update();

        // 1. Draw Background and UI using SpriteBatch
        batch.setProjectionMatrix(camera.combined); // Ensure batch uses the camera's projection matrix
        batch.begin();
        batch.draw(backgroundImage, 0, 0, screenWidth, screenHeight);

        // Draw Wave Counter
        String waveText = "WAVE " + waveNumber + "/" + MAX_WAVES;
        glyphLayout.setText(font, waveText);

        float textX = (screenWidth - glyphLayout.width) / 2;
        float textY = screenHeight - 50; // Adjust Y position as needed

        // Draw black outline for text
        font.setColor(Color.BLACK);
        font.draw(batch, waveText, textX - 1, textY - 1);
        font.draw(batch, waveText, textX + 1, textY - 1);
        font.draw(batch, waveText, textX - 1, textY + 1);
        font.draw(batch, waveText, textX + 1, textY + 1);
        // Draw white text on top
        font.setColor(Color.WHITE);
        font.draw(batch, waveText, textX, textY);
        batch.end(); // End SpriteBatch here

        // Handle tower placement (logic only)
        Towers newTower = placementManager.getNewTowerOnLeftClick(delta);
        if (newTower != null) {
            towers.add(newTower);
            System.out.println("Tower placed at: " + newTower.x + ", " + newTower.y);
        }

        // 2. Draw Game Objects (Towers and Enemies) using ShapeRenderer
        shapeRenderer.setProjectionMatrix(camera.combined); // Apply camera to shapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); // Begin ShapeRenderer for filled shapes

        // Update and render towers
        for (Towers tower : towers) {
            tower.update(delta);
            tower.render(shapeRenderer);
        }

        // Update and render enemies and handle tower attacks
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.move(delta);
            enemy.render(shapeRenderer);

            // Check for tower attacks on this enemy
            for (Towers tower : towers) {
                Vector2 towerCenter = tower.getCenter();
                Vector2 enemyCenter = new Vector2(enemy.x + Enemy.SIZE / 2f, enemy.y + Enemy.SIZE / 2f);

                float distance = towerCenter.dst(enemyCenter);

                if (distance <= tower.getAttackRange()) {
                    if (tower.canAttack()) {
                        enemy.takeDamage((int) tower.getAttackDamage());
                        System.out.println("Tower attacked enemy! Enemy health: " + enemy.getHealth());
                    }
                }
            }

            if (!enemy.isAlive() || enemy.hasReachedEnd()) {
                if (enemy.hasReachedEnd()) {
                    System.out.println("Enemy reached the end!");
                    // Reduce player's health/lives here
                } else if (!enemy.isAlive()) {
                    System.out.println("Enemy defeated!");
                }
                enemyIterator.remove();
            }
        }
        shapeRenderer.end(); // End ShapeRenderer here

        // Wave management (logic only)
        waveTimer += delta;
        if (waveTimer >= TIME_BETWEEN_WAVES) {
            if (enemiesSpawnedInWave < enemiesPerWave) {
                individualEnemySpawnTimer += delta;
                if (individualEnemySpawnTimer >= enemySpawnIntervalInWave) {
                    Vector2 startPoint = enemyPath.getWaypoint(0);
                    if (startPoint != null) {
                        enemies.add(new Enemy(startPoint.x, startPoint.y, 70f, 3, Color.RED, enemyPath));
                        enemiesSpawnedInWave++;
                        individualEnemySpawnTimer = 0f;
                    }
                }
            } else if (enemies.isEmpty()) {
                if (waveNumber < MAX_WAVES) {
                    waveNumber++;
                    enemiesPerWave += 2;
                    enemySpawnIntervalInWave = Math.max(0.2f, enemySpawnIntervalInWave - 0.1f);
                    enemiesSpawnedInWave = 0;
                    waveTimer = 0f;
                    System.out.println("Starting Wave " + waveNumber + "!");
                } else {
                    System.out.println("All waves completed!");
                    // Handle game won state
                }
            }
        }
    }

    private void initializePath() {
        enemyPath = new JunglePath();
       
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height); // Update camera on resize
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

    @Override
    public void pause() {
        // Handle pause logic if needed
    }

    @Override
    public void resume() {
        // Handle resume logic if needed
    }

    @Override
    public void hide() {
        // Handle hide logic if needed
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (backgroundImage != null) backgroundImage.dispose();
        if (font != null) font.dispose(); // Dispose of font
    }
}