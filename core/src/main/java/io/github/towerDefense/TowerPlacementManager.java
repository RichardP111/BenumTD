/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-06-02
 * This file is part of Rise of Benum Tower Defense.
 * Manages the placement of towers on the game map.
 */
package io.github.towerDefense;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3; // For unprojecting screen coordinates

import java.util.ArrayList;

public class TowerPlacementManager {
    private OrthographicCamera camera;
    private ArrayList<Towers> towers; // Reference to the list of towers in the game
    private float placementCooldown = 0.5f; // Cooldown to prevent rapid placement
    private float timeSinceLastPlacement = 0f;

    /**
     * Constructor for TowerPlacementManager.
     * @param camera The game's camera, used for unprojecting screen coordinates.
     * @param towers The ArrayList where new towers will be added.
     */
    public TowerPlacementManager(OrthographicCamera camera, ArrayList<Towers> towers) {
        this.camera = camera;
        this.towers = towers;
    }

    /**
     * Checks for user input (left click) to place a new tower.
     * Applies a cooldown to prevent multiple towers from being placed with a single click.
     * @param delta The time in seconds since the last render frame.
     * @return A new Towers object if a tower was placed, otherwise null.
     */
    public Towers getNewTowerOnLeftClick(float delta) {
        timeSinceLastPlacement += delta;

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && timeSinceLastPlacement >= placementCooldown) {
            // Get mouse coordinates
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.input.getY();

            // Unproject screen coordinates to world coordinates
            Vector3 worldCoordinates = camera.unproject(new Vector3(mouseX, mouseY,0));

            // For now, place a simple tower at the clicked location
            // You can add more complex logic here, like checking for valid placement spots
            // or selecting tower types from a UI.
            Towers newTower = new Towers(worldCoordinates.x, worldCoordinates.y, 150f, 1f, 1.5f, Color.BLUE);
            timeSinceLastPlacement = 0f; // Reset cooldown
            return newTower;
        }
        return null;
    }
}
