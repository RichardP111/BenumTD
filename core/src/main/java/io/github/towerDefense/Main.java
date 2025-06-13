/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-06-02
 * This file the main core of Rise of Benum Tower Defense.
 * 
 * This game was made possible with the help of open-source libraries, resources, and the support of coding community.
 * We would like to acknowledge the following resources:  https://docs.google.com/document/d/1O9Qpu0TRX--fMQT7RbOO81FkXH7bS1WFFZeqwoXXwIM/edit?usp=sharing 
 */

package io.github.towerDefense;

import com.badlogic.gdx.Game;

public class Main extends Game {
    @Override
    public void create() {
        this.setScreen(new SplashScreen(this));
    }
}
