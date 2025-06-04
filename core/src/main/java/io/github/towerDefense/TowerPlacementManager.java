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
import com.badlogic.gdx.math.Vector3; 

import io.github.towerDefense.map.jungleMap;

import java.util.ArrayList;

public class TowerPlacementManager {
    private OrthographicCamera camera;
    private ArrayList<Towers> towers; 
    private float placementCooldown = 0.5f; 
    private float timeSinceLastPlacement = 0f;
    private jungleMap gameMap;


    public TowerPlacementManager(OrthographicCamera camera, ArrayList<Towers> towers, jungleMap gameMap) {
        this.camera = camera;
        this.towers = towers;
        this.gameMap = gameMap;
    }


    public Towers getNewTowerOnLeftClick(float delta) {
        timeSinceLastPlacement += delta;

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && timeSinceLastPlacement >= placementCooldown) {
            // Get mouse coordinates
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.input.getY();

            Vector3 worldCoordinates = camera.unproject(new Vector3(mouseX, mouseY,0));


            
           // Check for overlapping before placing the tower
            if (!isOverlapping(worldCoordinates.x - Towers.SIZE / 2f, worldCoordinates.y - Towers.SIZE / 2f)) {
                // Check if player has enough BenumCoin
                int towerCost = Towers.getCost(); // Get the cost of the tower
                if (gameMap.spendBenumCoin(towerCost)) { // Attempt to spend BenumCoin
                    Towers newTower = new Towers(worldCoordinates.x, worldCoordinates.y, 150f, 1f, 1.5f, Color.BLUE);
                    timeSinceLastPlacement = 0f; 
                    System.out.println("Tower placed at: " + newTower.x + ", " + newTower.y + " for " + towerCost + " BenumCoin. Remaining: " + gameMap.getBenumCoin()); //
                    return newTower;
                } else {
                    System.out.println("Cannot place tower: Not enough BenumCoin! Need " + towerCost + ", have " + gameMap.getBenumCoin()); //
                }
            } else {
                System.out.println("Cannot place tower: Overlapping with an existing tower.");
            }
        }
        return null;
    }


    private boolean isOverlapping(float newX, float newY) {
        for (Towers tower : towers) {
            // Check if the new tower overlaps with any existing tower
            if (newX < tower.x + Towers.SIZE && newX + Towers.SIZE > tower.x &&
                newY < tower.y + Towers.SIZE && newY + Towers.SIZE > tower.y) {
                return true; // Overlap detected
            }
        }
        return false; // No overlap
    }
}