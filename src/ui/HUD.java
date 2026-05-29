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
import manager.ImageManager;
import manager.ScoreManager;
import manager.WaveManager;

/** Draws in-game stats and build buttons — premium glassmorphism style. */
public class HUD {
    public static final int PAUSE_RESUME = 1;
    public static final int PAUSE_SAVE = 2;
    public static final int PAUSE_RESTART = 3;
    public static final int PAUSE_MENU = 4;
    public static final int PAUSE_DELETE_SAVE = 5;

    private static final Color PANEL_BG   = new Color(12, 16, 22, 235);
    private static final Color GOLD       = new Color(235, 200, 110);
    private static final Color GOLD_DIM   = new Color(170, 140, 60);
    private static final Color MUTED      = new Color(180, 185, 190);
    private static final Color TEXT_VALUE = Color.WHITE;
    private static final Color ACCENT_LINE = new Color(GOLD.getRed(), GOLD.getGreen(), GOLD.getBlue(), 90);

    private final List<Button> buttons = new ArrayList<Button>();
    private final Button resumeButton = new Button(200, 248, 240, 32, "RESUME", null, new Color(80, 200, 120));
    private final Button saveButton = new Button(200, 286, 240, 32, "SAVE", null, new Color(80, 160, 200));
    private final Button deleteSaveBtn = new Button(200, 324, 240, 32, "DELETE SAVE", null, new Color(200, 100, 80));
    private final Button restartBtn = new Button(200, 362, 240, 32, "RESTART", null, new Color(200, 180, 80));
    private final Button menuBtn = new Button(200, 400, 240, 32, "MAIN MENU", null, new Color(180, 100, 100));

    private double endMessageTimer = 0.0;
    private static final double END_MESSAGE_DURATION = 3.0;
    private double gameTime = 0.0;

    public HUD() {
    }

    public void update(double dt) {
        gameTime += dt;
    }

    public void resetEndMessageTimer() {
        endMessageTimer = 0.0;
    }

    private void createButtons(int level) {
        buttons.clear();
        java.util.List<BuildingType> unlocked = GameConfig.getUnlockedTowers(level);
        int[] costs = {GameConfig.ARROW_TOWER_COST, GameConfig.CANNON_TOWER_COST, GameConfig.ICE_TOWER_COST,
                       GameConfig.LIGHTNING_TOWER_COST, GameConfig.HEAL_TOWER_COST, GameConfig.DECOY_COST};
        String[] labels = {"Arrow", "Cannon", "Ice", "Lightning", "Heal", "Decoy"};
        Color[] colors = {new Color(70, 170, 95), new Color(180, 110, 55), new Color(65, 170, 200),
                          new Color(175, 100, 200), new Color(75, 180, 145), new Color(200, 155, 80)};
        BuildingType[] allTypes = {BuildingType.ARROW_TOWER, BuildingType.CANNON_TOWER, BuildingType.ICE_TOWER,
                                   BuildingType.LIGHTNING_TOWER, BuildingType.HEAL_TOWER, BuildingType.DECOY};
        for (int i = 0; i < 6; i++) {
            if (unlocked.contains(allTypes[i])) {
                buttons.add(new Button(0, 0, 200, 24,
                    labels[i] + "  $" + costs[i], allTypes[i], colors[i]));
            }
        }
    }

    // ================================================================
    //  MAIN DRAW
    // ================================================================

    public void draw(GameEngine engine, Base base, EconomyManager economy,
            ScoreManager score, WaveManager waves, Difficulty difficulty,
            BuildingType selected, GameState state, int mouseX, int mouseY, double speedMul, int level) {
        createButtons(level);
        drawPanel(engine);
        drawTitle(engine);
        drawLevelInfo(engine, level);
        drawBaseHealthBar(engine, base);
        drawStats(engine, base, economy, score, waves, difficulty);
        drawButtons(engine, selected, mouseX, mouseY);
        drawHints(engine);
        drawTowerTooltip(engine, mouseX, mouseY, difficulty);
        drawBossWarning(engine, waves);
        drawPrepTimer(engine, waves);
        drawSpeedIndicator(engine, speedMul);
        drawStateOverlay(engine, state, mouseX, mouseY);
    }

    // ================================================================
    //  PANEL
    // ================================================================

    private void drawPanel(GameEngine engine) {
        int px = GameConfig.WINDOW_WIDTH - GameConfig.HUD_WIDTH;
        int w = GameConfig.HUD_WIDTH;
        int h = GameConfig.WINDOW_HEIGHT;

        // Main glass panel
        engine.changeColor(PANEL_BG);
        engine.drawSolidRectangle(px, 0, w, h);

        // Left gold accent line
        engine.changeColor(ACCENT_LINE);
        engine.drawSolidRectangle(px, 0, 2, h);

        // Right edge
        engine.changeColor(new Color(40, 45, 52, 120));
        engine.drawLine(px + w, 0, px + w, h);
    }

    // ================================================================
    //  TITLE
    // ================================================================

    private void drawTitle(GameEngine engine) {
        int px = GameConfig.WINDOW_WIDTH - GameConfig.HUD_WIDTH + 15;
        String title = "FOUR FRONTS";

        int tw = engine.textWidth(title, "Georgia", 18);
        engine.changeColor(new Color(0, 0, 0, 100));
        engine.drawBoldText(px + 2, 33, title, "Georgia", 18);
        engine.changeColor(GOLD);
        engine.drawBoldText(px, 31, title, "Georgia", 18);

        // Gold underline
        engine.changeColor(new Color(GOLD.getRed(), GOLD.getGreen(), GOLD.getBlue(), 70));
        engine.drawLine(px, 40, px + 275, 40);
    }

    // ================================================================
    //  LEVEL INFO
    // ================================================================

    private void drawLevelInfo(GameEngine engine, int level) {
        int px = GameConfig.WINDOW_WIDTH - GameConfig.HUD_WIDTH + 15;
        engine.changeColor(new Color(140, 145, 150));
        engine.drawBoldText(px, 58, "Level " + level + " / " + GameConfig.TOTAL_LEVELS, "Arial", 13);
    }

    // ================================================================
    //  BASE HEALTH BAR
    // ================================================================

    private void drawBaseHealthBar(GameEngine engine, Base base) {
        int x = GameConfig.WINDOW_WIDTH - GameConfig.HUD_WIDTH + 15, y = 70;
        int barWidth = 280, barHeight = 18;
        double ratio = Math.max(0, (double) base.getHp() / base.getMaxHp());

        // Glass background
        engine.changeColor(new Color(18, 24, 32, 220));
        engine.drawSolidRectangle(x - 2, y - 2, barWidth + 4, barHeight + 22);
        engine.changeColor(new Color(40, 45, 52, 100));
        engine.drawRectangle(x - 2, y - 2, barWidth + 4, barHeight + 22);

        // Label
        engine.changeColor(MUTED);
        engine.drawText(x, y - 1, "BASE HP", "Arial", 10);

        // Bar background
        y += 10;
        engine.changeColor(new Color(30, 36, 44));
        engine.drawSolidRectangle(x, y, barWidth, barHeight);

        // Bar fill with gradient colors
        Color barColor;
        if (ratio > 0.6) {
            barColor = new Color(70, 200, 110);
        } else if (ratio > 0.3) {
            barColor = new Color(220, 200, 60);
        } else {
            barColor = new Color(220, 70, 60);
        }
        engine.changeColor(barColor);
        engine.drawSolidRectangle(x + 1, y + 1, (int) ((barWidth - 2) * ratio), barHeight - 2);

        // Gold border
        engine.changeColor(new Color(GOLD_DIM.getRed(), GOLD_DIM.getGreen(), GOLD_DIM.getBlue(), 80));
        engine.drawRectangle(x, y, barWidth, barHeight);

        // HP text on bar
        engine.changeColor(Color.WHITE);
        String hpText = base.getHp() + " / " + base.getMaxHp();
        int hpTw = engine.textWidth(hpText, "Arial", 11);
        engine.drawBoldText(x + (barWidth - hpTw) / 2, y + 14, hpText, "Arial", 11);
    }

    // ================================================================
    //  STAT CARDS
    // ================================================================

    private void drawStatCard(GameEngine engine, int x, int y, int w, int h,
            String label, String value, Image icon) {
        // Glass card background
        engine.changeColor(new Color(18, 24, 32, 200));
        engine.drawSolidRectangle(x, y, w, h);

        // Subtle border
        engine.changeColor(new Color(42, 48, 56, 100));
        engine.drawRectangle(x, y, w, h);

        // Top gold accent
        engine.changeColor(new Color(GOLD_DIM.getRed(), GOLD_DIM.getGreen(), GOLD_DIM.getBlue(), 50));
        engine.drawLine(x, y, x + w, y);

        // Label
        engine.changeColor(MUTED);
        engine.drawText(x + 8, y + 6, label, "Arial", 10);

        // Value with optional icon
        if (icon != null) {
            engine.drawImage(icon, x + 8, y + 14, 16, 16);
            engine.changeColor(TEXT_VALUE);
            engine.drawBoldText(x + 28, y + 26, value, "Arial", 14);
        } else {
            engine.changeColor(TEXT_VALUE);
            engine.drawBoldText(x + 8, y + 26, value, "Arial", 14);
        }
    }

    private void drawStats(GameEngine engine, Base base, EconomyManager economy,
            ScoreManager score, WaveManager waves, Difficulty difficulty) {
        int x = GameConfig.WINDOW_WIDTH - GameConfig.HUD_WIDTH + 15;
        int y = 118;
        int cw = 135;
        int ch = 42;
        int gap = 8;
        Image coinIcon = ImageManager.getCoin();

        drawStatCard(engine, x, y, cw, ch, "MONEY", String.valueOf(economy.getMoney()), coinIcon);
        drawStatCard(engine, x + cw + gap, y, cw, ch, "KILLS", String.valueOf(score.getEnemiesKilled()), null);

        y += ch + gap;
        int waveNum = (int)(waves.getElapsedTime() / GameConfig.WAVE_LENGTH_SECONDS + 1);
        drawStatCard(engine, x, y, cw, ch, "WAVE", String.valueOf(waveNum), null);
        drawStatCard(engine, x + cw + gap, y, cw, ch, "SCORE", String.valueOf(score.getScore()), null);

        y += ch + gap;
        drawStatCard(engine, x, y, cw, ch, "STAGE", waves.getStage() + "  " + difficulty, null);
        drawStatCard(engine, x + cw + gap, y, cw, ch, "TIME", formatTime(waves.getElapsedTime()), null);
    }

    // ================================================================
    //  TOWER BUTTONS
    // ================================================================

    private void drawButtons(GameEngine engine, BuildingType selected, int mouseX, int mouseY) {
        int x = GameConfig.WINDOW_WIDTH - GameConfig.HUD_WIDTH + 20;
        int y = 274;
        int btnW = 280;
        int btnH = 44;
        int gap = 4;

        for (Button button : buttons) {
            button.setSize(btnW, btnH);
            button.setPosition(x, y);
            drawTowerBtn(engine, button, selected, mouseX, mouseY);
            y += btnH + gap;
        }
    }

    private void drawTowerBtn(GameEngine engine, Button btn, BuildingType selected, int mouseX, int mouseY) {
        int x = btn.getX(), y = btn.getY(), w = btn.getWidth(), h = btn.getHeight();
        BuildingType type = btn.getBuildingType();
        Color accent = btn.getColor();
        if (accent == null) accent = new Color(120, 125, 130);
        boolean on = btn.contains(mouseX, mouseY);
        boolean sel = (type == selected);

        // Glass fill
        if (sel) {
            engine.changeColor(new Color(accent.getRed() / 4, accent.getGreen() / 4, accent.getBlue() / 4, 200));
        } else if (on) {
            engine.changeColor(new Color(28, 36, 46, 200));
        } else {
            engine.changeColor(new Color(16, 20, 28, 160));
        }
        engine.drawSolidRectangle(x, y, w, h);

        // Left accent bar
        engine.changeColor(sel ? accent : on ? new Color(accent.getRed() / 2, accent.getGreen() / 2, accent.getBlue() / 2)
                : new Color(accent.getRed() / 3, accent.getGreen() / 3, accent.getBlue() / 3, 140));
        engine.drawSolidRectangle(x, y, 3, h);

        // Border
        engine.changeColor(sel ? accent : on ? new Color(55, 60, 68)
                : new Color(38, 42, 48, 100));
        engine.drawRectangle(x, y, w, h);

        // Tower icon
        Image icon = getTowerIcon(type);
        int iconSize = 42;
        int iconX = x + 8;
        int iconY = y + (h - iconSize) / 2;
        if (icon != null) {
            engine.drawImage(icon, iconX, iconY, iconSize, iconSize);
        }

        // Text
        engine.changeColor(sel ? Color.WHITE : on ? new Color(220, 225, 230) : MUTED);
        String text = btn.getText();
        int textX = iconX + iconSize + 10;
        engine.drawBoldText(textX, y + h / 2 + 5, text, "Arial", 15);
    }

    private Image getTowerIcon(BuildingType type) {
        switch (type) {
            case ARROW_TOWER:    return ImageManager.getArrowTower();
            case CANNON_TOWER:   return ImageManager.getAttackTower();
            case ICE_TOWER:      return ImageManager.getIceTower();
            case LIGHTNING_TOWER:return ImageManager.getLightningTower();
            case HEAL_TOWER:     return ImageManager.getHealTower();
            case DECOY:          return ImageManager.getBait();
            default:             return null;
        }
    }

    // ================================================================
    //  HINTS
    // ================================================================

    private void drawHints(GameEngine engine) {
        int x = GameConfig.WINDOW_WIDTH - GameConfig.HUD_WIDTH + 15;
        int y = GameConfig.WINDOW_HEIGHT - 100;
        int w = 280;
        int hh = 26;
        int gap = 32;
        int fontSize = 14;

        // Sell hint
        engine.changeColor(new Color(16, 22, 28, 180));
        engine.drawSolidRectangle(x, y, w, hh);
        engine.changeColor(new Color(38, 43, 50, 80));
        engine.drawRectangle(x, y, w, hh);
        engine.changeColor(MUTED);
        engine.drawText(x + 8, y + 18, "Right-click to sell (60%)", "Arial", fontSize);

        // Pause hint - blinking
        y += gap;
        if (((int)(gameTime / 0.6)) % 2 == 0) {
            engine.changeColor(new Color(16, 22, 28, 180));
            engine.drawSolidRectangle(x, y, w, hh);
            engine.changeColor(new Color(38, 43, 50, 80));
            engine.drawRectangle(x, y, w, hh);
            engine.changeColor(GOLD);
            engine.drawBoldText(x + 8, y + 18, "SPACE to pause", "Arial", fontSize);
        }

        // Speed hint
        y += gap;
        engine.changeColor(new Color(16, 22, 28, 180));
        engine.drawSolidRectangle(x, y, w, hh);
        engine.changeColor(new Color(38, 43, 50, 80));
        engine.drawRectangle(x, y, w, hh);
        engine.changeColor(new Color(120, 180, 230));
        engine.drawBoldText(x + 8, y + 18, "F - 2x speed", "Arial", fontSize);
    }

    // ================================================================
    //  STATE OVERLAY
    // ================================================================

    private void drawStateOverlay(GameEngine engine, GameState state, int mouseX, int mouseY) {
        if (state == GameState.PAUSED) {
            drawPauseMenu(engine, mouseX, mouseY);
        } else if (state == GameState.GAME_OVER_EFFECT) {
            drawMessage(engine, "THE FORTRESS FALLS", new Color(220, 50, 50));
        } else if (state == GameState.WIN_EFFECT) {
            drawMessage(engine, "VICTORY!", new Color(255, 215, 0));
        }
    }

    private void drawPauseMenu(GameEngine engine, int mouseX, int mouseY) {
        int pw = 320, ph = 280;
        int px = (GameConfig.WINDOW_WIDTH - GameConfig.HUD_WIDTH - pw) / 2;
        int py = (GameConfig.WINDOW_HEIGHT - ph) / 2;

        // Dark overlay
        engine.changeColor(new Color(0, 0, 0, 150));
        engine.drawSolidRectangle(0, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);

        // Glass panel
        engine.changeColor(new Color(12, 16, 24, 235));
        engine.drawSolidRectangle(px, py, pw, ph);
        engine.changeColor(new Color(42, 48, 56, 150));
        engine.drawRectangle(px, py, pw, ph);

        // Corner accents
        int c = 24;
        engine.changeColor(new Color(GOLD_DIM.getRed(), GOLD_DIM.getGreen(), GOLD_DIM.getBlue(), 100));
        engine.drawLine(px, py, px + c, py);   engine.drawLine(px, py, px, py + c);
        engine.drawLine(px + pw, py, px + pw - c, py);   engine.drawLine(px + pw, py, px + pw, py + c);
        engine.drawLine(px, py + ph, px + c, py + ph);   engine.drawLine(px, py + ph, px, py + ph - c);
        engine.drawLine(px + pw, py + ph, px + pw - c, py + ph);   engine.drawLine(px + pw, py + ph, px + pw, py + ph - c);

        // Title
        String title = "PAUSED";
        int ttw = engine.textWidth(title, "Georgia", 26);
        engine.changeColor(new Color(0, 0, 0, 100));
        engine.drawBoldText(px + (pw - ttw) / 2 + 2, py + 38, title, "Georgia", 26);
        engine.changeColor(GOLD);
        engine.drawBoldText(px + (pw - ttw) / 2, py + 36, title, "Georgia", 26);

        // Gold underline
        engine.changeColor(new Color(GOLD_DIM.getRed(), GOLD_DIM.getGreen(), GOLD_DIM.getBlue(), 60));
        engine.drawLine(px + 40, py + 46, px + pw - 40, py + 46);

        // Buttons
        int btnW = 240, btnH = 32, btnGap = 6;
        int bx = px + (pw - btnW) / 2;
        int by = py + 60;

        resumeButton.setSize(btnW, btnH);
        resumeButton.setPosition(bx, by);
        drawPauseBtn(engine, resumeButton, mouseX, mouseY);

        saveButton.setSize(btnW, btnH);
        saveButton.setPosition(bx, by + (btnH + btnGap));
        drawPauseBtn(engine, saveButton, mouseX, mouseY);

        deleteSaveBtn.setSize(btnW, btnH);
        deleteSaveBtn.setPosition(bx, by + 2 * (btnH + btnGap));
        drawPauseBtn(engine, deleteSaveBtn, mouseX, mouseY);

        restartBtn.setSize(btnW, btnH);
        restartBtn.setPosition(bx, by + 3 * (btnH + btnGap));
        drawPauseBtn(engine, restartBtn, mouseX, mouseY);

        menuBtn.setSize(btnW, btnH);
        menuBtn.setPosition(bx, by + 4 * (btnH + btnGap));
        drawPauseBtn(engine, menuBtn, mouseX, mouseY);
    }

    private void drawPauseBtn(GameEngine engine, Button btn, int mouseX, int mouseY) {
        int x = btn.getX(), y = btn.getY(), w = btn.getWidth(), h = btn.getHeight();
        Color accent = btn.getColor();
        boolean on = btn.contains(mouseX, mouseY);

        engine.changeColor(on ? new Color(30, 38, 48, 210) : new Color(18, 24, 34, 160));
        engine.drawSolidRectangle(x, y, w, h);
        engine.changeColor(on ? new Color(accent.getRed() / 2, accent.getGreen() / 2, accent.getBlue() / 2)
                : new Color(accent.getRed() / 3, accent.getGreen() / 3, accent.getBlue() / 3, 140));
        engine.drawSolidRectangle(x, y, 3, h);
        engine.changeColor(on ? new Color(50, 56, 64) : new Color(38, 43, 50, 100));
        engine.drawRectangle(x, y, w, h);
        engine.changeColor(on ? Color.WHITE : MUTED);

        String text = btn.getText();
        int tw = engine.textWidth(text, "Arial", 13);
        engine.drawBoldText(x + (w - tw) / 2, y + 22, text, "Arial", 13);
    }

    private void drawMessage(GameEngine engine, String text, Color color) {
        // Update animation timer
        endMessageTimer += 0.016; // Approximate frame time

        int cx = GameConfig.WINDOW_WIDTH / 2;
        int cy = GameConfig.WINDOW_HEIGHT / 2;

        // Animation phases
        double phase1 = Math.min(1.0, endMessageTimer / 0.5); // Zoom in (0-0.5s)
        double phase2 = Math.min(1.0, Math.max(0.0, (endMessageTimer - 0.5) / 0.3)); // Shake (0.5-0.8s)
        double phase3 = Math.min(1.0, Math.max(0.0, (endMessageTimer - 0.8) / 0.5)); // Glow pulse (0.8s+)

        // Zoom effect
        double scale = 0.3 + phase1 * 0.7;

        // Shake effect
        double shakeX = 0;
        double shakeY = 0;
        if (phase2 > 0 && phase2 < 1.0) {
            shakeX = Math.sin(endMessageTimer * 50) * 8 * (1.0 - phase2);
            shakeY = Math.cos(endMessageTimer * 47) * 6 * (1.0 - phase2);
        }

        // Pulsing glow
        double pulse = Math.sin(endMessageTimer * 4) * 0.3 + 0.7;

        // Calculate font size with scale
        int baseFontSize = 48;
        int fontSize = (int)(baseFontSize * scale);

        int approxW = text.length() * fontSize * 3 / 5;
        // Draw multiple layers for glow effect
        if (phase3 > 0) {
            // Outer glow layers
            for (int i = 5; i > 0; i--) {
                int alpha = (int)(30 * pulse * phase3 / i);
                engine.changeColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
                int offset = i * 3;
                engine.drawBoldText(cx - approxW / 2 + shakeX - offset, cy + fontSize / 3 + shakeY - offset, text, "Georgia", fontSize);
                engine.drawBoldText(cx - approxW / 2 + shakeX + offset, cy + fontSize / 3 + shakeY - offset, text, "Georgia", fontSize);
                engine.drawBoldText(cx - approxW / 2 + shakeX - offset, cy + fontSize / 3 + shakeY + offset, text, "Georgia", fontSize);
                engine.drawBoldText(cx - approxW / 2 + shakeX + offset, cy + fontSize / 3 + shakeY + offset, text, "Georgia", fontSize);
            }
        }

        // Main shadow
        engine.changeColor(new Color(0, 0, 0, (int)(200 * phase1)));
        engine.drawBoldText(cx - approxW / 2 + shakeX + 4, cy + fontSize / 3 + shakeY + 4, text, "Georgia", fontSize);

        // Main text with color
        Color mainColor = new Color(
            Math.min(255, (int)(color.getRed() * (0.8 + pulse * 0.2))),
            Math.min(255, (int)(color.getGreen() * (0.8 + pulse * 0.2))),
            Math.min(255, (int)(color.getBlue() * (0.8 + pulse * 0.2))),
            (int)(255 * phase1)
        );
        engine.changeColor(mainColor);
        engine.drawBoldText(cx - approxW / 2 + shakeX, cy + fontSize / 3 + shakeY, text, "Georgia", fontSize);

        // Bright highlight on top
        if (phase3 > 0) {
            engine.changeColor(new Color(255, 255, 255, (int)(150 * pulse * phase3)));
            engine.drawBoldText(cx - approxW / 2 + shakeX, cy + fontSize / 3 + shakeY - 2, text, "Georgia", fontSize);
        }
    }

    // ================================================================
    //  PAUSE CLICK HANDLING
    // ================================================================

    public int handlePauseClick(int mouseX, int mouseY) {
        if (resumeButton.contains(mouseX, mouseY)) return PAUSE_RESUME;
        if (saveButton.contains(mouseX, mouseY)) return PAUSE_SAVE;
        if (deleteSaveBtn.contains(mouseX, mouseY)) return PAUSE_DELETE_SAVE;
        if (restartBtn.contains(mouseX, mouseY)) return PAUSE_RESTART;
        if (menuBtn.contains(mouseX, mouseY)) return PAUSE_MENU;
        return 0;
    }

    // ================================================================
    //  UTILITY
    // ================================================================

    private String formatTime(double seconds) {
        int sec = (int) seconds;
        return String.format("%02d:%02d", sec / 60, sec % 60);
    }

    // ================================================================
    //  BOSS WARNING
    // ================================================================

    private void drawBossWarning(GameEngine engine, WaveManager waves) {
        int cx = (GameConfig.WINDOW_WIDTH - GameConfig.HUD_WIDTH) / 2;
        boolean visible = (System.currentTimeMillis() / 500) % 2 == 0;
        if (waves.isEliteWave() && !waves.isBossActive() && visible) {
            int boxW = 200, boxH = 36;
            int bx = cx - boxW / 2, by = 80;
            engine.changeColor(new Color(120, 40, 160, 200));
            engine.drawSolidRectangle(bx, by, boxW, boxH);
            engine.changeColor(new Color(180, 90, 220));
            engine.drawRectangle(bx, by, boxW, boxH, 2);
            engine.changeColor(new Color(230, 200, 255));
            int tw = engine.textWidth("!! ELITE WAVE !!", "Arial", 16);
            engine.drawBoldText(cx - tw / 2, by + 24, "!! ELITE WAVE !!", "Arial", 16);
        }
        if (waves.isBossActive() && visible) {
            int boxW = 260, boxH = 50;
            int bx = cx - boxW / 2, by = 80;
            engine.changeColor(new Color(180, 10, 10, 220));
            engine.drawSolidRectangle(bx, by, boxW, boxH);
            engine.changeColor(new Color(255, 50, 50));
            engine.drawRectangle(bx, by, boxW, boxH, 3);
            engine.changeColor(new Color(255, 180, 180));
            int tw = engine.textWidth("!! BOSS !!", "Arial", 22);
            engine.drawBoldText(cx - tw / 2, by + 32, "!! BOSS !!", "Arial", 22);
        }
    }

    // ================================================================
    //  PREP TIMER
    // ================================================================

    private void drawPrepTimer(GameEngine engine, WaveManager waves) {
        if (!waves.isPrepTime()) return;
        int cx = (GameConfig.WINDOW_WIDTH - GameConfig.HUD_WIDTH) / 2;
        int remaining = (int) Math.ceil(waves.getPrepTimer());
        int boxW = 240, boxH = 50;
        int bx = cx - boxW / 2, by = 40;

        engine.changeColor(new Color(10, 14, 20, 200));
        engine.drawSolidRectangle(bx, by, boxW, boxH);
        engine.changeColor(GOLD);
        engine.drawRectangle(bx, by, boxW, boxH, 2);

        String text = "Wave starts in " + remaining;
        int tw = engine.textWidth(text, "Arial", 18);
        engine.changeColor(GOLD);
        engine.drawBoldText(cx - tw / 2, by + 32, text, "Arial", 18);

        // Progress bar
        engine.changeColor(new Color(40, 46, 54));
        engine.drawSolidRectangle(bx + 10, by + boxH - 10, boxW - 20, 5);
        double ratio = waves.getPrepTimer() / 5.0;
        engine.changeColor(GOLD);
        engine.drawSolidRectangle(bx + 10, by + boxH - 10, (int) ((boxW - 20) * ratio), 5);
    }

    // ================================================================
    //  SPEED INDICATOR
    // ================================================================

    private void drawSpeedIndicator(GameEngine engine, double speedMul) {
        if (speedMul != 2.0) return;
        int x = GameConfig.WINDOW_WIDTH - GameConfig.HUD_WIDTH - 22, y = 4;
        int w = 26, h = 26;

        engine.changeColor(new Color(0, 0, 0, 180));
        engine.drawSolidRectangle(x, y, w, h);
        engine.changeColor(GOLD);
        engine.drawRectangle(x, y, w, h, 2);
        int tw = engine.textWidth("2x", "Arial", 14);
        engine.drawBoldText(x + (w - tw) / 2, y + 19, "2x", "Arial", 14);
    }

    // ================================================================
    //  TOWER TOOLTIP
    // ================================================================

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

    private void drawTowerTooltip(GameEngine engine, int mouseX, int mouseY, Difficulty difficulty) {
        BuildingType type = getHoveredBuildingType(mouseX, mouseY);
        if (type == null) return;
        int tw = 180, th = 100;
        int panelEdge = GameConfig.WINDOW_WIDTH - GameConfig.HUD_WIDTH;
        int tx = mouseX - tw - 8;
        int ty = mouseY - 50;
        if (tx < panelEdge - tw - 8) tx = mouseX + 16;
        if (ty < 0) ty = 4;
        if (ty + th > GameConfig.WINDOW_HEIGHT) ty = GameConfig.WINDOW_HEIGHT - th - 4;

        // Glass panel
        engine.changeColor(new Color(10, 14, 22, 240));
        engine.drawSolidRectangle(tx, ty, tw, th);
        engine.changeColor(new Color(45, 50, 58));
        engine.drawRectangle(tx, ty, tw, th);

        // Header
        engine.changeColor(new Color(24, 30, 38));
        engine.drawSolidRectangle(tx + 1, ty + 1, tw - 2, 22);

        String name = type.name().charAt(0) + type.name().substring(1).toLowerCase().replace("_", " ");
        engine.changeColor(GOLD);
        engine.drawBoldText(tx + 8, ty + 17, name, "Arial", 12);

        int ly = ty + 32;
        engine.changeColor(MUTED);
        engine.drawText(tx + 8, ly, "Cost", "Arial", 11);
        engine.changeColor(GOLD);
        engine.drawText(tx + 80, ly, "$" + getCost(type), "Arial", 11);

        engine.changeColor(MUTED);
        engine.drawText(tx + 8, ly + 16, "HP", "Arial", 11);
        engine.changeColor(new Color(100, 220, 130));
        engine.drawText(tx + 80, ly + 16, String.valueOf(getHp(type)), "Arial", 11);

        if (type != BuildingType.DECOY && type != BuildingType.HEAL_TOWER) {
            engine.changeColor(MUTED);
            engine.drawText(tx + 8, ly + 32, "Damage", "Arial", 11);
            engine.changeColor(new Color(255, 150, 100));
            engine.drawText(tx + 80, ly + 32, String.valueOf(getDamage(type)), "Arial", 11);

            engine.changeColor(MUTED);
            engine.drawText(tx + 8, ly + 48, "Range", "Arial", 11);
            engine.changeColor(Color.WHITE);
            engine.drawText(tx + 80, ly + 48, getRange(type) + " tiles", "Arial", 11);
        } else if (type == BuildingType.HEAL_TOWER) {
            engine.changeColor(MUTED);
            engine.drawText(tx + 8, ly + 32, "Heal", "Arial", 11);
            engine.changeColor(new Color(100, 220, 200));
            engine.drawText(tx + 80, ly + 32, "20 HP", "Arial", 11);

            engine.changeColor(MUTED);
            engine.drawText(tx + 8, ly + 48, "Range", "Arial", 11);
            engine.changeColor(Color.WHITE);
            engine.drawText(tx + 80, ly + 48, getRange(type) + " tiles", "Arial", 11);
        } else if (type == BuildingType.DECOY) {
            engine.changeColor(MUTED);
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
