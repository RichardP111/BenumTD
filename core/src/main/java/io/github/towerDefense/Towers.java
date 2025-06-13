/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-06-10
 * This file is part of Rise of Benum Tower Defense.
 * Defines the properties and behavior of a tower unit.
 */

package io.github.towerDefense;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture; 
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2; 

public class Towers {
    public float x, y;
    public static final float SIZE = 100;  // size of the tower
    private final float attackRange;
    private final float attackDamage;
    private final float attackCooldown;
    private float timeSinceLastAttack; 
    private static final int COST = 50;  
    private final String projectileTextureFileName;

    private Enemy currentTarget; 

    private Texture towerTexture;
    private Sprite towerSprite; 

    /**
     * Constructor for the Towers class.
     * 
     * @param x The x-coordinate of the tower.
     * @param y The y-coordinate of the tower.
     * @param attackRange The range within which the tower can attack enemies.
     * @param attackDamage The damage dealt by the tower per attack.
     * @param attackCooldown The time between attacks.
     * @param projectileTextureFileName The file name for the projectile texture.
     * @param towerType The file name for the tower image texture.
     */
    public Towers(float x, float y, float attackRange, float attackDamage, float attackCooldown, String projectileTextureFileName, String towerType) {
        this.x = x;
        this.y = y;
        this.attackRange = attackRange;
        this.attackDamage = attackDamage;
        this.attackCooldown = attackCooldown;
        this.timeSinceLastAttack = 0; 
        this.projectileTextureFileName = projectileTextureFileName;

        if (towerType != null && !towerType.isEmpty()) {
            this.towerTexture = new Texture(towerType);
            this.towerSprite = new Sprite(towerTexture);
            this.towerSprite.setSize(SIZE, SIZE); 
            this.towerSprite.setPosition(x, y); 
        }
    }

    /**
     * Updates the tower's state, including attacking logic.
     * Precondition: all parameters must be valid
     * Postcondition: The tower will find a target and shoot projectiles
     * 
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

        if (currentTarget != null && timeSinceLastAttack >= attackCooldown) {
            shootProjectile(projectiles, batch);
            timeSinceLastAttack = 0; 
        }
    }

    /**
     * Renders the tower's sprite using the provided SpriteBatch.
     * Precondition: The tower's texture must be loaded and the SpriteBatch must be valid.
     * Postcondition: The tower's sprite is drawn on the screen.
     * 
     * @param batch SpriteBatch used for rendering the tower's sprite.
     */
    public void renderSprite(SpriteBatch batch) {
        if (towerSprite != null) {
            towerSprite.draw(batch);
        }
    }

    /**
     * Checks if the given enemy is within the tower's attack range.
     * Precondition: The enemy must not be null.
     * Postcondition: Returns true if the enemy is within range, false otherwise.
     * 
     * @param enemy The enemy to check against the tower's attack range.
     * @return true if the enemy is within range, false otherwise.
     */
    private boolean isInRange(Enemy enemy) {
        if (enemy == null) return false;
        Vector2 towerCenter = getCenter();
        Vector2 enemyCenter = new Vector2(enemy.x + Enemy.SIZE / 2f, enemy.y + Enemy.SIZE / 2f);
        return towerCenter.dst(enemyCenter) <= attackRange;
    }

    /**
     * Finds a new target from the list of enemies.
     * Precondition: The enemies list must not be null and should contain at least one enemy.
     * Postcondition: Sets the currentTarget to the closest enemy within range, or null if no enemies are in range.
     * 
     * @param enemies The list of enemies to search for a target.
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
     * Shoots a projectile towards the current target if it is within range.
     * Precondition: The projectiles list must not be null and the batch must be valid.
     * Postcondition: A new Projectile is created and added to the projectiles list if the target is valid.
     * 
     * @param projectiles The list of projectiles to which the new projectile will be added.
     * @param batch SpriteBatch used for rendering the projectile.
     */
    private void shootProjectile(ArrayList<Projectile> projectiles, SpriteBatch batch) { 
        if (currentTarget != null && currentTarget.isAlive() && isInRange(currentTarget)) {
            Vector2 towerCenter = getCenter();
            projectiles.add(new Projectile(towerCenter.x, towerCenter.y, currentTarget, 500f, (int)attackDamage, this.projectileTextureFileName, batch)); 
        }
    }

    /**
     * Gets the center position of the tower.
     * Precondition: None
     * Postcondition: Returns a Vector2 representing the center of the tower.
     * 
     * @return A Vector2 representing the center of the tower.
     */
    public Vector2 getCenter() {
        return new Vector2(x + SIZE / 2f, y + SIZE / 2f);
    }

    /**
     * Gets the attack range of the tower.
     * Precondition: None
     * Postcondition: Returns the attack range of the tower.
     * @return The attack range.
     */
    public float getAttackRange() {
        return attackRange;
    }

    /**
     * Gets the attack damage of the tower.
     * Precondition: None
     * Postcondition: Returns the attack damage of the tower.
     * @return The attack damage.
     */
    public float getAttackDamage() {
        return attackDamage;
    }

    /**
     * Gets the cost of the tower.
     * Precondition: None
     * Postcondition: Returns the cost of the tower.
     * @return The cost.
     */
    public static int getCost() {
        return COST;
    }

    /**
     * Disposes of the tower's texture when it's no longer needed.
     * Precondition: None
     * Postcondition: The towerTexture is disposed of to free up resources.
     */
    public void dispose() {
        if (towerTexture != null) {
            towerTexture.dispose();
            towerTexture = null;
        }
    }
}