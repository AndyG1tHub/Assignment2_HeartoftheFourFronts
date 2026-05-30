/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package enemy;

/**
 * <p>Enumeration of enemy behaviour states. Includes MOVING, ATTACKING_BASE, ATTACKING_BUILDING, CHASING_DECOY and other state constants.</p>
 */
public enum EnemyState {
    SPAWNING,
    MOVING,
    ATTACKING_BASE,
    ATTACKING_BUILDING,
    CHASING_DECOY,
    DEAD
}
