/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-06-11
 * This file is part of Rise of Benum Tower Defense.
 * Manages the placement validation of towers on the game map.
 */

package io.github.towerDefense;

import java.util.ArrayList;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;  

import io.github.towerDefense.map.JunglePath;

public class TowerPlacementManager {
    private final ArrayList<Towers> towers; 

    public TowerPlacementManager(ArrayList<Towers> towers) { //, JungleMap gameMap
        this.towers = towers;
    }

    /**
     * Checks if the new tower position overlaps with any existing towers.
     * Precondition: The new tower's coordinates must be valid within the game map.
     * Postcondition: Returns true if the new tower overlaps with any existing tower, false otherwise.
     * 
     * @param newX The x-coordinate of the new tower.
     * @param newY The y-coordinate of the new tower.
     * @return true if the new tower overlaps with any existing tower, false otherwise.
     */
    public boolean isOverlapping(float newX, float newY) {
        for (Towers tower : towers) {
            if (newX < tower.x + Towers.SIZE &&  
                newX + Towers.SIZE > tower.x &&   
                newY < tower.y + Towers.SIZE && 
                newY + Towers.SIZE > tower.y) {   
                return true; 
            }
        }
        return false; 
    }

    /**
     * Checks if the tower is near a path.
     * Precondition: The path must not be null and must have at least two waypoints.
     * Postcondition: Returns true if the tower is within the specified clearance distance from any segment of the path, false otherwise.
     * 
     * @param towerCenterX x-coordinate of the tower's center
     * @param towerCenterY y-coordinate of the tower's center
     * @param path The path object
     * @param clearanceFromTowerEdgeToPath the minimum distance from the tower's edge to the path.
     * @return true if the tower is near the path, false otherwise.
     */
    public boolean isNearPath(float towerCenterX, float towerCenterY, JunglePath path, float clearanceFromTowerEdgeToPath) {
        if (path == null || path.getNumWaypoints() < 2) {
            return false; 
        }

        float effectiveMinDistanceToCenter = clearanceFromTowerEdgeToPath + (Towers.SIZE / 2f);

        Vector2 towerPos = new Vector2(towerCenterX, towerCenterY);

        for (int i = 0; i < path.getNumWaypoints() - 1; i++) {
            Vector2 p1 = path.getWaypoint(i);
            Vector2 p2 = path.getWaypoint(i + 1);
            if (p1 == null || p2 == null) continue;

            Vector2 nearest = new Vector2();
            Intersector.nearestSegmentPoint(p1, p2, towerPos, nearest);
            float distSq = nearest.dst2(towerPos);

            if (distSq < effectiveMinDistanceToCenter * effectiveMinDistanceToCenter) {
                return true; 
            }
        }
        return false; 
    }
}