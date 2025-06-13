/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-05-27
 * This file is part of Rise of Benum Tower Defense.
 * Manages path finding for the jungle map in the game.
 */
package io.github.towerDefense.map;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class JunglePath {
    private final List<Vector2> waypoints;
    private float pathLength;

    public JunglePath() {
        waypoints = new ArrayList<>();
        pathLength = 0f;
    }

    /**
     * Adds a waypoint to the path.
     * Precondition: x and y must be valid coordinates within the game map.
     * Postcondition: The waypoint is added to the path, and the path length is updated.
     * 
     * @param x The x-coordinate of the waypoint.
     * @param y The y-coordinate of the waypoint.
     */
    public void addWaypoint(float x, float y) {
        waypoints.add(new Vector2(x, y));
        if (waypoints.size() > 1) {
            Vector2 p1 = waypoints.get(waypoints.size() - 2);
            Vector2 p2 = waypoints.get(waypoints.size() - 1);
            pathLength += p1.dst(p2);
        }
    }

    /**
     * Retrieves a waypoint at the specified index.
     * Precondition: index must be within the bounds of the waypoints list.
     * Postcondition: Returns the waypoint at the specified index, or null if the index is out of bounds.
     * 
     * @param index
     * @return
     */
    public Vector2 getWaypoint(int index) {
        if (index >= 0 && index < waypoints.size()) {
            return waypoints.get(index);
        }
        return null;
    }

    /**
     * Returns the number of waypoints in the path.
     * Precondition: None
     * Postcondition: Returns the size of the waypoints list.
     * 
     * @return
     */

    public int getNumWaypoints() {
        return waypoints.size();
    }

    /**
     * Returns the total length of the path.
     * Precondition: None
     * Postcondition: Returns the total length of the path calculated from the waypoints.
     * 
     * @return
     */
    public float getPathLength() {
        return pathLength;
    }
}