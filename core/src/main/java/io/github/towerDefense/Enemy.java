/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-05-31
 * This file is part of Rise of Benum Tower Defense.
 * Defines the properties and behavior of an enemy unit.
 */
package io.github.towerDefense;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2; 

import io.github.towerDefense.map.JunglePath; 

public class Enemy {
    public float x, y;
    public static final float SIZE = 50; // size of the enemy
    private final float speed;
    private int health;
    private final JunglePath path; // the path the enemy follows
    private int currentWaypointIndex = 0; // current waypoint the enemy is moving towards
    private final Texture enemyTexture;

    public Enemy(float x, float y, float speed, int health, Color color, JunglePath path, SpriteBatch batch, Texture enemyTexture) {
        this.x = x - SIZE / 2f; 
        this.y = y - SIZE / 2f;
        this.speed = speed;
        this.health = health;
        this.path = path;
        this.currentWaypointIndex = 0; // start at the first waypoint
        this.enemyTexture = enemyTexture;
    }


    public void move(float delta) {
        if (path == null || currentWaypointIndex >= path.getNumWaypoints()) {
            return; // reached end of path
        }

        Vector2 targetWaypoint = path.getWaypoint(currentWaypointIndex);
        Vector2 currentPosition = new Vector2(x + SIZE / 2f, y + SIZE / 2f); 
        float distanceToWaypoint = currentPosition.dst(targetWaypoint);

        if (distanceToWaypoint < speed * delta) {
            
            this.x = targetWaypoint.x - SIZE / 2f;
            this.y = targetWaypoint.y - SIZE / 2f;
            currentWaypointIndex++;
            if (currentWaypointIndex >= path.getNumWaypoints()) {
                // reached the end of the path
                //return;
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
        return health > 0 && (path == null || currentWaypointIndex < path.getNumWaypoints());
    }


    public boolean hasReachedEnd() {
        return path != null && currentWaypointIndex >= path.getNumWaypoints();
    }
    public void dispose() {
    }
}