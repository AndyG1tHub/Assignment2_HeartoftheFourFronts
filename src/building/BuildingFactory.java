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
        if (type == BuildingType.WALL) {
            return new Wall(position);
        }
        if (type == BuildingType.HEAL_TOWER) {
            return new HealTower(position);
        }
        return null;
    }
}
