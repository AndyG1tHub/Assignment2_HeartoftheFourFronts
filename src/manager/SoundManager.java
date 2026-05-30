/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package manager;

import javax.sound.sampled.*;

import game.GameEngine;

/** Central sound facade. Singleton so any gameplay class can call it. */
public class SoundManager {
    private static SoundManager instance;

    private static final float SFX_VOLUME = -3.0f;
    private static final float BGM_VOLUME = -12.0f;

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
    private GameEngine.AudioClip rewardSpawn;
    private GameEngine.AudioClip waveStart;
    private GameEngine.AudioClip enemySpawn;
    private GameEngine.AudioClip fireDisaster;
    private GameEngine.AudioClip meteorDisaster;
    private GameEngine.AudioClip insufficientMoney;
    private GameEngine.AudioClip gameOver;
    private GameEngine.AudioClip gameWin;
    private GameEngine.AudioClip bgm;

    private Clip arrowClip, cannonClip, iceClip, lightningClip;
    private Clip deathClip, baseHitClip, buttonClip, placeClip, wallClip;
    private Clip healClip, decoyClip, rewardClip, rewardSpawnClip, waveClip, spawnClip;
    private Clip fireClip, meteorClip, moneyClip, overClip, winClip;

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
        arrowShoot = engine.loadAudio("sounds/arrow_shoot.wav");
        cannonShoot = engine.loadAudio("sounds/cannon_shoot.wav");
        iceShoot = engine.loadAudio("sounds/ice_shoot.wav");
        lightningShoot = engine.loadAudio("sounds/lightning_shoot.wav");
        enemyDeath = engine.loadAudio("sounds/enemy_death.wav");
        baseHit = engine.loadAudio("sounds/base_hit.wav");
        buttonClick = engine.loadAudio("sounds/button_click.wav");
        placeBuilding = engine.loadAudio("sounds/place_building.wav");
        wallBreak = engine.loadAudio("sounds/wall_break.wav");
        healTower = engine.loadAudio("sounds/heal_tower.wav");
        decoyDeploy = engine.loadAudio("sounds/decoy_deploy.wav");
        rewardCollect = engine.loadAudio("sounds/reward_collect.wav");
        rewardSpawn = engine.generateTone(1100, 1650, 160);
        waveStart = engine.loadAudio("sounds/wave_start.wav");
        enemySpawn = engine.loadAudio("sounds/enemy_spawn.wav");
        fireDisaster = engine.loadAudio("sounds/fire_disaster.wav");
        meteorDisaster = engine.loadAudio("sounds/meteor_disaster.wav");
        insufficientMoney = engine.loadAudio("sounds/insufficient_money.wav");
        gameOver = engine.loadAudio("sounds/game_over.wav");
        gameWin = engine.loadAudio("sounds/game_win.wav");
        bgm = engine.loadAudio("sounds/bgm.wav");
        preloadClips();
    }

    private void preloadClips() {
        arrowClip = createClip(arrowShoot);
        cannonClip = createClip(cannonShoot);
        iceClip = createClip(iceShoot);
        lightningClip = createClip(lightningShoot);
        deathClip = createClip(enemyDeath);
        baseHitClip = createClip(baseHit);
        buttonClip = createClip(buttonClick);
        placeClip = createClip(placeBuilding);
        wallClip = createClip(wallBreak);
        healClip = createClip(healTower);
        decoyClip = createClip(decoyDeploy);
        rewardClip = createClip(rewardCollect);
        rewardSpawnClip = createClip(rewardSpawn);
        waveClip = createClip(waveStart);
        spawnClip = createClip(enemySpawn);
        fireClip = createClip(fireDisaster);
        meteorClip = createClip(meteorDisaster);
        moneyClip = createClip(insufficientMoney);
        overClip = createClip(gameOver);
        winClip = createClip(gameWin);
    }

    private Clip createClip(GameEngine.AudioClip ac) {
        if (ac == null) return null;
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(ac.getAudioFormat(), ac.getData(), 0, (int) ac.getBufferSize());
            return clip;
        } catch (Exception e) {
            return null;
        }
    }

    private void playClip(Clip clip, float volume) {
        if (clip == null || muted) return;
        clip.stop();
        clip.setFramePosition(0);
        try {
            FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            control.setValue(volume);
        } catch (Exception ignored) {
        }
        clip.start();
    }

    public void toggleMute() {
        muted = !muted;
        if (muted) {
            if (bgm != null) engine.stopAudioLoop(bgm);
        } else {
            if (bgm != null) engine.startAudioLoop(bgm, BGM_VOLUME);
        }
    }

    public boolean isMuted() {
        return muted;
    }

    public void playArrowShoot() { playClip(arrowClip, SFX_VOLUME); }
    public void playCannonShoot() { playClip(cannonClip, SFX_VOLUME); }
    public void playIceShoot() { playClip(iceClip, SFX_VOLUME); }
    public void playLightningShoot() { playClip(lightningClip, SFX_VOLUME); }
    public void startLightningLoop() {
        if (!muted && lightningShoot != null) engine.startAudioLoop(lightningShoot, SFX_VOLUME);
    }
    public void stopLightningLoop() {
        if (lightningShoot != null) engine.stopAudioLoop(lightningShoot);
    }
    public void playEnemyDeath() { playClip(deathClip, SFX_VOLUME); }
    public void playBaseHit() { playClip(baseHitClip, SFX_VOLUME); }
    public void playButtonClick() { playClip(buttonClip, -5.0f); }
    public void playPlaceBuilding() { playClip(placeClip, SFX_VOLUME); }
    public void playWallBreak() { playClip(wallClip, SFX_VOLUME); }
    public void playHealTower() { playClip(healClip, SFX_VOLUME); }
    public void playDecoyDeploy() { playClip(decoyClip, SFX_VOLUME); }
    public void playRewardCollect() { playClip(rewardClip, -1.0f); }
    public void playRewardSpawn() { playClip(rewardSpawnClip, -12.0f); }
    public void playWaveStart() { playClip(waveClip, -6.0f); }
    public void playEnemySpawn() { playClip(spawnClip, -6.0f); }
    public void playFireDisaster() { playClip(fireClip, SFX_VOLUME); }
    public void playMeteorDisaster() { playClip(meteorClip, SFX_VOLUME); }
    public void playInsufficientMoney() { playClip(moneyClip, SFX_VOLUME); }
    public void playGameOver() { playClip(overClip, SFX_VOLUME); }
    public void playGameWin() { playClip(winClip, SFX_VOLUME); }

    public void startBgm() { if (!muted && bgm != null) engine.startAudioLoop(bgm, BGM_VOLUME); }
    public void stopBgm() { if (bgm != null) engine.stopAudioLoop(bgm); }
}
