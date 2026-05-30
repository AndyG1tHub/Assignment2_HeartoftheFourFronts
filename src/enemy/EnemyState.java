package enemy;

/** Current tactical state of an enemy. */
public enum EnemyState {
    SPAWNING,
    MOVING,
    ATTACKING_BASE,
    ATTACKING_BUILDING,
    CHASING_DECOY,
    DEAD
}
