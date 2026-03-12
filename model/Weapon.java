package model;

public class Weapon extends Item<Integer> {
    public Weapon(String name, Integer damage) {
        super(name, damage, ItemType.WEAPON);
    }

    @Override
    public void use(Player player) {
        System.out.println("You used " + getName() + " (Damage: " + getValue() + ")");
    }
}
