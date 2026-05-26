package ui;

import java.awt.Color;

import game.GameEngine;
import game.GameConfig;
import game.Difficulty;

/** Main menu screen with title and difficulty selection. */
public class MenuScreen {
    private static final Color BG = new Color(20, 24, 28);
    private static final Color ACCENT = new Color(200, 180, 120);
    private static final Color SUBTITLE = new Color(160, 165, 170);

    private final Button easyButton = new Button(300, 310, 240, 34, "EASY", null, new Color(80, 180, 120));
    private final Button normalButton = new Button(300, 350, 240, 34, "NORMAL", null, new Color(200, 180, 80));
    private final Button hardButton = new Button(300, 390, 240, 34, "HARD", null, new Color(200, 80, 70));

    public void draw(GameEngine engine) {
        engine.changeColor(BG);
        engine.drawSolidRectangle(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);

        drawBorder(engine);
        drawTitle(engine);
        drawSubtitle(engine);
        drawButtons(engine);
        drawFooter(engine);
    }

    private void drawBorder(GameEngine engine) {
        engine.changeColor(ACCENT);
        engine.drawRectangle(40, 40, GameConfig.WINDOW_WIDTH - 80, GameConfig.WINDOW_HEIGHT - 80, 2);
        engine.drawRectangle(44, 44, GameConfig.WINDOW_WIDTH - 88, GameConfig.WINDOW_HEIGHT - 88, 1);
    }

    private void drawTitle(GameEngine engine) {
        engine.changeColor(ACCENT);
        engine.drawBoldText(180, 140, "HEART OF THE", "Arial", 26);
        engine.drawBoldText(120, 175, "FOUR FRONTS", "Arial", 36);
        engine.drawLine(250, 195, 590, 195);
    }

    private void drawSubtitle(GameEngine engine) {
        engine.changeColor(SUBTITLE);
        engine.drawText(260, 235, "The Last Defence", "Arial", 18);
        engine.drawText(230, 265, "Protect the heart from all sides.", "Arial", 14);
    }

    private void drawButtons(GameEngine engine) {
        easyButton.draw(engine, false);
        normalButton.draw(engine, true);
        hardButton.draw(engine, false);
    }

    private void drawFooter(GameEngine engine) {
        engine.changeColor(new Color(100, 105, 110));
        engine.drawText(220, 490, "1-7: Build  |  Space: Pause  |  Esc: Menu", "Arial", 13);
        engine.drawText(170, 520, "M: Mute  |  Click to place towers", "Arial", 13);
    }

    public Difficulty handleClick(int mouseX, int mouseY) {
        if (easyButton.contains(mouseX, mouseY)) {
            return Difficulty.EASY;
        }
        if (normalButton.contains(mouseX, mouseY)) {
            return Difficulty.NORMAL;
        }
        if (hardButton.contains(mouseX, mouseY)) {
            return Difficulty.HARD;
        }
        return null;
    }
}
