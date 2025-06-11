/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-06-04
 * This file is part of Rise of Benum Tower Defense.
 * Manages the placement validation of towers on the game map.
 */
package io.github.towerDefense;

import java.util.ArrayList;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;  

import io.github.towerDefense.map.JungleMap;
import io.github.towerDefense.map.JunglePath;

public class TowerPlacementManager {
    private final ArrayList<Towers> towers; 

    public TowerPlacementManager(ArrayList<Towers> towers, JungleMap gameMap) {
        this.towers = towers;
    }
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