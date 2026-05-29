package game;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.*;
import building.*;
import building.tower.HealTower;
import enemy.*;
import combat.*;
import event.*;
import manager.*;
import ui.*;
import util.*;
import effect.*;

/** Main game class. It coordinates managers and keeps gameplay logic delegated. */
public class CoreSiege extends GameEngine {
    private static final java.io.File SAVE_FILE = new java.io.File("save.dat");

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
    private IntroScreen introScreen;

    private SoundManager soundManager;


    private BuildingType selectedBuilding = BuildingType.ARROW_TOWER;
    private int mouseX = -1, mouseY = -1;
    private boolean hasSave;
    private double speedMultiplier = 1.0;
    private int currentLevel = 1;
    private int maxUnlockedLevel = 1;
    private double endEffectTimer = 0.0;
    private static final double END_EFFECT_DURATION = 3.0;

    public static void main(String[] args) {
        createGame(new CoreSiege(), GameConfig.TARGET_FPS);
    }

    @Override
    public void init() {
        setWindowSize(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        menuScreen = new MenuScreen();
        hud = new HUD();
        SoundManager.init(this);
        soundManager = SoundManager.getInstance();
        soundManager.loadSounds();
        ImageManager.loadImages(this);
        introScreen = new IntroScreen();
        loadSaveMetadata();
        menuScreen.setHasContinue(hasSave);
        menuScreen.setMaxUnlockedLevel(maxUnlockedLevel);
        startNewGame(selectedDifficulty);
        gameState = GameState.INTRO;
    }

    void setCurrentLevel(int level) {
        currentLevel = Math.min(level, GameConfig.TOTAL_LEVELS);
    }

    int getCurrentLevel() {
        return currentLevel;
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
        eventManager = new EventManager(difficultyManager.getDisasterInterval());
        economyManager = new EconomyManager();
        scoreManager = new ScoreManager();
        waveManager = new WaveManager();
        soundManager.startBgm();
    }

    @Override
    public void update(double dt) {

        // INTRO
        if (gameState == GameState.INTRO) {

            introScreen.update(this);

            return;
        }

        // END EFFECT - show dramatic text then transition to end screen
        if (gameState == GameState.GAME_OVER_EFFECT || gameState == GameState.WIN_EFFECT) {
            endEffectTimer += dt;
            hud.update(dt);
            if (endEffectTimer >= END_EFFECT_DURATION) {
                gameState = (gameState == GameState.GAME_OVER_EFFECT) ? GameState.GAME_OVER : GameState.WIN;
            }
            return;
        }

        // MENU
        if (gameState != GameState.PLAYING) {
            return;
        }

        dt *= speedMultiplier;

        hud.update(dt);

        gridMap.update(dt);

        waveManager.update(dt, scoreManager);

        enemySpawner.update(dt, waveManager);

        eventManager.update(dt, gridMap, waveManager,
                enemySpawner, buildings);

        enemySpawner.updateEnemies(dt, enemyAI,
                economyManager, scoreManager, waveManager, buildings);

        for (Building building : new ArrayList<Building>(buildings)) {

            building.update(dt,
                    enemySpawner.getEnemies(),
                    projectileManager,
                    gridMap,
                    buildings);
        }

        removeDestroyedBuildings();

        projectileManager.update(dt);

        // Detect boss death from projectile hits that happened this frame
        for (Enemy enemy : enemySpawner.getEnemies()) {
            if (enemy.isDead() && enemy.getType() == EnemyType.BOSS) {
                waveManager.markBossDefeated();
            }
        }

        decoyManager.update(dt);

        checkGameEnd();
    }

    private void removeDestroyedBuildings() {
        Iterator<Building> iterator = buildings.iterator();
        while (iterator.hasNext()) {
            Building building = iterator.next();
            if (building.isDestroyed()) {
                gridMap.removeBuilding(building.getPosition());
                iterator.remove();
            }
        }
    }

    private boolean hasTriggeredEndSound;

    private void checkGameEnd() {
        if (base.isDestroyed()) {
            gameState = GameState.GAME_OVER_EFFECT;
            endEffectTimer = 0.0;
            hud.resetEndMessageTimer();
            clearActiveSave();
            if (!hasTriggeredEndSound) {
                soundManager.stopBgm();
                soundManager.playGameOver();
                hasTriggeredEndSound = true;
            }
        } else if (waveManager.hasWon()) {
            gameState = GameState.WIN_EFFECT;
            endEffectTimer = 0.0;
            hud.resetEndMessageTimer();
            if (!hasTriggeredEndSound) {
                soundManager.stopBgm();
                soundManager.playGameWin();
                hasTriggeredEndSound = true;
            }
            if (currentLevel < GameConfig.TOTAL_LEVELS) {
                currentLevel++;
            }
            if (currentLevel > maxUnlockedLevel) {
                maxUnlockedLevel = currentLevel;
                menuScreen.setMaxUnlockedLevel(maxUnlockedLevel);
            }
            clearActiveSave();
        }
    }

    @Override

    public void paintComponent() {

        drawBackground();
        // INTRO SCREEN
        if (gameState == GameState.INTRO) {

            introScreen.draw(this);

            return;
        }

        if (gameState == GameState.MENU) {
            menuScreen.draw(this, mouseX, mouseY);
            return;
        }

        // Show end screen after effect
        if (gameState == GameState.GAME_OVER || gameState == GameState.WIN) {
            menuScreen.drawEndScreen(this, gameState == GameState.WIN, mouseX, mouseY);
            return;
        }

        // Draw game scene for PLAYING, PAUSED, and EFFECT states
        gridMap.draw(this);
        for (Building building : buildings) {
            if (building instanceof HealTower) {
                ((HealTower) building).drawRangeEffect(this, gridMap);
            }
        }

        // Draw buildings and base with depth sorting
        // Buildings above the base (smaller row) are drawn first (behind)
        // Buildings below/beside the base (larger or equal row) are drawn last (in front)
        int baseRow = base.getPosition().row;

        // Draw buildings above the base (behind)
        for (Building building : buildings) {
            if (building.getPosition().row < baseRow) {
                building.draw(this, gridMap);
            }
        }

        // Draw the base
        base.draw(this, gridMap);

        // Draw buildings at or below the base (in front)
        for (Building building : buildings) {
            if (building.getPosition().row >= baseRow) {
                building.draw(this, gridMap);
            }
        }

        enemySpawner.draw(this, gridMap);
        projectileManager.draw(this, gridMap);
        decoyManager.draw(this);
        eventManager.draw(this, gridMap);
        hud.draw(this, base, economyManager, scoreManager, waveManager,
                selectedDifficulty, selectedBuilding, gameState, mouseX, mouseY, speedMultiplier, currentLevel);
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
        if (event.getButton() == MouseEvent.BUTTON3) {
            handleRightClick(event);
        } else {
            handlePlayClick(event);
        }
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
        if (action == MenuScreen.LEVEL_PLAY) {
            soundManager.playButtonClick();
            currentLevel = menuScreen.getSelectedLevel();
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
            } else if (action == HUD.PAUSE_DELETE_SAVE) {
                deleteSave();
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
        Building existing = gridMap.getBuildingAt(position);
        if (existing != null && existing.canUpgrade() && selectedBuilding != BuildingType.DECOY) {
            int cost = existing.getUpgradeCost();
            if (economyManager.spendMoney(cost)) {
                existing.upgrade();
                soundManager.playButtonClick();
            } else {
                soundManager.playInsufficientMoney();
            }
            return;
        }
        handleGridClick(position);
    }

    private void handleGridClick(GridPosition position) {
        if (selectedBuilding == BuildingType.DECOY) {
            placeDecoy(position);
        } else {
            placeBuilding(position);
        }
    }

    private void placeDecoy(GridPosition clickPos) {
        if (economyManager.spendMoney(GameConfig.DECOY_COST)) {
            GridPosition basePos = base.getPosition();
            int rowDiff = clickPos.row - basePos.row;
            int colDiff = clickPos.col - basePos.col;
            Direction dir;
            if (Math.abs(rowDiff) > Math.abs(colDiff)) {
                dir = rowDiff < 0 ? Direction.NORTH : Direction.SOUTH;
            } else {
                dir = colDiff < 0 ? Direction.WEST : Direction.EAST;
            }
            decoyManager.createDecoy(basePos, dir);
            soundManager.playDecoyDeploy();
        } else {
            soundManager.playInsufficientMoney();
        }
    }

    private void handleRightClick(MouseEvent event) {
        GridPosition position = gridMap.mouseToGrid(event.getX(), event.getY());
        if (position == null) return;
        Building building = gridMap.getBuildingAt(position);
        if (building == null) return;
        int refund = (int) (building.getCost() * GameConfig.BUILDING_SELL_RATIO);
        economyManager.addMoney(refund);
        gridMap.removeBuilding(position);
        buildings.remove(building);
        soundManager.playButtonClick();
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
        if (gameState == GameState.INTRO && keyCode == KeyEvent.VK_SPACE) {
            gameState = GameState.MENU;
            return;
        }
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
        } else if (keyCode == KeyEvent.VK_F) {
            speedMultiplier = (speedMultiplier == 2.0) ? 1.0 : 2.0;
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
        BuildingType[] all = {BuildingType.ARROW_TOWER, BuildingType.CANNON_TOWER, BuildingType.ICE_TOWER,
                              BuildingType.LIGHTNING_TOWER, BuildingType.HEAL_TOWER, BuildingType.DECOY};
        int[] keys = {KeyEvent.VK_1, KeyEvent.VK_2, KeyEvent.VK_3, KeyEvent.VK_4, KeyEvent.VK_5, KeyEvent.VK_6};
        java.util.List<BuildingType> unlocked = GameConfig.getUnlockedTowers(currentLevel);
        for (int i = 0; i < 6; i++) {
            if (keyCode == keys[i] && unlocked.contains(all[i])) {
                selectedBuilding = all[i];
                return;
            }
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
            writeSaveData(createCurrentSaveData(true));
            hasSave = true;
            menuScreen.setHasContinue(true);
            soundManager.playButtonClick();
        } catch (Exception e) {
            System.out.println("Save failed: " + e.getMessage());
        }
    }

    public void loadGame() {
        try {
            SaveData data = readSaveData();
            maxUnlockedLevel = clampLevel(data.unlockedLevel);
            menuScreen.setMaxUnlockedLevel(maxUnlockedLevel);
            if (!data.hasActiveSave) {
                hasSave = false;
                menuScreen.setHasContinue(false);
                return;
            }

            Difficulty diff = Difficulty.values()[Math.max(0, Math.min(data.difficulty, Difficulty.values().length - 1))];
            currentLevel = clampLevel(data.level);
            startNewGame(diff);
            base.setHp(data.baseHp);
            economyManager.setMoney(data.money);
            scoreManager.setScore(data.score, data.kills, data.rewards, data.buildingsBuilt);
            waveManager.setElapsedTime(data.waveTime, data.stage);
            for (String buildingLine : data.buildingLines) {
                String[] parts = buildingLine.split(",");
                BuildingType type = BuildingType.values()[Integer.parseInt(parts[0])];
                int row = Integer.parseInt(parts[1]);
                int col = Integer.parseInt(parts[2]);
                int hp = Integer.parseInt(parts[3]);
                int lvl = parts.length > 4 ? Integer.parseInt(parts[4]) : 0;
                GridPosition pos = new GridPosition(row, col);
                Building b = buildingFactory.createBuilding(type, pos);
                if (b != null && gridMap.placeBuilding(b)) {
                    b.setUpgradeLevel(lvl);
                    if (b.getHp() > hp) b.takeDamage(b.getHp() - hp);
                    buildings.add(b);
                }
            }
            gameState = GameState.PLAYING;
            soundManager.startBgm();
        } catch (Exception e) {
            System.out.println("Load failed: " + e.getMessage());
            resetSaveFile();
        }
    }

    public void deleteSave() {
        try {
            resetSaveFile();
            soundManager.playButtonClick();
        } catch (Exception e) {
            System.out.println("Delete save failed: " + e.getMessage());
        }
    }

    private void loadSaveMetadata() {
        try {
            SaveData data = readSaveData();
            hasSave = data.hasActiveSave;
            maxUnlockedLevel = clampLevel(data.unlockedLevel);
        } catch (Exception e) {
            resetSaveFile();
        }
    }

    private SaveData readSaveData() throws java.io.IOException {
        ensureSaveFileExists();
        SaveData data = new SaveData();
        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.FileReader(SAVE_FILE));
        try {
            String line;
            while ((line = in.readLine()) != null) {
                if (line.startsWith("hasActiveSave=")) data.hasActiveSave = Boolean.parseBoolean(line.substring(14));
                else if (line.startsWith("unlockedLevel=")) data.unlockedLevel = Integer.parseInt(line.substring(14));
                else if (line.startsWith("difficulty=")) data.difficulty = Integer.parseInt(line.substring(11));
                else if (line.startsWith("money=")) data.money = Integer.parseInt(line.substring(6));
                else if (line.startsWith("score=")) data.score = Integer.parseInt(line.substring(6));
                else if (line.startsWith("kills=")) data.kills = Integer.parseInt(line.substring(6));
                else if (line.startsWith("rewards=")) data.rewards = Integer.parseInt(line.substring(8));
                else if (line.startsWith("buildingsBuilt=")) data.buildingsBuilt = Integer.parseInt(line.substring(15));
                else if (line.startsWith("level=")) data.level = Integer.parseInt(line.substring(6));
                else if (line.startsWith("baseHp=")) data.baseHp = Integer.parseInt(line.substring(7));
                else if (line.startsWith("waveTime=")) data.waveTime = Double.parseDouble(line.substring(9));
                else if (line.startsWith("stage=")) data.stage = Integer.parseInt(line.substring(6));
                else if (line.startsWith("building=")) data.buildingLines.add(line.substring(9));
            }
        } finally {
            in.close();
        }
        data.unlockedLevel = clampLevel(data.unlockedLevel);
        data.level = clampLevel(data.level);
        return data;
    }

    private void writeSaveData(SaveData data) throws java.io.IOException {
        java.io.PrintWriter out = new java.io.PrintWriter(new java.io.FileWriter(SAVE_FILE));
        try {
            out.println("hasActiveSave=" + data.hasActiveSave);
            out.println("unlockedLevel=" + clampLevel(data.unlockedLevel));
            out.println("difficulty=" + data.difficulty);
            out.println("money=" + data.money);
            out.println("score=" + data.score);
            out.println("kills=" + data.kills);
            out.println("rewards=" + data.rewards);
            out.println("buildingsBuilt=" + data.buildingsBuilt);
            out.println("level=" + clampLevel(data.level));
            out.println("baseHp=" + data.baseHp);
            out.println("waveTime=" + data.waveTime);
            out.println("stage=" + data.stage);
            for (String buildingLine : data.buildingLines) {
                out.println("building=" + buildingLine);
            }
        } finally {
            out.close();
        }
    }

    private void ensureSaveFileExists() throws java.io.IOException {
        if (!SAVE_FILE.exists()) {
            writeSaveData(new SaveData());
        }
    }

    private void clearActiveSave() {
        try {
            SaveData data = new SaveData();
            data.unlockedLevel = maxUnlockedLevel;
            writeSaveData(data);
            hasSave = false;
            menuScreen.setHasContinue(false);
        } catch (Exception e) {
            System.out.println("Clear save failed: " + e.getMessage());
        }
    }

    private void resetSaveFile() {
        try {
            writeSaveData(new SaveData());
        } catch (Exception e) {
            System.out.println("Reset save failed: " + e.getMessage());
        }
        hasSave = false;
        maxUnlockedLevel = 1;
        if (menuScreen != null) {
            menuScreen.setHasContinue(false);
            menuScreen.setMaxUnlockedLevel(maxUnlockedLevel);
        }
    }

    private SaveData createCurrentSaveData(boolean active) {
        SaveData data = new SaveData();
        data.hasActiveSave = active;
        data.unlockedLevel = maxUnlockedLevel;
        data.difficulty = selectedDifficulty.ordinal();
        data.money = economyManager.getMoney();
        data.score = scoreManager.getScore();
        data.kills = scoreManager.getEnemiesKilled();
        data.rewards = scoreManager.getRewardPointsCollected();
        data.buildingsBuilt = scoreManager.getBuildingsBuilt();
        data.level = currentLevel;
        data.baseHp = base.getHp();
        data.waveTime = waveManager.getElapsedTime();
        data.stage = waveManager.getStage();
        for (Building b : buildings) {
            data.buildingLines.add(b.getType().ordinal() + "," + b.getPosition().row + "," + b.getPosition().col + "," + b.getHp() + "," + b.getUpgradeLevel());
        }
        return data;
    }

    private int clampLevel(int level) {
        if (level < 1 || level > GameConfig.TOTAL_LEVELS) {
            return 1;
        }
        return level;
    }

    private static class SaveData {
        boolean hasActiveSave = false;
        int unlockedLevel = 1;
        int difficulty = Difficulty.NORMAL.ordinal();
        int money = GameConfig.STARTING_MONEY;
        int score = 0;
        int kills = 0;
        int rewards = 0;
        int buildingsBuilt = 0;
        int level = 1;
        int baseHp = GameConfig.BASE_MAX_HP;
        double waveTime = 0.0;
        int stage = 1;
        List<String> buildingLines = new ArrayList<String>();
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        mouseX = event.getX();
        mouseY = event.getY();
    }

    public void setGameState(GameState state) {

        gameState = state;
    }
}
