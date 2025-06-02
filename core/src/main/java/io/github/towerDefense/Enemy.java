/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-05-31
 * This file is part of Rise of Benum Tower Defense.
 * Defines the properties and behavior of an enemy unit.
 */
package io.github.towerDefense;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer; // Import Color for drawing enemies
import com.badlogic.gdx.math.Vector2; // Import Vector2 for pathfinding

import io.github.towerDefense.map.JunglePath; // Import Path

public class Enemy {
    public float x, y;
    public static final float SIZE = 30; // Size of the enemy (e.g., a square)
    private float speed;
    private int health;
    private Color color; // Color of the enemy
    private JunglePath path; // The path the enemy follows
    private int currentWaypointIndex; // Current waypoint the enemy is moving towards

    /**
     * Constructor for the Enemy class.
     * @param x The initial x-coordinate of the enemy.
     * @param y The initial y-coordinate of the enemy.
     * @param speed The movement speed of the enemy.
     * @param health The health points of the enemy.
     * @param color The color of the enemy.
     * @param path The path for the enemy to follow.
     */
    public Enemy(float x, float y, float speed, int health, Color color, JunglePath path) {
        this.x = x - SIZE / 2f; // Center the enemy
        this.y = y - SIZE / 2f;
        this.speed = speed;
        this.health = health;
        this.color = color;
        this.path = path;
        this.currentWaypointIndex = 0; // Start at the first waypoint
    }

    /**
     * Updates the enemy's position based on its speed and delta time, following the defined path.
     * @param delta The time in seconds since the last render frame.
     */
    public void move(float delta) {
        if (path == null || currentWaypointIndex >= path.getNumWaypoints()) {
            return; // No path or reached end of path
        }

        Vector2 targetWaypoint = path.getWaypoint(currentWaypointIndex);
        Vector2 currentPosition = new Vector2(x + SIZE / 2f, y + SIZE / 2f); // Center of the enemy

        float distanceToWaypoint = currentPosition.dst(targetWaypoint);

        if (distanceToWaypoint < speed * delta) {
            // If very close to the waypoint, snap to it and move to the next
            this.x = targetWaypoint.x - SIZE / 2f;
            this.y = targetWaypoint.y - SIZE / 2f;
            currentWaypointIndex++;
            if (currentWaypointIndex >= path.getNumWaypoints()) {
                // Reached the end of the path
                // Handle enemy reaching the end (e.g., reduce player life)
                return;
            }
        } else {
            // Move towards the waypoint
            Vector2 direction = new Vector2(targetWaypoint).sub(currentPosition).nor();
            this.x += direction.x * speed * delta;
            this.y += direction.y * speed * delta;
        }
    }

    /**
     * Renders the enemy on the screen.
     * @param shapeRenderer The ShapeRenderer used for drawing.
     */
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color); // Set the enemy's color
        shapeRenderer.rect(x, y, SIZE, SIZE); // Draw the enemy as a square
    }

    /**
     * Reduces the enemy's health.
     * @param damage The amount of damage to apply.
     */
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            // Handle enemy death (e.g., remove from list, play animation, give rewards)
            System.out.println("Enemy defeated!");
        }
    }

    /**
     * Gets the current health of the enemy.
     * @return The enemy's health.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Checks if the enemy is alive.
     * @return true if health is greater than 0, false otherwise.
     */
    public boolean isAlive() {
        return health > 0 && (path == null || currentWaypointIndex < path.getNumWaypoints()); // Also consider alive if still on path
    }

    /**
     * Checks if the enemy has reached the end of its path.
     * @return true if the enemy has reached the last waypoint, false otherwise.
     */
    public boolean hasReachedEnd() {
        return path != null && currentWaypointIndex >= path.getNumWaypoints();
    }
}