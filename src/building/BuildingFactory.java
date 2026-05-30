/**
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)



 * Factory that creates building instances from a BuildingType value.
 * Hides construction details from the main game loop.
 * CoreSiege calls createBuilding() and gets a ready Building back.
 * Adding a new tower type only requires changes here and a new class.
 */
package building;

import core.GridPosition;
import building.tower.*;

/** Creates buildings and keeps construction details out of CoreSiege. */
public class BuildingFactory {
    public Building createBuilding(BuildingType type, GridPosition position) {
        if (type == BuildingType.ARROW_TOWER) {
            return new ArrowTower(position);
        }
        if (type == BuildingType.CANNON_TOWER) {
            return new CannonTower(position);
        }
        if (type == BuildingType.ICE_TOWER) {
            return new IceTower(position);
        }
        if (type == BuildingType.LIGHTNING_TOWER) {
            return new LightningTower(position);
        }
        if (type == BuildingType.HEAL_TOWER) {
            return new HealTower(position);
        }
        return null;
    }
}
