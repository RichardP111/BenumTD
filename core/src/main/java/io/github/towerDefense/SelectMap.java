/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-05-27
 * This file is part of Rise of Benum Tower Defense.
 * Select map screen for the game.
 */

package io.github.towerDefense;

import com.badlogic.gdx.Screen;

public class SelectMap implements Screen {
    private final Main game;

    public SelectMap(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        System.out.println("Select Loaded");
    }

    @Override
    public void render(float delta) {
        
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
