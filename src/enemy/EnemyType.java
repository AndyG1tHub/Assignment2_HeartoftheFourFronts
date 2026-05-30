/**
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)


 * Enemy archetypes used by EnemyFactory and wave spawning.
 * MELEE: balanced unit; TANK: slow meat-shield; ASSASSIN: fast glass cannon.
 * ARCHER: ranged dealer; BOSS: level-end boss with laser; ELITE: mini-boss.
 */
package enemy;

/** Enemy archetypes used by EnemyFactory and the wave spawner. */
public enum EnemyType {
    MELEE,
    TANK,
    ASSASSIN,
    ARCHER,
    BOSS,
    ELITE
}
