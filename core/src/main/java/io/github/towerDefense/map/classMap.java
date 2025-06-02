/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-05-30
 * This file is part of Rise of Benum Tower Defense.
 * City map class for the game.
 * This class will handle the city map layout and logic.
 */

package io.github.towerDefense.map;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color; // Import ShapeRenderer
import com.badlogic.gdx.graphics.Texture; // Import Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch; // Import Vector2
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.towerDefense.Enemy;
import io.github.towerDefense.Main; // Import Enemy

public class classMap implements Screen {
    private final Main game;

    private Texture backgroundImage;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer; // Add ShapeRenderer
    private ArrayList<Enemy> enemies; // List to hold enemies
    private JunglePath enemyPath; // The path for enemies to follow

    private float waveTimer;
    private final float TIME_BETWEEN_WAVES = 5f; // Time between waves
    private int waveNumber;
    private int enemiesPerWave;
    private int enemiesSpawnedInWave;
    private float enemySpawnIntervalInWave; // Time between individual enemy spawns within a wave
    private float individualEnemySpawnTimer;

    public classMap(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer(); // Initialize ShapeRenderer
        backgroundImage = new Texture("maps/jungleMap.png");
        enemies = new ArrayList<>();
        
        initializePath(); // Define the enemy path
        
        waveTimer = TIME_BETWEEN_WAVES; // Start timer for the first wave
        waveNumber = 0;
        enemiesPerWave = 5; // Initial number of enemies per wave
        enemySpawnIntervalInWave = 1f; // Initial spawn interval
        individualEnemySpawnTimer = 0f;
    }

// In your cityMap.java file, within the initializePath() method:

private void initializePath() {
    enemyPath = new JunglePath();

    float w = Gdx.graphics.getWidth();
    float h = Gdx.graphics.getHeight();

    enemyPath.addWaypoint(w * 1.00f, h * 0.87f); // Start off-screen top right
    enemyPath.addWaypoint(w * 0.82f, h * 0.87f); // Move down
    enemyPath.addWaypoint(w * 0.82f, h * 0.66f); // Start curve
    enemyPath.addWaypoint(w * 0.84f, h * 0.65f); // Wrap around right pond
    enemyPath.addWaypoint(w * 0.86f, h * 0.64f);
    enemyPath.addWaypoint(w * 0.88f, h * 0.61f);
    enemyPath.addWaypoint(w * 0.91f, h * 0.59f);
    enemyPath.addWaypoint(w * 0.92f, h * 0.57f);
    enemyPath.addWaypoint(w * 0.95f, h * 0.50f);
    enemyPath.addWaypoint(w * 0.95f, h * 0.40f);
    enemyPath.addWaypoint(w * 0.95f, h * 0.38f);
    enemyPath.addWaypoint(w * 0.94f, h * 0.35f);
    enemyPath.addWaypoint(w * 0.92f, h * 0.30f);
    enemyPath.addWaypoint(w * 0.87f, h * 0.25f);
    enemyPath.addWaypoint(w * 0.84f, h * 0.23f);
    enemyPath.addWaypoint(w * 0.78f, h * 0.20f);
    enemyPath.addWaypoint(w * 0.70f, h * 0.20f);
    enemyPath.addWaypoint(w * 0.65f, h * 0.22f);
    enemyPath.addWaypoint(w * 0.62f, h * 0.27f);
    enemyPath.addWaypoint(w * 0.58f, h * 0.34f);
    enemyPath.addWaypoint(w * 0.58f, h * 0.53f);
    enemyPath.addWaypoint(w * 0.58f, h * 0.53f);
    enemyPath.addWaypoint(w * 0.61f, h * 0.58f);
    enemyPath.addWaypoint(w * 0.66f, h * 0.63f);
    enemyPath.addWaypoint(w * 0.70f, h * 0.67f);
    enemyPath.addWaypoint(w * 0.70f, h * 0.87f);
    enemyPath.addWaypoint(w * 0.65f, h * 0.87f);
    enemyPath.addWaypoint(w * 0.57f, h * 0.70f);
    enemyPath.addWaypoint(w * 0.50f, h * 0.50f);
    enemyPath.addWaypoint(w * 0.42f, h * 0.30f);
    enemyPath.addWaypoint(w * 0.35f, h * 0.20f);
    enemyPath.addWaypoint(w * 0.31f, h * 0.20f);
    enemyPath.addWaypoint(w * 0.31f, h * 0.40f);
    enemyPath.addWaypoint(w * 0.38f, h * 0.45f);
    enemyPath.addWaypoint(w * 0.41f, h * 0.49f);
    enemyPath.addWaypoint(w * 0.43f, h * 0.52f);
    enemyPath.addWaypoint(w * 0.45f, h * 0.65f);
    enemyPath.addWaypoint(w * 0.42f, h * 0.75f);
    enemyPath.addWaypoint(w * 0.39f, h * 0.83f);
    enemyPath.addWaypoint(w * 0.36f, h * 0.85f);
    enemyPath.addWaypoint(w * 0.30f, h * 0.87f);
    enemyPath.addWaypoint(w * 0.22f, h * 0.87f);
    enemyPath.addWaypoint(w * 0.18f, h * 0.84f);
    enemyPath.addWaypoint(w * 0.12f, h * 0.79f);
    enemyPath.addWaypoint(w * 0.10f, h * 0.75f);
    enemyPath.addWaypoint(w * 0.07f, h * 0.70f);
    enemyPath.addWaypoint(w * 0.07f, h * 0.57f);
    enemyPath.addWaypoint(w * 0.10f, h * 0.50f);
    enemyPath.addWaypoint(w * 0.15f, h * 0.45f);
    enemyPath.addWaypoint(w * 0.20f, h * 0.40f);
    enemyPath.addWaypoint(w * 0.20f, h * 0.20f);
    enemyPath.addWaypoint(w * 0.00f, h * 0.20f);
}


    
    /** 
     * @param delta
     */
    @Override
    public void render(float delta) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        batch.begin();
        batch.draw(backgroundImage, 0, 0, screenWidth, screenHeight);
        batch.end();

        // Update and render enemies
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.move(delta);
            enemy.render(shapeRenderer);

            // Remove enemy if it's no longer alive or has reached the end of the path
            if (!enemy.isAlive() || enemy.hasReachedEnd()) {
                if(enemy.hasReachedEnd()){
                    System.out.println("Enemy reached the end!");
                    // Here you would typically reduce player's health/lives
                }
                enemyIterator.remove();
            }
        }
        shapeRenderer.end();

        // Wave management
        waveTimer += delta;
        if (waveTimer >= TIME_BETWEEN_WAVES) {
            if (enemiesSpawnedInWave < enemiesPerWave) {
                individualEnemySpawnTimer += delta;
                if (individualEnemySpawnTimer >= enemySpawnIntervalInWave) {
                    // Spawn a new enemy at the start of the path
                    // Ensure the enemy is initialized at the first waypoint's coordinates
                    Vector2 startPoint = enemyPath.getWaypoint(0);
                    if (startPoint != null) {
                        enemies.add(new Enemy(startPoint.x, startPoint.y, 70f, 3, Color.RED, enemyPath));
                        enemiesSpawnedInWave++;
                        individualEnemySpawnTimer = 0f;
                    }
                }
            } else if (enemies.isEmpty()) {
                // All enemies from the current wave are defeated or reached the end
                // Start a new wave
                waveNumber++;
                enemiesPerWave += 2; // Increase enemies for the next wave
                enemySpawnIntervalInWave = Math.max(0.2f, enemySpawnIntervalInWave - 0.1f); // Make spawns faster
                enemiesSpawnedInWave = 0;
                waveTimer = 0f; // Reset wave timer
                System.out.println("Starting Wave " + waveNumber + "!");
            }
        }

        // Optional: Draw the path for debugging
        drawPath();
    }

    // Helper method to draw the enemy path (for debugging)
    private void drawPath() {
        if (enemyPath == null || enemyPath.getNumWaypoints() < 2) {
            return;
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLUE);
        for (int i = 0; i < enemyPath.getNumWaypoints() - 1; i++) {
            Vector2 p1 = enemyPath.getWaypoint(i);
            Vector2 p2 = enemyPath.getWaypoint(i + 1);
            shapeRenderer.line(p1.x, p1.y, p2.x, p2.y);
        }
        shapeRenderer.end();
    }


    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override public void dispose() {
        batch.dispose();
        backgroundImage.dispose();
        shapeRenderer.dispose(); // Dispose ShapeRenderer
    }
}