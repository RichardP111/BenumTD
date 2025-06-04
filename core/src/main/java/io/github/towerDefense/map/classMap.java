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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch; 
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.towerDefense.Enemy;
import io.github.towerDefense.Main; 

public class classMap implements Screen {
    private final Main game;

    private Texture backgroundImage;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer; 
    private ArrayList<Enemy> enemies; 
    private JunglePath enemyPath; //path for enemies to follow

    private float waveTimer;
    private final float TIME_BETWEEN_WAVES = 5f; 
    private int waveNumber;
    private int enemiesPerWave;
    private int enemiesSpawnedInWave;
    private float enemySpawnIntervalInWave; //time between each spawn 
    private float individualEnemySpawnTimer;

    public classMap(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        backgroundImage = new Texture("maps/jungleMap.png"); //map of jungle
        enemies = new ArrayList<>();
        
        initializePath(); //start the path for enemies

        waveTimer = TIME_BETWEEN_WAVES; 
        waveNumber = 0;
        enemiesPerWave = 5; //how many enemies in each wave
        enemySpawnIntervalInWave = 1f; //time between each enemy spawn in a wave
        individualEnemySpawnTimer = 0f;
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

        batch.begin();
        batch.draw(backgroundImage, 0, 0, screenWidth, screenHeight);
        batch.end();

        //update and render enemies
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.move(delta);
            enemy.render(shapeRenderer);

            //remove enemy if it's no longer alive or has reached the end of the path
            if (!enemy.isAlive() || enemy.hasReachedEnd()) {
                if(enemy.hasReachedEnd()){
                    //reduce player health in future 
                }
                enemyIterator.remove();
            }
        }
        shapeRenderer.end();

        //wave management
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
                waveNumber++;
                enemiesPerWave += 2; //icrease enemies for the next wave
                enemySpawnIntervalInWave = Math.max(0.2f, enemySpawnIntervalInWave - 0.1f); // Make spawns faster
                enemiesSpawnedInWave = 0;
                waveTimer = 0f; //reset timer
            }
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override public void dispose() {
        batch.dispose();
        backgroundImage.dispose();
        shapeRenderer.dispose(); //dispose ShapeRenderer
    }
}