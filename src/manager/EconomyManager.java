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
