/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-05-27
 * This file is part of Rise of Benum Tower Defense.
 * Creates the visual tower.
 */
package io.github.towerDefense;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer; // Import Color for drawing towers
import com.badlogic.gdx.math.Vector2;


public class Towers { // Renamed from TowerPlacementManager to Towers
    public float x, y;
    public static final float SIZE = 40; // Size of the square
    private Color color;
    private float attackRange;
    private int attackDamage;
    private float attackCooldown;
    private float timeSinceLastAttack;



    // Constructor — no return type, same name as class
    public Towers(float x, float y, Color color) {
        this.x = x - SIZE / 2f; // Center the tower
        this.y = y - SIZE / 2f;
        this.color = color;

        this.attackRange = 100f; // Example range
        this.attackDamage = 10; // Example damage
        this.attackCooldown = 1f; // Example cooldown in seconds
        this.timeSinceLastAttack = 0f; // Initialize attack timer
    }


    
    /** 
     * @param shapeRenderer
     */
    public void render(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.WHITE); 
        shapeRenderer.rect(x, y, SIZE, SIZE);
    }
    public void update(float delta) {
        timeSinceLastAttack += delta; // Update the attack timer
    }
    public boolean canAttack(){
        if (timeSinceLastAttack >= attackCooldown) {
            timeSinceLastAttack = 0; // Reset the timer after an attack
            return true; // Tower can attack
        }
        return false; // Tower is still on cooldown
    }
    public Vector2 getCenter(){
        return new Vector2(x + SIZE / 2f, y + SIZE / 2f); // Return the center of the tower
    }

    public float getAttackRange() {
        return attackRange; // Return the attack range
    }

    public float getAttackDamage() {
        return attackDamage; // Return the attack damage
    }
}