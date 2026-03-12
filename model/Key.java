package model;

public class Key extends Item<String> {
    public Key(String name, String code) {
        super(name, code, ItemType.KEY);
    }

    // Unlocks nearby locations if their requirement matches this key's code
    @Override
    public void use(Player player) {
        boolean unlocked = false;
        for (Location<?> loc : player.getCurrentLocation().getExits().values()) {
            if (loc.checkRequirement(player)) {
                continue;
            } else if (loc.requirement instanceof String && loc.requirement.equals(this.getValue())) {
                System.out.println("You unlocked the path to: " + loc.getName());
                unlocked = true;
            }
        }
        if (!unlocked) {
            System.out.println("Key didn't match any nearby locks.");
        }
    }
}
