package model;
import java.util.*;

public class Location<T> {
    private String name;
    T requirement;
    private List<Item<?>> items;
    private Set<Character> characters;
    private Map<String, Location<?>> exits;

    public Location(String name, T requirement) {
        this.name = name;
        this.requirement = requirement;
        this.items = new ArrayList<>();
        this.characters = new HashSet<>();
        this.exits = new HashMap<>();
    }
    // Checks if player meets the requirement to enter
    // Determines if the player can access the location based on its requirement
    public boolean checkRequirement(Player player) {
        return requirement == null || player.hasAccess(requirement);
    }

    public String getName() {
        return name;
    }

    public List<Item<?>> getItems() {
        return items;
    }

    public Set<Character> getCharacters() {
        return characters;
    }

    public Map<String, Location<?>> getExits() {
        return exits;
    }

    public void addItem(Item<?> item) {
        items.add(item);
    }

    public void addCharacter(Character character) {
        characters.add(character);
    }

    public void addExit(String direction, Location<?> location) {
        exits.put(direction, location);
    }
    // Gets the location in the specified direction
    public Location<?> getExit(String direction) {
        return exits.get(direction);
    }
}