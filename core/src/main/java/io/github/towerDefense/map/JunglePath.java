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
     * @return
     */

    public int getNumWaypoints() {
        return waypoints.size();
    }

    /**
     * Returns the total length of the path.
     * @return
     */
    public float getPathLength() {
        return pathLength;
    }
}