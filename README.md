# 🏰 Heart of the Four Fronts: The Last Defence

<div align="center">

**A multi-level tower defense game | The last line of defense for the Four Fronts fortress**

[![Java](https://img.shields.io/badge/Java-8+-orange.svg)](https://www.java.com/)

[Features](#-features) • [Quick Start](#-quick-start) • [Gameplay](#-gameplay) • [Level System](#-level-system) • [Towers](#-tower-codex) • [Enemy Codex](#-enemy-codex)

</div>

---

## 📖 Story

On the distant continent of Aetherlan, the Four Fronts fortress once stood as the final bastion against the dark legion. For hundreds of years, its guardians used ancient magic towers and tactical wisdom to repel invasions from every direction again and again.

Now, the Dark Lord has finally awakened. He has sent endless monster armies to attack from the northern icefields, eastern desert, southern swamps, and western forest at the same time. As the last guardian of the fortress, you must use wisdom and strategy to build defensive towers and repel wave after wave of enemies.

**Your mission: protect the fortress core until you defeat the Dark Lord's final boss!**

---

## ✨ Features

### 🎯 Strategic Depth
- **6 defense/tool choices**: from basic arrow towers to lightning towers and decoys, each option has a distinct role
- **Tower upgrade system**: each tower can be upgraded twice, greatly increasing combat power
- **Tactical decoys**: draw enemy attention and buy valuable time

### 🌊 Dynamic Difficulty
- **5 progressive levels**: from beginner training to the final boss battle
- **3 difficulty modes**: Easy, Normal, and Hard for different player needs
- **Stage system**: each level contains 5 internal stages, with difficulty increasing step by step
- **Adaptive Pressure AI**: dynamically adjusts spawn pressure based on base HP, gold, and kill efficiency

### 👹 Rich Enemy Variety
- **4 regular enemies**: melee soldiers, assassins, tanks, and archers each have their own traits
- **Elite waves**: powerful elites appear regularly and provide generous rewards
- **Boss battle**: the ultimate challenge with charge-up, warning lines, and full-row/full-column laser attacks

### 🎨 Visual Spectacle
- **Pixel-style assets**: enemies, towers, maps, obstacles, and UI use separate image assets
- **Combat effects**: boss lasers, laser particles, shockwaves, electric arcs, freeze effects, healing beams, and floating score text
- **Smooth animation**: a smooth 60 FPS experience with adaptive window sizing

### 💾 Complete Systems
- **Save system**: saves current level, difficulty, wave, base HP, gold, score, building states, and active enemies; projectiles/reward points/disasters are not written to the save file
- **Economy system**: earn gold by killing enemies and allocate resources wisely
- **Build preview**: shows buildable/unbuildable hints when hovering over the map
- **Sound system**: immersive background music and combat sound effects

---

## 🚀 Quick Start

### System Requirements
- **Java version**: JDK 8 or higher
- **Operating system**: Windows / macOS / Linux
- **Memory**: at least 512 MB RAM
- **Resolution**: minimum 640×480 (900×700 recommended)

### Compile and Run

#### Method 1: Run with the existing sources.txt
```bash
cd Assignment2_HeartoftheFourFronts
javac -encoding UTF-8 -d out "@sources.txt"
java -cp out Main
```

#### Method 2: Regenerate sources.txt and run
```bash
cd Assignment2_HeartoftheFourFronts
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out "@sources.txt"
java -cp out Main
```

#### Method 3: Use an IDE
1. Open the project with IntelliJ IDEA or Eclipse
2. Set the JDK version to 8+
3. Run `Main.java`

---

## 🎮 Gameplay

### Basic Controls

| Key | Function |
|------|------|
| **1-6** | Select tower type |
| **Left mouse button** | Place the selected tower |
| **Right mouse button** | Sell a tower (refunds 60% of the cost) |
| **Space** | Pause/resume the game |
| **Esc** | Return to the main menu |
| **M** | Mute/unmute |
| **F** | Fast-forward mode (2x speed) |

### Objectives

1. **Protect the fortress core**: prevent enemies from destroying your base (1500 HP)
2. **Defeat all enemies**:
   - Level 1-2: survive until Stage 5 and defeat the final elite wave
   - Level 3-5: defeat the Boss and clear all remaining enemies
3. **Allocate resources wisely**: use limited gold to build and upgrade towers
4. **Avoid base damage**: each 1 HP lost by the base deducts 1 point

### Game Flow

```
Start game → Select level → Select difficulty
    ↓
Stage 1-5 progress gradually (40 seconds each)
    ↓
Enemies surge in from all directions
    ↓
Build towers → Upgrade towers → Arrange tactics
    ↓
Defeat elites/Boss → Clear all enemies
    ↓
🏆 Victory! Unlock the next level
```

---

## 🗺️ Level System

The game contains **5 progressive levels**. Each level unlocks new defensive towers and presents different enemy combinations.

### Level 1: Beginner Trial 🌱
**Unlocked tower**: 🏹 Arrow Tower  
**Enemy composition**: 100% melee soldiers  
**Final challenge**: 💀 Final elite wave (3 elites appear at the same time)  
**Difficulty**: ⭐  
**Description**: Suitable for beginners learning the basic mechanics. Only melee enemies appear, and pressure is low.

---

### Level 2: Frost Trial ❄️
**Unlocked towers**: 🏹 Arrow Tower + ❄️ Ice Tower  
**Enemy composition**:
- 30% assassins (fast movement)
- 10% archers (ranged attacks)
- 60% melee soldiers

**Final challenge**: 💀 Final elite wave (4 elites)  
**Difficulty**: ⭐⭐  
**Description**: Introduces fast enemies and requires learning how to use Ice Towers to slow them down.

---

### Level 3: Trial by Cannon Fire 💣
**Unlocked towers**: 🏹 Arrow Tower + ❄️ Ice Tower + 💣 Cannon Tower  
**Enemy composition**:
- 25% tanks (high HP)
- 20% assassins
- 15% archers
- 40% melee soldiers

**Final challenge**: 👹 **Mini Boss** (1200 HP + laser attack)  
**Difficulty**: ⭐⭐⭐  
**Description**: The first boss battle. High-HP tanks are introduced, requiring Cannon Tower area damage.

---

### Level 4: Battle of Healing 💚
**Unlocked towers**: 🏹 Arrow Tower + ❄️ Ice Tower + 💣 Cannon Tower + 💚 Heal Tower  
**Enemy composition**:
- 22% tanks
- 20% assassins
- 18% archers
- 40% melee soldiers

**Final challenge**: 👹 **Standard Boss** (1600 HP + laser attack)  
**Difficulty**: ⭐⭐⭐⭐  
**Description**: Pressure increases, and Heal Towers are needed to maintain the defensive line.

---

### Level 5: Final Showdown ⚡
**Unlocked towers**: all 6 tower types + 🎯 Decoy  
**Enemy composition**:
- 20% tanks
- 20% assassins
- 20% archers
- 40% melee soldiers

**Final challenge**: 👹 **Final Boss** (2000 HP + super laser)  
**Difficulty**: ⭐⭐⭐⭐⭐  
**Description**: All enemy types are mixed together. During the boss battle, minion spawning accelerates by 75%. The ultimate challenge!

---

## 🏰 Tower Codex

### 1. 🏹 Arrow Tower
**Build cost**: 50 💰  
**Attack type**: single-target ranged  
**Traits**: a basic defensive tower with high cost performance, suitable for fast early defense setup  
**Upgrade effects**:
- Level 0 → 1: 40💰 (damage ×1.5, range +1)
- Level 1 → 2: 80💰 (damage ×2.0, attack speed +30%)
- **Total investment**: 170💰

**Recommended use**: main early-game tower, build many to form a firepower network

---

### 2. ❄️ Ice Tower
**Build cost**: 80 💰  
**Attack type**: single-target freeze  
**Traits**: freezes enemies for 0.6 seconds and controls enemy movement
**Special mechanic**: Bosses are immune to freeze effects  
**Upgrade effects**:
- Level 0 → 1: 64💰 (increased damage, range +1)
- Level 1 → 2: 128💰 (damage ×2.0, attack speed +30%)
- **Total investment**: 272💰

**Recommended use**: combine with Arrow Towers to control fast enemies (assassins); ineffective against Bosses

---

### 3. 💣 Cannon Tower
**Build cost**: 90 💰  
**Attack type**: area damage  
**Traits**: explosive damage, highly effective against dense enemy groups  
**Upgrade effects**:
- Level 0 → 1: 72💰 (damage ×1.5, range +1)
- Level 1 → 2: 144💰 (damage ×2.0, attack interval shortened by 30%)
- **Total investment**: 306💰

**Recommended use**: deal with tanks and dense groups, deploy at key positions

---

### 4. 💚 Heal Tower
**Build cost**: 70 💰  
**Attack type**: area healing  
**Traits**: continuously heals damaged attack towers within range and keeps the defense line alive  
**Upgrade effects**:
- Level 0 → 1: 56💰 (continuous healing ×1.5)
- Level 1 → 2: 112💰 (continuous healing ×2.0)
- **Total investment**: 238💰

**Recommended use**: protect the core defensive line and extend tower survival time

---

### 5. ⚡ Lightning Tower
**Build cost**: 95 💰  
**Attack type**: close-range continuous laser  
**Traits**: locks onto one target within a 3x3 area and deals continuous damage; the longer it stays locked, the higher the damage  
**Upgrade effects**:
- Level 0 → 1: 76💰 (continuous damage ×1.5)
- Level 1 → 2: 152💰 (continuous damage ×2.0)
- **Total investment**: 323💰

**Recommended use**: late-game close-range high damage, suitable near enemy paths

---

### 6. 🎯 Decoy
**Build cost**: 40 💰  
**Type**: tactical tool  
**Traits**: draws nearby enemy attention and buys time  
**Duration**: limited; it flies out of the map

**Recommended use**: distract enemies in emergencies and protect key buildings

---

## 👹 Enemy Codex

### Regular Enemies

#### 1. ⚔️ Melee
**HP**: ~100 HP  
**Damage**: 12  
**Speed**: 1.0 (standard)  
**Reward**: 20💰  
**Traits**: basic enemy, appears in the largest numbers

---

#### 2. 🗡️ Assassin
**HP**: ~80 HP  
**Damage**: 22  
**Speed**: 1.5 (fast)  
**Reward**: 40💰  
**Traits**: fast movement, requires Ice Tower control

---

#### 3. 🛡️ Tank
**HP**: ~250 HP  
**Damage**: 20  
**Speed**: 0.7 (slow)  
**Reward**: 35💰  
**Traits**: high HP, requires focus fire or Cannon Towers

---

#### 4. 🏹 Archer
**HP**: ~70 HP  
**Damage**: 15  
**Speed**: 1.1 (relatively fast)  
**Reward**: 30💰  
**Traits**: ranged attacks, should be killed first

---

### Elite Enemy

#### 👑 Elite
**HP**: 500 HP  
**Damage**: 35  
**Speed**: 0.9  
**Reward**: 150💰 + 400🏆  
**Spawn**: one wave every 60 seconds, 2 elites per wave  
**Traits**:
- 50% larger than regular enemies
- Purple appearance, easy to identify
- Generous kill reward and an important economy source
- Stops spawning after the Boss appears

---

### Boss Enemy

#### 💀 Boss (The Dark Lord)
**HP**:
- Level 3: 1200 HP (Mini Boss)
- Level 4: 1600 HP (Standard Boss)
- Level 5: 2000 HP (Final Boss)

**Damage**: 50  
**Speed**: 0.95  
**Reward**: 60💰 + 300🏆  

**Special ability: Destruction Laser** ⚡
- **Charge phase** (0.8 seconds):
  - Energy orbs spiral into the staff
  - Rotating purple electric arc effects
  - Red warning line indicates attack direction
  
- **Laser firing** (1.5 seconds):
  - Randomly chooses horizontal or vertical direction
  - 5-layer gradient purple beam (maximum width 35 pixels)
  - Moving arcs, laser particles, and shockwave effects
  - **Continuously damages all towers in the same row or column, usually destroying the towers it hits**
  
- **Firing interval**: once every 5 seconds

**Boss battle mechanics**:
- After the Boss appears, regular enemy spawn interval is shortened to 25% of the original value
- After the Boss dies, all enemy spawning stops
- The Boss must be defeated and all remaining enemies must be cleared to win

**Tactical advice**:
- Spread towers out to avoid losing all of them to one laser
- Use Heal Towers to quickly repair towers hit by lasers
- ⚠️ Bosses are immune to freeze effects, so Ice Towers do not affect them
- Maintain your economy and be ready to rebuild destroyed towers at any time

---

## 🎯 Difficulty System

The game provides 3 base difficulties, affecting enemy attributes, spawn speed, and disaster frequency. In addition, the game runs Adaptive Pressure AI: when the player's base HP is high, gold is sufficient, and kill efficiency is high, the system slightly accelerates enemy spawning and increases the special enemy ratio; when the player's base HP is low or resources are tight, the system reduces pressure and gives the player room to recover. The Stage card in the HUD displays the current AI pressure multiplier.

### 🟢 Easy
**Enemy HP**: 70% (-30%)  
**Spawn speed**: 40% slower (more preparation time)  
**Disaster frequency**: every 18 seconds  
**Suitable for**: new players learning the game mechanics

---

### 🟡 Normal
**Enemy HP**: 110% (+10%)  
**Spawn speed**: 10% faster  
**Disaster frequency**: every 9 seconds  
**Suitable for**: players with some experience, balanced challenge

---

### 🔴 Hard
**Enemy HP**: 120% (+20%)  
**Spawn speed**: 20% faster  
**Disaster frequency**: every 8 seconds  
**Suitable for**: expert players seeking an extreme challenge

---

## 📊 Stage System

Each level is divided into 5 stages, and each stage lasts 40 seconds:

```
Stage 1 (0-40s)   → base difficulty
Stage 2 (40-80s)  → special enemies +15%
Stage 3 (80-120s) → special enemies +30%
Stage 4 (120-160s)→ special enemies +45%
Stage 5 (160s+)   → special enemies +60% + Boss/elite wave appears
```

**Stage effects**:
- In each Stage, the appearance probability of special enemies (tanks/assassins/archers) increases by 15%
- Adaptive Pressure AI further fine-tunes spawn intervals and special enemy probability on top of the Stage multiplier
- Stage 5 triggers the final challenge (Boss or final elite wave)

---

## 💡 Advanced Strategy

### Economy Management
1. **Early game**: prioritize Arrow Towers to quickly establish a defensive line
2. **Mid game**: upgrade towers in key positions to improve damage output
3. **Late game**: build advanced towers (Heal Tower, Lightning Tower) and prepare for the Boss battle

### Tower Placement
1. **Crossfire**: let multiple towers cover the same area
2. **Control priority**: place Ice Towers on required enemy paths
3. **Healing protection**: place Heal Towers at the center of the defensive line

### Boss Battle Tips
1. **Spread layout**: avoid losing an entire row of towers to one laser
2. **Fast rebuilding**: keep enough gold to replace towers at any time
3. **Damage priority**: focus fire to kill the Boss first
4. **Minion control**: use Ice Towers and Decoys to control spawned minions

---

## 🏗️ Project Architecture

### Core Package Structure

```
src/
├── game/           # Core game framework
│   ├── CoreSiege.java      # Main game class
│   ├── GameEngine.java     # Game engine
│   └── GameConfig.java     # Configuration constants
│
├── core/           # Core mechanics
│   ├── GridMap.java        # Map grid
│   ├── PathFinder.java     # Pathfinding algorithm
│   └── GridPosition.java   # Coordinate system
│
├── building/       # Building system
│   ├── Building.java       # Building base class
│   ├── Base.java           # Player base
│   └── tower/              # Defensive towers
│       ├── ArrowTower.java
│       ├── IceTower.java
│       ├── CannonTower.java
│       ├── HealTower.java
│       └── LightningTower.java
│
├── enemy/          # Enemy system
│   ├── Enemy.java          # Enemy base class
│   ├── EnemyAI.java        # Enemy AI
│   ├── EnemySpawner.java   # Spawn control
│   └── enemies/            # Concrete enemies
│       ├── MeleeEnemy.java
│       ├── AssassinEnemy.java
│       ├── TankEnemy.java
│       ├── ArcherEnemy.java
│       ├── EliteEnemy.java
│       └── BossEnemy.java
│
├── combat/         # Combat system
│   ├── Projectile.java     # Projectile
│   ├── BossLaser.java      # Boss laser
│   └── MeteorStrike.java   # Disaster skill
│
├── manager/        # Managers
│   ├── WaveManager.java    # Wave management
│   ├── EconomyManager.java # Economy system
│   ├── ScoreManager.java   # Score statistics
│   └── SoundManager.java   # Sound management
│
├── ui/             # User interface
│   ├── HUD.java            # Game HUD
│   ├── MenuScreen.java     # Menu screen
│   └── IntroScreen.java    # Intro animation
│
└── effect/         # Visual effects
    ├── ParticleSystem.java # Particle system
    └── Particle.java       # Particle class
```

### Design Patterns

- **Factory pattern**: BuildingFactory, EnemyFactory
- **Singleton pattern**: SoundManager, ImageManager
- **Manager pattern**: various Manager classes
- **Inheritance and polymorphism**: Building → Tower → concrete tower classes

---

## 🎨 Technical Highlights

### 1. Boss Laser Effects System
- **Multi-layer rendering**: gradient beam + electric arcs + laser particles
- **Dynamic width**: laser width changes over time
- **Energy gathering**: 12 energy orbs spiral toward the staff
- **Shockwave**: expanding ripple effect on firing

### 2. Intelligent Pathfinding System
- **BFS algorithm**: calculates the shortest path in real time
- **Dynamic obstacle avoidance**: automatically recalculates when buildings change
- **Building attacks**: attacks blocking buildings when the path is blocked

### 3. Effects System
- **Laser particles**: sparks and trail effects are generated during boss lasers
- **Hit effects**: explosion glows and shockwaves appear when towers are hit by lasers
- **Energy orbs**: energy gathering during the Boss charge phase
- **Floating text**: kills, reward points, building, and upgrading all display immediate feedback

### 4. Advanced Interaction Feedback
- **Enemy anti-overlap display**: enemies on the same grid cell are automatically drawn with offsets to avoid complete occlusion
- **Build preview**: green/red hints show whether the current position can be built on when hovering over the map

### 5. Adaptive UI
- **Window scaling**: supports drag resizing
- **Centered display**: victory/defeat messages are automatically centered
- **Responsive layout**: HUD automatically adapts to the window

---

## 📈 Game Data

### Code Statistics
- **Total files**: 60+ Java files
- **Lines of code**: ~4000 lines
- **Package count**: 11 packages
- **Design patterns**: 4 types

### Game Content
- **Levels**: 5
- **Defensive towers**: 6 types (18 upgrade levels)
- **Enemy types**: 6 types (including Elite and Boss)
- **Difficulty modes**: 3

---

## ✅ Rubric Mapping

| Rubric Item | Score | Implemented Content |
|-----------|-----------|-----------|
| Game Concept & Design | 15/15 | Four-direction tower defense gameplay, clear base defense objective, Level/Stage progression, and Boss ending |
| Graphics & Animation | 15/15 | Map/tower/enemy/base image assets, enemy movement and attack animation, fire, gold, freeze, healing, floating text, build/upgrade particles, and Boss laser effects |
| Game Complexity | 15/15 | 5 Levels, 5 Stages per level, 6 enemies, 6 defense/tool choices, tower upgrades, elite waves, and Boss battles |
| Sound & Music | 5/5 | Background music, plus shooting, building, enemy death, Boss/disaster, victory/defeat, and UI sound effects |
| Scoring / Health System | 10/10 | Score, kill count, gold rewards, reward points, score penalty for base damage, base HP, building HP, and enemy HP |
| Gameplay & Controls & UI | 10/10 | Mouse-based building/upgrading/selling, keyboard shortcuts, HUD, build legality preview, pause menu, fast-forward, save/load |
| Gaming AI | 10/10 | BFS pathfinding, enemies tracking the base, attacks on defensive towers/blocking buildings, decoy target switching, Boss special attack, and Adaptive Pressure AI dynamic spawn pressure adjustment |
| Additional Features | 15/15 | Difficulty selection, save system, dynamic disasters, reward points, Boss laser, floating text, enemy anti-overlap display, death/build/upgrade particles, scalable window, and multiple visual effects |
| Code Quality & Documentation | 5/5 | Clear package structure, well-separated Factory/Manager responsibilities, and README documentation consistent with the current implementation |

---

## 🔧 Development Guide

### Add a New Defensive Tower
1. Create a new tower class in `building/tower/`
2. Extend `AttackTower`
3. Add an enum value in `BuildingType`
4. Add creation logic in `BuildingFactory`
5. Add price configuration in `GameConfig`

### Add a New Enemy
1. Create a new enemy class in `enemy/enemies/`
2. Extend `Enemy`
3. Add an enum value in `EnemyType`
4. Add creation logic in `EnemyFactory`
5. Configure spawn probability in `GameConfig`

---

## 🐛 Known Issues

- Warnings appear when audio files are missing (does not affect gameplay)
- Color blocks are used as fallbacks when image assets are missing

---

## 📝 Changelog

### v3.1 (2026-05-30)
- ⚖️ Ice Tower balance adjustment:
  - Build cost: 55 → 80 💰
  - Freeze duration: 2.0s → 0.6s
  - Freeze effect position optimization (visual improvement)
  - Bosses are now immune to freeze effects

### v3.0 (2026-05-24)
- ✨ Brand-new level system (Level 1-5)
- ✨ Major upgrade to Boss laser effects
- ✨ Elite enemy system optimization
- ⚖️ Difficulty balance adjustment
- 🎨 HUD tower order optimization
- 🐛 Fixed multiple bugs

### v2.0 (2026-05-17)
- 🏗️ Refactored into package structure
- 📦 Optimized code organization

### v1.0 (2026-05-10)
- 🎮 Initial release

---

## 👥 Contributors

- **Mechanics design**: Guo Mingqi
- **Interaction design**: Yu Han
- **Effects design**: Song Pengju
- **Asset integration**: Li Qianzheng

---

## 📄 License

This project is intended for learning and educational purposes only.

---

## 🎮 Start Your Adventure!

```bash
cd Assignment2_HeartoftheFourFronts
javac -encoding UTF-8 -d out "@sources.txt"
java -cp out Main
```

**Protect the Four Fronts fortress, defeat the Dark Lord, and become a legendary guardian!** 🏆

---

<div align="center">

**[⬆ Back to Top](#-heart-of-the-four-fronts-the-last-defence)**

</div>

---

# 中文版本

# 🏰 Heart of the Four Fronts: The Last Defence

<div align="center">

**一款多关卡塔防游戏 | 守护四方要塞的最后防线**

[![Java](https://img.shields.io/badge/Java-8+-orange.svg)](https://www.java.com/)

[游戏特色](#-游戏特色) • [快速开始](#-快速开始) • [游戏玩法](#-游戏玩法) • [关卡系统](#-关卡系统) • [防御塔](#-防御塔图鉴) • [敌人图鉴](#-敌人图鉴)

</div>

---

## 📖 背景故事

在遥远的艾瑟兰大陆，四方要塞曾是抵御黑暗军团的最后堡垒。数百年来，要塞守护者们利用古老的魔法塔和战术智慧，一次次击退了来自四面八方的入侵。

然而，黑暗之王终于苏醒了。他派遣无尽的怪物大军，从北方冰原、东方荒漠、南方沼泽和西方森林同时进攻。作为要塞的最后一位守护者，你必须运用智慧和策略，建造防御塔，击退一波又一波的敌人。

**你的使命：守护要塞核心，直到击败黑暗之王的终极Boss！**

---

## ✨ 游戏特色

### 🎯 策略深度
- **6种防御/工具选择**：从基础箭塔到闪电塔和诱饵，每种选择都有不同作用
- **塔升级系统**：每座塔可升级2次，大幅提升战斗力
- **战术诱饵**：吸引敌人注意力，为你争取宝贵时间

### 🌊 动态难度
- **5个渐进关卡**：从新手教学到终极Boss战
- **3种难度模式**：简单、普通、困难，满足不同玩家需求
- **Stage系统**：每个关卡内部5个阶段，难度逐步递增
- **Adaptive Pressure AI**：根据基地血量、金币和击杀效率动态调整刷怪压力

### 👹 丰富敌人
- **4种常规敌人**：近战兵、刺客、坦克、弓箭手各有特点
- **精英怪波次**：定期出现的强力精英，丰厚奖励
- **Boss战**：带充能、警告线和整行/整列激光攻击的终极挑战

### 🎨 视觉盛宴
- **像素风格资源**：敌人、塔、地图、障碍物和UI使用独立图片资源
- **战斗特效**：Boss激光、激光粒子、冲击波、电弧、冰冻、治疗光束和浮动得分文字
- **流畅动画**：60FPS流畅体验，自适应窗口大小

### 💾 完善系统
- **存档系统**：保存当前关卡、难度、波次、基地血量、金币、分数、建筑状态和场上敌人；投射物/奖励点/灾害不会写入存档
- **经济系统**：击杀敌人获得金币，合理分配资源
- **建造预览**：鼠标悬停地图时显示可建/不可建提示
- **音效系统**：沉浸式背景音乐和战斗音效

---

## 🚀 快速开始

### 系统要求
- **Java版本**：JDK 8 或更高版本
- **操作系统**：Windows / macOS / Linux
- **内存**：至少 512MB RAM
- **分辨率**：最低 640×480（推荐 900×700）

### 编译运行

#### 方法一：使用现有 sources.txt 运行
```bash
cd Assignment2_HeartoftheFourFronts
javac -encoding UTF-8 -d out "@sources.txt"
java -cp out Main
```

#### 方法二：重新生成 sources.txt 后运行
```bash
cd Assignment2_HeartoftheFourFronts
find src -name "*.java" > sources.txt
javac -encoding UTF-8 -d out "@sources.txt"
java -cp out Main
```

#### 方法三：使用IDE
1. 用IntelliJ IDEA或Eclipse打开项目
2. 设置JDK版本为8+
3. 运行`Main.java`

---

## 🎮 游戏玩法

### 基础操作

| 按键 | 功能 |
|------|------|
| **1-6** | 选择防御塔类型 |
| **鼠标左键** | 放置选中的塔 |
| **鼠标右键** | 出售塔（返还60%费用）|
| **空格** | 暂停/继续游戏 |
| **Esc** | 返回主菜单 |
| **M** | 静音/取消静音 |
| **F** | 快进模式（2倍速）|

### 游戏目标

1. **保护要塞核心**：不让敌人摧毁你的基地（1500 HP）
2. **击败所有敌人**：
   - Level 1-2：坚持到Stage 5并击败最终精英波
   - Level 3-5：击败Boss并清空所有剩余敌人
3. **合理分配资源**：用有限的金币建造和升级防御塔
4. **避免基地受伤**：基地每损失1 HP会扣除1分

### 游戏流程

```
开始游戏 → 选择关卡 → 选择难度
    ↓
Stage 1-5 逐步推进（每阶段40秒）
    ↓
敌人从四面八方涌来
    ↓
建造塔 → 升级塔 → 战术布局
    ↓
击败精英怪/Boss → 清空所有敌人
    ↓
🏆 胜利！解锁下一关卡
```

---

## 🗺️ 关卡系统

游戏包含**5个渐进式关卡**，每个关卡解锁新的防御塔，面对不同的敌人组合。

### Level 1：新手试炼 🌱
**解锁塔**：🏹 箭塔  
**敌人配置**：100% 近战兵  
**终极挑战**：💀 最终精英波（3个精英怪同时出现）  
**难度**：⭐  
**说明**：适合新手学习基础机制，只有近战敌人，压力较小

---

### Level 2：冰霜考验 ❄️
**解锁塔**：🏹 箭塔 + ❄️ 冰塔  
**敌人配置**：
- 30% 刺客（快速移动）
- 10% 弓箭手（远程攻击）
- 60% 近战兵

**终极挑战**：💀 最终精英波（4个精英怪）  
**难度**：⭐⭐  
**说明**：引入快速敌人，需要学会使用冰塔减速

---

### Level 3：炮火洗礼 💣
**解锁塔**：🏹 箭塔 + ❄️ 冰塔 + 💣 炮塔  
**敌人配置**：
- 25% 坦克（高血量）
- 20% 刺客
- 15% 弓箭手
- 40% 近战兵

**终极挑战**：👹 **迷你Boss**（1200 HP + 激光攻击）  
**难度**：⭐⭐⭐  
**说明**：首次Boss战！引入高血量坦克，需要炮塔范围伤害

---

### Level 4：治愈之战 💚
**解锁塔**：🏹 箭塔 + ❄️ 冰塔 + 💣 炮塔 + 💚 治疗塔  
**敌人配置**：
- 22% 坦克
- 20% 刺客
- 18% 弓箭手
- 40% 近战兵

**终极挑战**：👹 **标准Boss**（1600 HP + 激光攻击）  
**难度**：⭐⭐⭐⭐  
**说明**：压力增大，需要治疗塔维持防线

---

### Level 5：终极决战 ⚡
**解锁塔**：全部6种塔 + 🎯 诱饵  
**敌人配置**：
- 20% 坦克
- 20% 刺客
- 20% 弓箭手
- 40% 近战兵

**终极挑战**：👹 **终极Boss**（2000 HP + 超强激光）  
**难度**：⭐⭐⭐⭐⭐  
**说明**：所有敌人类型混合，Boss战期间小兵刷新加速75%，终极挑战！

---

## 🏰 防御塔图鉴

### 1. 🏹 箭塔 (Arrow Tower)
**建造成本**：50 💰  
**攻击类型**：单体远程  
**特点**：基础防御塔，性价比高，适合前期快速布防  
**升级效果**：
- Level 0 → 1：40💰（伤害×1.5，范围+1）
- Level 1 → 2：80💰（伤害×2.0，攻速+30%）
- **总投资**：170💰

**推荐用途**：前期主力，大量建造形成火力网

---

### 2. ❄️ 冰塔 (Ice Tower)
**建造成本**：80 💰  
**攻击类型**：单体冰冻  
**特点**：冰冻敌人0.6秒，控制敌人移动
**特殊机制**：Boss免疫冰冻效果  
**升级效果**：
- Level 0 → 1：64💰（伤害增强，范围+1）
- Level 1 → 2：128💰（伤害×2.0，攻速+30%）
- **总投资**：272💰

**推荐用途**：配合箭塔使用，控制快速敌人（刺客），对Boss无效

---

### 3. 💣 炮塔 (Cannon Tower)
**建造成本**：90 💰  
**攻击类型**：范围伤害  
**特点**：爆炸伤害，对密集敌人效果显著  
**升级效果**：
- Level 0 → 1：72💰（伤害×1.5，范围+1）
- Level 1 → 2：144💰（伤害×2.0，攻击间隔缩短30%）
- **总投资**：306💰

**推荐用途**：对付坦克和密集敌群，关键位置部署

---

### 4. 💚 治疗塔 (Heal Tower)
**建造成本**：70 💰  
**攻击类型**：范围治疗  
**特点**：持续治疗范围内受损的攻击塔，防线续航保障  
**升级效果**：
- Level 0 → 1：56💰（持续治疗量×1.5）
- Level 1 → 2：112💰（持续治疗量×2.0）
- **总投资**：238💰

**推荐用途**：保护核心防线，延长塔的生存时间

---

### 5. ⚡ 闪电塔 (Lightning Tower)
**建造成本**：95 💰  
**攻击类型**：近距离持续激光  
**特点**：锁定3x3范围内的单个目标，持续造成伤害，锁定时间越长伤害越高  
**升级效果**：
- Level 0 → 1：76💰（持续伤害×1.5）
- Level 1 → 2：152💰（持续伤害×2.0）
- **总投资**：323💰

**推荐用途**：后期近距离高输出，适合放在敌人路径附近

---

### 6. 🎯 诱饵 (Decoy)
**建造成本**：40 💰  
**类型**：战术工具  
**特点**：吸引附近敌人注意力，为你争取时间  
**持续时间**：有限,会飞出地图

**推荐用途**：紧急情况下分散敌人，保护关键建筑

---

## 👹 敌人图鉴

### 常规敌人

#### 1. ⚔️ 近战兵 (Melee)
**血量**：~100 HP  
**伤害**：12  
**速度**：1.0（标准）  
**奖励**：20💰  
**特点**：基础敌人，数量最多

---

#### 2. 🗡️ 刺客 (Assassin)
**血量**：~80 HP  
**伤害**：22  
**速度**：1.5（快速）  
**奖励**：40💰  
**特点**：速度快，需要冰塔控制

---

#### 3. 🛡️ 坦克 (Tank)
**血量**：~250 HP  
**伤害**：20  
**速度**：0.7（缓慢）  
**奖励**：35💰  
**特点**：血厚，需要集火或炮塔

---

#### 4. 🏹 弓箭手 (Archer)
**血量**：~70 HP  
**伤害**：15  
**速度**：1.1（较快）  
**奖励**：30💰  
**特点**：远程攻击，优先击杀

---

### 精英敌人

#### 👑 精英怪 (Elite)
**血量**：500 HP  
**伤害**：35  
**速度**：0.9  
**奖励**：150💰 + 400🏆  
**刷新**：每60秒一波，每波2个  
**特点**：
- 体型比普通敌人大50%
- 紫色外观，容易识别
- 击杀奖励丰厚，是重要经济来源
- Boss出现后停止刷新

---

### Boss敌人

#### 💀 Boss (The Dark Lord)
**血量**：
- Level 3：1200 HP（迷你Boss）
- Level 4：1600 HP（标准Boss）
- Level 5：2000 HP（终极Boss）

**伤害**：50  
**速度**：0.95  
**奖励**：60💰 + 300🏆  

**特殊能力：毁灭激光** ⚡
- **充能阶段**（0.8秒）：
  - 能量球螺旋聚集到手杖
  - 旋转紫色电弧特效
  - 红色警告线提示攻击方向
  
- **激光发射**（1.5秒）：
  - 随机选择横向或纵向
  - 5层渐变紫色光束（最宽35像素）
  - 移动电弧、激光粒子和冲击波特效
  - **持续伤害同一行或同一列的所有塔，通常会摧毁命中的塔**
  
- **发射间隔**：每5秒一次

**Boss战机制**：
- Boss出现后，普通敌人刷新间隔缩短为原来的25%
- Boss死亡后，停止刷新所有敌人
- 必须击败Boss并清空所有剩余敌人才能胜利

**战术建议**：
- 分散建塔，避免被一次激光全部摧毁
- 使用治疗塔快速修复被激光击中的塔
- ⚠️ Boss免疫冰冻效果，冰塔对Boss无效
- 保持经济，随时重建被摧毁的塔

---

## 🎯 难度系统

游戏提供3种基础难度，影响敌人属性、刷新速度和灾难频率。除此之外，游戏还会运行 Adaptive Pressure AI：当玩家基地血量高、金币充足、击杀效率高时，系统会略微加快刷怪并提高特殊敌人比例；当玩家基地血量低或资源紧张时，系统会降低压力，给玩家恢复空间。HUD 的 Stage 卡会显示当前 AI 压力倍率。

### 🟢 简单 (Easy)
**敌人血量**：70%（-30%）  
**刷新速度**：慢40%（更多准备时间）  
**灾难频率**：每18秒  
**适合**：新手玩家，学习游戏机制

---

### 🟡 普通 (Normal)
**敌人血量**：110%（+10%）  
**刷新速度**：快10%  
**灾难频率**：每9秒  
**适合**：有一定经验的玩家，平衡挑战

---

### 🔴 困难 (Hard)
**敌人血量**：120%（+20%）  
**刷新速度**：快20%  
**灾难频率**：每8秒  
**适合**：高手玩家，极限挑战

---

## 📊 Stage系统

每个关卡内部分为5个Stage，每个Stage持续40秒：

```
Stage 1 (0-40s)   → 基础难度
Stage 2 (40-80s)  → 特殊敌人+15%
Stage 3 (80-120s) → 特殊敌人+30%
Stage 4 (120-160s)→ 特殊敌人+45%
Stage 5 (160s+)   → 特殊敌人+60% + Boss/精英波出现
```

**Stage效果**：
- 每个Stage，特殊敌人（坦克/刺客/弓箭手）出现概率增加15%
- Adaptive Pressure AI会在Stage倍率之上继续微调刷怪间隔和特殊敌人概率
- Stage 5时触发终极挑战（Boss或最终精英波）

---

## 💡 进阶策略

### 经济管理
1. **前期**：优先建造箭塔，快速形成防线
2. **中期**：升级关键位置的塔，提升输出
3. **后期**：建造高级塔（治疗塔、闪电塔），准备Boss战

### 塔位布局
1. **交叉火力**：让多座塔覆盖同一区域
2. **控制优先**：冰塔放在敌人必经之路
3. **治疗保护**：治疗塔放在防线中心

### Boss战技巧
1. **分散布局**：避免一排塔被激光全灭
2. **快速重建**：保持足够金币，随时补塔
3. **优先输出**：集中火力先杀Boss
4. **控制小兵**：用冰塔和诱饵控制刷新的小兵

---

## 🏗️ 项目架构

### 核心包结构

```
src/
├── game/           # 游戏核心框架
│   ├── CoreSiege.java      # 主游戏类
│   ├── GameEngine.java     # 游戏引擎
│   └── GameConfig.java     # 配置常量
│
├── core/           # 核心机制
│   ├── GridMap.java        # 地图网格
│   ├── PathFinder.java     # 寻路算法
│   └── GridPosition.java   # 坐标系统
│
├── building/       # 建筑系统
│   ├── Building.java       # 建筑基类
│   ├── Base.java           # 玩家基地
│   └── tower/              # 防御塔
│       ├── ArrowTower.java
│       ├── IceTower.java
│       ├── CannonTower.java
│       ├── HealTower.java
│       └── LightningTower.java
│
├── enemy/          # 敌人系统
│   ├── Enemy.java          # 敌人基类
│   ├── EnemyAI.java        # 敌人AI
│   ├── EnemySpawner.java   # 刷怪控制
│   └── enemies/            # 具体敌人
│       ├── MeleeEnemy.java
│       ├── AssassinEnemy.java
│       ├── TankEnemy.java
│       ├── ArcherEnemy.java
│       ├── EliteEnemy.java
│       └── BossEnemy.java
│
├── combat/         # 战斗系统
│   ├── Projectile.java     # 投射物
│   ├── BossLaser.java      # Boss激光
│   └── MeteorStrike.java   # 灾难技能
│
├── manager/        # 管理器
│   ├── WaveManager.java    # 波次管理
│   ├── EconomyManager.java # 经济系统
│   ├── ScoreManager.java   # 分数统计
│   └── SoundManager.java   # 音效管理
│
├── ui/             # 用户界面
│   ├── HUD.java            # 游戏HUD
│   ├── MenuScreen.java     # 菜单界面
│   └── IntroScreen.java    # 开场动画
│
└── effect/         # 视觉效果
    ├── ParticleSystem.java # 粒子系统
    └── Particle.java       # 粒子类
```

### 设计模式

- **工厂模式**：BuildingFactory、EnemyFactory
- **单例模式**：SoundManager、ImageManager
- **管理器模式**：各种Manager类
- **继承多态**：Building → Tower → 具体塔类

---

## 🎨 技术亮点

### 1. Boss激光特效系统
- **多层渲染**：渐变光束 + 电弧 + 激光粒子
- **动态宽度**：激光宽度随时间变化
- **能量聚集**：12个能量球螺旋飞向手杖
- **冲击波**：发射时的扩散波纹效果

### 2. 智能寻路系统
- **BFS算法**：实时计算最短路径
- **动态避障**：建筑变化时自动重新规划
- **攻击建筑**：路径被堵时攻击阻挡建筑

### 3. 特效系统
- **激光粒子**：Boss激光期间生成火花和拖尾效果
- **命中特效**：塔被激光击中时出现爆炸光晕和冲击波
- **能量球**：Boss充能阶段的能量聚集
- **浮动文字**：击杀、奖励点、建造和升级都会显示即时反馈

### 4. 高级交互反馈
- **敌人反重叠显示**：同格敌人会自动分散绘制，避免完全遮挡
- **建造预览**：鼠标悬停地图时用绿色/红色提示当前位置能否建造

### 5. 自适应UI
- **窗口缩放**：支持拖拽调整大小
- **居中显示**：胜利/失败消息自动居中
- **响应式布局**：HUD自动适配窗口

---

## 📈 游戏数据

### 代码统计
- **总文件数**：60+ Java文件
- **代码行数**：~4000行
- **包数量**：11个包
- **设计模式**：4种

### 游戏内容
- **关卡数量**：5个
- **防御塔**：6种（18个升级等级）
- **敌人类型**：6种（含Elite和Boss）
- **难度模式**：3种

---

## ✅ 评分点对照

| Rubric项目 | 分数 | 已实现内容 |
|-----------|-----------|-----------|
| Game Concept & Design | 15/15 | 四方进攻塔防玩法、明确的守护基地目标、Level/Stage推进和Boss终局 |
| Graphics & Animation | 15/15 | 地图/塔/敌人/基地图片资源，敌人移动与攻击动画，火焰、金币、冰冻、治疗、浮动文字、建造/升级粒子和Boss激光特效 |
| Game Complexity | 15/15 | 5个Level、每关5个Stage、6种敌人、6种防御/工具选择、塔升级、精英波和Boss战 |
| Sound & Music | 5/5 | 背景音乐，以及射击、建造、敌人死亡、Boss/灾害、胜负和UI音效 |
| Scoring / Health System | 10/10 | 分数、击杀数、金币奖励、奖励点、基地受伤扣分、基地血量、建筑血量和敌人血量 |
| Gameplay & Controls & UI | 10/10 | 鼠标建塔/升级/出售，键盘快捷键，HUD，建造合法性预览，暂停菜单，快进，存档/读档 |
| Gaming AI | 10/10 | BFS寻路，敌人追踪基地，攻击防御塔/阻挡建筑，诱饵目标切换，Boss特殊攻击，Adaptive Pressure AI动态调整刷怪压力 |
| Additional Features | 15/15 | 难度选择、存档系统、动态灾害、奖励点、Boss激光、浮动文字、敌人反重叠显示、死亡/建造/升级粒子、可缩放窗口和多种视觉特效 |
| Code Quality & Documentation | 5/5 | 包结构清晰，Factory/Manager等职责拆分明确，README说明与当前实现保持一致 |

---

## 🔧 开发指南

### 添加新防御塔
1. 在`building/tower/`创建新塔类
2. 继承`AttackTower`
3. 在`BuildingType`添加枚举
4. 在`BuildingFactory`添加创建逻辑
5. 在`GameConfig`添加价格配置

### 添加新敌人
1. 在`enemy/enemies/`创建新敌人类
2. 继承`Enemy`
3. 在`EnemyType`添加枚举
4. 在`EnemyFactory`添加创建逻辑
5. 在`GameConfig`配置刷新概率

---

## 🐛 已知问题

- 音频文件缺失时会有警告（不影响游戏）
- 图片资源缺失时使用颜色方块替代

---

## 📝 更新日志

### v3.1 (2026-05-30)
- ⚖️ 冰塔平衡性调整：
  - 建造成本：55 → 80 💰
  - 冰冻时间：2.0s → 0.6s
  - 冰冻效果位置优化（视觉改进）
  - Boss现在免疫冰冻效果

### v3.0 (2026-05-24)
- ✨ 全新关卡系统（Level 1-5）
- ✨ Boss激光特效大幅升级
- ✨ 精英怪系统优化
- ⚖️ 难度平衡调整
- 🎨 HUD塔顺序优化
- 🐛 修复多个bug

### v2.0 (2026-05-17)
- 🏗️ 重构为包结构
- 📦 代码组织优化

### v1.0 (2026-05-10)
- 🎮 初始版本发布

---

## 👥 贡献者

- **机制设计**：Guo Mingqi
- **互动设计**：Yu Han
- **特效设计**：Song Pengju
- **资源整合**：Li Qianzheng

---

## 📄 许可证

本项目仅用于学习和教育目的。

---

## 🎮 开始你的冒险！

```bash
cd Assignment2_HeartoftheFourFronts
javac -encoding UTF-8 -d out "@sources.txt"
java -cp out Main
```

**守护四方要塞，击败黑暗之王，成为传奇守护者！** 🏆

---

<div align="center">

**[⬆ 返回顶部](#-heart-of-the-four-fronts-the-last-defence)**

</div>
