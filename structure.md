# Project Structure

```
src/
├── Main.java                    # Entry point
├── building/
│   ├── Base.java                # Player base (HP, hitbox)
│   ├── Building.java            # Abstract building base
│   ├── BuildingFactory.java     # Factory for creating buildings
│   ├── BuildingType.java        # Enum of building types
│   ├── Decoy.java               # Decoy building (lures enemies)
│   ├── Wall.java                # Wall building (barrier)
│   └── tower/
│       ├── ArrowTower.java      # Fast, cheap tower
│       ├── CannonTower.java     # High damage, slow tower
│       ├── HealTower.java       # Heals nearby buildings
│       ├── IceTower.java        # Slows enemies
│       └── LightningTower.java  # AoE damage tower
├── combat/
│   └── ProjectileManager.java   # Manages projectile updates + rendering
├── core/
│   ├── GridMap.java             # 20x20 grid, obstacles, drawing
│   ├── GridPosition.java        # Row/col pair
│   ├── PathFinder.java          # BFS pathfinding
│   ├── Tile.java                # Cell with terrain type + building
│   └── TileType.java            # Enum: EMPTY, OBSTACLE, BASE, BUILDING
├── effect/
│   ├── ParticleSystem.java      # Visual particle effects
│   └── RewardPoint.java         # Collectible reward drops
├── enemy/
│   ├── Enemy.java               # Base enemy (stats, movement, combat)
│   ├── EnemyAI.java             # Decision-making (pathfinding, targeting)
│   ├── EnemyFactory.java        # Creates enemies by type + difficulty
│   ├── EnemySpawner.java        # Spawns enemies from edges
│   ├── EnemyState.java          # Enum: SPAWNING, MOVING, ATTACKING, etc.
│   ├── EnemyType.java           # Enum of enemy types
│   └── enemies/
│       ├── ArcherEnemy.java     # Ranged attacker
│       ├── AssassinEnemy.java   # Fast, high reward
│       ├── HealerEnemy.java     # Heals nearby allies
│       ├── MeleeEnemy.java      # Basic melee
│       └── TankEnemy.java       # High HP, slow
├── event/
│   └── EventManager.java        # Random in-game events (disasters)
├── game/
│   ├── CoreSiege.java           # Main game loop, state management, save/load
│   ├── Difficulty.java          # Enum: EASY, NORMAL, HARD
│   ├── GameConfig.java          # All balance constants + layout config
│   ├── GameEngine.java          # Swing window, rendering, input, audio
│   └── GameState.java           # Enum: INTRO, MENU, PLAYING, PAUSED, etc.
├── manager/
│   ├── DecoyManager.java        # Decoy lifecycle
│   ├── DifficultyManager.java   # Difficulty modifiers
│   ├── EconomyManager.java      # Money tracking
│   ├── ImageManger.java         # Loads + serves all sprites
│   ├── ScoreManager.java        # Score/kills tracking
│   ├── SoundManager.java        # Preloaded audio clips
│   └── WaveManager.java         # Wave progression + timing
├── ui/
│   ├── Button.java              # Clickable UI button
│   ├── HUD.java                 # In-game stats, building buttons, tooltips
│   ├── IntroScreen.java         # Opening cutscene (Space to skip)
│   └── MenuScreen.java          # Main menu, help screen, end screen
└── util/
    └── EventSoundUtil.java      # Helper for event sounds
Images/                          # Sprite assets (towers, enemies, UI, map)
sounds/                          # WAV audio files
```

# Key Gameplay Features

| Feature | Details |
|---------|---------|
| **Controls** | 1-7 select building, click to place, right-click to sell (60% refund) |
| **Wall Limit** | Easy: unlimited, Normal: 16, Hard: 8 |
| **Save/Load** | Auto-deleted on win or game over |
| **Intro** | Press Space to skip |
| **Obstacles** | Random trees/fire; validated so all edge spawns have a path to base |
| **Adaptive UI** | Window resizable (min 640×480), map grid auto-centers |
