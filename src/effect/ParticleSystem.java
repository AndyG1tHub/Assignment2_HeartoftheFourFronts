/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

/**
 * Central manager for visual feedback particles and floating text.
 * spawnEnemyDeath() creates a red/gold burst on enemy death.
 * spawnBuild() creates a green ring with "BUILD" floating text.
 * spawnUpgrade() creates a blue burst with "UPGRADE" floating text.
 * spawnScoreText() and spawnRewardText() show point and money gains.
 * Each frame updates all active particles and texts, removing expired ones.
 */
package effect;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import core.GridMap;
import core.GridPosition;
import game.GameEngine;
import game.GameConfig;

/** Owns lightweight particles used by combat and building feedback. */
public class ParticleSystem {
    private final List<Particle> particles = new ArrayList<Particle>();
    private final List<FloatingText> floatingTexts = new ArrayList<FloatingText>();
    private final Random random = new Random();

    public void spawn(double x, double y, Color color) {
        particles.add(new Particle(x, y, 0.5, color));
    }

    public void spawnEnemyDeath(GridMap map, GridPosition position) {
        spawnBurst(map.tileCenterX(position), map.tileCenterY(position), 22,
                new Color(220, 75, 85), new Color(255, 205, 80), 145.0, 0.65);
    }

    public void spawnBuild(GridMap map, GridPosition position) {
        spawnRing(map.tileCenterX(position), map.tileCenterY(position), 16,
                new Color(90, 210, 125), new Color(235, 205, 110), 90.0, 0.45);
        spawnText(map, position, "BUILD", new Color(115, 235, 145));
    }

    public void spawnUpgrade(GridMap map, GridPosition position) {
        spawnBurst(map.tileCenterX(position), map.tileCenterY(position), 24,
                new Color(120, 190, 255), new Color(255, 230, 120), 120.0, 0.55);
        spawnText(map, position, "UPGRADE", new Color(130, 205, 255));
    }

    public void spawnScoreText(GridMap map, GridPosition position, int score) {
        spawnText(map, position, "+" + score + "SCORE", new Color(255, 220, 110));
    }

    public void spawnRewardText(GridMap map, GridPosition position, int money, int score) {
        spawnText(map, position, "+$" + money + " +" + score + "SCORE", new Color(255, 230, 90));
    }

    public void spawnText(GridMap map, GridPosition position, String text, Color color) {
        floatingTexts.add(new FloatingText(map.tileCenterX(position),
                map.tileCenterY(position) - GameConfig.TILE_SIZE * 0.45, text, color));
    }

    private void spawnBurst(double x, double y, int count, Color first, Color second,
            double speed, double life) {
        for (int i = 0; i < count; i++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double particleSpeed = speed * (0.35 + random.nextDouble() * 0.65);
            double vx = Math.cos(angle) * particleSpeed;
            double vy = Math.sin(angle) * particleSpeed - GameConfig.TILE_SIZE * 0.35;
            Color color = random.nextBoolean() ? first : second;
            double size = 3.0 + random.nextDouble() * 3.0;
            particles.add(new Particle(x, y, vx, vy, life, size, color));
        }
    }

    private void spawnRing(double x, double y, int count, Color first, Color second,
            double speed, double life) {
        for (int i = 0; i < count; i++) {
            double angle = Math.PI * 2.0 * i / count;
            double vx = Math.cos(angle) * speed;
            double vy = Math.sin(angle) * speed - GameConfig.TILE_SIZE * 0.15;
            Color color = (i % 2 == 0) ? first : second;
            particles.add(new Particle(x, y, vx, vy, life, 4.0, color));
        }
    }

    public void update(double dt) {
        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            particle.update(dt);
            if (particle.isFinished()) {
                iterator.remove();
            }
        }
        Iterator<FloatingText> textIterator = floatingTexts.iterator();
        while (textIterator.hasNext()) {
            FloatingText floatingText = textIterator.next();
            floatingText.update(dt);
            if (floatingText.isFinished()) {
                textIterator.remove();
            }
        }
    }

    public void draw(GameEngine engine) {
        for (Particle particle : particles) {
            particle.draw(engine);
        }
        for (FloatingText floatingText : floatingTexts) {
            floatingText.draw(engine);
        }
    }
}
