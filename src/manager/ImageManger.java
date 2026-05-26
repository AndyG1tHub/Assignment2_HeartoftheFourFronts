package manager;

import java.awt.*;
import java.awt.image.BufferedImage;

import enemy.EnemyType;
import game.GameEngine;

/** Loads image assets and serves animation frames. */
public class ImageManger {
    private static final int ENEMY_FRAME_SIZE = 256;
    private static final int ENEMY_FRAME_INSET = 14;
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
    private static Image bait;
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
    private static Image[] meleeEnemyRunLeft;
    private static Image[] meleeEnemyAttack;
    private static Image[] meleeEnemyAttackLeft;
    private static Image[] tankEnemyRun;
    private static Image[] tankEnemyRunLeft;
    private static Image[] tankEnemyAttack;
    private static Image[] tankEnemyAttackLeft;
    private static Image[] assassinEnemyRun;
    private static Image[] assassinEnemyRunLeft;
    private static Image[] assassinEnemyAttack;
    private static Image[] assassinEnemyAttackLeft;
    private static Image[] archerEnemyRun;
    private static Image[] archerEnemyRunLeft;
    private static Image[] archerEnemyAttack;
    private static Image[] archerEnemyAttackLeft;
    private static Image[] healerEnemyRun;
    private static Image[] healerEnemyRunLeft;
    private static Image[] healerEnemyAttack;
    private static Image[] healerEnemyAttackLeft;

    public void loadImages(GameEngine engine) {
        this.engine = engine;
        loadStaticImages(engine);
        loadEnemySprites(engine);
    }

    public static Image getEnemySprite(EnemyType type, double animationTime) {
        return getEnemySprite(type, animationTime, false);
    }

    public static Image getEnemySprite(EnemyType type, double animationTime, boolean facingLeft) {
        Image[] frames = getEnemyRunFrames(type);
        if (facingLeft) {
            frames = getEnemyRunLeftFrames(type);
        }
        if (frames == null || frames.length == 0) {
            return null;
        }
        int frameIndex = ((int) (animationTime * 8)) % frames.length;
        return frames[frameIndex];
    }

    public static Image getEnemyAttackSprite(EnemyType type, double animationTime) {
        return getEnemyAttackSprite(type, animationTime, false);
    }

    public static Image getEnemyAttackSprite(EnemyType type, double animationTime, boolean facingLeft) {
        Image[] frames = getEnemyAttackFrames(type);
        if (facingLeft) {
            frames = getEnemyAttackLeftFrames(type);
        }
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

    private static Image[] getEnemyRunLeftFrames(EnemyType type) {
        if (type == EnemyType.TANK) {
            return tankEnemyRunLeft;
        }
        if (type == EnemyType.ASSASSIN) {
            return assassinEnemyRunLeft;
        }
        if (type == EnemyType.ARCHER) {
            return archerEnemyRunLeft;
        }
        if (type == EnemyType.HEALER) {
            return healerEnemyRunLeft;
        }
        return meleeEnemyRunLeft;
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

    private static Image[] getEnemyAttackLeftFrames(EnemyType type) {
        if (type == EnemyType.TANK) {
            return tankEnemyAttackLeft;
        }
        if (type == EnemyType.ASSASSIN) {
            return assassinEnemyAttackLeft;
        }
        if (type == EnemyType.ARCHER) {
            return archerEnemyAttackLeft;
        }
        if (type == EnemyType.HEALER) {
            return healerEnemyAttackLeft;
        }
        return meleeEnemyAttackLeft;
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
        meleeEnemyRunLeft = flipFrames(meleeEnemyRun);
        meleeEnemyAttack = loadEnemyAttackSprite(engine, meleeSheet);
        meleeEnemyAttackLeft = flipFrames(meleeEnemyAttack);

        Image tankSheet = engine.loadImage("Images/tankEnemy.png");
        tankEnemyRun = loadEnemyRunSprite(engine, tankSheet);
        tankEnemyRunLeft = flipFrames(tankEnemyRun);
        tankEnemyAttack = loadEnemyAttackSprite(engine, tankSheet);
        tankEnemyAttackLeft = flipFrames(tankEnemyAttack);

        Image assassinSheet = engine.loadImage("Images/assassinEnemy.png");
        assassinEnemyRun = loadEnemyRunSprite(engine, assassinSheet);
        assassinEnemyRunLeft = flipFrames(assassinEnemyRun);
        assassinEnemyAttack = loadEnemyAttackSprite(engine, assassinSheet);
        assassinEnemyAttackLeft = flipFrames(assassinEnemyAttack);

        Image archerSheet = engine.loadImage("Images/archerEnemy.png");
        archerEnemyRun = loadEnemyRunSprite(engine, archerSheet);
        archerEnemyRunLeft = flipFrames(archerEnemyRun);
        archerEnemyAttack = loadEnemyAttackSprite(engine, archerSheet);
        archerEnemyAttackLeft = flipFrames(archerEnemyAttack);

        Image healerSheet = engine.loadImage("Images/healerEnemy.png");
        healerEnemyRun = loadEnemyRunSprite(engine, healerSheet);
        healerEnemyRunLeft = flipFrames(healerEnemyRun);
        healerEnemyAttack = loadEnemyAttackSprite(engine, healerSheet);
        healerEnemyAttackLeft = flipFrames(healerEnemyAttack);
    }

    private Image[] loadEnemyRunSprite(GameEngine engine, Image sheet) {
        Image[] frames = new Image[ENEMY_RUN_FRAME_COUNT];
        for (int i = 0; i < frames.length; i++) {
            int row = i / ENEMY_SHEET_COLUMNS;
            int col = i % ENEMY_SHEET_COLUMNS;
            frames[i] = getEnemyFrame(engine, sheet, row, col);
        }
        return frames;
    }

    private Image[] loadEnemyAttackSprite(GameEngine engine, Image sheet) {
        Image[] frames = new Image[ENEMY_ATTACK_FRAME_COUNT];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = getEnemyFrame(engine, sheet, 2, i);
        }
        return frames;
    }

    private Image getEnemyFrame(GameEngine engine, Image sheet, int row, int col) {
        int x = col * ENEMY_FRAME_SIZE + ENEMY_FRAME_INSET;
        int y = row * ENEMY_FRAME_SIZE + ENEMY_FRAME_INSET;
        int size = ENEMY_FRAME_SIZE - ENEMY_FRAME_INSET * 2;
        return engine.subImage(sheet, x, y, size, size);
    }

    private Image[] flipFrames(Image[] frames) {
        Image[] flipped = new Image[frames.length];
        for (int i = 0; i < frames.length; i++) {
            flipped[i] = flipImage(frames[i]);
        }
        return flipped;
    }

    private Image flipImage(Image image) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        BufferedImage flipped = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = flipped.createGraphics();
        graphics.drawImage(image, width, 0, -width, height, null);
        graphics.dispose();
        return flipped;
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

    public static Image getBait() {
        return bait;
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
