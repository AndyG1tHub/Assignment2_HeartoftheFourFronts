package manager;

import java.awt.*;

import enemy.EnemyType;
import game.GameEngine;

/** Loads image assets and serves animation frames. */
public class ImageManger {
    private static final int ENEMY_FRAME_SIZE = 256;
    private static final int ENEMY_SHEET_COLUMNS = 8;
    private static final int ENEMY_RUN_FRAME_COUNT = 16;
    private static final int ENEMY_ATTACK_FRAME_COUNT = 8;

    GameEngine engine;
    private static Image arrowTower;
    private static Image attackTower;
    private static Image healTower;
    private static Image iceTower;
    private static Image lightningTower;
    Image wall;
    Image bait;
    Image homeItems;
    Image enemies;
    Image naturalDisaster;
    private static Image baseImage;
    private static Image arrowProjectile;
    private static Image cannonProjectile;
    private static Image iceProjectile;
    private static Image laserEffect;
    private static Image healRangeEffect;
    private static Image explosionFireball;

    private static Image[] meleeEnemyRun;
    private static Image[] meleeEnemyAttack;
    private static Image[] tankEnemyRun;
    private static Image[] tankEnemyAttack;
    private static Image[] assassinEnemyRun;
    private static Image[] assassinEnemyAttack;
    private static Image[] archerEnemyRun;
    private static Image[] archerEnemyAttack;
    private static Image[] healerEnemyRun;
    private static Image[] healerEnemyAttack;

    public void loadImages(GameEngine engine) {
        this.engine = engine;
        loadStaticImages(engine);
        loadEnemySprites(engine);
    }

    public static Image getEnemySprite(EnemyType type, double animationTime) {
        Image[] frames = getEnemyRunFrames(type);
        if (frames == null || frames.length == 0) {
            return null;
        }
        int frameIndex = ((int) (animationTime * 8)) % frames.length;
        return frames[frameIndex];
    }

    public static Image getEnemyAttackSprite(EnemyType type, double animationTime) {
        Image[] frames = getEnemyAttackFrames(type);
        if (frames == null || frames.length == 0) {
            return null;
        }
        int frameIndex = ((int) (animationTime * 8)) % frames.length;
        return frames[frameIndex];
    }

    private static Image[] getEnemyRunFrames(EnemyType type) {
        if (type == EnemyType.TANK) {
            return tankEnemyRun;
        }
        if (type == EnemyType.ASSASSIN) {
            return assassinEnemyRun;
        }
        if (type == EnemyType.ARCHER) {
            return archerEnemyRun;
        }
        if (type == EnemyType.HEALER) {
            return healerEnemyRun;
        }
        return meleeEnemyRun;
    }

    private static Image[] getEnemyAttackFrames(EnemyType type) {
        if (type == EnemyType.TANK) {
            return tankEnemyAttack;
        }
        if (type == EnemyType.ASSASSIN) {
            return assassinEnemyAttack;
        }
        if (type == EnemyType.ARCHER) {
            return archerEnemyAttack;
        }
        if (type == EnemyType.HEALER) {
            return healerEnemyAttack;
        }
        return meleeEnemyAttack;
    }

    private void loadStaticImages(GameEngine engine) {
        arrowTower = engine.loadImage("Images/arrowTower.png");
        attackTower = engine.loadImage("Images/attackTower.png");
        healTower = engine.loadImage("Images/healTower.png");
        iceTower = engine.loadImage("Images/iceTower.png");
        lightningTower = engine.loadImage("Images/lightningTower.png");
        wall = engine.loadImage("Images/wall.png");
        bait = engine.loadImage("Images/bait.png");
        homeItems = engine.loadImage("Images/homeItems.png");
        naturalDisaster = engine.loadImage("Images/naturalDisaster.png");
        baseImage = engine.subImage(homeItems, 45, 80, 470, 500);
        arrowProjectile = engine.subImage(homeItems, 575, 310, 250, 175);
        cannonProjectile = engine.subImage(homeItems, 895, 330, 245, 170);
        iceProjectile = engine.subImage(homeItems, 1180, 315, 265, 180);
        laserEffect = engine.subImage(homeItems, 170, 650, 300, 265);
        healRangeEffect = engine.subImage(homeItems, 555, 650, 340, 265);
        explosionFireball = engine.subImage(homeItems, 995, 630, 350, 285);
    }

    private void loadEnemySprites(GameEngine engine) {
        Image meleeSheet = engine.loadImage("Images/meleeEnemy.png");
        meleeEnemyRun = loadEnemyRunSprite(engine, meleeSheet);
        meleeEnemyAttack = loadEnemyAttackSprite(engine, meleeSheet);

        Image tankSheet = engine.loadImage("Images/tankEnemy.png");
        tankEnemyRun = loadEnemyRunSprite(engine, tankSheet);
        tankEnemyAttack = loadEnemyAttackSprite(engine, tankSheet);

        Image assassinSheet = engine.loadImage("Images/assassinEnemy.png");
        assassinEnemyRun = loadEnemyRunSprite(engine, assassinSheet);
        assassinEnemyAttack = loadEnemyAttackSprite(engine, assassinSheet);

        Image archerSheet = engine.loadImage("Images/archerEnemy.png");
        archerEnemyRun = loadEnemyRunSprite(engine, archerSheet);
        archerEnemyAttack = loadEnemyAttackSprite(engine, archerSheet);

        Image healerSheet = engine.loadImage("Images/healerEnemy.png");
        healerEnemyRun = loadEnemyRunSprite(engine, healerSheet);
        healerEnemyAttack = loadEnemyAttackSprite(engine, healerSheet);
    }

    private Image[] loadEnemyRunSprite(GameEngine engine, Image sheet) {
        Image[] frames = new Image[ENEMY_RUN_FRAME_COUNT];
        for (int i = 0; i < frames.length; i++) {
            int row = i / ENEMY_SHEET_COLUMNS;
            int col = i % ENEMY_SHEET_COLUMNS;
            frames[i] = engine.subImage(sheet, col * ENEMY_FRAME_SIZE, row * ENEMY_FRAME_SIZE,
                    ENEMY_FRAME_SIZE, ENEMY_FRAME_SIZE);
        }
        return frames;
    }

    private Image[] loadEnemyAttackSprite(GameEngine engine, Image sheet) {
        Image[] frames = new Image[ENEMY_ATTACK_FRAME_COUNT];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = engine.subImage(sheet, i * ENEMY_FRAME_SIZE, ENEMY_FRAME_SIZE * 2,
                    ENEMY_FRAME_SIZE, ENEMY_FRAME_SIZE);
        }
        return frames;
    }

    public static Image getArrowTower() {
        return arrowTower;
    }

    public static Image getAttackTower() {
        return attackTower;
    }

    public static Image getHealTower() {
        return healTower;
    }

    public static Image getIceTower() {
        return iceTower;
    }

    public static Image getLightningTower() {
        return lightningTower;
    }

    public static Image getBaseImage() {
        return baseImage;
    }

    public static Image getArrowProjectile() {
        return arrowProjectile;
    }

    public static Image getCannonProjectile() {
        return cannonProjectile;
    }

    public static Image getIceProjectile() {
        return iceProjectile;
    }

    public static Image getLaserEffect() {
        return laserEffect;
    }

    public static Image getHealRangeEffect() {
        return healRangeEffect;
    }

    public static Image getExplosionFireball() {
        return explosionFireball;
    }
}
