/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-06-11
 * This file is part of Rise of Benum Tower Defense.
 * Defines the properties and behavior of an enemy unit.
 */
package io.github.towerDefense;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import io.github.towerDefense.map.JunglePath;

public class Enemy {
    public float x, y;
    public static final float SIZE = 50; //size of the enemy
    protected float speed;
    protected int health;
    private final JunglePath path;
    private int currentWaypointIndex = 0;
    private final Texture enemyTexture;
    private final Sound deathSound;

    public Enemy(float x, float y, float speed, int health, JunglePath path, Texture enemyTexture, Sound deathSound) {
        this.x = x - SIZE / 2f;
        this.y = y - SIZE / 2f;
        this.speed = speed;
        this.health = health;
        this.path = path;
        this.currentWaypointIndex = 0;
        this.enemyTexture = enemyTexture;
        this.deathSound = deathSound;
    }

    public void move(float delta) {
        if (path == null || currentWaypointIndex >= path.getNumWaypoints()) {
            //end path
            return;
        }

        Vector2 targetWaypoint = path.getWaypoint(currentWaypointIndex);
        Vector2 currentPosition = new Vector2(x + SIZE / 2f, y + SIZE / 2f);
        float distanceToWaypoint = currentPosition.dst(targetWaypoint);

        if (distanceToWaypoint < speed * delta) {
            this.x = targetWaypoint.x - SIZE / 2f;
            this.y = targetWaypoint.y - SIZE / 2f;
            currentWaypointIndex++;
            if (currentWaypointIndex >= path.getNumWaypoints()) {
                //end
            }
        } else {
            Vector2 direction = new Vector2(targetWaypoint).sub(currentPosition).nor();
            this.x += direction.x * speed * delta;
            this.y += direction.y * speed * delta;
        }
    }

    public void render(SpriteBatch batch) {
        if (enemyTexture != null) {
            batch.draw(enemyTexture, x, y, SIZE, SIZE);
        }
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            System.out.println("Enemy defeated");
        }
    }

    public int getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0; 
    }

    public boolean hasReachedEnd() {
        return currentWaypointIndex >= path.getNumWaypoints(); 
    }

    public void playDeathSound() { 
        if (deathSound != null) {
            deathSound.play(1f); 
        }
    }

    public void dispose() { 
        if (enemyTexture != null) {
            enemyTexture.dispose();
        }
        if (deathSound != null) {
            deathSound.dispose();
        }
    }
}