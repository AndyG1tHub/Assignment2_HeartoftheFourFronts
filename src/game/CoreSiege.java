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
    private ImageManger imageManger;
    private SoundManager soundManager;
    private ParticleSystem particleSystem;

    private BuildingType selectedBuilding = BuildingType.ARROW_TOWER;
    private int mouseX = -1, mouseY = -1;
    private boolean hasSave;

    public static void main(String[] args) {
        createGame(new CoreSiege(), GameConfig.TARGET_FPS);
    }

    @Override
    public void init() {
        setWindowSize(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        menuScreen = new MenuScreen();
        hud = new HUD();
        imageManger = new ImageManger();
        SoundManager.init(this);
        soundManager = SoundManager.getInstance();
        soundManager.loadSounds();
        imageManger.loadImages(this);
        hasSave = saveFileExists();
        menuScreen.setHasContinue(hasSave);
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
        economyManager.updateIncome(dt);
        waveManager.update(dt, scoreManager);
        enemySpawner.update(dt, waveManager);
        eventManager.update(dt, gridMap, waveManager, enemySpawner, buildings);
        particleSystem.update(dt);
        enemySpawner.updateEnemies(dt, enemyAI, economyManager, scoreManager);
        for (Building building : new ArrayList<Building>(buildings)) {
            building.update(dt, enemySpawner.getEnemies(), projectileManager, gridMap, buildings);
        }
        removeDestroyedBuildings();
        projectileManager.update(dt);
        decoyManager.update(dt);
        checkGameEnd();
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
            menuScreen.draw(this, mouseX, mouseY);
            return;
        }
        if (gameState == GameState.GAME_OVER || gameState == GameState.WIN) {
            menuScreen.drawEndScreen(this, gameState == GameState.WIN, mouseX, mouseY);
            return;
        }
        gridMap.draw(this);
        for (Building building : buildings) {
            building.draw(this, gridMap);
        }
        base.draw(this, gridMap);
        enemySpawner.draw(this, gridMap);
        projectileManager.draw(this, gridMap);
        decoyManager.draw(this);
        eventManager.draw(this, gridMap);
        particleSystem.draw(this);
        hud.draw(this, base, economyManager, scoreManager, waveManager,
                selectedDifficulty, selectedBuilding, gameState, mouseX, mouseY);
    }

    private void drawBackground() {
        changeBackgroundColor(new Color(20, 24, 28));
        clearBackground(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
    }

    @Override
    public void mousePressed(MouseEvent event) {
        if (gameState == GameState.MENU) {
            handleMenuClick(event);
            return;
        }
        if (gameState == GameState.GAME_OVER || gameState == GameState.WIN) {
            handleEndClick(event);
            return;
        }
        if (gameState == GameState.PAUSED) {
            handlePlayClick(event);
            return;
        }
        if (gameState != GameState.PLAYING) {
            return;
        }
        handlePlayClick(event);
    }

    private void handleEndClick(MouseEvent event) {
        int action = menuScreen.handleEndClick(event.getX(), event.getY());
        if (action == MenuScreen.END_RESTART) {
            soundManager.playButtonClick();
            startNewGame(selectedDifficulty);
            gameState = GameState.PLAYING;
        } else if (action == MenuScreen.END_MENU) {
            soundManager.playButtonClick();
            gameState = GameState.MENU;
        }
    }

    private void handleMenuClick(MouseEvent event) {
        int action = menuScreen.handleClick(event.getX(), event.getY());
        if (action == MenuScreen.MenuAction.PLAY) {
            soundManager.playButtonClick();
            startNewGame(menuScreen.getSelectedDifficulty());
            gameState = GameState.PLAYING;
        } else if (action == MenuScreen.CONTINUE) {
            soundManager.playButtonClick();
            loadGame();
        } else if (action == MenuScreen.MenuAction.SOUND) {
            soundManager.playButtonClick();
        }
    }

    private void handlePlayClick(MouseEvent event) {
        if (gameState == GameState.PAUSED) {
            int action = hud.handlePauseClick(event.getX(), event.getY());
            if (action == HUD.PAUSE_RESUME) {
                gameState = GameState.PLAYING;
            } else if (action == HUD.PAUSE_SAVE) {
                saveGame();
            } else if (action == HUD.PAUSE_RESTART) {
                restartGame();
            } else if (action == HUD.PAUSE_MENU) {
                returnToMenu();
            }
            return;
        }
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
        if ((gameState == GameState.GAME_OVER || gameState == GameState.WIN)
                && keyCode == KeyEvent.VK_ENTER) {
            startNewGame(selectedDifficulty);
            gameState = GameState.PLAYING;
        } else if (keyCode == KeyEvent.VK_ESCAPE) {
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

    public void restartGame() {
        soundManager.playButtonClick();
        startNewGame(selectedDifficulty);
        gameState = GameState.PLAYING;
    }

    public void returnToMenu() {
        soundManager.playButtonClick();
        soundManager.stopBgm();
        gameState = GameState.MENU;
    }

    public void saveGame() {
        try {
            java.io.PrintWriter out = new java.io.PrintWriter(new java.io.FileWriter("save.dat"));
            out.println("difficulty=" + selectedDifficulty.ordinal());
            out.println("money=" + economyManager.getMoney());
            out.println("score=" + scoreManager.getScore());
            out.println("kills=" + scoreManager.getEnemiesKilled());
            out.println("rewards=" + scoreManager.getRewardPointsCollected());
            out.println("buildingsBuilt=" + scoreManager.getBuildingsBuilt());
            out.println("baseHp=" + base.getHp());
            out.println("waveTime=" + waveManager.getElapsedTime());
            out.println("stage=" + waveManager.getStage());
            for (Building b : buildings) {
                out.println("building=" + b.getType().ordinal() + "," + b.getPosition().row + "," + b.getPosition().col + "," + b.getHp());
            }
            out.close();
            hasSave = true;
            menuScreen.setHasContinue(true);
            soundManager.playButtonClick();
        } catch (Exception e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    public void loadGame() {
        try {
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader("save.dat"));
            String line;
            int diffOrd = 0, money = 0, score = 0, kills = 0, rewards = 0, buildingsBuilt = 0;
            int baseHp = GameConfig.BASE_MAX_HP;
            double waveTime = 0;
            int stage = 1;
            List<String[]> savedBuildings = new ArrayList<String[]>();
            while ((line = in.readLine()) != null) {
                if (line.startsWith("difficulty=")) diffOrd = Integer.parseInt(line.substring(11));
                else if (line.startsWith("money=")) money = Integer.parseInt(line.substring(6));
                else if (line.startsWith("score=")) score = Integer.parseInt(line.substring(6));
                else if (line.startsWith("kills=")) kills = Integer.parseInt(line.substring(6));
                else if (line.startsWith("rewards=")) rewards = Integer.parseInt(line.substring(8));
                else if (line.startsWith("buildingsBuilt=")) buildingsBuilt = Integer.parseInt(line.substring(15));
                else if (line.startsWith("baseHp=")) baseHp = Integer.parseInt(line.substring(7));
                else if (line.startsWith("waveTime=")) waveTime = Double.parseDouble(line.substring(9));
                else if (line.startsWith("stage=")) stage = Integer.parseInt(line.substring(6));
                else if (line.startsWith("building=")) savedBuildings.add(line.substring(9).split(","));
            }
            in.close();

            Difficulty diff = Difficulty.values()[diffOrd];
            startNewGame(diff);
            base.setHp(baseHp);
            economyManager.setMoney(money);
            scoreManager.setScore(score, kills, rewards, buildingsBuilt);
            waveManager.setElapsedTime(waveTime, stage);
            for (String[] parts : savedBuildings) {
                BuildingType type = BuildingType.values()[Integer.parseInt(parts[0])];
                int row = Integer.parseInt(parts[1]);
                int col = Integer.parseInt(parts[2]);
                int hp = Integer.parseInt(parts[3]);
                GridPosition pos = new GridPosition(row, col);
                Building b = buildingFactory.createBuilding(type, pos);
                if (b != null && gridMap.placeBuilding(b)) {
                    while (b.getHp() > hp) b.takeDamage(1);
                    buildings.add(b);
                }
            }
            gameState = GameState.PLAYING;
            soundManager.startBgm();
        } catch (Exception e) {
            System.out.println("Load failed: " + e.getMessage());
            hasSave = false;
            menuScreen.setHasContinue(false);
        }
    }

    private boolean saveFileExists() {
        try {
            return new java.io.File("save.dat").exists();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
    }
}
