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
    private final List<Button> buttons = new ArrayList<Button>();

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
        int y = 245;
        buttons.add(new Button(x, y, 200, 26, "1 Arrow", BuildingType.ARROW_TOWER, new Color(70, 170, 95)));
        buttons.add(new Button(x, y + 30, 200, 26, "2 Cannon", BuildingType.CANNON_TOWER, new Color(180, 110, 55)));
        buttons.add(new Button(x, y + 60, 200, 26, "3 Ice", BuildingType.ICE_TOWER, new Color(65, 170, 200)));
        buttons.add(new Button(x, y + 90, 200, 26, "4 Lightning", BuildingType.LIGHTNING_TOWER, new Color(175, 100, 200)));
        buttons.add(new Button(x, y + 120, 200, 26, "5 Wall", BuildingType.WALL, new Color(110, 110, 120)));
        buttons.add(new Button(x, y + 150, 200, 26, "6 Heal", BuildingType.HEAL_TOWER, new Color(75, 180, 145)));
        buttons.add(new Button(x, y + 180, 200, 26, "7 Decoy", BuildingType.DECOY, new Color(200, 155, 80)));
    }

    public void draw(GameEngine engine, Base base, EconomyManager economy,
            ScoreManager score, WaveManager waves, Difficulty difficulty,
            BuildingType selected, GameState state) {
        drawPanel(engine);
        drawTitle(engine);
        drawStats(engine, base, economy, score, waves, difficulty);
        drawCosts(engine);
        drawButtons(engine, selected);
        drawStateOverlay(engine, state);
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

    private void drawStats(GameEngine engine, Base base, EconomyManager economy,
            ScoreManager score, WaveManager waves, Difficulty difficulty) {
        int x = 660;
        int y = 60;
        engine.changeColor(TEXT_LABEL);
        engine.drawText(x, y, "BASE", "Arial", 11);
        engine.changeColor(TEXT_VALUE);
        engine.drawText(x, y + 16, base.getHp() + " / " + base.getMaxHp(), "Arial", 14);

        y += 44;
        engine.changeColor(TEXT_LABEL);
        engine.drawText(x, y, "MONEY", "Arial", 11);
        engine.changeColor(TEXT_VALUE);
        engine.drawText(x, y + 16, "$" + economy.getMoney(), "Arial", 14);

        y += 44;
        engine.changeColor(TEXT_LABEL);
        engine.drawText(x, y, "STAGE", "Arial", 11);
        engine.changeColor(TEXT_VALUE);
        engine.drawText(x, y + 16, waves.getStage() + "  " + difficulty, "Arial", 14);

        y += 44;
        engine.changeColor(TEXT_LABEL);
        engine.drawText(x, y, "SCORE", "Arial", 11);
        engine.changeColor(TEXT_VALUE);
        engine.drawText(x, y + 16, String.valueOf(score.getScore()), "Arial", 14);
    }

    private void drawCosts(GameEngine engine) {
        engine.changeColor(TEXT_LABEL);
        engine.drawText(660, 440, "Costs:", "Arial", 11);
        String[] costs = {
            "$" + GameConfig.ARROW_TOWER_COST + "  $" + GameConfig.CANNON_TOWER_COST + "  $" + GameConfig.ICE_TOWER_COST,
            "$" + GameConfig.LIGHTNING_TOWER_COST + "  $" + GameConfig.WALL_COST + "  $" + GameConfig.HEAL_TOWER_COST,
            "$" + GameConfig.DECOY_COST
        };
        engine.changeColor(new Color(130, 135, 140));
        engine.drawText(660, 458, costs[0], "Arial", 12);
        engine.drawText(660, 475, costs[1], "Arial", 12);
        if (costs.length > 2) engine.drawText(660, 492, costs[2], "Arial", 12);
    }

    private void drawButtons(GameEngine engine, BuildingType selected) {
        for (Button button : buttons) {
            button.draw(engine, button.getBuildingType() == selected);
        }
    }

    private void drawStateOverlay(GameEngine engine, GameState state) {
        if (state == GameState.PAUSED) {
            drawOverlay(engine, "PAUSED", new Color(200, 180, 80));
        } else if (state == GameState.GAME_OVER) {
            drawOverlay(engine, "GAME OVER", new Color(200, 60, 60));
        } else if (state == GameState.WIN) {
            drawOverlay(engine, "YOU WIN!", new Color(80, 200, 120));
        }
    }

    private void drawOverlay(GameEngine engine, String text, Color color) {
        engine.changeColor(new Color(0, 0, 0, 120));
        engine.drawSolidRectangle(0, 0, 640, GameConfig.WINDOW_HEIGHT);
        engine.changeColor(new Color(20, 24, 28, 200));
        engine.drawSolidRectangle(200, 280, 240, 60);
        engine.changeColor(color);
        engine.drawRectangle(200, 280, 240, 60, 2);
        engine.changeColor(color);
        engine.drawBoldText(230, 320, text, "Arial", 32);
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
