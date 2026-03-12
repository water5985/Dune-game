package model;

public class Coin extends Item<Integer> {
    public Coin(int amount) {
        super("Coin", amount, ItemType.COIN);
    }

    @Override
    public void use(Player player) {
        player.addCoins(getValue());
        System.out.println("You used a coin (+" + getValue() + "). Total coins: " + player.getCoins());
    }
}
