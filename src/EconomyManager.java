/** Owns player money and passive income. */
public class EconomyManager {
    private int money;
    private double incomeBuffer;

    public EconomyManager() {
        money = GameConfig.STARTING_MONEY;
    }

    public void updateIncome(double dt) {
        incomeBuffer += GameConfig.BASE_INCOME_PER_SECOND * dt;
        if (incomeBuffer >= 1.0) {
            int wholeMoney = (int) incomeBuffer;
            money += wholeMoney;
            incomeBuffer -= wholeMoney;
        }
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

    public int getMoney() {
        return money;
    }
}
