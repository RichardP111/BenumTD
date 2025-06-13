/**
 * @author Sahil Sahu & Richard Pu
 * Last modified: 2025-06-12
 * This file is part of Rise of Benum Tower Defense.
 * Tutorial class for the game.
 * This class will handle the tutorial logic, including wave management, tower placement, and enemy spawning.
 */
package io.github.towerDefense;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.towerDefense.map.JunglePath;

public class Tutorial implements Screen {
    private final Main game;

    //map variables
    private Texture backgroundImage;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private JunglePath enemyPath;

    //sound variables
    private Sound mainSound;
    private long mainID;
    private Sound towerPlaceSound;
    private Sound buttonClickSound;
    private Sound newRoundSound;
    private Sound gameOverSound;
    private Sound gameWinSound;
    private Sound enemyDeathSound1;
    private Sound enemyDeathSound2;
    private Sound enemyDeathSound3;

    //wave variables
    private final float TIME_BETWEEN_WAVES = 5f;
    private final int MAX_WAVES = 5;
    private float waveTimer;
    private int waveNumber;
    private int enemiesPerWave;
    private int enemiesSpawnedInWave;
    private float enemySpawnIntervalInWave;
    private float individualEnemySpawnTimer;

    //font variables
    private BitmapFont font;
    private GlyphLayout glyphLayout;

    //game state variables
    private ArrayList<Enemy> enemies;
    private ArrayList<Towers> towers;
    private ArrayList<Projectile> projectiles;
    private OrthographicCamera camera;
    private TowerPlacementManager placementManager;
    private boolean paused;
    private int tutorialState; 

    private int benumCoin;
    private int lives;

    //dag and drop variables
    private Stage stage;
    private DragAndDrop dragAndDrop;
    private Texture towerIconTexture1, towerIconTexture2, towerIconTexture3;
    private Image towerDraggableImage1, towerDraggableImage2, towerDraggableImage3;
    private Actor mapDropTargetActor;
    private DragAndDrop.Payload currentDragPayload = null;

    //textures/files
    private Texture leaveButtonTexture;
    private String projectileFileName;
    private String towerImageFileName;
    private Texture enemyTexture1;
    private Texture enemyTexture2;
    private Texture enemyTexture3;
    private Texture textBackgroundTexture;
    private Texture okButtonTexture;

    //tower properties
    private static final int COST_TOWER_1 = 20;
    private static final float RANGE_TOWER_1 = 200f;
    private static final float DAMAGE_TOWER_1 = 1f;
    private static final float COOLDOWN_TOWER_1 = 0.3f;
    private static final String TOWER1_IMAGE_PATH = "benum.jpg";

    private static final int COST_TOWER_2 = 50;
    private static final float RANGE_TOWER_2 = 170f;
    private static final float DAMAGE_TOWER_2 = 1.5f;
    private static final float COOLDOWN_TOWER_2 = 0.26f;
    private static final String TOWER2_IMAGE_PATH = "benum2.png";

    private static final int COST_TOWER_3 = 100;
    private static final float RANGE_TOWER_3 = 150f;
    private static final float DAMAGE_TOWER_3 = 2.5f;
    private static final float COOLDOWN_TOWER_3 = 0.2f;
    private static final String TOWER3_IMAGE_PATH = "benum3.png";

    //boundaries
    private static final float PATH_CLEARANCE_FROM_TOWER_EDGE = 10f;
    private static final float USER_PANEL_HEIGHT = 170f;

    public Tutorial(Main game) {
        this.game = game;
    }

    /**
     * Sets up the game map, initializes variables, and prepares the game state.
     * Pre: none
     * Post: the game map is ready to be displayed and interacted with.
     */
    @Override
    public void show() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        backgroundImage = new Texture("maps/jungleMap.jpg");
        enemyTexture1 = new Texture("enemy.jpg");
        enemyTexture2 = new Texture("enemy2.jpg");
        enemyTexture3 = new Texture("enemy3.jpg");
        towers = new ArrayList<>();
        enemies = new ArrayList<>();
        projectiles = new ArrayList<>();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        benumCoin = 1000;
        lives = 500;

        placementManager = new TowerPlacementManager(towers);
        enemyPath = new JunglePath();

        //wave management variables
        waveTimer = TIME_BETWEEN_WAVES;
        waveNumber = 1;
        enemiesPerWave = 1;
        enemiesSpawnedInWave = 0;
        enemySpawnIntervalInWave = 1.0f;
        individualEnemySpawnTimer = 0f;

        //fonts
        font = new BitmapFont();
        glyphLayout = new GlyphLayout();
        font.getData().setScale(2.5f);

        //load sounds
        mainSound = Gdx.audio.newSound(Gdx.files.internal("audio/main.mp3"));
        if (SettingsScreen.musicEnabled) {
            mainID = mainSound.play(0.5f);
            mainSound.setLooping(mainID, true);
        }

        towerPlaceSound = Gdx.audio.newSound(Gdx.files.internal("audio/towerPlace.mp3"));
        buttonClickSound = Gdx.audio.newSound(Gdx.files.internal("audio/buttonClick.wav"));
        newRoundSound = Gdx.audio.newSound(Gdx.files.internal("audio/newRound.wav"));
        gameWinSound = Gdx.audio.newSound(Gdx.files.internal("audio/gameWin.wav"));
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("audio/gameOver.mp3"));
        enemyDeathSound1 = Gdx.audio.newSound(Gdx.files.internal("audio/peteDie.mp3"));
        enemyDeathSound2 = Gdx.audio.newSound(Gdx.files.internal("audio/nikDie.mp3"));
        enemyDeathSound3 = Gdx.audio.newSound(Gdx.files.internal("audio/jeffDie.mp3"));

        stage = new Stage(new ScreenViewport());
        dragAndDrop = new DragAndDrop();

        //drag and drop setup
        mapDropTargetActor = new Actor();
        mapDropTargetActor.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(mapDropTargetActor); 

        towerIconTexture1 = new Texture(TOWER1_IMAGE_PATH);
        towerIconTexture2 = new Texture(TOWER2_IMAGE_PATH);
        towerIconTexture3 = new Texture(TOWER3_IMAGE_PATH);

        towerDraggableImage1 = new Image(towerIconTexture1);
        towerDraggableImage1.setSize(Towers.SIZE, Towers.SIZE);
        towerDraggableImage1.setPosition(10, 10);
        stage.addActor(towerDraggableImage1);

        towerDraggableImage2 = new Image(towerIconTexture2);
        towerDraggableImage2.setSize(Towers.SIZE, Towers.SIZE);
        towerDraggableImage2.setPosition(10 + towerDraggableImage1.getWidth() + 10, 10);
        stage.addActor(towerDraggableImage2);

        towerDraggableImage3 = new Image(towerIconTexture3);
        towerDraggableImage3.setSize(Towers.SIZE, Towers.SIZE);
        towerDraggableImage3.setPosition(10 + towerDraggableImage1.getWidth() + 10 + towerDraggableImage2.getWidth() + 10, 10);
        stage.addActor(towerDraggableImage3);

        addDragAndDropSource(towerDraggableImage1, "TowerType1", towerIconTexture1);
        addDragAndDropSource(towerDraggableImage2, "TowerType2", towerIconTexture2);
        addDragAndDropSource(towerDraggableImage3, "TowerType3", towerIconTexture3);

        dragAndDrop.addTarget(new DragAndDrop.Target(mapDropTargetActor) {
            @Override
            public boolean drag(DragAndDrop.Source source, DragAndDrop.Payload payload, float x, float y, int pointer) {
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
                float attackRange = 0, attackDamage = 0, attackCooldown = 0;

                switch (towerTypeString) {
                    case "TowerType1":
                        projectileFileName = "compMice.png";
                        towerImageFileName = TOWER1_IMAGE_PATH;
                        attackRange = RANGE_TOWER_1;
                        attackDamage = DAMAGE_TOWER_1;
                        attackCooldown = COOLDOWN_TOWER_1;
                        break;
                    case "TowerType2":
                        projectileFileName = "table.png";
                        towerImageFileName = TOWER2_IMAGE_PATH;
                        attackRange = RANGE_TOWER_2;
                        attackDamage = DAMAGE_TOWER_2;
                        attackCooldown = COOLDOWN_TOWER_2;
                        break;
                    case "TowerType3":
                        projectileFileName = "school.png";
                        towerImageFileName = TOWER3_IMAGE_PATH;
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
                boolean inUserPanel = placeY < USER_PANEL_HEIGHT;

                if (canAfford && !overlaps && !nearPath && !inUserPanel) {
                    if (spendBenumCoin(towerCost)) {
                        towers.add(new Towers(placeX, placeY, attackRange, attackDamage, attackCooldown, projectileFileName, towerImageFileName));
                        if (SettingsScreen.effectEnabled){
                            towerPlaceSound.play(500f);
                        }
                    }
                }
            }
        });

        //leave button
        leaveButtonTexture = new Texture("leaveButton.png");
        ImageButton leaveButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(leaveButtonTexture)));
        leaveButton.addListener(new ClickListener() {
            /**
             * Called when the leave button is clicked.
             * Pre-conditions: x and y coordinates are valid.
             * Post-conditions: The main sound stops, button click sound plays, and the game returns to the start screen.
             * 
             * @param event
             * @param x
             * @param y
             */
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mainSound.stop();
                if (SettingsScreen.effectEnabled){
                    buttonClickSound.play(1f);
                }
                game.setScreen(new StartScreen(game));
            }
        });

        leaveButton.setSize(200, 200);
        leaveButton.setPosition(Gdx.graphics.getWidth() - leaveButton.getWidth() - 20, -20);
        stage.addActor(leaveButton);

        paused = true; 
        tutorialState = 0; 
        textBackgroundTexture = new Texture("textBackground.png");
        okButtonTexture = new Texture("okButton.png"); 

        //ok button
        ImageButton okButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(okButtonTexture)));
        okButton.setName("okButton");
        okButton.setSize(150, 70);
        stage.addActor(okButton);

        okButton.addListener(new ClickListener() {
            /**
             * Called when the OK button is clicked.
             * Pre-conditions: x and y coordinates are valid.
             * Post-conditions: Plays the button click sound, updates the tutorial state, or resumes the game if the tutorial is complete.
             * 
             * @param event the input event
             * @param x the x-coordinate of the click
             * @param y the y-coordinate of the click
             */
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (SettingsScreen.effectEnabled){
                    buttonClickSound.play(1f);
                }
    
                if (tutorialState < 2) { 
                    tutorialState++;
                } else { 
                    paused = false;
                }
            }
        });

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {});
        Gdx.input.setInputProcessor(multiplexer);
    }

    /** 
     * Returns the cost of the tower
     * Pre-condition: towerTypeString must be a valid tower type string.
     * Post-condition: Returns the cost of the tower as an integer.
     * 
     * @param towerTypeString the type of the tower as a string
     * @return the cost of the tower as an integer
     */
    private int getTowerCost(String towerTypeString) {
        if ("TowerType1".equals(towerTypeString)) return COST_TOWER_1;
        if ("TowerType2".equals(towerTypeString)) return COST_TOWER_2;
        if ("TowerType3".equals(towerTypeString)) return COST_TOWER_3;
        return Integer.MAX_VALUE;
    }

    /**
     * Adds a drag and drop source for the specified tower type.
     * Pre-conditions: sourceActor must be a valid Image actor, towerType must be a valid string, and dragActorTexture must not be null.
     * Post-conditions: The drag and drop source is added to the DragAndDrop system.
     * 
     * @param sourceActor the Image actor that will be the source of the drag and drop
     * @param towerType the type of the tower as a string
     * @param dragActorTexture the texture for the drag actor
     */
    private void addDragAndDropSource(final Image sourceActor, final String towerType, Texture dragActorTexture) {
        dragAndDrop.addSource(new DragAndDrop.Source(sourceActor) {
            @Override
            public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {
                currentDragPayload = new DragAndDrop.Payload();
                currentDragPayload.setObject(towerType);

                Image dragImage = new Image(dragActorTexture);
                dragImage.setSize(Towers.SIZE, Towers.SIZE);
                currentDragPayload.setDragActor(dragImage);

                dragAndDrop.setDragActorPosition(-dragImage.getWidth() / 2f + 100, dragImage.getHeight() / 2f - 100);

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

    /**
     * main render method that updates the game state and renders the game map.
     */
    @Override
    public void render(float delta) {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(backgroundImage, 0, 0, screenWidth, screenHeight);

        //wave text
        String waveText = "WAVE " + waveNumber + "/" + MAX_WAVES;
        glyphLayout.setText(font, waveText);
        float textX = (screenWidth - glyphLayout.width) / 2;
        float textY = screenHeight - 20;
        font.setColor(Color.WHITE);
        font.draw(batch, waveText, textX, textY);

        //coin text
        String coinText = "BenumCoin: " + benumCoin;
        glyphLayout.setText(font, coinText);
        font.setColor(Color.BLACK);
        font.draw(batch, coinText, 11, screenHeight - 19);
        font.setColor(Color.YELLOW);
        font.draw(batch, coinText, 10, screenHeight - 20);

        //live text
        String livesText = "Lives: " + lives;
        glyphLayout.setText(font, livesText);
        font.setColor(Color.RED);
        font.draw(batch, livesText, 10, screenHeight - 70);

        //tower cost tex6
        font.setColor(Color.GREEN);
        font.getData().setScale(1.5f);

        String cost1Text = "$" + COST_TOWER_1;
        glyphLayout.setText(font, cost1Text);
        font.draw(batch, cost1Text, towerDraggableImage1.getX() + (towerDraggableImage1.getWidth() - glyphLayout.width) / 2, towerDraggableImage1.getY() + towerDraggableImage1.getHeight() + 20);

        String cost2Text = "$" + COST_TOWER_2;
        glyphLayout.setText(font, cost2Text);
        font.draw(batch, cost2Text, towerDraggableImage2.getX() + (towerDraggableImage2.getWidth() - glyphLayout.width) / 2, towerDraggableImage2.getY() + towerDraggableImage2.getHeight() + 20);

        String cost3Text = "$" + COST_TOWER_3;
        glyphLayout.setText(font, cost3Text);
        font.draw(batch, cost3Text, towerDraggableImage3.getX() + (towerDraggableImage3.getWidth() - glyphLayout.width) / 2, towerDraggableImage3.getY() + towerDraggableImage3.getHeight() + 20);

        font.getData().setScale(2.5f);

        batch.end();

        ImageButton okButton = (ImageButton) stage.getRoot().findActor("okButton");

        //pause logic
        if (paused) {
            batch.begin();

            float backgroundWidth = textBackgroundTexture.getWidth();
            float backgroundHeight = textBackgroundTexture.getHeight();
            float backgroundX = (screenWidth - backgroundWidth) / 2;
            float backgroundY = (screenHeight - backgroundHeight) / 2;
            batch.draw(textBackgroundTexture, backgroundX, backgroundY);

            font.getData().setScale(1.5f);
            String tutorialText;
            switch (tutorialState) { //instructions for each tutorial state
                case 0:
                    tutorialText = "Welcome to Rise of Benum Tower Defense!\n\n" +
                                   "Your goal is to prevent enemies from reaching the end of the path.\n" +
                                   "Click OK to continue.";
                    break;
                case 1:
                    tutorialText = "To place a tower, drag one of the icons from the bottom panel onto the map.\n\n" +
                                   "Green circles indicate valid placement areas.\n" +
                                   "Earn BenumCoins by defeating enemies to build more towers.";
                    break;
                case 2:
                    tutorialText = "Keep an eye on your lives in the top left corner. If it reaches zero, it's game over!\n\n" +
                                   "You have " + MAX_WAVES + " waves to complete. Good luck!";
                    break;
                case 3: 
                    tutorialText = "Wave 1: The first wave is coming!\n\n" +
                                   "Get ready to place your first tower. Enemies are simple, but don't underestimate them!";
                    break;
                case 4: 
                    tutorialText = "Wave 2: Stronger enemies appear!\n\n" +
                                   "Consider using different your towers. BenumCoins are crucial.";
                    break;
                case 5: 
                    tutorialText = "Wave 3: More enemies, faster!\n\n" +
                                   "Focus on chokepoints and ensuring your towers cover the path effectively. Efficiency is key.";
                    break;
                case 6: 
                    tutorialText = "Wave 4: Prepare for a challenge!\n\n" +
                                   "The enemies are tougher now. Make sure your defenses are robust. Don't be afraid to sell and re-strategize.";
                    break;
                case 7: 
                    tutorialText = "FINAL WAVE! Wave 5: This is it, Commander!\n\n" +
                                   "Deploy your best strategy and towers to defeat the final assault. Victory is within reach!";
                    break;
                default:
                    tutorialText = "Game Paused. Click OK to continue.";
                    break;
            }

            float textPadding = 40f;
            float textDrawX = backgroundX + textPadding;
            float textDrawY = backgroundY + backgroundHeight - textPadding;
            float textTargetWidth = backgroundWidth - (2 * textPadding);

            font.setColor(Color.BLACK);
            font.draw(batch, tutorialText, textDrawX, textDrawY, textTargetWidth, Align.center, true);

            batch.end();

            if (okButton != null) {
                float okButtonWidth = okButton.getWidth();
                okButton.setPosition(backgroundX + (backgroundWidth - okButtonWidth) / 2, backgroundY + 50);
                okButton.setVisible(true);
            }

            if (mapDropTargetActor != null) {
                mapDropTargetActor.setTouchable(Touchable.disabled);
            }

        } else { 
            if (okButton != null) {
                okButton.setVisible(false);
            }

            if (mapDropTargetActor != null) {
                mapDropTargetActor.setTouchable(Touchable.enabled);
            }

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
                boolean inUserPanel = potentialTowerCenterY < USER_PANEL_HEIGHT;

                Gdx.gl.glEnable(GL20.GL_BLEND);
                Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
                shapeRenderer.setProjectionMatrix(camera.combined);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

                if (canAfford && !overlaps && !nearPath && !inUserPanel) {
                    shapeRenderer.setColor(0, 1, 0, 0.3f);
                } else {
                    shapeRenderer.setColor(1, 0, 0, 0.3f);
                }

                shapeRenderer.circle(potentialTowerCenterX, potentialTowerCenterY, Towers.SIZE * 2f);
                shapeRenderer.end();
                Gdx.gl.glDisable(GL20.GL_BLEND);
            }

            batch.begin();

            //tower rendering
            for (Towers tower : towers) {
                tower.update(delta, enemies, projectiles, batch);
                tower.renderSprite(batch);
            }
            batch.end();

            batch.begin();

            //render projectiles and enemies
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
                        lives--;
                    } else if (!enemy.isAlive()) {
                        addBenumCoin(5);
                        if (SettingsScreen.effectEnabled){
                            enemy.playDeathSound();
                        }
                        enemyIterator.remove();
                    }
                }
            }
            batch.end();

            //wave management
            waveTimer += delta;
            if (waveTimer >= TIME_BETWEEN_WAVES) {
                if (enemiesSpawnedInWave < enemiesPerWave) {
                    individualEnemySpawnTimer += delta;
                    if (individualEnemySpawnTimer >= enemySpawnIntervalInWave) {
                        Vector2 startPoint = enemyPath.getWaypoint(0);
                        if (startPoint != null) {
                            int enemyHealth;
                            float enemySpeed;
                            Texture currentEnemyTexture;
                            Sound currentDeathSound;

                            if (waveNumber <= 2) { //change enemy properties based on wave number
                                enemyHealth = 5;
                                enemySpeed = 100f + waveNumber * 2f;
                                currentEnemyTexture = enemyTexture1;
                                currentDeathSound = enemyDeathSound1;
                            } else if (waveNumber <= 3) {
                                enemyHealth = 10;
                                enemySpeed = 120f + (waveNumber - 5) * 1.5f;
                                currentEnemyTexture = enemyTexture2;
                                currentDeathSound = enemyDeathSound2;
                            } else {
                                enemyHealth = 15;
                                enemySpeed = 140f + (waveNumber - 15) * 1f;
                                currentEnemyTexture = enemyTexture3;
                                currentDeathSound = enemyDeathSound3;
                            }

                            enemies.add(new Enemy(startPoint.x, startPoint.y, enemySpeed, enemyHealth, enemyPath, currentEnemyTexture, currentDeathSound));
                            enemiesSpawnedInWave++;
                            individualEnemySpawnTimer = 0f;
                        }
                    }

                } else if (enemies.isEmpty()) { //if all enemies in the wave are defeated
                    if (waveNumber < MAX_WAVES) {
                        waveNumber++;
                        enemiesPerWave += 2;
                        enemySpawnIntervalInWave = Math.max(0.1f, enemySpawnIntervalInWave - 0.05f);
                        enemiesSpawnedInWave = 0;
                        waveTimer = 0f;
                        if (SettingsScreen.effectEnabled){
                            newRoundSound.play(1f);
                        }
                        paused = true;
                        tutorialState = 2 + waveNumber;
                    } else {
                        if (SettingsScreen.effectEnabled){
                            gameWinSound.play(1f);
                        }
                        mainSound.stop();
                        completeTutorial();
                    }
                }
            }
        }

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        if (lives <= 0) { //lose condition
            if (SettingsScreen.effectEnabled) {
                gameOverSound.play(1f);
            }
            mainSound.stop();
            dispose();
            game.setScreen(new StartScreen(game));
        }
    }

    /**
     * Get the current amount of BenumCoin.
     * Pre-condition: none
     * Post-condition: Returns the current amount of BenumCoin as an integer.
     * 
     * @return the current amount of BenumCoin
     */
    public int getBenumCoin() {
        return benumCoin;
    }

    /**
     * Add a specified amount of BenumCoin to the current total.
     * Pre-condition: amount must be a integer
     * Post-condition: The amount of BenumCoin is increased by the specified amount.
     * 
     * @param amount the amount of BenumCoin to add
     */
    public void addBenumCoin(int amount) {
        benumCoin += amount;
    }

    /**
     * Spend a specified amount of BenumCoin.
     * Pre-condition: amount must be a integer and less than or equal to the current BenumCoin.
     * Post-condition: If the amount is less than or equal to the current BenumCoin, it is subtracted from the total and returns true. Otherwise, returns false.
     * 
     * @param amount the amount of BenumCoin to spend
     * @return true if the transaction was successful, false otherwise
     */
    public boolean spendBenumCoin(int amount) {
        if (benumCoin >= amount) {
            benumCoin -= amount;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Complete the tutorial and save the state to preferences.
     * Pre-condition: none
     * Post-condition: The tutorial is marked as complete in the preferences file, and the game transitions to the start screen.
     */
    public void completeTutorial() {
        FileHandle tutorialFile = Gdx.files.local("preferences.txt");
        tutorialFile.writeString("true", false);
        game.setScreen(new StartScreen(game)); 
        this.dispose(); 
    }

    /**
     * Resizes the game screen and updates the camera and stage viewport.
     * Pre-condition: width and height are valid integers.
     * Post-condition: The camera and stage viewport are updated to the new width and height, and the enemy path is recalculated based on the new dimensions.
     * 
     * @param width the new width of the game screen
     * @param height the new height of the game screen
     */
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, true);

        if (mapDropTargetActor != null) {
            mapDropTargetActor.setBounds(0, 0, width, height);
        }

        //path finding
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


    /**
     * Disposes of all resources used by the game.
     * Pre-condition: None
     * Post-condition: All textures, sounds, and other resources are disposed of to free up memory.
     */
    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (shapeRenderer != null) shapeRenderer.dispose();
        if (backgroundImage != null) backgroundImage.dispose();
        if (font != null) font.dispose();
        if (stage != null) stage.dispose();
        if (towerIconTexture1 != null) towerIconTexture1.dispose();
        if (towerIconTexture2 != null) towerIconTexture2.dispose();
        if (towerIconTexture3 != null) towerIconTexture3.dispose();
        if (leaveButtonTexture != null) leaveButtonTexture.dispose();
        if (enemyTexture1 != null) enemyTexture1.dispose();
        if (enemyTexture2 != null) enemyTexture2.dispose();
        if (enemyTexture3 != null) enemyTexture3.dispose();
        if (enemyDeathSound1 != null) enemyDeathSound1.dispose();
        if (enemyDeathSound2 != null) enemyDeathSound2.dispose();
        if (enemyDeathSound3 != null) enemyDeathSound3.dispose();
        if (mainSound != null) mainSound.dispose();
        if (towerPlaceSound != null) towerPlaceSound.dispose();
        if (buttonClickSound != null) buttonClickSound.dispose();
        if (newRoundSound != null) newRoundSound.dispose();
        if (gameOverSound != null) gameOverSound.dispose();
        if (gameWinSound != null) gameWinSound.dispose();
        if (textBackgroundTexture != null) textBackgroundTexture.dispose();
        if (okButtonTexture != null) okButtonTexture.dispose();

        for (Towers tower : towers) {
            tower.dispose();
        }

        for (Enemy enemy : enemies) {
            enemy.dispose();
        }
    }
}