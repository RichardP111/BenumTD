/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-05-27
 * This file is part of Rise of Benum Tower Defense.
 * Tutorial screen for the game.
 */

package io.github.towerDefense;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public class SelectMap implements Screen {
    private final Main game;

    public SelectMap(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        System.out.println("Tutorial Loaded");
    }

    
    /** 
     * @param delta
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Render UI or game start menu here
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
