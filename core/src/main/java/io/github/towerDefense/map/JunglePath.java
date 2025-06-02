// Path.java
package io.github.towerDefense.map;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;

public class JunglePath {
    private List<Vector2> waypoints;
    private float pathLength;

    public JunglePath() {
        waypoints = new ArrayList<>();
        pathLength = 0f;
    }

    
    /** 
     * @param x
     * @param y
     */
    public void addWaypoint(float x, float y) {
        waypoints.add(new Vector2(x, y));
        if (waypoints.size() > 1) {
            Vector2 p1 = waypoints.get(waypoints.size() - 2);
            Vector2 p2 = waypoints.get(waypoints.size() - 1);
            pathLength += p1.dst(p2);
        }
    }

    public Vector2 getWaypoint(int index) {
        if (index >= 0 && index < waypoints.size()) {
            return waypoints.get(index);
        }
        return null;
    }

    public int getNumWaypoints() {
        return waypoints.size();
    }

    public float getPathLength() {
        return pathLength;
    }
}