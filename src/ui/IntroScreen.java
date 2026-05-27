package ui;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import game.CoreSiege;
import game.GameState;

/**
 * Simple beautiful intro screen.
 */
public class IntroScreen {

    // Story text
    private String[] story = {

            "Long before the fall of kingdoms...",

            "The Four Fronts protected mankind.",

            "But war shattered the alliance.",

            "Darkness rises once again.",

            "Only one fortress still stands.",

            "You are the final commander.",

            "Hold the line."
    };

    // Current line
    private int currentLine = 0;

    // Timing
    private long lastSwitchTime;

    private final int DISPLAY_TIME = 3500;

    // Background image
    private Image backgroundImage;

    /**
     * Constructor
     */
    public IntroScreen() {

        lastSwitchTime = System.currentTimeMillis();

        // Background image
        backgroundImage = Toolkit.getDefaultToolkit().getImage(
                "Images/background.png");
    }

    /**
     * Update intro
     */
    public void update(CoreSiege game) {

        long current = System.currentTimeMillis();

        // Switch story text
        if (current - lastSwitchTime > DISPLAY_TIME) {

            currentLine++;

            lastSwitchTime = current;
        }

        // End intro
        if (currentLine >= story.length) {

            game.setGameState(GameState.MENU);
        }
    }

    /**
     * Draw intro
     */
    public void draw(CoreSiege game) {

        // =====================================
        // Background image
        // =====================================

        game.drawImage(
                backgroundImage,
                0,
                0,
                1280,
                720);

        // =====================================
        // Story text
        // =====================================

        if (currentLine < story.length) {

            // Final line bigger
            if (currentLine >= 5) {

                // Shadow
                game.changeColor(new Color(
                        40,
                        40,
                        40));

                game.drawText(
                        370,
                        614,
                        story[currentLine],
                        "Georgia",
                        46);

                // Main text
                game.changeColor(new Color(
                        255,
                        230,
                        180));

                game.drawText(
                        366,
                        610,
                        story[currentLine],
                        "Georgia",
                        46);

            } else {

                // Shadow
                game.changeColor(new Color(
                        20,
                        20,
                        20));

                game.drawText(
                        250,
                        614,
                        story[currentLine],
                        "Georgia",
                        36);

                // Main text
                game.changeColor(Color.WHITE);

                game.drawText(
                        246,
                        610,
                        story[currentLine],
                        "Georgia",
                        36);
            }
        }
    }
}