/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-05-30
 * This file is part of Rise of Benum Tower Defense.
 * Jungle map class for the game.
 * This class will handle the jungle map layout and logic.
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

import io.github.towerDefense.Enemy;
import io.github.towerDefense.Main;
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

    public jungleMap(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        backgroundImage = new Texture("maps/jungleMap.jpg");
        enemies = new ArrayList<>();
        
        initializePath();
        
        waveTimer = TIME_BETWEEN_WAVES;
        waveNumber = 0; // Initialize wave number to 0, it will become 1 when the first wave starts
        enemiesPerWave = 5;
        enemySpawnIntervalInWave = 1f;
        individualEnemySpawnTimer = 0f;

        // Initialize font for UI text
        font = new BitmapFont();
        font.setColor(Color.WHITE); // Set default color to white
        font.getData().setScale(2f); // Adjust font size as needed
        glyphLayout = new GlyphLayout(); // Initialize GlyphLayout
    }

    private void initializePath() {
        enemyPath = new JunglePath();

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        enemyPath.addWaypoint(w * 1.00f, h * 0.87f);
        enemyPath.addWaypoint(w * 0.82f, h * 0.87f); 
        enemyPath.addWaypoint(w * 0.82f, h * 0.66f);
        enemyPath.addWaypoint(w * 0.84f, h * 0.65f);
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

    @Override
    public void render(float delta) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        // Draw background
        batch.begin();
        batch.draw(backgroundImage, 0, 0, screenWidth, screenHeight);
        
        // Draw Wave Counter
        String waveText = "WAVE " + waveNumber + "/" + MAX_WAVES;
        glyphLayout.setText(font, waveText); // Update glyphLayout with current text

        float textX = (screenWidth - glyphLayout.width) / 2; // Center horizontally
        float textY = screenHeight - 50; // Position 50 pixels from the top edge for the baseline

        // Draw black outline
        font.setColor(Color.BLACK);
        font.draw(batch, waveText, textX - 1, textY - 1);
        font.draw(batch, waveText, textX + 1, textY - 1);
        font.draw(batch, waveText, textX - 1, textY + 1);
        font.draw(batch, waveText, textX + 1, textY + 1);
        // Draw white text on top
        font.setColor(Color.WHITE);
        font.draw(batch, waveText, textX, textY);
        
        batch.end();

        // Update and render enemies using ShapeRenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.move(delta);
            enemy.render(shapeRenderer);

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
                    Vector2 startPoint = enemyPath.getWaypoint(0);
                    if (startPoint != null) {
                        enemies.add(new Enemy(startPoint.x, startPoint.y, 70f, 3, Color.RED, enemyPath));
                        enemiesSpawnedInWave++;
                        individualEnemySpawnTimer = 0f;
                    }
                }
            } else if (enemies.isEmpty()) {
                // All enemies from the current wave are defeated or reached the end
                // Start a new wave if MAX_WAVES has not been reached
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

    /**
     * Returns the initialized enemy path.
     * This getter is crucial for passing the path to other classes like TowerPlacementManager.
     * @return The JunglePath object for enemies.
     */
    public JunglePath getEnemyPath() {
        return enemyPath;
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override public void dispose() {
        batch.dispose();
        backgroundImage.dispose();
        shapeRenderer.dispose();
        font.dispose(); // Dispose the font
    }
}