package game;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.*;
import building.*;
import building.tower.*;
import enemy.*;
import combat.*;
import event.*;
import manager.*;
import ui.*;
import util.*;
import effect.*;

/** Main game class. It coordinates managers and keeps gameplay logic delegated. */
public class CoreSiege extends GameEngine {
    private GameState gameState = GameState.MENU;
    private Difficulty selectedDifficulty = Difficulty.NORMAL;

    private GridMap gridMap;
    private Base base;
    private PathFinder pathFinder;
    private BuildingFactory buildingFactory;
    private List<Building> buildings;

    private DifficultyManager difficultyManager;
    private EnemyFactory enemyFactory;
    private EnemySpawner enemySpawner;
    private EnemyAI enemyAI;
    private DecoyManager decoyManager;
    private ProjectileManager projectileManager;
    private EventManager eventManager;
    private EconomyManager economyManager;
    private ScoreManager scoreManager;
    private WaveManager waveManager;
    private HUD hud;
    private MenuScreen menuScreen;
    private AssetManager assetManager;
    private SoundManager soundManager;
    private ParticleSystem particleSystem;

    private BuildingType selectedBuilding = BuildingType.ARROW_TOWER;

    public static void main(String[] args) {
        createGame(new CoreSiege(), GameConfig.TARGET_FPS);
    }

    @Override
    public void init() {
        setWindowSize(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        menuScreen = new MenuScreen();
        hud = new HUD();
        assetManager = new AssetManager();
        SoundManager.init(this);
        soundManager = SoundManager.getInstance();
        soundManager.loadSounds();
        assetManager.loadAssets(this);
        startNewGame(selectedDifficulty);
        gameState = GameState.MENU;
    }

    private void startNewGame(Difficulty difficulty) {
        hasTriggeredEndSound = false;
        selectedDifficulty = difficulty;
        gridMap = new GridMap();
        base = new Base(gridMap.getBasePosition());
        pathFinder = new PathFinder();
        buildingFactory = new BuildingFactory();
        buildings = new ArrayList<Building>();
        difficultyManager = new DifficultyManager(difficulty);
        enemyFactory = new EnemyFactory(difficultyManager);
        enemySpawner = new EnemySpawner(gridMap, enemyFactory, difficultyManager);
        enemyAI = new EnemyAI(gridMap, base, pathFinder);
        decoyManager = new DecoyManager(gridMap);
        enemyAI.setDecoyManager(decoyManager);
        projectileManager = new ProjectileManager();
        eventManager = new EventManager();
        economyManager = new EconomyManager();
        scoreManager = new ScoreManager();
        waveManager = new WaveManager();
        particleSystem = new ParticleSystem();
        soundManager.startBgm();
    }

    @Override
    public void update(double dt) {
        if (gameState != GameState.PLAYING) {
            return;
        }
        updateEconomy(dt);
        updateWaves(dt);
        updateEvents(dt);
        updateEnemies(dt);
        updateBuildings(dt);
        updateProjectiles(dt);
        updateDecoys(dt);
        checkGameEnd();
    }

    private void updateEconomy(double dt) {
        economyManager.updateIncome(dt);
    }

    private void updateWaves(double dt) {
        waveManager.update(dt, scoreManager);
        enemySpawner.update(dt, waveManager);
    }

    private void updateEvents(double dt) {
        eventManager.update(dt, gridMap, waveManager, enemySpawner, buildings);
        particleSystem.update(dt);
    }

    private void updateEnemies(double dt) {
        enemySpawner.updateEnemies(dt, enemyAI, economyManager, scoreManager);
    }

    private void updateBuildings(double dt) {
        for (Building building : new ArrayList<Building>(buildings)) {
            building.update(dt, enemySpawner.getEnemies(), projectileManager, gridMap, buildings);
        }
        removeDestroyedBuildings();
    }

    private void removeDestroyedBuildings() {
        Iterator<Building> iterator = buildings.iterator();
        while (iterator.hasNext()) {
            Building building = iterator.next();
            if (building.isDestroyed()) {
                gridMap.removeBuilding(building.getPosition());
                if (building.getType() == BuildingType.WALL) {
                    soundManager.playWallBreak();
                }
                iterator.remove();
            }
        }
    }

    private void updateProjectiles(double dt) {
        projectileManager.update(dt);
    }

    private void updateDecoys(double dt) {
        decoyManager.update(dt);
    }

    private boolean hasTriggeredEndSound;

    private void checkGameEnd() {
        if (base.isDestroyed()) {
            gameState = GameState.GAME_OVER;
            if (!hasTriggeredEndSound) {
                soundManager.stopBgm();
                soundManager.playGameOver();
                hasTriggeredEndSound = true;
            }
        } else if (waveManager.hasWon()) {
            gameState = GameState.WIN;
            if (!hasTriggeredEndSound) {
                soundManager.stopBgm();
                soundManager.playGameWin();
                hasTriggeredEndSound = true;
            }
        }
    }

    @Override
    public void paintComponent() {
        drawBackground();
        if (gameState == GameState.MENU) {
            menuScreen.draw(this);
            return;
        }
        drawMap();
        drawBuildings();
        drawBase();
        drawEnemies();
        drawProjectiles();
        drawDecoys();
        drawEvents();
        drawHUD();
    }

    private void drawBackground() {
        changeBackgroundColor(new Color(20, 24, 28));
        clearBackground(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
    }

    private void drawMap() {
        gridMap.draw(this);
    }

    private void drawBuildings() {
        for (Building building : buildings) {
            building.draw(this, gridMap);
        }
    }

    private void drawBase() {
        base.draw(this, gridMap);
    }

    private void drawEnemies() {
        enemySpawner.draw(this, gridMap);
    }

    private void drawProjectiles() {
        projectileManager.draw(this, gridMap);
    }

    private void drawDecoys() {
        decoyManager.draw(this);
    }

    private void drawEvents() {
        eventManager.draw(this, gridMap);
        particleSystem.draw(this);
    }

    private void drawHUD() {
        hud.draw(this, base, economyManager, scoreManager, waveManager,
                selectedDifficulty, selectedBuilding, gameState);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (gameState == GameState.MENU) {
            handleMenuClick(event);
            return;
        }
        if (gameState != GameState.PLAYING) {
            return;
        }
        handlePlayClick(event);
    }

    private void handleMenuClick(MouseEvent event) {
        Difficulty difficulty = menuScreen.handleClick(event.getX(), event.getY());
        if (difficulty != null) {
            soundManager.playButtonClick();
            startNewGame(difficulty);
            gameState = GameState.PLAYING;
        }
    }

    private void handlePlayClick(MouseEvent event) {
        BuildingType clickedType = hud.getClickedBuildingType(event.getX(), event.getY());
        if (clickedType != null) {
            selectedBuilding = clickedType;
            soundManager.playButtonClick();
            return;
        }
        GridPosition position = gridMap.mouseToGrid(event.getX(), event.getY());
        if (position == null || eventManager.handleClick(position, economyManager, scoreManager)) {
            return;
        }
        handleGridClick(position);
    }

    private void handleGridClick(GridPosition position) {
        if (selectedBuilding == BuildingType.DECOY) {
            placeDecoy();
        } else {
            placeBuilding(position);
        }
    }

    private void placeDecoy() {
        if (economyManager.spendMoney(GameConfig.DECOY_COST)) {
            decoyManager.createDecoy(base.getPosition(), Direction.NORTH);
            soundManager.playDecoyDeploy();
        } else {
            soundManager.playInsufficientMoney();
        }
    }

    private void placeBuilding(GridPosition position) {
        Building building = buildingFactory.createBuilding(selectedBuilding, position);
        if (building == null || !economyManager.spendMoney(building.getCost())) {
            if (building != null) soundManager.playInsufficientMoney();
            return;
        }
        if (gridMap.placeBuilding(building)) {
            buildings.add(building);
            scoreManager.addBuildingBuilt();
            soundManager.playPlaceBuilding();
        } else {
            economyManager.addMoney(building.getCost());
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == KeyEvent.VK_ESCAPE) {
            gameState = GameState.MENU;
        } else if (keyCode == KeyEvent.VK_SPACE) {
            togglePause();
        } else if (keyCode == KeyEvent.VK_M) {
            soundManager.toggleMute();
        } else {
            selectBuildingByKey(keyCode);
        }
    }

    private void togglePause() {
        if (gameState == GameState.PLAYING) {
            gameState = GameState.PAUSED;
        } else if (gameState == GameState.PAUSED) {
            gameState = GameState.PLAYING;
        }
    }

    private void selectBuildingByKey(int keyCode) {
        if (keyCode == KeyEvent.VK_1) {
            selectedBuilding = BuildingType.ARROW_TOWER;
        } else if (keyCode == KeyEvent.VK_2) {
            selectedBuilding = BuildingType.CANNON_TOWER;
        } else if (keyCode == KeyEvent.VK_3) {
            selectedBuilding = BuildingType.ICE_TOWER;
        } else if (keyCode == KeyEvent.VK_4) {
            selectedBuilding = BuildingType.LIGHTNING_TOWER;
        } else if (keyCode == KeyEvent.VK_5) {
            selectedBuilding = BuildingType.WALL;
        } else if (keyCode == KeyEvent.VK_6) {
            selectedBuilding = BuildingType.HEAL_TOWER;
        } else if (keyCode == KeyEvent.VK_7) {
            selectedBuilding = BuildingType.DECOY;
        }
    }
}
