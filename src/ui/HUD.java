package ui;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import building.Base;
import building.BuildingType;
import game.GameEngine;
import game.GameConfig;
import game.GameState;
import game.Difficulty;
import manager.EconomyManager;
import manager.ImageManger;
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
        int x = GameConfig.WINDOW_WIDTH - 240;
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
        drawTowerTooltip(engine, mouseX, mouseY);
        drawStateOverlay(engine, state, mouseX, mouseY);
    }

    private void drawPanel(GameEngine engine) {
        engine.changeColor(PANEL_BG);
        engine.drawSolidRectangle(GameConfig.WINDOW_WIDTH - 260, 0, 260, GameConfig.WINDOW_HEIGHT);
        engine.changeColor(DIVIDER);
        engine.drawLine(GameConfig.WINDOW_WIDTH - 260, 0, GameConfig.WINDOW_WIDTH - 260, GameConfig.WINDOW_HEIGHT);
    }

    private void drawTitle(GameEngine engine) {
        int px = GameConfig.WINDOW_WIDTH - 245;
        engine.changeColor(SECTION_TITLE);
        engine.drawBoldText(px, 35, "FOUR FRONTS", "Arial", 16);
        engine.drawLine(px, 42, px + 215, 42);
    }

    private void drawBaseHealthBar(GameEngine engine, Base base) {
        int x = GameConfig.WINDOW_WIDTH - 245, y = 56;
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
        drawStatPanel(engine, x, y, label, value, null);
    }

    private void drawStatPanel(GameEngine engine, int x, int y, String label, String value, Image icon) {
        engine.changeColor(new Color(32, 36, 42));
        engine.drawSolidRectangle(x, y, 100, 38);
        engine.changeColor(new Color(55, 60, 68));
        engine.drawRectangle(x, y, 100, 38);
        engine.changeColor(TEXT_LABEL);
        engine.drawText(x + 6, y + 6, label, "Arial", 10);

        if (icon != null) {
            engine.drawImage(icon, x + 6, y + 10, 16, 16);
            engine.changeColor(TEXT_VALUE);
            engine.drawText(x + 26, y + 24, value, "Arial", 14);
        } else {
            engine.changeColor(TEXT_VALUE);
            engine.drawText(x + 6, y + 24, value, "Arial", 14);
        }
    }

    private void drawStats(GameEngine engine, Base base, EconomyManager economy,
            ScoreManager score, WaveManager waves, Difficulty difficulty) {
        int x = GameConfig.WINDOW_WIDTH - 245;
        int y = 78;
        Image coinIcon = ImageManger.getCoin();
        drawStatPanel(engine, x, y, "MONEY", String.valueOf(economy.getMoney()), coinIcon);
        drawStatPanel(engine, x + 108, y, "KILLS", String.valueOf(score.getEnemiesKilled()));
        y += 46;
        drawStatPanel(engine, x, y, "STAGE", waves.getStage() + "  " + difficulty);
        drawStatPanel(engine, x + 108, y, "SCORE", String.valueOf(score.getScore()));
    }

    private void drawButtons(GameEngine engine, BuildingType selected) {
        int x = GameConfig.WINDOW_WIDTH - 240;
        int y = 260;
        for (Button button : buttons) {
            button.setPosition(x, y);
            button.draw(engine, button.getBuildingType() == selected);
            y += 26;
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
        int pw = 280, ph = 230;
        int px = (GameConfig.WINDOW_WIDTH - 260 - pw) / 2;
        int py = (GameConfig.WINDOW_HEIGHT - ph) / 2;
        engine.changeColor(new Color(0, 0, 0, 140));
        engine.drawSolidRectangle(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        engine.changeColor(new Color(20, 24, 28, 220));
        engine.drawSolidRectangle(px, py, pw, ph);
        engine.changeColor(new Color(200, 180, 120));
        engine.drawRectangle(px, py, pw, ph, 2);
        engine.changeColor(new Color(200, 180, 120));
        engine.drawBoldText(px + 40, py + 23, "PAUSED", "Arial", 24);
        resumeButton.setPosition(px + 20, py + 38);
        saveButton.setPosition(px + 20, py + 76);
        deleteSaveBtn.setPosition(px + 20, py + 114);
        restartBtn.setPosition(px + 20, py + 152);
        menuBtn.setPosition(px + 20, py + 190);
        resumeButton.draw(engine, false, mouseX, mouseY);
        saveButton.draw(engine, false, mouseX, mouseY);
        deleteSaveBtn.draw(engine, false, mouseX, mouseY);
        restartBtn.draw(engine, false, mouseX, mouseY);
        menuBtn.draw(engine, false, mouseX, mouseY);
    }

    private void drawMessage(GameEngine engine, String text, Color color) {
        int mw = 240, mh = 60;
        int mx = (GameConfig.WINDOW_WIDTH - 260 - mw) / 2;
        int my = (GameConfig.WINDOW_HEIGHT - mh) / 2;
        engine.changeColor(new Color(0, 0, 0, 120));
        engine.drawSolidRectangle(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        engine.changeColor(new Color(20, 24, 28, 200));
        engine.drawSolidRectangle(mx, my, mw, mh);
        engine.changeColor(color);
        engine.drawRectangle(mx, my, mw, mh, 2);
        engine.changeColor(color);
        engine.drawBoldText(mx + 30, my + 40, text, "Arial", 32);
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

    private BuildingType getHoveredBuildingType(int mouseX, int mouseY) {
        for (Button button : buttons) {
            if (button.contains(mouseX, mouseY)) {
                return button.getBuildingType();
            }
        }
        return null;
    }

    private void drawTowerTooltip(GameEngine engine, int mouseX, int mouseY) {
        BuildingType type = getHoveredBuildingType(mouseX, mouseY);
        if (type == null) return;
        int tw = 170, th = 100;
        int panelEdge = GameConfig.WINDOW_WIDTH - 260;
        int tx = mouseX - tw - 8;
        int ty = mouseY - 50;
        if (tx < panelEdge - tw - 8) tx = mouseX + 16;
        if (ty < 0) ty = 4;
        if (ty + th > GameConfig.WINDOW_HEIGHT) ty = GameConfig.WINDOW_HEIGHT - th - 4;

        engine.changeColor(new Color(10, 12, 16, 230));
        engine.drawSolidRectangle(tx, ty, tw, th);
        engine.changeColor(new Color(55, 60, 68));
        engine.drawRectangle(tx, ty, tw, th, 1);

        engine.changeColor(new Color(35, 40, 48));
        engine.drawSolidRectangle(tx + 1, ty + 1, tw - 2, 22);

        String name = type.name().charAt(0) + type.name().substring(1).toLowerCase().replace("_", " ");
        engine.changeColor(new Color(200, 180, 120));
        engine.drawBoldText(tx + 8, ty + 16, name, "Arial", 12);

        int ly = ty + 32;
        engine.changeColor(new Color(120, 125, 130));
        engine.drawText(tx + 8, ly, "Cost", "Arial", 11);
        engine.changeColor(new Color(255, 220, 100));
        engine.drawText(tx + 80, ly, "$" + getCost(type), "Arial", 11);

        engine.changeColor(new Color(120, 125, 130));
        engine.drawText(tx + 8, ly + 16, "HP", "Arial", 11);
        engine.changeColor(new Color(100, 220, 130));
        engine.drawText(tx + 80, ly + 16, String.valueOf(getHp(type)), "Arial", 11);

        if (type != BuildingType.WALL && type != BuildingType.DECOY && type != BuildingType.HEAL_TOWER) {
            engine.changeColor(new Color(120, 125, 130));
            engine.drawText(tx + 8, ly + 32, "Damage", "Arial", 11);
            engine.changeColor(new Color(255, 150, 100));
            engine.drawText(tx + 80, ly + 32, String.valueOf(getDamage(type)), "Arial", 11);

            engine.changeColor(new Color(120, 125, 130));
            engine.drawText(tx + 8, ly + 48, "Range", "Arial", 11);
            engine.changeColor(Color.WHITE);
            engine.drawText(tx + 80, ly + 48, String.valueOf(getRange(type)) + " tiles", "Arial", 11);
        } else if (type == BuildingType.HEAL_TOWER) {
            engine.changeColor(new Color(120, 125, 130));
            engine.drawText(tx + 8, ly + 32, "Heal", "Arial", 11);
            engine.changeColor(new Color(100, 220, 200));
            engine.drawText(tx + 80, ly + 32, "20 HP", "Arial", 11);

            engine.changeColor(new Color(120, 125, 130));
            engine.drawText(tx + 8, ly + 48, "Range", "Arial", 11);
            engine.changeColor(Color.WHITE);
            engine.drawText(tx + 80, ly + 48, getRange(type) + " tiles", "Arial", 11);
        } else if (type == BuildingType.WALL) {
            engine.changeColor(new Color(120, 125, 130));
            engine.drawText(tx + 8, ly + 32, "Type", "Arial", 11);
            engine.changeColor(new Color(150, 150, 160));
            engine.drawText(tx + 80, ly + 32, "Barrier", "Arial", 11);
        } else if (type == BuildingType.DECOY) {
            engine.changeColor(new Color(120, 125, 130));
            engine.drawText(tx + 8, ly + 32, "Type", "Arial", 11);
            engine.changeColor(new Color(200, 155, 80));
            engine.drawText(tx + 80, ly + 32, "Lure", "Arial", 11);
        }
    }

    private int getCost(BuildingType type) {
        switch (type) {
            case ARROW_TOWER: return GameConfig.ARROW_TOWER_COST;
            case CANNON_TOWER: return GameConfig.CANNON_TOWER_COST;
            case ICE_TOWER: return GameConfig.ICE_TOWER_COST;
            case LIGHTNING_TOWER: return GameConfig.LIGHTNING_TOWER_COST;
            case WALL: return GameConfig.WALL_COST;
            case HEAL_TOWER: return GameConfig.HEAL_TOWER_COST;
            case DECOY: return GameConfig.DECOY_COST;
            default: return 0;
        }
    }

    private int getHp(BuildingType type) {
        switch (type) {
            case ARROW_TOWER: return 120;
            case CANNON_TOWER: return 150;
            case ICE_TOWER: return 120;
            case LIGHTNING_TOWER: return 110;
            case WALL: return 300;
            case HEAL_TOWER: return 120;
            case DECOY: return 100;
            default: return 0;
        }
    }

    private int getDamage(BuildingType type) {
        switch (type) {
            case ARROW_TOWER: return 15;
            case CANNON_TOWER: return 45;
            case ICE_TOWER: return 18;
            case LIGHTNING_TOWER: return 28;
            default: return 0;
        }
    }

    private int getRange(BuildingType type) {
        switch (type) {
            case ARROW_TOWER: return 4;
            case CANNON_TOWER: return 4;
            case ICE_TOWER: return 4;
            case LIGHTNING_TOWER: return 5;
            case HEAL_TOWER: return 3;
            default: return 0;
        }
    }
}
