/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package combat;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.GridPosition;
import core.GridMap;
import game.GameEngine;

/** Updates and draws projectile visuals. */
public class ProjectileManager {
    private final List<Projectile> projectiles = new ArrayList<Projectile>();

    public void addProjectile(GridPosition start, GridPosition target, Color color) {
        projectiles.add(new Projectile(start, target, color));
    }

    public void addProjectile(GridPosition start, GridPosition target, Color color, Runnable onHitCallback) {
        projectiles.add(new Projectile(start, target, color, onHitCallback));
    }

    public void update(double dt) {
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            projectile.update(dt);
            if (projectile.isFinished()) {
                iterator.remove();
            }
        }
    }

    public void draw(GameEngine engine, GridMap map) {
        for (Projectile projectile : projectiles) {
            projectile.draw(engine, map);
        }
    }
}
