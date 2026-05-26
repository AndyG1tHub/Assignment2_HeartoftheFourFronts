package ui;

import java.awt.Color;

import game.GameEngine;
import game.GameConfig;
import game.Difficulty;

/** Main menu screen with Play, Difficulty, Help, Exit options. */
public class MenuScreen {
    private static final Color BG = new Color(20, 24, 28);
    private static final Color ACCENT = new Color(200, 180, 120);
    private static final Color SUBTITLE = new Color(160, 165, 170);

    public static final int END_NONE = 0;
    public static final int END_RESTART = 1;
    public static final int END_MENU = 2;

    private int menuState; // 0=main, 1=help
    private Difficulty currentDifficulty = Difficulty.NORMAL;

    private final Button playButton = new Button(320, 280, 200, 36, "PLAY", null, new Color(80, 180, 120));
    private final Button diffButton = new Button(320, 324, 200, 36, "", null, new Color(200, 180, 80));
    private final Button helpButton = new Button(320, 368, 200, 36, "HELP", null, new Color(130, 150, 180));
    private final Button exitButton = new Button(320, 412, 200, 36, "EXIT", null, new Color(180, 100, 100));
    private final Button backButton = new Button(320, 420, 200, 34, "BACK", null, SUBTITLE);
    private final Button restartButton = new Button(330, 320, 240, 34, "RESTART", null, new Color(200, 180, 80));
    private final Button menuButton = new Button(330, 365, 240, 34, "MAIN MENU", null, SUBTITLE);

    public MenuScreen() {
        updateDiffLabel();
    }

    private void updateDiffLabel() {
        diffButton.setText("DIFFICULTY: " + currentDifficulty);
    }

    public void draw(GameEngine engine, int mouseX, int mouseY) {
        engine.changeColor(BG);
        engine.drawSolidRectangle(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);

        drawBorder(engine);
        drawTitle(engine);
        drawSubtitle(engine);

        if (menuState == 1) {
            drawHelpContent(engine, mouseX, mouseY);
        } else {
            drawMainButtons(engine, mouseX, mouseY);
        }
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
    }

    private void drawMainButtons(GameEngine engine, int mouseX, int mouseY) {
        playButton.draw(engine, false, mouseX, mouseY);
        diffButton.draw(engine, false, mouseX, mouseY);
        helpButton.draw(engine, false, mouseX, mouseY);
        exitButton.draw(engine, false, mouseX, mouseY);
    }

    private void drawHelpContent(GameEngine engine, int mouseX, int mouseY) {
        engine.changeColor(Color.WHITE);
        engine.drawBoldText(280, 280, "CONTROLS", "Arial", 22);
        engine.changeColor(SUBTITLE);
        engine.drawText(260, 310, "1-7 : Select building type", "Arial", 15);
        engine.drawText(260, 330, "Click : Place selected building", "Arial", 15);
        engine.drawText(260, 350, "Space : Pause / Resume", "Arial", 15);
        engine.drawText(260, 370, "Esc   : Return to menu", "Arial", 15);
        engine.drawText(260, 390, "M     : Toggle mute", "Arial", 15);
        backButton.draw(engine, false, mouseX, mouseY);
    }

    private void drawFooter(GameEngine engine) {
        engine.changeColor(new Color(100, 105, 110));
        engine.drawText(230, 520, "Defend the heart from four fronts.", "Arial", 13);
    }

    public int handleClick(int mouseX, int mouseY) {
        if (menuState == 1) {
            if (backButton.contains(mouseX, mouseY)) {
                menuState = 0;
                return MenuAction.SOUND;
            }
            return MenuAction.NONE;
        }
        if (playButton.contains(mouseX, mouseY)) {
            return MenuAction.PLAY;
        }
        if (diffButton.contains(mouseX, mouseY)) {
            cycleDifficulty();
            return MenuAction.SOUND;
        }
        if (helpButton.contains(mouseX, mouseY)) {
            menuState = 1;
            return MenuAction.SOUND;
        }
        if (exitButton.contains(mouseX, mouseY)) {
            System.exit(0);
        }
        return MenuAction.NONE;
    }

    private void cycleDifficulty() {
        if (currentDifficulty == Difficulty.EASY) {
            currentDifficulty = Difficulty.NORMAL;
        } else if (currentDifficulty == Difficulty.NORMAL) {
            currentDifficulty = Difficulty.HARD;
        } else {
            currentDifficulty = Difficulty.EASY;
        }
        updateDiffLabel();
    }

    public Difficulty getSelectedDifficulty() {
        return currentDifficulty;
    }

    public void drawEndScreen(GameEngine engine, boolean won, int mouseX, int mouseY) {
        engine.changeColor(BG);
        engine.drawSolidRectangle(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
        drawBorder(engine);
        engine.changeColor(won ? new Color(80, 200, 120) : new Color(200, 60, 60));
        engine.drawBoldText(won ? 280 : 250, 210, won ? "YOU WIN!" : "GAME OVER", "Arial", 42);
        engine.changeColor(SUBTITLE);
        engine.drawText(310, 260, "Choose what to do next.", "Arial", 16);
        restartButton.draw(engine, true, mouseX, mouseY);
        menuButton.draw(engine, false, mouseX, mouseY);
    }

    public int handleEndClick(int mouseX, int mouseY) {
        if (restartButton.contains(mouseX, mouseY)) {
            return END_RESTART;
        }
        if (menuButton.contains(mouseX, mouseY)) {
            return END_MENU;
        }
        return END_NONE;
    }

    /** Return codes for menu clicks. */
    public static class MenuAction {
        public static final int NONE = 0;
        public static final int PLAY = 1;
        public static final int SOUND = 2;
    }
}
