/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-06-10
 * This file is part of Rise of Benum Tower Defense.
 * Projectile class for the game, manages the behavior of projectiles fired by towers.
 */

package io.github.towerDefense;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite; 
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
    private final Vector2 position;
    private Vector2 velocity;
    private final Enemy target;
    private final float speed;
    private final int damage;
    private boolean active; 

    private Texture projectileTexture;
    private final Sprite projectileSprite;

    public static final float SIZE = 50f; //size of the projectile

/**
 * Constructor for the Projectile class.
 * Pre-condition: The target must not be null, and the projectile texture file must exist.
 * Post-condition: Initializes the projectile's position, velocity, and sprite.
 * 
 * @param x The x-coordinate of the projectile's initial position.
 * @param y The y-coordinate of the projectile's initial position.
 * @param target The target enemy that the projectile will follow.
 * @param speed The speed of the projectile.
 * @param damage The damage dealt by the projectile upon impact.
 * @param textureFileName The file name of the projectile texture to be used for rendering.
 * @param batch The SpriteBatch used for rendering the projectile.
 */
public Projectile(float x, float y, Enemy target, float speed, int damage, String textureFileName, SpriteBatch batch) {        
        this.position = new Vector2(x, y);
        this.target = target;
        this.speed = speed;
        this.damage = damage;
        this.active = true;

        projectileTexture = new Texture(textureFileName);
        projectileSprite = new Sprite(projectileTexture);
        projectileSprite.setSize(SIZE * 2, SIZE * 2); 
        projectileSprite.setOriginCenter(); 
        projectileSprite.setPosition(position.x - projectileSprite.getWidth() / 2, position.y - projectileSprite.getHeight() / 2);

    

        if (target != null) {
            Vector2 targetCenter = new Vector2(target.x + Enemy.SIZE / 2f, target.y + Enemy.SIZE / 2f);
            this.velocity = targetCenter.cpy().sub(position).nor().scl(speed);
        } else {
            this.velocity = new Vector2(0, 0); 
            this.active = false; 
            if (projectileTexture != null) {
                projectileTexture.dispose();
                projectileTexture = null;
            }
        }
    }

    /**
     * Updates the projectile's position and checks for collision with the target.
     * Pre-condition: The projectile must be active and the target must be alive.
     * Post-condition: The projectile's position is updated, and if it collides with the target, the target takes damage and the projectile is deactivated.
     * 
     * @param delta The time elapsed since the last update in seconds.
     */

    public void update(float delta) {
        if (!active) return;

        if (target != null && target.isAlive()) {
            Vector2 targetCenter = new Vector2(target.x + Enemy.SIZE / 2f, target.y + Enemy.SIZE / 2f);

            Vector2 directionToTarget = targetCenter.cpy().sub(position);
            this.velocity = directionToTarget.nor().scl(speed);

            position.add(velocity.x * delta, velocity.y * delta);

            projectileSprite.setPosition(position.x - projectileSprite.getWidth() / 2, position.y - projectileSprite.getHeight() / 2);
            projectileSprite.setRotation(velocity.angleDeg());

            if (position.dst(targetCenter) < SIZE / 2f + Enemy.SIZE / 2f + (speed * delta * 0.5f)) {
                target.takeDamage(damage);
                active = false; 
                dispose();
            }
        } else {
            active = false;
            dispose(); 
        }
    }

    /**
     * Renders the projectile's sprite.
     * Pre-condition: The projectile must be active and the projectile sprite must not be null.
     * Post-condition: The projectile's sprite is drawn on the screen using the provided SpriteBatch.
     * 
     * @param batch SpriteBatch used for rendering the projectile.
     */
    public void render(SpriteBatch batch) {
        if (!active || projectileSprite == null) return;
        projectileSprite.draw(batch);
    }

    /**
     * Checks if the projectile is still active.
     * Pre-condition: None.
     * Post-condition: Returns true if the projectile is active, false otherwise.
     * 
     * @return true if the projectile is active, false otherwise.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Returns the damage dealt by the projectile.
     * Pre-condition: None.
     * Post-condition: Returns the damage value of the projectile.
     * 
     * @return the damage dealt by the projectile.
     */
    public int getDamage() {
        return damage;
    }

    /**
     * Returns the target enemy of the projectile.
     * Pre-condition: None.
     * Post-condition: Returns the target enemy that the projectile is following.
     * 
     * @return the target enemy of the projectile.
     */
    public Enemy getTarget() {
        return target;
    }

    /**
     * Returns the current position of the projectile.
     * Pre-condition: None.
     * Post-condition: Returns the position of the projectile as a Vector2 object.
     * 
     * @return the position of the projectile.
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Disposes of the projectile's resources.
     * Pre-condition: None
     * Post-condition: The projectile's texture is disposed of to free up resources.
     */
    public void dispose() {
        if (projectileTexture != null) {
            projectileTexture.dispose();
            projectileTexture = null; 
        }
    }
}