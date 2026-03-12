package model;

public class Consumable extends Item<Integer> {
    public Consumable(String name, Integer healingPower) {
        super(name, healingPower, ItemType.CONSUMABLE);
    }

    @Override
    public void use(Player player) {
        player.heal(getValue());
    }
}
