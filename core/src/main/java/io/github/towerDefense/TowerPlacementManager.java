/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-05-27
 * This file is part of Rise of Benum Tower Defense.
 * keeps track of all Towers placed.
 */
package io.github.towerDefense; // Suggesting a new package for input handling

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList; // Import ArrayList

import io.github.towerDefense.Towers; // Import the correct Towers class

public class TowerPlacementManager {
    private OrthographicCamera camera;
    private Vector3 unprojectedCoordinates;
    private ArrayList<Towers> existingTowers; // New field to hold existing towers

    public TowerPlacementManager(OrthographicCamera camera, ArrayList<Towers> existingTowers) {
        this.camera = camera;
        this.unprojectedCoordinates = new Vector3();
        this.existingTowers = existingTowers; // Initialize the list
    }

    /**
     * Checks for a left-click and returns a new Towers object at the clicked position
     * in world coordinates, ONLY if it doesn't overlap with existing towers.
     *
     * @return A new Towers object if a left-click occurred and no overlap, otherwise null.
     */
    public Towers getNewTowerOnLeftClick() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            // Get raw screen click coordinates
            float screenX = Gdx.input.getX();
            float screenY = Gdx.input.getY();

            // Set the vector with screen coordinates
            unprojectedCoordinates.set(screenX, screenY, 0);

            // Convert screen coordinates to world coordinates using the camera
            camera.unproject(unprojectedCoordinates);

            // Create a potential new tower for collision checking
            Towers potentialNewTower = new Towers(unprojectedCoordinates.x, unprojectedCoordinates.y, Color.WHITE); // Assuming a default color for the tower

            // Check for overlap with existing towers
            if (!isOverlapping(potentialNewTower)) {
                return potentialNewTower; // No overlap, so place the tower
            } else {
                System.out.println("Cannot place tower: overlaps with existing tower.");
            }
        }
        return null; // No new tower was placed or it overlapped
    }

    /**
     * Checks if a potential new tower overlaps with any of the existing towers.
     * @param potentialTower The tower to check for overlap.
     * @return true if there is an overlap, false otherwise.
     */
    private boolean isOverlapping(Towers potentialTower) {
        for (Towers existingTower : existingTowers) {
            // Simple AABB (Axis-Aligned Bounding Box) collision detection
            if (potentialTower.x < existingTower.x + Towers.SIZE &&
                potentialTower.x + Towers.SIZE > existingTower.x &&
                potentialTower.y < existingTower.y + Towers.SIZE &&
                potentialTower.y + Towers.SIZE > existingTower.y) {
                return true; // Overlap detected
            }
        }
        return false; // No overlap
    }
}