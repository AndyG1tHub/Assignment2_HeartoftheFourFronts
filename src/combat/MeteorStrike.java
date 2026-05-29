package combat;

import java.awt.Color;
import java.util.List;

import core.GridPosition;
import core.GridMap;
import enemy.Enemy;
import building.Building;
import event.Disaster;
import event.EventType;
import game.GameConfig;
import game.GameEngine;
import manager.ImageManger;
import manager.SoundManager;

/** Warning marker followed by one burst of damage. */
public class MeteorStrike extends Disaster {
    private double warningTimer;
    private boolean struck;

    public MeteorStrike(GridPosition position) {
        super(EventType.METEOR_STRIKE, position, 1, 45, 2.5);
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        warningTimer += dt;
    }

    @Override
    public void applyEffect(List<Enemy> enemies, List<Building> buildings) {
        if (struck || warningTimer < 1.2) {
            return;
        }

        // Meteor instantly kills everything in 3x3 range
        for (Enemy enemy : enemies) {
            if (affects(enemy.getGridPosition())) {
                enemy.takeDamage(enemy.getMaxHp()); // Instant kill
            }
        }
        for (Building building : buildings) {
            if (affects(building.getPosition())) {
                building.takeDamage(building.getMaxHp()); // Instant destroy
            }
        }

        struck = true;
        SoundManager sm = SoundManager.getInstance();
        if (sm != null) sm.playMeteorDisaster();
    }

    @Override
    public void draw(GameEngine engine, GridMap map) {
        if (struck && ImageManger.getExplosionFireball() != null) {
            int size = (radius * 2 + 1) * GameConfig.TILE_SIZE;
            int x = map.tileCenterX(position) - size / 2;
            int y = map.tileCenterY(position) - size / 2;
            engine.drawImage(ImageManger.getExplosionFireball(), x, y, size, size);
            return;
        }
        Color color = struck ? new Color(255, 130, 45) : new Color(255, 230, 90);
        drawArea(engine, map, color);
    }
}
