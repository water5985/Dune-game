package model;

public class Enemy extends Character {
    private int strength;
    private int hp;
    private final EnemyType type;

    public Enemy(String name, int strength, EnemyType type) {
        super(name);
        this.strength = strength;
        this.hp = 100;
        this.type = type;
    }

    public int getHealth() {
        return hp;
    }

    public EnemyType getType() {
        return type;
    }

    @Override
    public void interact(Player player) {
        if (isAlive()) {
            System.out.println(name + " attacks you with strength " + strength);
            player.takeDamage(strength);
        } else {
            System.out.println(name + " is already defeated.");
        }
    }

    // Takes damage and checks for defeat
    public void takeDamage(int damage) {
        if (isAlive()) {
            hp -= damage;
            System.out.println(name + " takes " + damage + " damage. Remaining HP: " + hp);
            if (hp <= 0) {
                System.out.println(name + " has been defeated!");
            }
        }
    }

    public boolean isAlive() {
        return hp > 0;
    }
}
