/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-06-02
 * This file the main core of Rise of Benum Tower Defense.
 */

package io.github.towerDefense;

import com.badlogic.gdx.Game;

public class Main extends Game {
    @Override
    public void create() {
        this.setScreen(new SplashScreen(this));
    }
}
