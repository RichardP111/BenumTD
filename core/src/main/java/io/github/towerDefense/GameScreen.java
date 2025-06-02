/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-05-27
 * This file is part of Rise of Benum Tower Defense.
 * main screen for the game.
 */

package io.github.towerDefense;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import io.github.towerDefense.map.JunglePath;

public class GameScreen implements Screen {
    private final Main game;
    private ShapeRenderer batch;
    private ArrayList<Towers> towers;
    private ArrayList<Enemy> enemies;
    private OrthographicCamera camera;
    private TowerPlacementManager placementManager;

    private float enemySpawnTimer = 0f;
    private final float ENEMY_SPAWN_INTERVAL = 3f;

    public GameScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        System.out.println("Tower Screen/Game Screen Loaded");
        batch = new ShapeRenderer();
        towers = new ArrayList<>();
        enemies = new ArrayList<>();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600); // Set camera size
        placementManager = new TowerPlacementManager(camera,towers); 
    }

    
    /** 
     * @param delta
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        Towers newTower = placementManager.getNewTowerOnLeftClick();
        if (newTower != null) {
            towers.add(newTower);
        }

        enemySpawnTimer += delta;
        if (enemySpawnTimer >= ENEMY_SPAWN_INTERVAL) {
            float SpawnY = (float) (Math.random()*(camera.viewportHeight - Enemy.SIZE));
            enemies.add(new Enemy(camera.viewportWidth, SpawnY, 100f, 3, Color.RED, new JunglePath()));
            enemySpawnTimer = 0f;
        }
        for (Towers tower : towers) {
            tower.update(delta); // Update each tower's state
        }
        batch.begin(ShapeRenderer.ShapeType.Filled);

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while(enemyIterator.hasNext()){
            Enemy enemy = enemyIterator.next();
            enemy.move(delta);
            
            // Check for tower attacks on this enemy
            for (Towers tower : towers) {
                Vector2 towerCenter = tower.getCenter();
                Vector2 enemyCenter = new Vector2(enemy.x + Enemy.SIZE / 2f, enemy.y + Enemy.SIZE / 2f);

                // Calculate distance between tower and enemy centers
                float distance = towerCenter.dst(enemyCenter);

                if (distance <= tower.getAttackRange()) {
                    // Enemy is within attack range
                    if (tower.canAttack()) {
                        enemy.takeDamage((int) tower.getAttackDamage());
                        System.out.println("Tower attacked enemy! Enemy health: " + enemy.getHealth());
                    }
                }
            }

            if(enemy.x + Enemy.SIZE < 0|| !enemy.isAlive()) {
             if (!enemy.isAlive()) {
                    System.out.println("Enemy defeated!");
                } else{
                    System.out.println("Enemy escaped!");
                }
                enemyIterator.remove(); // Remove the enemy if it goes off-screen or is defeated
            } else {
                enemy.render(batch); // Render the enemy if it's still alive
            }
        }
        for (Towers tower : towers) {
            tower.render(batch);
        }
        batch.end();
        // Render UI or game start menu here
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}

