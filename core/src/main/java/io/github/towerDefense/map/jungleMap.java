/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-06-04
 * This file is part of Rise of Benum Tower Defense.
 * Jungle map class for the game.
 * This class will handle the jungle map layout and logic
 */
package io.github.towerDefense.map;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20; 
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor; 
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.viewport.ScreenViewport; 

import io.github.towerDefense.Enemy;
import io.github.towerDefense.Main;
import io.github.towerDefense.Projectile;
import io.github.towerDefense.SettingsScreen;
import io.github.towerDefense.TowerPlacementManager;
import io.github.towerDefense.Towers;
public class JungleMap implements Screen {
    private final Main game;

    private Texture backgroundImage;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private ArrayList<Enemy> enemies;
    private JunglePath enemyPath;

    private Sound mainSound;
    private long mainID;
    private Sound towerPlaceSound;
    private Sound buttonClickSound; 
    private Sound newRoundSound;
    private Sound gameOverSound;
    private Sound gameWinSound;
    private Sound enemyDeathSound;

    private float waveTimer;
    private final float TIME_BETWEEN_WAVES = 5f;
    private int waveNumber;
    private final int MAX_WAVES = 40;
    private int enemiesPerWave;
    private int enemiesSpawnedInWave;
    private float enemySpawnIntervalInWave;
    private float individualEnemySpawnTimer;

    private BitmapFont font;
    private GlyphLayout glyphLayout;

    private ArrayList<Towers> towers;
    private ArrayList<Projectile> projectiles;
    private OrthographicCamera camera; 
    private TowerPlacementManager placementManager;

    private int benumCoin;

    private Stage stage; 
    private DragAndDrop dragAndDrop;
    private Texture towerIconTexture1, towerIconTexture2, towerIconTexture3;
    private Image towerDraggableImage1, towerDraggableImage2, towerDraggableImage3;
    private Actor mapDropTargetActor; 
    private DragAndDrop.Payload currentDragPayload = null; 

    private Texture enemyTexture;
    private String projectileFileName;

    //tower properties
    private static final int COST_TOWER_1 = 50;
    private static final Color COLOR_TOWER_1 = Color.BLUE;
    private static final float RANGE_TOWER_1 = 150f;
    private static final float DAMAGE_TOWER_1 = 1f;
    private static final float COOLDOWN_TOWER_1 = 0.3f;

    private static final int COST_TOWER_2 = 75;
    private static final Color COLOR_TOWER_2 = Color.GREEN;
    private static final float RANGE_TOWER_2 = 170f;
    private static final float DAMAGE_TOWER_2 = 1.5f;
    private static final float COOLDOWN_TOWER_2 = 0.26f;

    private static final int COST_TOWER_3 = 100;
    private static final Color COLOR_TOWER_3 = Color.RED;
    private static final float RANGE_TOWER_3 = 200f;
    private static final float DAMAGE_TOWER_3 = 2f;
    private static final float COOLDOWN_TOWER_3 = 0.2f;

    private static final float PATH_CLEARANCE_FROM_TOWER_EDGE = 10f; 

    public JungleMap(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        backgroundImage = new Texture("maps/jungleMap.jpg"); 
        enemyTexture = new Texture("enemy.jpg");
        towers = new ArrayList<>();
        enemies = new ArrayList<>();
        projectiles = new ArrayList<>();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        benumCoin = 200; 
        placementManager = new TowerPlacementManager(towers, this); 
        enemyPath = new JunglePath();

        waveTimer = TIME_BETWEEN_WAVES; 
        waveNumber = 1;
        enemiesPerWave = 3; 
        enemiesSpawnedInWave = 0;
        enemySpawnIntervalInWave = 1.0f; 
        individualEnemySpawnTimer = 0f;

        font = new BitmapFont(); 
        glyphLayout = new GlyphLayout();

        mainSound = Gdx.audio.newSound(Gdx.files.internal("audio/main.mp3"));
        if (SettingsScreen.musicEnabled) {
            mainID = mainSound.play(1.0f);
            mainSound.setLooping(mainID, true);
        }

        towerPlaceSound = Gdx.audio.newSound(Gdx.files.internal("audio/towerPlace.mp3"));
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("audio/buttonClick.wav"));
        newRoundSound = Gdx.audio.newSound(Gdx.files.internal("audio/newRound.wav"));
        gameWinSound = Gdx.audio.newSound(Gdx.files.internal("audio/gameWin.wav"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("audio/gameOver.wav"));
        enemyDeathSound = Gdx.audio.newSound(Gdx.files.internal("audio/jeffDie.mp3"));

        stage = new Stage(new ScreenViewport()); 
        dragAndDrop = new DragAndDrop();

        mapDropTargetActor = new Actor();
        mapDropTargetActor.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); 
        stage.addActor(mapDropTargetActor); 

        Pixmap pixmap1 = new Pixmap(50, 50, Pixmap.Format.RGBA8888);
        pixmap1.setColor(COLOR_TOWER_1);
        pixmap1.fillRectangle(0, 0, 50, 50);
        towerIconTexture1 = new Texture(pixmap1);
        pixmap1.dispose();

        Pixmap pixmap2 = new Pixmap(50, 50, Pixmap.Format.RGBA8888);
        pixmap2.setColor(COLOR_TOWER_2);
        pixmap2.fillRectangle(0, 0, 50, 50);
        towerIconTexture2 = new Texture(pixmap2);
        pixmap2.dispose();

        Pixmap pixmap3 = new Pixmap(50, 50, Pixmap.Format.RGBA8888);
        pixmap3.setColor(COLOR_TOWER_3);
        pixmap3.fillRectangle(0, 0, 50, 50);
        towerIconTexture3 = new Texture(pixmap3);
        pixmap3.dispose();

        towerDraggableImage1 = new Image(towerIconTexture1);
        towerDraggableImage1.setPosition(10, 10); 
        stage.addActor(towerDraggableImage1);
      
        towerDraggableImage2 = new Image(towerIconTexture2);
        towerDraggableImage2.setPosition(10 + towerDraggableImage1.getWidth() + 10, 10);
        stage.addActor(towerDraggableImage2);

        towerDraggableImage3 = new Image(towerIconTexture3);
        towerDraggableImage3.setPosition(10 + towerDraggableImage1.getWidth() + 10 + towerDraggableImage2.getWidth() + 10, 10);
        stage.addActor(towerDraggableImage3);

        addDragAndDropSource(towerDraggableImage1, "TowerType1", towerIconTexture1);
        addDragAndDropSource(towerDraggableImage2, "TowerType2", towerIconTexture2);
        addDragAndDropSource(towerDraggableImage3, "TowerType3", towerIconTexture3);
        
        dragAndDrop.addTarget(new DragAndDrop.Target(mapDropTargetActor) { 
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                Vector3 mouseScreenCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                Vector3 worldCoordinates = camera.unproject(mouseScreenCoords.cpy()); 

                float potentialTowerCenterX = worldCoordinates.x;
                float potentialTowerCenterY = worldCoordinates.y;
                float checkTopLeftX = potentialTowerCenterX - Towers.SIZE / 2f;
                float checkTopLeftY = potentialTowerCenterY - Towers.SIZE / 2f;

                boolean canAfford = getBenumCoin() >= getTowerCost( (String) payload.getObject());
                boolean overlaps = placementManager.isOverlapping(checkTopLeftX, checkTopLeftY);
                boolean nearPath = placementManager.isNearPath(potentialTowerCenterX, potentialTowerCenterY, enemyPath, PATH_CLEARANCE_FROM_TOWER_EDGE);

                if (payload.getDragActor() != null) {

                    if (canAfford && !overlaps && !nearPath) {
                         payload.getDragActor().setColor(Color.GREEN); 
                    } else {
                         payload.getDragActor().setColor(Color.RED); 
                    }
                }
                return true; 
            }

            @Override
            public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {
                if (payload.getDragActor() != null) {
                    payload.getDragActor().setColor(Color.WHITE); 
                }
            }

            @Override
            public void drop(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
                Vector3 worldCoordinates = camera.unproject(new Vector3(x, Gdx.graphics.getHeight()-y, 0)); 

                String towerTypeString = (String) payload.getObject();
                
                int towerCost = getTowerCost(towerTypeString);
                Color towerColor = Color.WHITE; 
                float attackRange = 0, attackDamage = 0, attackCooldown = 0;

                switch (towerTypeString) {
                    case "TowerType1":
                        projectileFileName = "compMice.png";
                        towerColor = COLOR_TOWER_1;
                        attackRange = RANGE_TOWER_1;
                        attackDamage = DAMAGE_TOWER_1;
                        attackCooldown = COOLDOWN_TOWER_1;
                        break;
                    case "TowerType2":
                        projectileFileName = "table.png";
                        towerColor = COLOR_TOWER_2;
                        attackRange = RANGE_TOWER_2;
                        attackDamage = DAMAGE_TOWER_2;
                        attackCooldown = COOLDOWN_TOWER_2;
                        break;
                    case "TowerType3":
                        projectileFileName = "school.png";
                        towerColor = COLOR_TOWER_3;
                        attackRange = RANGE_TOWER_3;
                        attackDamage = DAMAGE_TOWER_3;
                        attackCooldown = COOLDOWN_TOWER_3;
                        break;
                    default:
                        break;
                }

                float placeX = worldCoordinates.x - Towers.SIZE / 2f;
                float placeY = worldCoordinates.y - Towers.SIZE / 2f;

                
                boolean canAfford = getBenumCoin() >= towerCost;
                boolean overlaps = placementManager.isOverlapping(placeX, placeY);
                boolean nearPath = placementManager.isNearPath(placeX, placeY, enemyPath, PATH_CLEARANCE_FROM_TOWER_EDGE);


                if (canAfford && !overlaps && !nearPath) {
                    if (spendBenumCoin(towerCost)) {
                        towers.add(new Towers(placeX, placeY, attackRange, attackDamage, attackCooldown, towerColor, projectileFileName));
                        if (SettingsScreen.effectEnabled){
                            towerPlaceSound.play(500f);
                        }
                    }
                }
            }
        });

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage); 
        multiplexer.addProcessor(new InputAdapter() {});
        Gdx.input.setInputProcessor(multiplexer);
    }

    private int getTowerCost(String towerTypeString) {
        if ("TowerType1".equals(towerTypeString)) return COST_TOWER_1;
        if ("TowerType2".equals(towerTypeString)) return COST_TOWER_2;
        if ("TowerType3".equals(towerTypeString)) return COST_TOWER_3;
        return Integer.MAX_VALUE; 
    }

    private void addDragAndDropSource(final Image sourceActor, final String towerType, Texture dragActorTexture) {
        dragAndDrop.addSource(new DragAndDrop.Source(sourceActor) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                currentDragPayload = new DragAndDrop.Payload(); 
                currentDragPayload.setObject(towerType); 

                Image dragImage = new Image(dragActorTexture);
                dragImage.setSize(Towers.SIZE, Towers.SIZE); 
                currentDragPayload.setDragActor(dragImage); 
                
                dragAndDrop.setDragActorPosition((-dragImage.getWidth()/2f+50), (-dragImage.getHeight()/2f));
                                
                sourceActor.getColor().a = 0.4f; 
                return currentDragPayload;
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {
                sourceActor.getColor().a = 1f; 
                if (payload != null && payload.getDragActor() != null) { 
                     payload.getDragActor().setColor(Color.WHITE); 
                }
                currentDragPayload = null;
            }
        });
    }


    @Override
    public void render(float delta) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        camera.update(); 

        batch.setProjectionMatrix(camera.combined); 
        batch.begin();
        batch.draw(backgroundImage, 0, 0, screenWidth, screenHeight);

        String waveText = "WAVE " + waveNumber + "/" + MAX_WAVES;
        glyphLayout.setText(font, waveText);
        float textX = (screenWidth - glyphLayout.width) / 2;
        float textY = screenHeight - 20; 
        font.setColor(Color.WHITE);
        font.draw(batch, waveText, textX, textY);
        
        String coinText = "BenumCoin: " + benumCoin;
        glyphLayout.setText(font, coinText);
        font.setColor(Color.BLACK);
        font.draw(batch, coinText, 11, screenHeight - 19); 
        font.setColor(Color.YELLOW);
        font.draw(batch, coinText, 10, screenHeight - 20); 
        batch.end();

        if (currentDragPayload != null) {
            Vector3 mouseScreenCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 mouseWorldCoords = camera.unproject(mouseScreenCoords.cpy());

            float potentialTowerCenterX = mouseWorldCoords.x;
            float potentialTowerCenterY = mouseWorldCoords.y;
            float checkTopLeftX = potentialTowerCenterX - Towers.SIZE / 2f;
            float checkTopLeftY = potentialTowerCenterY - Towers.SIZE / 2f;

            String towerTypeString = (String) currentDragPayload.getObject();
            boolean canAfford = getBenumCoin() >= getTowerCost(towerTypeString);
            boolean overlaps = placementManager.isOverlapping(checkTopLeftX, checkTopLeftY);
            boolean nearPath = placementManager.isNearPath(potentialTowerCenterX, potentialTowerCenterY, enemyPath, PATH_CLEARANCE_FROM_TOWER_EDGE);

            Gdx.gl.glEnable(GL20.GL_BLEND); 
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            shapeRenderer.setProjectionMatrix(camera.combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); 

            if (canAfford && !overlaps && !nearPath) {
                shapeRenderer.setColor(0, 1, 0, 0.3f);
            } else {
                shapeRenderer.setColor(1, 0, 0, 0.3f); 
            }

            shapeRenderer.circle(potentialTowerCenterX, potentialTowerCenterY, Towers.SIZE * 2f);
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        shapeRenderer.setProjectionMatrix(camera.combined); 
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled); 

        // Update and Render Towers
        for (Towers tower : towers) {
            tower.update(delta, enemies, projectiles, batch);
            tower.render(shapeRenderer);
        }
        shapeRenderer.end();

        batch.begin(); 
        Iterator<Projectile> projectileIterator = projectiles.iterator();
        while (projectileIterator.hasNext()) {
            Projectile projectile = projectileIterator.next();
            projectile.update(delta); 

            if (!projectile.isActive()) {
                projectileIterator.remove();
            } else {
                projectile.render(batch);
            }
        }

        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.move(delta); 
            enemy.render(batch); 

            if (!enemy.isAlive() || enemy.hasReachedEnd()) {
                if (enemy.hasReachedEnd()) {
                    //lose life
                } else if (!enemy.isAlive()) {
                    addBenumCoin(10); 
                    if (SettingsScreen.effectEnabled){
                        enemyDeathSound.play(1f);
                    }
                    enemy.dispose();
                    enemyIterator.remove(); 
                }
            }
        }

        batch.end(); 

        waveTimer += delta;
        if (waveTimer >= TIME_BETWEEN_WAVES) {
            if (enemiesSpawnedInWave < enemiesPerWave) {
                individualEnemySpawnTimer += delta;
                if (individualEnemySpawnTimer >= enemySpawnIntervalInWave) {
                    Vector2 startPoint = enemyPath.getWaypoint(0);
                    if (startPoint != null) {
                        int enemyHealth = 3 + waveNumber;
                        float enemySpeed = 70f + waveNumber * 2f;
                        enemies.add(new Enemy(startPoint.x, startPoint.y, enemySpeed, enemyHealth, Color.ORANGE, enemyPath, batch, enemyTexture));
                        enemiesSpawnedInWave++;
                        individualEnemySpawnTimer = 0f;
                    }
                }

            } else if (enemies.isEmpty()) { 
                if (waveNumber < MAX_WAVES) {
                    waveNumber++;
                    enemiesPerWave += 2; 
                    enemySpawnIntervalInWave = Math.max(0.1f, enemySpawnIntervalInWave - 0.05f); 
                    enemiesSpawnedInWave = 0;
                    waveTimer = 0f; 
                    if (SettingsScreen.effectEnabled){
                        newRoundSound.play(1f);
                    }
                } else {
                    //end game
                    if (SettingsScreen.effectEnabled){
                        gameWinSound.play(1f);
                    }
                }
            }
        }
        shapeRenderer.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f)); 
        stage.draw(); 
    }

    public int getBenumCoin() {
        return benumCoin;
    }

    public void addBenumCoin(int amount) {
        benumCoin += amount;
    }

    public boolean spendBenumCoin(int amount) {
        if (benumCoin >= amount) {
            benumCoin -= amount;
            return true; 
        } else {
            return false; 
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height); 
        stage.getViewport().update(width, height, true); 

        if (mapDropTargetActor != null) {
            mapDropTargetActor.setBounds(0, 0, width, height);
        }

        enemyPath = new JunglePath(); 
        enemyPath.addWaypoint(width * 1.00f, height * 0.87f);
        enemyPath.addWaypoint(width * 0.82f, height * 0.87f);
        enemyPath.addWaypoint(width * 0.82f, height * 0.66f);
        enemyPath.addWaypoint(width * 0.84f, height * 0.65f);
        enemyPath.addWaypoint(width * 0.86f, height * 0.64f);
        enemyPath.addWaypoint(width * 0.88f, height * 0.61f);
        enemyPath.addWaypoint(width * 0.91f, height * 0.59f);
        enemyPath.addWaypoint(width * 0.92f, height * 0.57f);
        enemyPath.addWaypoint(width * 0.95f, height * 0.50f);
        enemyPath.addWaypoint(width * 0.95f, height * 0.40f);
        enemyPath.addWaypoint(width * 0.95f, height * 0.38f);
        enemyPath.addWaypoint(width * 0.94f, height * 0.35f);
        enemyPath.addWaypoint(width * 0.92f, height * 0.30f);
        enemyPath.addWaypoint(width * 0.87f, height * 0.25f);
        enemyPath.addWaypoint(width * 0.84f, height * 0.23f);
        enemyPath.addWaypoint(width * 0.78f, height * 0.20f);
        enemyPath.addWaypoint(width * 0.70f, height * 0.20f);
        enemyPath.addWaypoint(width * 0.65f, height * 0.22f);
        enemyPath.addWaypoint(width * 0.62f, height * 0.27f);
        enemyPath.addWaypoint(width * 0.58f, height * 0.34f);
        enemyPath.addWaypoint(width * 0.58f, height * 0.53f);
        enemyPath.addWaypoint(width * 0.61f, height * 0.58f);
        enemyPath.addWaypoint(width * 0.66f, height * 0.63f);
        enemyPath.addWaypoint(width * 0.70f, height * 0.67f);
        enemyPath.addWaypoint(width * 0.70f, height * 0.87f);
        enemyPath.addWaypoint(width * 0.65f, height * 0.87f);
        enemyPath.addWaypoint(width * 0.57f, height * 0.70f);
        enemyPath.addWaypoint(width * 0.50f, height * 0.50f);
        enemyPath.addWaypoint(width * 0.42f, height * 0.30f);
        enemyPath.addWaypoint(width * 0.35f, height * 0.20f);
        enemyPath.addWaypoint(width * 0.31f, height * 0.20f);
        enemyPath.addWaypoint(width * 0.31f, height * 0.40f);
        enemyPath.addWaypoint(width * 0.38f, height * 0.45f);
        enemyPath.addWaypoint(width * 0.41f, height * 0.49f);
        enemyPath.addWaypoint(width * 0.43f, height * 0.52f);
        enemyPath.addWaypoint(width * 0.45f, height * 0.65f);
        enemyPath.addWaypoint(width * 0.42f, height * 0.75f);
        enemyPath.addWaypoint(width * 0.39f, height * 0.83f);
        enemyPath.addWaypoint(width * 0.36f, height * 0.85f);
        enemyPath.addWaypoint(width * 0.30f, height * 0.87f);
        enemyPath.addWaypoint(width * 0.22f, height * 0.87f);
        enemyPath.addWaypoint(width * 0.18f, height * 0.84f);
        enemyPath.addWaypoint(width * 0.12f, height * 0.79f);
        enemyPath.addWaypoint(width * 0.10f, height * 0.75f);
        enemyPath.addWaypoint(width * 0.07f, height * 0.70f);
        enemyPath.addWaypoint(width * 0.07f, height * 0.57f);
        enemyPath.addWaypoint(width * 0.10f, height * 0.50f);
        enemyPath.addWaypoint(width * 0.15f, height * 0.45f);
        enemyPath.addWaypoint(width * 0.20f, height * 0.40f);
        enemyPath.addWaypoint(width * 0.20f, height * 0.20f);
        enemyPath.addWaypoint(width * 0.00f, height * 0.20f); 
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (backgroundImage != null) backgroundImage.dispose();
        if (enemyTexture != null) enemyTexture.dispose();
        if (font != null) font.dispose(); 
        if (stage != null) stage.dispose();
        if (towerIconTexture1 != null) towerIconTexture1.dispose();
        if (towerIconTexture2 != null) towerIconTexture2.dispose();
        if (towerIconTexture3 != null) towerIconTexture3.dispose();

    }
}
