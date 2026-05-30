/*
 * 159.261 Games Programming - Assignment 2
 * Team members:
 * - Guo Mingqi (ID: 24009196)
 * - Yu Han (ID: 24008995)
 * - Song Pengju (ID: 24009198)
 * - Li Qianzheng (ID: 24009199)
 */

/**
 * Owns the player's money balance with atomic spend/earn operations.
 * spendMoney() returns false if the cost is unaffordable.
 * Money comes from enemy kills, reward points, and building sales.
 * Starting amount is defined in GameConfig.STARTING_MONEY.
 */
package manager;

import game.GameConfig;

/** Owns player money. */
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
