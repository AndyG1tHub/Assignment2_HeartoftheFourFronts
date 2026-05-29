package combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import building.Building;
import core.GridPosition;
import game.GameConfig;
import game.GameEngine;

/** Epic boss laser inspired by PvZ Zomboss — charges with energy buildup,
 *  then fires a devastating beam from the staff that obliterates towers! */
public class BossLaser {
    public static final double FIRE_INTERVAL = 5.0;
    private static final double CHARGE_TIME = 0.8;   // dramatic charge-up
    private static final double BEAM_TIME = 1.5;      // intense beam duration
    public static final double TOTAL_DURATION = CHARGE_TIME + BEAM_TIME;

    private final boolean horizontal;
    private final int fixedRowOrCol;
    private final GridPosition bossPos;
    private double elapsed;
    private boolean beamActive;
    private boolean finished;
    private final List<Building> targets;
    private final List<LaserParticle> particles;
    private final List<EnergyOrb> chargeOrbs;
    private final List<ShockWave> shockWaves;
    private double screenShake;
    private double beamWidth;

    public BossLaser(GridPosition bossPos, List<Building> buildings) {
        horizontal = Math.random() < 0.5;
        fixedRowOrCol = horizontal ? bossPos.row : bossPos.col;
        this.bossPos = bossPos;
        elapsed = 0;
        beamActive = false;
        finished = false;
        particles = new ArrayList<LaserParticle>();
        chargeOrbs = new ArrayList<EnergyOrb>();
        shockWaves = new ArrayList<ShockWave>();
        screenShake = 0;
        beamWidth = 0;

        targets = new ArrayList<Building>();
        for (Building b : buildings) {
            if (b.isDestroyed()) continue;
            GridPosition bp = b.getPosition();
            boolean hit = horizontal ? (bp.row == fixedRowOrCol) : (bp.col == fixedRowOrCol);
            if (hit) {
                targets.add(b);
            }
        }

        // Spawn initial charge orbs
        for (int i = 0; i < 12; i++) {
            chargeOrbs.add(new EnergyOrb(bossPos));
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
            beamWidth = 0;
            // Create massive shockwave on beam fire
            shockWaves.add(new ShockWave(bossPos, 0));
            screenShake = 0.3;
        }

        if (beamActive) {
            double beamElapsed = elapsed - CHARGE_TIME;
            double remaining = BEAM_TIME - beamElapsed;
            if (remaining <= 0) {
                finished = true;
                return;
            }

            // Beam width grows then shrinks
            double widthProgress = beamElapsed / BEAM_TIME;
            if (widthProgress < 0.2) {
                beamWidth = widthProgress / 0.2;
            } else if (widthProgress > 0.8) {
                beamWidth = (1.0 - widthProgress) / 0.2;
            } else {
                beamWidth = 1.0;
            }

            applyDamage(remaining, dt);
            spawnParticles(dt);
            screenShake = Math.max(0, screenShake - dt * 0.8);
        } else {
            // Charging phase
            updateChargeOrbs(dt);
            if (elapsed > CHARGE_TIME * 0.5 && Math.random() < dt * 8) {
                chargeOrbs.add(new EnergyOrb(bossPos));
            }
        }

        updateParticles(dt);
        updateShockWaves(dt);
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
        int tileSize = GameConfig.TILE_SIZE;
        int gridW = GameConfig.GRID_COLS * tileSize;
        int gridH = GameConfig.GRID_ROWS * tileSize;
        int mapX = GameConfig.MAP_OFFSET_X;
        int mapY = GameConfig.MAP_OFFSET_Y;

        // Dense particle stream along beam
        double chance = 25.0 * dt * beamWidth;
        if (horizontal) {
            for (int i = 0; i < (int) (chance * GameConfig.GRID_COLS) + 1; i++) {
                double px = mapX + Math.random() * gridW;
                double py = mapY + fixedRowOrCol * tileSize + tileSize / 2 + (Math.random() - 0.5) * 20 * beamWidth;
                particles.add(new LaserParticle(px, py, true, beamWidth));
            }
        } else {
            for (int i = 0; i < (int) (chance * GameConfig.GRID_ROWS) + 1; i++) {
                double px = mapX + fixedRowOrCol * tileSize + tileSize / 2 + (Math.random() - 0.5) * 20 * beamWidth;
                double py = mapY + Math.random() * gridH;
                particles.add(new LaserParticle(px, py, false, beamWidth));
            }
        }

        // Explosive sparks at tower hits
        for (Building b : targets) {
            if (b.isDestroyed()) continue;
            GridPosition bp = b.getPosition();
            double tx = mapX + bp.col * tileSize + tileSize / 2;
            double ty = mapY + bp.row * tileSize + tileSize / 2;
            if (Math.random() < dt * 15.0) {
                for (int i = 0; i < 8; i++) {
                    particles.add(new LaserParticle(tx, ty, false, beamWidth));
                }
                // Add shockwave at tower
                if (Math.random() < dt * 3.0) {
                    shockWaves.add(new ShockWave(bp, 0.3));
                }
            }
        }
    }

    private void updateChargeOrbs(double dt) {
        Iterator<EnergyOrb> it = chargeOrbs.iterator();
        while (it.hasNext()) {
            EnergyOrb orb = it.next();
            orb.update(dt);
            if (orb.dead) it.remove();
        }
    }

    private void updateShockWaves(double dt) {
        Iterator<ShockWave> it = shockWaves.iterator();
        while (it.hasNext()) {
            ShockWave wave = it.next();
            wave.update(dt);
            if (wave.dead) it.remove();
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

        // Staff position (purple part of boss - offset upward/to side)
        int staffX = mapX + bossPos.col * tileSize + tileSize / 2;
        int staffY = mapY + bossPos.row * tileSize + tileSize / 2 - (int)(tileSize * 0.4);

        if (beamActive) {
            // EPIC BEAM FIRING!
            double beamElapsed = elapsed - CHARGE_TIME;
            double pulse = 0.8 + 0.2 * Math.sin(beamElapsed * 15.0);
            double widthMul = beamWidth * pulse;

            // Draw shockwaves first
            for (ShockWave wave : shockWaves) {
                wave.draw(engine);
            }

            // Draw particles behind beam
            for (LaserParticle p : particles) {
                p.draw(engine);
            }

            if (horizontal) {
                int y = mapY + fixedRowOrCol * tileSize + tileSize / 2;

                // Outer explosive glow
                engine.changeColor(new Color(160, 60, 255, (int)(60 * widthMul)));
                engine.drawLine(staffX, y, mapX + gridW, y, (float)(35 * widthMul));

                // Mid purple glow
                engine.changeColor(new Color(180, 80, 255, (int)(100 * widthMul)));
                engine.drawLine(staffX, y, mapX + gridW, y, (float)(22 * widthMul));

                // Bright purple core
                engine.changeColor(new Color(200, 120, 255, (int)(180 * widthMul)));
                engine.drawLine(staffX, y, mapX + gridW, y, (float)(12 * widthMul));

                // Inner bright beam
                engine.changeColor(new Color(230, 180, 255, (int)(220 * widthMul)));
                engine.drawLine(staffX, y, mapX + gridW, y, (float)(6 * widthMul));

                // White hot center
                engine.changeColor(new Color(255, 245, 255, (int)(200 * widthMul)));
                engine.drawLine(staffX, y, mapX + gridW, y, (float)(2 * widthMul));

                // Electric arcs along beam
                drawElectricArcs(engine, staffX, y, mapX + gridW, y, beamElapsed, widthMul);
            } else {
                int x = mapX + fixedRowOrCol * tileSize + tileSize / 2;

                // Outer explosive glow
                engine.changeColor(new Color(160, 60, 255, (int)(60 * widthMul)));
                engine.drawLine(x, staffY, x, mapY + gridH, (float)(35 * widthMul));

                // Mid purple glow
                engine.changeColor(new Color(180, 80, 255, (int)(100 * widthMul)));
                engine.drawLine(x, staffY, x, mapY + gridH, (float)(22 * widthMul));

                // Bright purple core
                engine.changeColor(new Color(200, 120, 255, (int)(180 * widthMul)));
                engine.drawLine(x, staffY, x, mapY + gridH, (float)(12 * widthMul));

                // Inner bright beam
                engine.changeColor(new Color(230, 180, 255, (int)(220 * widthMul)));
                engine.drawLine(x, staffY, x, mapY + gridH, (float)(6 * widthMul));

                // White hot center
                engine.changeColor(new Color(255, 245, 255, (int)(200 * widthMul)));
                engine.drawLine(x, staffY, x, mapY + gridH, (float)(2 * widthMul));

                // Electric arcs along beam
                drawElectricArcs(engine, x, staffY, x, mapY + gridH, beamElapsed, widthMul);
            }

            // Explosive impact effects at towers
            for (Building b : targets) {
                if (b.isDestroyed()) continue;
                GridPosition bp = b.getPosition();
                double tx = mapX + bp.col * tileSize + tileSize / 2;
                double ty = mapY + bp.row * tileSize + tileSize / 2;
                double hitPulse = 0.7 + 0.3 * Math.sin(beamElapsed * 20.0 + bp.row * 3.0 + bp.col * 2.0);

                // Outer explosion glow
                engine.changeColor(new Color(255, 80, 255, (int)(50 * hitPulse * widthMul)));
                engine.drawSolidCircle(tx, ty, tileSize * 0.9 * hitPulse);

                // Mid glow
                engine.changeColor(new Color(255, 150, 255, (int)(120 * hitPulse * widthMul)));
                engine.drawSolidCircle(tx, ty, tileSize * 0.6 * hitPulse);

                // Bright center
                engine.changeColor(new Color(255, 220, 255, (int)(200 * hitPulse * widthMul)));
                engine.drawSolidCircle(tx, ty, tileSize * 0.35 * hitPulse);

                // White core
                engine.changeColor(new Color(255, 255, 255, (int)(180 * hitPulse * widthMul)));
                engine.drawSolidCircle(tx, ty, tileSize * 0.15 * hitPulse);
            }

            // Staff emission point glow
            double staffPulse = 0.9 + 0.1 * Math.sin(beamElapsed * 25.0);
            engine.changeColor(new Color(200, 100, 255, (int)(150 * staffPulse * widthMul)));
            engine.drawSolidCircle(staffX, staffY, tileSize * 0.5 * staffPulse);
            engine.changeColor(new Color(255, 200, 255, (int)(220 * staffPulse * widthMul)));
            engine.drawSolidCircle(staffX, staffY, tileSize * 0.25 * staffPulse);

        } else {
            // CHARGING PHASE - Energy gathering at staff
            double chargeRatio = elapsed / CHARGE_TIME;
            double pulse = 0.6 + 0.4 * Math.sin(elapsed * 25.0);

            // Draw energy orbs converging to staff
            for (EnergyOrb orb : chargeOrbs) {
                orb.draw(engine, staffX, staffY);
            }

            // Pulsing charge glow at staff
            int alpha = (int) (200 * chargeRatio * pulse);
            double radius = tileSize * (0.4 + 0.3 * chargeRatio) * pulse;

            // Outer charge glow
            engine.changeColor(new Color(180, 80, 255, (int)(alpha * 0.5)));
            engine.drawSolidCircle(staffX, staffY, radius * 1.8);

            // Mid glow
            engine.changeColor(new Color(200, 120, 255, alpha));
            engine.drawSolidCircle(staffX, staffY, radius * 1.2);

            // Bright core
            engine.changeColor(new Color(230, 180, 255, (int)(alpha * 1.2)));
            engine.drawSolidCircle(staffX, staffY, radius * 0.7);

            // White center
            engine.changeColor(new Color(255, 240, 255, (int)(alpha * 0.9)));
            engine.drawSolidCircle(staffX, staffY, radius * 0.3);

            // Electric arcs during charge
            if (chargeRatio > 0.3) {
                drawChargeArcs(engine, staffX, staffY, chargeRatio, pulse);
            }

            // Warning indicator line
            if (chargeRatio > 0.5) {
                int warningAlpha = (int)(120 * (chargeRatio - 0.5) * 2.0 * pulse);
                engine.changeColor(new Color(255, 100, 100, warningAlpha));
                if (horizontal) {
                    int y = mapY + fixedRowOrCol * tileSize + tileSize / 2;
                    engine.drawLine(mapX, y, mapX + gridW, y, 2);
                } else {
                    int x = mapX + fixedRowOrCol * tileSize + tileSize / 2;
                    engine.drawLine(x, mapY, x, mapY + gridH, 2);
                }
            }
        }
    }

    private void drawElectricArcs(GameEngine engine, double x1, double y1, double x2, double y2, double time, double intensity) {
        // Draw electric arcs along the beam
        int numArcs = 3;
        for (int i = 0; i < numArcs; i++) {
            double t = (time * 3.0 + i * 0.7) % 1.0;
            double arcX = x1 + (x2 - x1) * t;
            double arcY = y1 + (y2 - y1) * t;
            double offset = Math.sin(time * 10.0 + i * 2.0) * 15 * intensity;

            if (Math.abs(x2 - x1) > Math.abs(y2 - y1)) {
                // Horizontal beam
                engine.changeColor(new Color(200, 150, 255, (int)(150 * intensity)));
                engine.drawLine(arcX, arcY, arcX + 10, arcY + offset, (float)(3 * intensity));
            } else {
                // Vertical beam
                engine.changeColor(new Color(200, 150, 255, (int)(150 * intensity)));
                engine.drawLine(arcX, arcY, arcX + offset, arcY + 10, (float)(3 * intensity));
            }
        }
    }

    private void drawChargeArcs(GameEngine engine, int cx, int cy, double chargeRatio, double pulse) {
        // Electric arcs during charging
        int numArcs = (int)(chargeRatio * 8);
        int tileSize = GameConfig.TILE_SIZE;
        for (int i = 0; i < numArcs; i++) {
            double angle = (elapsed * 5.0 + i * Math.PI * 2.0 / 8) % (Math.PI * 2);
            double dist = tileSize * (0.6 + 0.4 * pulse) * chargeRatio;
            double ex = cx + Math.cos(angle) * dist;
            double ey = cy + Math.sin(angle) * dist;

            int alpha = (int)(180 * chargeRatio * pulse);
            engine.changeColor(new Color(220, 160, 255, alpha));
            engine.drawLine(cx, cy, ex, ey, (float)(2 * pulse));
        }
    }

    /** Energy orb that spirals toward the staff during charge. */
    private static class EnergyOrb {
        double angle;
        double distance;
        double speed;
        double life;
        boolean dead;
        Color color;

        EnergyOrb(GridPosition bossPos) {
            this.angle = Math.random() * Math.PI * 2;
            this.distance = GameConfig.TILE_SIZE * (2.0 + Math.random() * 2.0);
            this.speed = 80 + Math.random() * 60;
            this.life = 1.0;
            this.dead = false;
            int r = 180 + (int)(Math.random() * 75);
            int g = 80 + (int)(Math.random() * 80);
            int b = 255;
            this.color = new Color(r, g, b);
        }

        void update(double dt) {
            distance -= speed * dt;
            angle += dt * 2.0;
            if (distance < 5) {
                dead = true;
            }
        }

        void draw(GameEngine engine, int staffX, int staffY) {
            double x = staffX + Math.cos(angle) * distance;
            double y = staffY + Math.sin(angle) * distance;
            double size = 3 + 2 * (1.0 - distance / (GameConfig.TILE_SIZE * 4));

            engine.changeColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 180));
            engine.drawSolidCircle(x, y, size);
            engine.changeColor(new Color(255, 240, 255, 220));
            engine.drawSolidCircle(x, y, size * 0.4);
        }
    }

    /** Expanding shockwave ring. */
    private static class ShockWave {
        GridPosition pos;
        double radius;
        double life;
        boolean dead;

        ShockWave(GridPosition pos, double delay) {
            this.pos = pos;
            this.radius = 0;
            this.life = 0.6 + delay;
            this.dead = false;
        }

        void update(double dt) {
            life -= dt;
            if (life <= 0) {
                dead = true;
                return;
            }
            radius += dt * GameConfig.TILE_SIZE * 4;
        }

        void draw(GameEngine engine) {
            int tileSize = GameConfig.TILE_SIZE;
            int mapX = GameConfig.MAP_OFFSET_X;
            int mapY = GameConfig.MAP_OFFSET_Y;
            double cx = mapX + pos.col * tileSize + tileSize / 2;
            double cy = mapY + pos.row * tileSize + tileSize / 2;

            double ratio = life / 0.6;
            int alpha = (int)(150 * ratio);

            engine.changeColor(new Color(200, 120, 255, alpha));
            engine.drawCircle(cx, cy, radius, (float)(4 * ratio));
            engine.changeColor(new Color(255, 200, 255, (int)(alpha * 0.6)));
            engine.drawCircle(cx, cy, radius * 0.9, (float)(2 * ratio));
        }
    }

    /** Laser spark particle with trail. */
    private static class LaserParticle {
        double x, y, vx, vy;
        double life;
        boolean dead;
        final boolean beamSpark;
        double size;

        LaserParticle(double x, double y, boolean beamSpark, double intensity) {
            this.x = x;
            this.y = y;
            this.beamSpark = beamSpark;
            if (beamSpark) {
                this.vx = (Math.random() - 0.5) * 60 * intensity;
                this.vy = (Math.random() - 0.5) * 15 * intensity;
            } else {
                double angle = Math.random() * Math.PI * 2;
                double speed = (Math.random() * 80 + 40) * intensity;
                this.vx = Math.cos(angle) * speed;
                this.vy = Math.sin(angle) * speed;
            }
            this.life = 0.4 + Math.random() * 0.6;
            this.size = (2 + Math.random() * 3) * intensity;
            this.dead = false;
        }

        void update(double dt) {
            life -= dt;
            if (life <= 0) { dead = true; return; }
            vy += 20 * dt;
            x += vx * dt;
            y += vy * dt;
            vx *= 0.95;
            vy *= 0.95;
        }

        void draw(GameEngine engine) {
            double ratio = life / 1.0;
            int alpha = (int) (240 * ratio);

            // Outer glow
            engine.changeColor(new Color(200, 100, 255, (int)(alpha * 0.5)));
            engine.drawSolidCircle(x, y, size * ratio * 1.5);

            // Mid glow
            engine.changeColor(new Color(230, 150, 255, alpha));
            engine.drawSolidCircle(x, y, size * ratio);

            // Bright center
            engine.changeColor(new Color(255, 230, 255, (int)(alpha * 0.8)));
            engine.drawSolidCircle(x, y, size * ratio * 0.4);
        }
    }
}
