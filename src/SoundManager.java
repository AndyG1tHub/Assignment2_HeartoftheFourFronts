/** Central sound facade. Singleton so any gameplay class can call it. */
public class SoundManager {
    private static SoundManager instance;

    private GameEngine engine;
    private boolean muted;

    private GameEngine.AudioClip arrowShoot;
    private GameEngine.AudioClip cannonShoot;
    private GameEngine.AudioClip iceShoot;
    private GameEngine.AudioClip lightningShoot;
    private GameEngine.AudioClip enemyDeath;
    private GameEngine.AudioClip baseHit;
    private GameEngine.AudioClip buttonClick;
    private GameEngine.AudioClip placeBuilding;
    private GameEngine.AudioClip wallBreak;
    private GameEngine.AudioClip healTower;
    private GameEngine.AudioClip decoyDeploy;
    private GameEngine.AudioClip rewardCollect;
    private GameEngine.AudioClip waveStart;
    private GameEngine.AudioClip enemySpawn;
    private GameEngine.AudioClip fireDisaster;
    private GameEngine.AudioClip meteorDisaster;
    private GameEngine.AudioClip insufficientMoney;
    private GameEngine.AudioClip bgm;

    private SoundManager(GameEngine engine) {
        this.engine = engine;
    }

    public static void init(GameEngine engine) {
        instance = new SoundManager(engine);
    }

    public static SoundManager getInstance() {
        return instance;
    }

    public void loadSounds() {
        arrowShoot = tryLoad("sounds/arrow_shoot.wav");
        cannonShoot = tryLoad("sounds/cannon_shoot.wav");
        iceShoot = tryLoad("sounds/ice_shoot.wav");
        lightningShoot = tryLoad("sounds/lightning_shoot.wav");
        enemyDeath = tryLoad("sounds/enemy_death.wav");
        baseHit = tryLoad("sounds/base_hit.wav");
        buttonClick = tryLoad("sounds/button_click.wav");
        placeBuilding = tryLoad("sounds/place_building.wav");
        wallBreak = tryLoad("sounds/wall_break.wav");
        healTower = tryLoad("sounds/heal_tower.wav");
        decoyDeploy = tryLoad("sounds/decoy_deploy.wav");
        rewardCollect = tryLoad("sounds/reward_collect.wav");
        waveStart = tryLoad("sounds/wave_start.wav");
        enemySpawn = tryLoad("sounds/enemy_spawn.wav");
        fireDisaster = tryLoad("sounds/fire_disaster.wav");
        meteorDisaster = tryLoad("sounds/meteor_disaster.wav");
        insufficientMoney = tryLoad("sounds/insufficient_money.wav");
        bgm = tryLoad("sounds/bgm.wav");
    }

    private GameEngine.AudioClip tryLoad(String path) {
        GameEngine.AudioClip clip = engine.loadAudio(path);
        if (clip == null) {
            System.out.println("[SoundManager] missing: " + path + " (silent)");
        }
        return clip;
    }

    public void toggleMute() {
        muted = !muted;
        if (muted) {
            if (bgm != null) engine.stopAudioLoop(bgm);
        } else {
            if (bgm != null) engine.startAudioLoop(bgm);
        }
    }

    public boolean isMuted() {
        return muted;
    }

    private void play(GameEngine.AudioClip clip) {
        if (clip != null && !muted) {
            engine.playAudio(clip);
        }
    }

    public void playArrowShoot() { play(arrowShoot); }
    public void playCannonShoot() { play(cannonShoot); }
    public void playIceShoot() { play(iceShoot); }
    public void playLightningShoot() { play(lightningShoot); }
    public void playEnemyDeath() { play(enemyDeath); }
    public void playBaseHit() { play(baseHit); }
    public void playButtonClick() { play(buttonClick); }
    public void playPlaceBuilding() { play(placeBuilding); }
    public void playWallBreak() { play(wallBreak); }
    public void playHealTower() { play(healTower); }
    public void playDecoyDeploy() { play(decoyDeploy); }
    public void playRewardCollect() { play(rewardCollect); }
    public void playWaveStart() { play(waveStart); }
    public void playEnemySpawn() { play(enemySpawn); }
    public void playFireDisaster() { play(fireDisaster); }
    public void playMeteorDisaster() { play(meteorDisaster); }
    public void playInsufficientMoney() { play(insufficientMoney); }
    public void startBgm() { if (!muted && bgm != null) engine.startAudioLoop(bgm); }
    public void stopBgm() { if (bgm != null) engine.stopAudioLoop(bgm); }
}
