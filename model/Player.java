package model;
import java.util.*;



public class Player {
    private int health = 100;
    private List<Item<?>> inventory = new ArrayList<>();
    private Location<?> currentLocation;
    private int coins = 0;

    public void addCoins(int amount) {
        coins += amount;
    }

    public int getCoins() {
        return coins;
    }


    public void heal(int amount) {
        health += amount;
        System.out.println("Healed by " + amount + ". Current health: " + health);
    }

    public void takeDamage(int amount) {
        health -= amount;
        System.out.println("Took " + amount + " damage. Current health: " + health);
        if (health <= 0) {
            System.out.println("You have died. Game Over.");
            System.exit(0);
        }
    }


    public void addItem(Item<?> item) {
        inventory.add(item);
    }

    public boolean hasAccess(Object requirement) {
        for (Item<?> item : inventory) {
            if (item instanceof Key) {
                if (((Key) item).getValue().equals(requirement)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Item<?>> getInventory() {
        return inventory;
    }

    public Location<?> getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location<?> currentLocation) {
        this.currentLocation = currentLocation;
    }
    private String commandArg;

    public String getCommandArg() {
        return commandArg;
    }

    public void setCommandArg(String arg) {
        this.commandArg = arg;
    }


    public boolean move(String direction) {
        Location<?> current = this.getCurrentLocation();
        Location<?> next = current.getExit(direction);

        if (next != null && next.checkRequirement(this)) {
            this.setCurrentLocation(next);
            return true;
        } else {
            System.out.println("You need a key to go that way!");
            return false;
        }
    }

    public int getHealth() {
        return health;
    }

    public String describeInventory() {
        if (inventory.isEmpty()) {
            return "(empty)";
        }

        StringBuilder sb = new StringBuilder();
        for (Item<?> item : inventory) {
            sb.append(item.getName()).append(", ");
        }
        return sb.substring(0, sb.length() - 2);
    }

    private int x = 100;
    private int y = 300;

    public int getX() { return x; }
    public int getY() { return y; }

    public void moveInRoom(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void resetPosition() {
        x = 100;
        y = 300;
    }

    private int bonusDamage = 0;

    public void addBonusDamage(int value) {
        bonusDamage += value;
    }

    public int getBonusDamage() {
        return bonusDamage;
    }


}
