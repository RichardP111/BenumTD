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
    public static final float SIZE = 60; //size of the enemy
    protected float speed;
    protected int health;
    private final JunglePath path;
    private int currentWaypointIndex = 0;
    private final Texture enemyTexture;
    private final Sound deathSound;

    /**
     * Constructor for the Enemy class.
     * @param x The x-coordinate of the enemy.
     * @param y The y-coordinate of the enemy.
     * @param speed The speed at which the enemy moves.
     * @param health The health of the enemy.
     * @param path The path that the enemy follows.
     * @param enemyTexture The texture for rendering the enemy.
     * @param deathSound The sound played when the enemy is defeated.
     */
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

    /**
     * Moves the enemy towards the next waypoint in the path.
     * Precondition: The path must not be null and the current waypoint index must be within bounds.
     * Postcondition: The enemy's position is updated to move towards the next waypoint.
     * @param delta Time since the last update in seconds.
     */

    public void move(float delta) {
        if (path == null || currentWaypointIndex >= path.getNumWaypoints()) {
            return;
        }

        Vector2 targetWaypoint = path.getWaypoint(currentWaypointIndex);
        Vector2 currentPosition = new Vector2(x + SIZE / 2f, y + SIZE / 2f);
        float distanceToWaypoint = currentPosition.dst(targetWaypoint);

        if (distanceToWaypoint < speed * delta) {
            this.x = targetWaypoint.x - SIZE / 2f;
            this.y = targetWaypoint.y - SIZE / 2f;
            currentWaypointIndex++;

        } else {
            Vector2 direction = new Vector2(targetWaypoint).sub(currentPosition).nor();
            this.x += direction.x * speed * delta;
            this.y += direction.y * speed * delta;
        }
    }

    /**
     * Renders the enemy on the screen using the provided SpriteBatch.
     * Precondition: The enemyTexture must not be null.
     * Postcondition: The enemy is drawn at its current position with the specified size.
     * 
     * @param batch The SpriteBatch used for rendering the enemy.
     */
    public void render(SpriteBatch batch) {
        if (enemyTexture != null) {
            batch.draw(enemyTexture, x, y, SIZE, SIZE);
        }
    }

    /**
     * Applies damage to the enemy.
     * @param damage The amount of damage to apply.
     */

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            System.out.println("Enemy defeated");
        }
    }

    /**
     * Returns the current health of the enemy.
     * @return The health of the enemy.
     */

    public int getHealth() {
        return health;
    }

    /**
     * Checks if the enemy is still alive.
     * Precondition: None
     * Postcondition: Returns true if the enemy's health is greater than 0, false otherwise.
     * 
     * @return true if the enemy's health is greater than 0, false otherwise.
     */
    public boolean isAlive() {
        return health > 0; 
    }

    
    /**
     * Checks if the enemy has reached the end of the path.
     * Precondition: None
     * Postcondition: Returns true if the enemy has reached or exceeded the last waypoint, false otherwise.
     * 
     * @return true if the enemy has reached the end of the path, false otherwise.
     */
    public boolean hasReachedEnd() {
        return currentWaypointIndex >= path.getNumWaypoints(); 
    }

    /**
     * Plays the death sound when the enemy is defeated.
     * Precondition: None
     * Postcondition: The death sound is played at full volume.
     */
    public void playDeathSound() { 
        if (deathSound != null) {
            deathSound.play(1f); 
        }
    }

    /**
     * Disposes of the resources used by the enemy.
     * Precondition: None
     * Postcondition: The enemy's texture and sound resources are disposed of to free up memory.
     */

    public void dispose() { 
        if (enemyTexture != null) {
            enemyTexture.dispose();
        }
        if (deathSound != null) {
            deathSound.dispose();
        }
    }
}