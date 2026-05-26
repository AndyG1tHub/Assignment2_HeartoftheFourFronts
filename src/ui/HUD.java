package ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import building.Base;
import building.BuildingType;
import game.GameEngine;
import game.GameConfig;
import game.GameState;
import game.Difficulty;
import manager.EconomyManager;
import manager.ScoreManager;
import manager.WaveManager;

/** Draws in-game stats and build buttons. */
public class HUD {
    public static final int PAUSE_RESUME = 1;
    public static final int PAUSE_SAVE = 2;
    public static final int PAUSE_RESTART = 3;
    public static final int PAUSE_MENU = 4;
    public static final int PAUSE_DELETE_SAVE = 5;

    private final List<Button> buttons = new ArrayList<Button>();
    private final Button resumeButton = new Button(200, 248, 240, 32, "RESUME", null, new Color(80, 200, 120));
    private final Button saveButton = new Button(200, 286, 240, 32, "SAVE", null, new Color(80, 160, 200));
    private final Button deleteSaveBtn = new Button(200, 324, 240, 32, "DELETE SAVE", null, new Color(200, 100, 80));
    private final Button restartBtn = new Button(200, 362, 240, 32, "RESTART", null, new Color(200, 180, 80));
    private final Button menuBtn = new Button(200, 400, 240, 32, "MAIN MENU", null, new Color(180, 100, 100));

    private static final Color PANEL_BG = new Color(28, 32, 36);
    private static final Color DIVIDER = new Color(55, 60, 65);
    private static final Color TEXT_LABEL = new Color(160, 165, 170);
    private static final Color TEXT_VALUE = Color.WHITE;
    private static final Color SECTION_TITLE = new Color(200, 180, 120);

    public HUD() {
        createButtons();
    }

    private void createButtons() {
        int x = 660;
        int y = 260;
        int[] costs = {GameConfig.ARROW_TOWER_COST, GameConfig.CANNON_TOWER_COST, GameConfig.ICE_TOWER_COST,
                       GameConfig.LIGHTNING_TOWER_COST, GameConfig.WALL_COST, GameConfig.HEAL_TOWER_COST, GameConfig.DECOY_COST};
        String[] labels = {"1 Arrow", "2 Cannon", "3 Ice", "4 Lightning", "5 Wall", "6 Heal", "7 Decoy"};
        Color[] colors = {new Color(70, 170, 95), new Color(180, 110, 55), new Color(65, 170, 200),
                          new Color(175, 100, 200), new Color(110, 110, 120), new Color(75, 180, 145), new Color(200, 155, 80)};
        BuildingType[] types = {BuildingType.ARROW_TOWER, BuildingType.CANNON_TOWER, BuildingType.ICE_TOWER,
                                BuildingType.LIGHTNING_TOWER, BuildingType.WALL, BuildingType.HEAL_TOWER, BuildingType.DECOY};
        for (int i = 0; i < 7; i++) {
            buttons.add(new Button(x, y + i * 26, 200, 24, labels[i] + "  $" + costs[i], types[i], colors[i]));
        }
    }

    public void draw(GameEngine engine, Base base, EconomyManager economy,
            ScoreManager score, WaveManager waves, Difficulty difficulty,
            BuildingType selected, GameState state, int mouseX, int mouseY) {
        drawPanel(engine);
        drawTitle(engine);
        drawBaseHealthBar(engine, base);
        drawStats(engine, base, economy, score, waves, difficulty);
        drawButtons(engine, selected);
        drawStateOverlay(engine, state, mouseX, mouseY);
    }

    private void drawPanel(GameEngine engine) {
        engine.changeColor(PANEL_BG);
        engine.drawSolidRectangle(640, 0, GameConfig.WINDOW_WIDTH - 640, GameConfig.WINDOW_HEIGHT);
        engine.changeColor(DIVIDER);
        engine.drawLine(640, 0, 640, GameConfig.WINDOW_HEIGHT);
    }

    private void drawTitle(GameEngine engine) {
        engine.changeColor(SECTION_TITLE);
        engine.drawBoldText(655, 35, "FOUR FRONTS", "Arial", 16);
        engine.drawLine(655, 42, 870, 42);
    }

    private void drawBaseHealthBar(GameEngine engine, Base base) {
        int x = 655, y = 56;
        int barWidth = 210, barHeight = 16;
        double ratio = Math.max(0, (double) base.getHp() / base.getMaxHp());
        Color barColor;
        if (ratio > 0.6) {
            barColor = new Color(70, 190, 100);
        } else if (ratio > 0.3) {
            barColor = new Color(210, 190, 60);
        } else {
            barColor = new Color(210, 70, 60);
        }
        engine.changeColor(new Color(35, 40, 45));
        engine.drawSolidRectangle(x, y, barWidth, barHeight);
        engine.changeColor(barColor);
        engine.drawSolidRectangle(x + 1, y + 1, (int) ((barWidth - 2) * ratio), barHeight - 2);
        engine.changeColor(new Color(80, 85, 90));
        engine.drawRectangle(x, y, barWidth, barHeight);
        engine.changeColor(Color.WHITE);
        engine.drawText(x + 4, y + 13, "BASE  " + base.getHp() + "/" + base.getMaxHp(), "Arial", 11);
    }

    private void drawStatPanel(GameEngine engine, int x, int y, String label, String value) {
        engine.changeColor(new Color(32, 36, 42));
        engine.drawSolidRectangle(x, y, 100, 38);
        engine.changeColor(new Color(55, 60, 68));
        engine.drawRectangle(x, y, 100, 38);
        engine.changeColor(TEXT_LABEL);
        engine.drawText(x + 6, y + 6, label, "Arial", 10);
        engine.changeColor(TEXT_VALUE);
        engine.drawText(x + 6, y + 24, value, "Arial", 14);
    }

    private void drawStats(GameEngine engine, Base base, EconomyManager economy,
            ScoreManager score, WaveManager waves, Difficulty difficulty) {
        int x = 655;
        int y = 78;
        drawStatPanel(engine, x, y, "MONEY", "$" + economy.getMoney());
        drawStatPanel(engine, x + 108, y, "KILLS", String.valueOf(score.getEnemiesKilled()));
        y += 46;
        drawStatPanel(engine, x, y, "STAGE", waves.getStage() + "  " + difficulty);
        drawStatPanel(engine, x + 108, y, "SCORE", String.valueOf(score.getScore()));
    }

    private void drawButtons(GameEngine engine, BuildingType selected) {
        for (Button button : buttons) {
            button.draw(engine, button.getBuildingType() == selected);
        }
    }

    private void drawStateOverlay(GameEngine engine, GameState state, int mouseX, int mouseY) {
        if (state == GameState.PAUSED) {
            drawPauseMenu(engine, mouseX, mouseY);
        } else if (state == GameState.GAME_OVER) {
            drawMessage(engine, "GAME OVER", new Color(200, 60, 60));
        } else if (state == GameState.WIN) {
            drawMessage(engine, "YOU WIN!", new Color(80, 200, 120));
        }
    }

    private void drawPauseMenu(GameEngine engine, int mouseX, int mouseY) {
        engine.changeColor(new Color(0, 0, 0, 140));
        engine.drawSolidRectangle(0, 0, 640, GameConfig.WINDOW_HEIGHT);
        engine.changeColor(new Color(20, 24, 28, 220));
        engine.drawSolidRectangle(180, 220, 280, 230);
        engine.changeColor(new Color(200, 180, 120));
        engine.drawRectangle(180, 220, 280, 230, 2);
        engine.changeColor(new Color(200, 180, 120));
        engine.drawBoldText(255, 243, "PAUSED", "Arial", 24);
        resumeButton.draw(engine, false, mouseX, mouseY);
        saveButton.draw(engine, false, mouseX, mouseY);
        deleteSaveBtn.draw(engine, false, mouseX, mouseY);
        restartBtn.draw(engine, false, mouseX, mouseY);
        menuBtn.draw(engine, false, mouseX, mouseY);
    }

    private void drawMessage(GameEngine engine, String text, Color color) {
        engine.changeColor(new Color(0, 0, 0, 120));
        engine.drawSolidRectangle(0, 0, 640, GameConfig.WINDOW_HEIGHT);
        engine.changeColor(new Color(20, 24, 28, 200));
        engine.drawSolidRectangle(200, 280, 240, 60);
        engine.changeColor(color);
        engine.drawRectangle(200, 280, 240, 60, 2);
        engine.changeColor(color);
        engine.drawBoldText(230, 320, text, "Arial", 32);
    }

    public int handlePauseClick(int mouseX, int mouseY) {
        if (resumeButton.contains(mouseX, mouseY)) return PAUSE_RESUME;
        if (saveButton.contains(mouseX, mouseY)) return PAUSE_SAVE;
        if (deleteSaveBtn.contains(mouseX, mouseY)) return PAUSE_DELETE_SAVE;
        if (restartBtn.contains(mouseX, mouseY)) return PAUSE_RESTART;
        if (menuBtn.contains(mouseX, mouseY)) return PAUSE_MENU;
        return 0;
    }

    public BuildingType getClickedBuildingType(int mouseX, int mouseY) {
        for (Button button : buttons) {
            if (button.contains(mouseX, mouseY)) {
                return button.getBuildingType();
            }
        }
        return null;
    }
}
