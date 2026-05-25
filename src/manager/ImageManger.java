package manager;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

import enemy.EnemyType;
import game.GameEngine;

public class ImageManger {
    private static Image arrowTower;
    private static Image attackTower;
    private static Image iceTower;
    private static Image lightningTower;
    private static Image healTower;
    private static Image baseImage;
    private static Image wallImage;
    private static Image baitImage;
    private static Image homeItems;
    private static Image naturalDisaster;
    private static Image meleeEnemy;
    private static Image tankEnemy;
    private static Image archerEnemy;
    private static Image assassinEnemy;
    private static Image healerEnemy;
    private static final Map<EnemyType, Image> enemySprites = new HashMap<EnemyType, Image>();

    public void loadImages(GameEngine engine) {
        arrowTower = engine.loadImage("Images/arrowTower.png");
        attackTower = engine.loadImage("Images/attackTower.png");
        iceTower = engine.loadImage("Images/iceTower.png");
        lightningTower = engine.loadImage("Images/lightningTower.png");
        healTower = engine.loadImage("Images/healTower.png");
        baseImage = engine.loadImage("Images/homeItems.png");
        wallImage = engine.loadImage("Images/wall.png");
        baitImage = engine.loadImage("Images/bait.png");
        homeItems = engine.loadImage("Images/homeItems.png");
        naturalDisaster = engine.loadImage("Images/naturalDisaster.png");
        meleeEnemy = engine.loadImage("Images/meleeEnemy.png");
        tankEnemy = engine.loadImage("Images/tankEnemy.png");
        archerEnemy = engine.loadImage("Images/archerEnemy.png");
        assassinEnemy = engine.loadImage("Images/assassinEnemy.png");
        healerEnemy = engine.loadImage("Images/healerEnemy.png");
        enemySprites.put(EnemyType.MELEE, meleeEnemy);
        enemySprites.put(EnemyType.TANK, tankEnemy);
        enemySprites.put(EnemyType.ARCHER, archerEnemy);
        enemySprites.put(EnemyType.ASSASSIN, assassinEnemy);
        enemySprites.put(EnemyType.HEALER, healerEnemy);
    }

    public static Image getArrowTower() { return arrowTower; }
    public static Image getAttackTower() { return attackTower; }
    public static Image getIceTower() { return iceTower; }
    public static Image getLightningTower() { return lightningTower; }
    public static Image getHealTower() { return healTower; }
    public static Image getBaseImage() { return baseImage; }
    public static Image getHealRangeEffect() { return homeItems; }
    public static Image getExplosionFireball() { return naturalDisaster; }

    public static Image getEnemySprite(EnemyType type, double animationTime) {
        return enemySprites.get(type);
    }

    public static Image getArrowProjectile() { return arrowTower; }
    public static Image getCannonProjectile() { return attackTower; }
    public static Image getIceProjectile() { return iceTower; }
    public static Image getLaserEffect() { return lightningTower; }
}
