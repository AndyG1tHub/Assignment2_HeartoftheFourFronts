/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

package manager;

import game.GameConfig;

/**
 * <p>Owns and manages the player's money supply. Handles spending, rewards from kills and collectables, and sell refunds.</p>
 */
public class EconomyManager {
    private int money;

    public EconomyManager() {
        money = GameConfig.STARTING_MONEY;
    }

    public boolean canAfford(int cost) {
        return money >= cost;
    }

    public boolean spendMoney(int amount) {
        if (!canAfford(amount)) {
            return false;
        }
        money -= amount;
        return true;
    }

    public void addMoney(int amount) {
        money += amount;
    }

    public void setMoney(int amount) {
        money = Math.max(0, amount);
    }

    public int getMoney() {
        return money;
    }
}
