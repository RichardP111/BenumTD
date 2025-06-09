/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-06-02
 * This file is part of Rise of Benum Tower Defense.
 * Defines the properties and behavior of a tower unit.
 */
package io.github.towerDefense;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2; 

public class Towers {
    public float x, y;
    public static final float SIZE = 50; 
    private float attackRange;
    private float attackDamage;
    private float attackCooldown;
    private float timeSinceLastAttack; 
    private Color color;
    private static final int COST = 50; 
    private String projectileTextureFileName;

    private Enemy currentTarget; 

    /**
     * Constructor for the Towers class.
     * @param x The x-coordinate of the tower.
     * @param y The y-coordinate of the tower.
     * @param attackRange The range within which the tower can attack enemies.
     * @param attackDamage The damage dealt by the tower per attack.
     * @param attackCooldown The time (in seconds) between attacks.
     * @param color The color of the tower.
     */
    public Towers(float x, float y, float attackRange, float attackDamage, float attackCooldown, Color color, String projectileTextureFileName) {
        this.x = x;
        this.y = y;
        this.attackRange = attackRange;
        this.attackDamage = attackDamage;
        this.attackCooldown = attackCooldown;
        this.timeSinceLastAttack = 0; 
        this.color = color;
        this.projectileTextureFileName = projectileTextureFileName;
    }

    /**
     * Updates the tower's state, including attacking logic.
     * @param delta The time elapsed since the last frame.
     * @param enemies The list of active enemies on the map.
     * @param projectiles The list of active projectiles on the map (to add new ones).
     * @param batch The SpriteBatch instance used for rendering (passed to projectiles).
     */
    public void update(float delta, ArrayList<Enemy> enemies, ArrayList<Projectile> projectiles, SpriteBatch batch) { 
        timeSinceLastAttack += delta; 

        if (currentTarget == null || !currentTarget.isAlive() || !isInRange(currentTarget)) {
            findNewTarget(enemies);
        }

        // If we have a target and can attack, shoot!
        if (currentTarget != null && timeSinceLastAttack >= attackCooldown) {
            shootProjectile(projectiles, batch);
            timeSinceLastAttack = 0; 
        }
    }

    /**
     * Renders the tower on the screen.
     * @param shapeRenderer The ShapeRenderer to draw the tower's shape.
     */
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);
        shapeRenderer.rect(x, y, SIZE, SIZE);
    }

    /**
     * Checks if a given enemy is within the tower's attack range.
     * @param enemy The enemy to check.
     * @return true if the enemy is in range, false otherwise.
     */
    private boolean isInRange(Enemy enemy) {
        if (enemy == null) return false;
        Vector2 towerCenter = getCenter();
        Vector2 enemyCenter = new Vector2(enemy.x + Enemy.SIZE / 2f, enemy.y + Enemy.SIZE / 2f);
        return towerCenter.dst(enemyCenter) <= attackRange;
    }

    /**
     * Finds the nearest active enemy within range and sets it as the current target.
     * @param enemies The list of active enemies.
     */
    private void findNewTarget(ArrayList<Enemy> enemies) {
        currentTarget = null; 
        float closestDistance = Float.MAX_VALUE;

        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) { 
                Vector2 towerCenter = getCenter();
                Vector2 enemyCenter = new Vector2(enemy.x + Enemy.SIZE / 2f, enemy.y + Enemy.SIZE / 2f);
                float distance = towerCenter.dst(enemyCenter);

                if (distance <= attackRange) {
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        currentTarget = enemy;
                    }
                }
            }
        }
    }

    /**
     * Creates and fires a projectile towards the current target.
     * @param projectiles The list to which the new projectile will be added.
     * @param batch The SpriteBatch to be passed to the Projectile constructor.
     */
    private void shootProjectile(ArrayList<Projectile> projectiles, SpriteBatch batch) { 
        if (currentTarget != null && currentTarget.isAlive() && isInRange(currentTarget)) {
            Vector2 towerCenter = getCenter();
            projectiles.add(new Projectile(towerCenter.x, towerCenter.y, currentTarget, 100f, (int)attackDamage, this.projectileTextureFileName, batch)); 
        }
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
     * @return The cost.
     */
    public static int getCost() {
        return COST;
    }
}