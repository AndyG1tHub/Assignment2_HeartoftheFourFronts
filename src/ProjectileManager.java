import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Updates and draws projectile visuals. */
public class ProjectileManager {
    private final List<Projectile> projectiles = new ArrayList<Projectile>();

    public void addProjectile(GridPosition start, GridPosition target, Color color) {
        projectiles.add(new Projectile(start, target, color));
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
