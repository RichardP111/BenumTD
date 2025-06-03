/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-06-02
 * This file the main core of Rise of Benum Tower Defense.
 */

package io.github.towerDefense;

import com.badlogic.gdx.Game;
import io.github.towerDefense.map.jungleMap; // Import jungleMap

public class Main extends Game {
    @Override
    public void create() {
        // Set the screen to jungleMap directly for testing the integrated logic
        this.setScreen(new jungleMap(this));
    }
}
