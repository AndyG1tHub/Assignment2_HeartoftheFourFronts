package combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import building.Building;
import core.GridPosition;
import game.GameConfig;
import game.GameEngine;

/** Purple boss laser — charges for 0.3s, then fires a horizontal or vertical beam
 *  that destroys towers in its path over 2 seconds. Includes particles and glow. */
public class BossLaser {
    public static final double FIRE_INTERVAL = 5.0;
    private static final double CHARGE_TIME = 0.3;   // short charge-up before beam
    private static final double BEAM_TIME = 2.0;      // beam active duration
    public static final double TOTAL_DURATION = CHARGE_TIME + BEAM_TIME;

    private final boolean horizontal;
    private final int fixedRowOrCol;
    private final GridPosition bossPos;
    private double elapsed;
    private boolean beamActive;
    private boolean finished;
    private final List<Building> targets;
    private final List<LaserParticle> particles;

    public BossLaser(GridPosition bossPos, List<Building> buildings) {
        horizontal = Math.random() < 0.5;
        fixedRowOrCol = horizontal ? bossPos.row : bossPos.col;
        this.bossPos = bossPos;
        elapsed = 0;
        beamActive = false;
        finished = false;
        particles = new ArrayList<LaserParticle>();

        targets = new ArrayList<Building>();
        for (Building b : buildings) {
            if (b.isDestroyed()) continue;
            GridPosition bp = b.getPosition();
            boolean hit = horizontal ? (bp.row == fixedRowOrCol) : (bp.col == fixedRowOrCol);
            if (hit) {
                targets.add(b);
            }
        }
    }

    public boolean isActive() {
        return !finished;
    }

    public boolean isBeamActive() {
        return beamActive;
    }

    public void update(double dt) {
        if (finished) return;
        elapsed += dt;

        if (!beamActive && elapsed >= CHARGE_TIME) {
            beamActive = true;
        }

        if (beamActive) {
            double beamElapsed = elapsed - CHARGE_TIME;
            double remaining = BEAM_TIME - beamElapsed;
            if (remaining <= 0) {
                finished = true;
                return;
            }
            applyDamage(remaining, dt);
            spawnParticles(dt);
        }

        updateParticles(dt);
    }

    private void applyDamage(double remaining, double dt) {
        double maxRatio = 0;
        for (Building b : targets) {
            if (!b.isDestroyed()) {
                double ratio = (double) b.getHp() / remaining;
                if (ratio > maxRatio) maxRatio = ratio;
            }
        }
        double frameDamage = maxRatio * dt;
        for (Building b : targets) {
            if (!b.isDestroyed()) {
                b.takeDamage((int) Math.ceil(frameDamage));
            }
        }
    }

    private void spawnParticles(double dt) {
        // Spawn particles along the beam at random positions
        int tileSize = GameConfig.TILE_SIZE;
        int gridW = GameConfig.GRID_COLS * tileSize;
        int gridH = GameConfig.GRID_ROWS * tileSize;
        int mapX = GameConfig.MAP_OFFSET_X;
        int mapY = GameConfig.MAP_OFFSET_Y;

        double chance = 8.0 * dt; // particles per second along beam
        if (horizontal) {
            for (int i = 0; i < (int) (chance * GameConfig.GRID_COLS) + 1; i++) {
                double px = mapX + Math.random() * gridW;
                double py = mapY + fixedRowOrCol * tileSize + tileSize / 2 + (Math.random() - 0.5) * 8;
                particles.add(new LaserParticle(px, py, true));
            }
        } else {
            for (int i = 0; i < (int) (chance * GameConfig.GRID_ROWS) + 1; i++) {
                double px = mapX + fixedRowOrCol * tileSize + tileSize / 2 + (Math.random() - 0.5) * 8;
                double py = mapY + Math.random() * gridH;
                particles.add(new LaserParticle(px, py, false));
            }
        }

        // Tower hit sparks
        for (Building b : targets) {
            if (b.isDestroyed()) continue;
            GridPosition bp = b.getPosition();
            double tx = mapX + bp.col * tileSize + tileSize / 2;
            double ty = mapY + bp.row * tileSize + tileSize / 2;
            if (Math.random() < dt * 5.0) {
                for (int i = 0; i < 3; i++) {
                    particles.add(new LaserParticle(tx, ty, false));
                }
            }
        }
    }

    private void updateParticles(double dt) {
        Iterator<LaserParticle> it = particles.iterator();
        while (it.hasNext()) {
            LaserParticle p = it.next();
            p.update(dt);
            if (p.dead) it.remove();
        }
    }

    public void draw(GameEngine engine) {
        if (finished) return;
        int tileSize = GameConfig.TILE_SIZE;
        int mapX = GameConfig.MAP_OFFSET_X;
        int mapY = GameConfig.MAP_OFFSET_Y;
        int gridW = GameConfig.GRID_COLS * tileSize;
        int gridH = GameConfig.GRID_ROWS * tileSize;

        // Draw particles behind beam
        for (LaserParticle p : particles) {
            p.draw(engine);
        }

        if (beamActive) {
            double beamElapsed = elapsed - CHARGE_TIME;
            double pulse = 0.75 + 0.25 * Math.sin(beamElapsed * 12.0);
            int coreAlpha = (int) (200 * pulse);
            int glowAlpha = (int) (90 * pulse);
            int outerAlpha = (int) (35 * pulse);

            if (horizontal) {
                int y = mapY + fixedRowOrCol * tileSize + tileSize / 2;
                // Outer glow
                engine.changeColor(new Color(180, 80, 255, outerAlpha));
                engine.drawLine(mapX, y, mapX + gridW, y, 16);
                // Mid glow
                engine.changeColor(new Color(200, 120, 255, glowAlpha));
                engine.drawLine(mapX, y, mapX + gridW, y, 8);
                // Core beam
                engine.changeColor(new Color(220, 180, 255, coreAlpha));
                engine.drawLine(mapX, y, mapX + gridW, y, 3);
                // White hot center
                engine.changeColor(new Color(255, 240, 255, (int)(140 * pulse)));
                engine.drawLine(mapX, y, mapX + gridW, y, 1);
            } else {
                int x = mapX + fixedRowOrCol * tileSize + tileSize / 2;
                // Outer glow
                engine.changeColor(new Color(180, 80, 255, outerAlpha));
                engine.drawLine(x, mapY, x, mapY + gridH, 16);
                // Mid glow
                engine.changeColor(new Color(200, 120, 255, glowAlpha));
                engine.drawLine(x, mapY, x, mapY + gridH, 8);
                // Core beam
                engine.changeColor(new Color(220, 180, 255, coreAlpha));
                engine.drawLine(x, mapY, x, mapY + gridH, 3);
                // White hot center
                engine.changeColor(new Color(255, 240, 255, (int)(140 * pulse)));
                engine.drawLine(x, mapY, x, mapY + gridH, 1);
            }

            // Tower hit glow
            for (Building b : targets) {
                if (b.isDestroyed()) continue;
                GridPosition bp = b.getPosition();
                double tx = mapX + bp.col * tileSize + tileSize / 2;
                double ty = mapY + bp.row * tileSize + tileSize / 2;
                double hitPulse = 0.6 + 0.4 * Math.sin(beamElapsed * 18.0 + bp.row * 2.0 + bp.col);
                engine.changeColor(new Color(255, 100, 255, (int)(80 * hitPulse)));
                engine.drawSolidCircle(tx, ty, tileSize * 0.5);
                engine.changeColor(new Color(255, 200, 255, (int)(150 * hitPulse)));
                engine.drawCircle(tx, ty, tileSize * 0.5, 2);
            }
        } else {
            // Charging glow at boss position
            double chargeRatio = elapsed / CHARGE_TIME;
            double pulse = 0.5 + 0.5 * Math.sin(elapsed * 20.0);
            int alpha = (int) (180 * chargeRatio * pulse);
            int bx = mapX + bossPos.col * tileSize + tileSize / 2;
            int by = mapY + bossPos.row * tileSize + tileSize / 2;
            double radius = tileSize * 0.6 * (0.8 + 0.2 * chargeRatio);

            engine.changeColor(new Color(200, 100, 255, alpha));
            engine.drawSolidCircle(bx, by, radius);
            engine.changeColor(new Color(255, 200, 255, (int)(alpha * 0.7)));
            engine.drawCircle(bx, by, radius * 1.3, 2);
        }
    }

    /** Tiny spark on the laser beam or tower hit location. */
    private static class LaserParticle {
        double x, y, vx, vy;
        double life;
        boolean dead;
        final boolean beamSpark;

        LaserParticle(double x, double y, boolean beamSpark) {
            this.x = x;
            this.y = y;
            this.beamSpark = beamSpark;
            if (beamSpark) {
                this.vx = (Math.random() - 0.5) * 30;
                this.vy = (Math.random() - 0.5) * 6;
            } else {
                double angle = Math.random() * Math.PI * 2;
                double speed = Math.random() * 50 + 20;
                this.vx = Math.cos(angle) * speed;
                this.vy = Math.sin(angle) * speed;
            }
            this.life = 0.3 + Math.random() * 0.5;
            this.dead = false;
        }

        void update(double dt) {
            life -= dt;
            if (life <= 0) { dead = true; return; }
            vy += 15 * dt; // slight gravity
            x += vx * dt;
            y += vy * dt;
        }

        void draw(GameEngine engine) {
            double ratio = life / 0.8;
            int alpha = (int) (220 * ratio);
            engine.changeColor(new Color(220, 140, 255, alpha));
            engine.drawSolidCircle(x, y, 2.5 * ratio);
            engine.changeColor(new Color(255, 220, 255, (int)(alpha * 0.6)));
            engine.drawSolidCircle(x, y, 1.2 * ratio);
        }
    }
}
