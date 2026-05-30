/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import core.GridPosition;
import core.GridMap;
import building.Decoy;
import util.Direction;
import game.GameEngine;

/**
 * <p>Manages the active decoy building: placement validity, lifetime tracking, removal on expiry and draw logic.</p>
 */
public class DecoyManager {
    private final List<Decoy> decoys = new ArrayList<Decoy>();
    private final GridMap map;

    public DecoyManager(GridMap map) {
        this.map = map;
    }

    public void createDecoy(GridPosition start, Direction direction) {
        decoys.add(new Decoy(start, direction));
    }

    public void update(double dt) {
        Iterator<Decoy> iterator = decoys.iterator();
        while (iterator.hasNext()) {
            Decoy decoy = iterator.next();
            decoy.update(dt);
            if (decoy.isDestroyed() || decoy.isOutside(map)) {
                iterator.remove();
            }
        }
    }

    public Decoy findAttractingDecoy(GridPosition enemyPosition) {
        for (Decoy decoy : decoys) {
            if (!decoy.isDestroyed() && decoy.attracts(enemyPosition)) {
                return decoy;
            }
        }
        return null;
    }

    public void draw(GameEngine engine) {
        for (Decoy decoy : decoys) {
            decoy.draw(engine, map);
        }
    }
}
