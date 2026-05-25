# 项目结构文档

## 概述

这是一个Java塔防游戏项目，采用清晰的包结构组织代码，便于维护和扩展。

## 目录结构

```
Assignment2_HeartoftheFourFronts-main/
├── src/                          # 源代码目录
│   ├── Main.java                 # 程序入口
│   │
│   ├── game/                     # 游戏核心框架
│   │   ├── CoreSiege.java       # 主游戏类，协调所有管理器
│   │   ├── GameEngine.java      # 游戏引擎基类，提供渲染、输入、音频等基础功能
│   │   ├── GameConfig.java      # 游戏配置常量（窗口大小、FPS等）
│   │   ├── GameState.java       # 游戏状态枚举（菜单、游戏中、暂停、结束）
│   │   └── Difficulty.java      # 难度枚举（简单、普通、困难）
│   │
│   ├── core/                     # 核心游戏机制
│   │   ├── GridMap.java         # 地图网格系统，管理地块布局
│   │   ├── GridPosition.java    # 网格位置坐标类
│   │   ├── Tile.java            # 地块类，表示地图上的单个格子
│   │   ├── TileType.java        # 地块类型枚举（路径、可建造、基地等）
│   │   └── PathFinder.java      # 寻路算法，用于敌人导航
│   │
│   ├── building/                 # 建筑系统
│   │   ├── Building.java        # 建筑抽象基类
│   │   ├── BuildingFactory.java # 建筑工厂，创建各种建筑实例
│   │   ├── BuildingType.java    # 建筑类型枚举
│   │   ├── Base.java            # 玩家基地，需要保护的目标
│   │   ├── Wall.java            # 墙体建筑，阻挡敌人
│   │   ├── Decoy.java           # 诱饵建筑，吸引敌人
│   │   │
│   │   └── tower/               # 防御塔子包
│   │       ├── AttackTower.java      # 攻击塔抽象基类
│   │       ├── ArrowTower.java       # 箭塔，基础远程攻击
│   │       ├── CannonTower.java      # 炮塔，范围伤害
│   │       ├── IceTower.java         # 冰塔，减速敌人
│   │       ├── LightningTower.java   # 闪电塔，链式攻击
│   │       └── HealTower.java        # 治疗塔，恢复建筑生命值
│   │
│   ├── enemy/                    # 敌人系统
│   │   ├── Enemy.java           # 敌人类，包含移动、战斗逻辑
│   │   ├── EnemyType.java       # 敌人类型枚举（普通、快速、坦克等）
│   │   ├── EnemyState.java      # 敌人状态枚举（移动、攻击、死亡）
│   │   ├── EnemyFactory.java    # 敌人工厂，创建不同类型敌人
│   │   ├── EnemySpawner.java    # 敌人生成器，控制刷怪时机和数量
│   │   └── EnemyAI.java         # 敌人AI，处理寻路和目标选择
│   │
│   ├── combat/                   # 战斗系统
│   │   ├── Projectile.java      # 投射物类（箭、炮弹等）
│   │   ├── ProjectileManager.java # 投射物管理器
│   │   ├── DamageArea.java      # 伤害区域类
│   │   ├── FireZone.java        # 火焰区域，持续伤害
│   │   └── MeteorStrike.java    # 陨石打击技能
│   │
│   ├── event/                    # 事件系统
│   │   ├── GameEvent.java       # 游戏事件类
│   │   ├── EventType.java       # 事件类型枚举
│   │   ├── EventManager.java    # 事件管理器，处理游戏事件
│   │   ├── Disaster.java        # 灾难事件类
│   │   ├── DisasterType.java    # 灾难类型枚举
│   │   └── DisasterManager.java # 灾难管理器
│   │
│   ├── manager/                  # 管理器集合
│   │   ├── DifficultyManager.java    # 难度管理器，调整游戏难度参数
│   │   ├── EconomyManager.java       # 经济管理器，处理金币收入
│   │   ├── ScoreManager.java         # 分数管理器，记录玩家得分
│   │   ├── WaveManager.java          # 波次管理器，控制敌人波次
│   │   ├── DecoyManager.java         # 诱饵管理器
│   │   ├── RewardPointManager.java   # 奖励点管理器
│   │   ├── ImageManger.java          # 图片资源管理器，加载并缓存精灵图
│   │   └── SoundManager.java         # 音效管理器，播放音乐和音效
│   │
│   ├── ui/                       # 用户界面
│   │   ├── HUD.java             # 抬头显示，显示金币、生命值等信息
│   │   ├── MenuScreen.java      # 菜单屏幕
│   │   └── Button.java          # 按钮UI组件
│   │
│   ├── effect/                   # 视觉效果
│   │   ├── Particle.java        # 粒子类
│   │   ├── ParticleSystem.java  # 粒子系统，管理特效
│   │   └── RewardPoint.java     # 奖励点特效
│   │
│   └── util/                     # 工具类
│       ├── AssetManager.java    # 资源管理器，加载图片等资源
│       └── Direction.java       # 方向枚举（上、下、左、右）
│
└── sounds/                       # 音频资源目录
|    └── (音效文件)

```

## 包说明

### 1. game 包 - 游戏核心框架
包含游戏的主要入口和引擎基础设施。
- **CoreSiege**: 主游戏类，继承自GameEngine，协调所有子系统
- **GameEngine**: 提供窗口管理、渲染、输入处理、音频播放等基础功能
- **GameConfig**: 定义游戏配置常量
- **GameState**: 游戏状态枚举
- **Difficulty**: 难度级别枚举

### 2. core 包 - 核心游戏机制
包含地图、网格、寻路等基础游戏机制。
- **GridMap**: 管理游戏地图的网格系统
- **GridPosition**: 表示网格坐标
- **Tile**: 地图上的单个地块
- **TileType**: 地块类型（路径、可建造区域等）
- **PathFinder**: A*寻路算法实现

### 3. building 包 - 建筑系统
包含所有建筑相关的类，防御塔单独放在tower子包中。
- **Building**: 建筑抽象基类
- **BuildingFactory**: 使用工厂模式创建建筑
- **Base**: 玩家基地
- **Wall**: 墙体
- **Decoy**: 诱饵
- **tower子包**: 包含所有防御塔类型

### 4. enemy 包 - 敌人系统
包含敌人的创建、AI和行为逻辑。
- **Enemy**: 敌人实体类
- **EnemyFactory**: 工厂模式创建敌人
- **EnemySpawner**: 控制敌人生成时机
- **EnemyAI**: 敌人的智能行为

### 5. combat 包 - 战斗系统
包含投射物、伤害区域等战斗相关功能。
- **Projectile**: 投射物（箭、炮弹等）
- **ProjectileManager**: 管理所有投射物
- **DamageArea**: 范围伤害区域
- **FireZone**: 持续伤害的火焰区域
- **MeteorStrike**: 特殊技能

### 6. event 包 - 事件系统
包含游戏事件和灾难系统。
- **GameEvent**: 游戏事件类
- **EventManager**: 事件管理和分发
- **Disaster**: 灾难事件
- **DisasterManager**: 灾难管理器

### 7. manager 包 - 管理器集合
集中管理游戏的各个子系统。
- **DifficultyManager**: 难度调整
- **EconomyManager**: 经济系统（金币收入）
- **ScoreManager**: 分数统计
- **WaveManager**: 敌人波次控制
- **DecoyManager**: 诱饵管理
- **RewardPointManager**: 奖励点管理
- **SoundManager**: 音效和音乐播放

### 8. ui 包 - 用户界面
包含所有UI组件。
- **HUD**: 游戏内抬头显示
- **MenuScreen**: 主菜单界面
- **Button**: 按钮组件

### 9. effect 包 - 视觉效果
包含粒子系统和特效。
- **Particle**: 单个粒子
- **ParticleSystem**: 粒子系统管理
- **RewardPoint**: 奖励点特效

### 10. util 包 - 工具类
包含辅助工具和枚举。
- **AssetManager**: 资源加载管理
- **Direction**: 方向枚举

## 编译和运行

### 编译
```bash
cd src
javac Main.java
```

### 运行
```bash
java Main
```

## 设计模式

项目中使用了以下设计模式：

1. **工厂模式** (Factory Pattern)
   - `BuildingFactory`: 创建不同类型的建筑
   - `EnemyFactory`: 创建不同类型的敌人

2. **单例模式** (Singleton Pattern)
   - `SoundManager`: 全局音效管理器

3. **管理器模式** (Manager Pattern)
   - 多个Manager类负责管理各自领域的对象和逻辑

4. **继承和多态**
   - `Building` -> `AttackTower` -> 各种具体塔类
   - `GameEngine` -> `CoreSiege`

## 代码统计

- 总文件数: 55个Java文件
- 总代码行数: 约3500行
- 包数量: 11个包（包括1个子包）

## 扩展指南

### 添加新的防御塔
1. 在 `building/tower/` 包中创建新的塔类
2. 继承 `AttackTower` 类
3. 在 `BuildingType` 枚举中添加新类型
4. 在 `BuildingFactory` 中添加创建逻辑

### 添加新的敌人类型
1. 在 `EnemyType` 枚举中添加新类型
2. 在 `EnemyFactory` 中添加创建逻辑
3. 根据需要调整 `EnemyAI` 行为

### 添加新的游戏事件
1. 在 `EventType` 枚举中添加新事件类型
2. 在 `EventManager` 中添加处理逻辑
3. 在相应的地方触发事件

## 维护建议

1. **保持包的职责单一**: 每个包只负责一个功能领域
2. **避免循环依赖**: 注意包之间的依赖关系
3. **使用接口**: 对于需要多态的地方，考虑使用接口
4. **文档注释**: 为公共API添加JavaDoc注释
5. **单元测试**: 为核心逻辑添加测试用例

## 版本历史

- **v2.0** (2026-05-24): 重构为包结构，提高代码组织性
- **v1.0**: 初始版本，所有类在同一目录下

## Enemy enemies 子包说明

`src/enemy/enemies/` 采用与 `building/tower/` 类似的组织方式：`enemy.Enemy` 作为通用基类，保存移动、受伤、减速、奖励、绘制等公共逻辑；具体敌人类型放在 `enemy.enemies` 子包中，由 `EnemyFactory` 根据 `EnemyType` 创建。

- **MeleeEnemy（近战）**: 标准敌人，血量、速度、伤害比较均衡，作为基础刷怪单位。
- **TankEnemy（肉盾）**: 高血量、低速度，伤害较高，用来吸收防御塔火力。
- **AssassinEnemy（刺客）**: 血量较低但移动速度快、伤害高，适合快速冲击基地。
- **ArcherEnemy（射手）**: 脆皮输出型敌人，速度和伤害中等偏高，可以在距离基地 3 格内停下攻击。
- **HealerEnemy（治疗）**: 支援型敌人，会周期性治疗附近受伤的其他敌人。

当前刷怪逻辑在 `EnemySpawner` 中按波次逐步解锁：第 1 阶段以近战为主，混入刺客和射手；第 2 阶段加入肉盾和治疗；第 3 阶段五种敌人都会出现。
