/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-06-02
 * This file is part of Rise of Benum Tower Defense.
 * Defines the properties and behavior of a tower unit.
 */
package io.github.towerDefense;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Towers {
    public float x, y;
    public static final float SIZE = 50; // Size of the tower (e.g., a square)
    private float attackRange;
    private float attackDamage;
    private float attackCooldown;
    private float timeSinceLastAttack;
    private Color color;
    private static final int COST = 50;

    /**
     * Constructor for the Towers class.
     * @param x The x-coordinate of the tower.
     * @param y The y-coordinate of the tower.
     * @param attackRange The range within which the tower can attack enemies.
     * @param attackDamage The damage dealt by the tower per attack.
     * @param attackCooldown The time (in seconds) between attacks.
     * @param color The color of the tower.
     */
    public Towers(float x, float y, float attackRange, float attackDamage, float attackCooldown, Color color) {
        this.x = x; 
        this.y = y;
        this.attackRange = attackRange;
        this.attackDamage = attackDamage;
        this.attackCooldown = attackCooldown;
        this.timeSinceLastAttack = 0; 
        this.color = color;
    }

    
    public void update(float delta) {
        timeSinceLastAttack += delta;
    }

    
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color); 
        shapeRenderer.rect(x, y, SIZE, SIZE); 
    }

    /**
     * Checks if the tower can attack based on its cooldown.
     * @return true if the tower is ready to attack, false otherwise.
     */
    public boolean canAttack() {
        if (timeSinceLastAttack >= attackCooldown) {
            timeSinceLastAttack = 0; // Reset cooldown
            return true;
        }
        return false;
    }

    /**
     * Gets the center position of the tower.
     * @return A Vector2 representing the center of the tower.
     */
    public Vector2 getCenter() {
        return new Vector2(x + SIZE / 2f, y + SIZE / 2f);
    }

    /**
     * Gets the attack range of the tower.
     * @return The attack range.
     */
    public float getAttackRange() {
        return attackRange;
    }

    /**
     * Gets the attack damage of the tower.
     * @return The attack damage.
     */
    public float getAttackDamage() {
        return attackDamage;
    }
    /**
     * Gets the cost of the tower.
     * @return The cost of the tower.
     */
    public static int getCost() {
        return COST;
    }
}
