/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-05-28
 * This file the starting splash screen of Rise of Benum Tower Defense.
 */
package io.github.towerDefense;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class splashScreen implements Screen {
    private final Main game;
    private SpriteBatch batch;
    private Texture image;
    private float elapsedTime = 0f;
    private float alpha = 0f;
    private enum State { FADE_IN, HOLD, FADE_OUT }
    private State state = State.FADE_IN;

    public splashScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        image = new Texture("benumTowerLogo.png");
    }

    

    @Override
    public void render(float delta) {
        elapsedTime += delta;

        switch (state) {
            case FADE_IN:
                alpha += delta / 1f; // Fade in over 2s
                if (alpha >= 1f) {
                    alpha = 1f;
                    state = State.HOLD;
                    elapsedTime = 0f;
                }
                break;
            case HOLD:
                if (elapsedTime >= 1f) { // Hold for 1 second
                    state = State.FADE_OUT;
                    elapsedTime = 0f;
                }
                break;
            case FADE_OUT:
                alpha -= delta / 1f; // Fade out over 1s
                if (alpha <= 0f) {
                    alpha = 0f;
                    game.setScreen(new startScreen(game)); // switch screen here
                    dispose();
                    return;
                }
                break;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int windowWidth = Gdx.graphics.getWidth();
        int windowHeight = Gdx.graphics.getHeight();
        int imgWidth = image.getWidth();
        int imgHeight = image.getHeight();

        float scale = 0.6f * Math.min((float) windowWidth / imgWidth, (float) windowHeight / imgHeight);
        int scaledWidth = (int) (imgWidth * scale);
        int scaledHeight = (int) (imgHeight * scale);

        int x = (windowWidth - scaledWidth) / 2;
        int y = (windowHeight - scaledHeight) / 2;

        batch.begin();
        batch.setColor(1f, 1f, 1f, alpha); 
        batch.draw(image, x, y, scaledWidth, scaledHeight);
        batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
