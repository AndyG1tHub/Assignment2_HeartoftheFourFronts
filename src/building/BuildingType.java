/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

/**
 * Enumeration of all player-buildable structures.
 * Used by HUD for the build menu and by BuildingFactory for instantiation.
 * Adding a new value triggers compile errors on every switch statement.
 */
package building;

/** Build menu choices and building categories. */
public enum BuildingType {
    ARROW_TOWER,
    CANNON_TOWER,
    ICE_TOWER,
    LIGHTNING_TOWER,
    HEAL_TOWER,
    DECOY
}
