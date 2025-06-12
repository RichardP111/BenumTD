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

    public static final float SIZE = 50f;

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
     * @param delta Time since last update
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
     * @param batch
     */
    public void render(SpriteBatch batch) {
        if (!active || projectileSprite == null) return;
        projectileSprite.draw(batch);
    }

    /** 
     * @return boolean
     */
    public boolean isActive() {
        return active;
    }

    /** 
     * @return int
     */
    public int getDamage() {
        return damage;
    }

    /** 
     * @return Enemy
     */
    public Enemy getTarget() {
        return target;
    }

    /** 
     * @return Vector2
     */
    public Vector2 getPosition() {
        return position;
    }

    public void dispose() {
        if (projectileTexture != null) {
            projectileTexture.dispose();
            projectileTexture = null; 
        }
    }
}